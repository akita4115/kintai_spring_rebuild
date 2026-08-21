import { useEffect, useState } from "react";

const AttendanceInput = () => {

	// 現在年月
	const now = new Date();

	const currentMonth =
		`${now.getFullYear()}-${String(
			now.getMonth() + 1
		).padStart(2, "0")}`;

	// 表示対象年月
	const [targetMonth, setTargetMonth] =
		useState(currentMonth);

	// 勤怠一覧
	const [attendanceList, setAttendanceList] =
		useState([]);

	// エラーメッセージ
	const [errorMessage, setErrorMessage] =
		useState("");

	/**
	 * 勤怠入力データを取得
	 */
	const getAttendanceList = async () => {

		try {
			const response = await fetch(
				`/api/attendance/input?targetMonth=${encodeURIComponent(
					targetMonth
				)}`
			);

			if (!response.ok) {
				throw new Error(
					"勤怠入力データの取得に失敗しました。"
				);
			}

			const data = await response.json();

			if (
				data.errors
				&& Object.keys(data.errors).length > 0
			) {
				setErrorMessage(
					Object.values(data.errors)[0]
				);

				setAttendanceList([]);
				return;
			}

			setAttendanceList(
				data.attendanceList ?? []
			);

			setErrorMessage("");

		} catch (error) {
			console.error(error);

			setErrorMessage(
				"勤怠入力データの取得に失敗しました。"
			);

			setAttendanceList([]);
		}
	};

	/**
	 * 初回表示
	 */
	useEffect(() => {
		getAttendanceList();
	}, []);

	/**
	 * 表示ボタン
	 */
	const handleDisplay = () => {
		getAttendanceList();
	};

	/**
	 * HH:mmを分へ変換
	 */
	const timeToMinutes = (time) => {

		const [hours, minutes] =
			time.split(":").map(Number);

			return hours * 60 + minutes;
	};

	/**
	 * 分をHH:mmへ変換
	 */
	const minutesToTime = (totalMinutes) => {

		const hours =
		Math.floor(totalMinutes / 60);

		const minutes = 
			totalMinutes % 60;
	
		return `${String(hours).padStart(2, "0")}:${String(
			minutes
		).padStart(2, "0")}`;
	};

	/**
 * 勤務時間・残業時間を計算
 */
const calculateWorkingHours = (attendance) => {

	const calculatedAttendance = {
		...attendance,
	};

	const {
		startTime,
		endTime,
		breakTime,
		nightBreakTime,
	} = calculatedAttendance;

	// 必要な時刻が未入力の場合
	if (
		!startTime
		|| !endTime
		|| !breakTime
		|| !nightBreakTime
	) {
		calculatedAttendance.workTime = "";
		calculatedAttendance.overTime = "";

		return calculatedAttendance;
	}

	const startMinutes =
		timeToMinutes(startTime);

	let endMinutes =
		timeToMinutes(endTime);

	// 終了時刻が開始時刻より前の場合は翌日として計算
	if (endMinutes < startMinutes) {
		endMinutes += 24 * 60;
	}

	const breakMinutes =
		timeToMinutes(breakTime);

	const nightBreakMinutes =
		timeToMinutes(nightBreakTime);

	// 終了－開始－昼休憩－夜休憩
	const workMinutes = Math.max(
		0,
		endMinutes
			- startMinutes
			- breakMinutes
			- nightBreakMinutes
	);

	// 8時間を超えた分は残業
	const overMinutes = Math.max(
		0,
		workMinutes - 8 * 60
	);

	calculatedAttendance.workTime =
		minutesToTime(workMinutes);

	calculatedAttendance.overTime =
		minutesToTime(overMinutes);

	return calculatedAttendance;
};
	/**
	* 勤怠一覧の入力内容を変更
　	*/
	const handleAttendanceChange = (
		index,
		field,
		value
	) => {

	// 変更前の勤怠情報
	const previousAttendance =
		attendanceList[index];

	// 一覧をコピー
	const updatedList = [...attendanceList];

	// 変更された値を設定
	updatedList[index] = {
		...updatedList[index],
		[field]: value,
	};

	// 区分を変更した場合
	if (field === "kbn") {

		const kbnNames = {
			"1": "出勤",
			"2": "休日",
			"3": "有給",
			"4": "休出",
			"5": "欠勤",
			"6": "特休",
			"7": "代休",
			"8": "振休",
		};

		updatedList[index].attendanceType =
			kbnNames[value];

		// 時刻を入力する勤務扱い区分
		const workKbnList = ["1", "4"];

		const wasWork =
			workKbnList.includes(
				previousAttendance.kbn
			);

		const isWork =
			workKbnList.includes(value);

		// 休日扱いから勤務扱いへ変更
		if (!wasWork && isWork) {

			updatedList[index].startTime = "09:00";
			updatedList[index].endTime = "18:00";
			updatedList[index].breakTime = "01:00";
			updatedList[index].nightBreakTime = "00:00";
			updatedList[index].workTime = "08:00";
			updatedList[index].overTime = "00:00";
		}

		// 勤務扱いから休日扱いへ変更
		if (wasWork && !isWork) {

			updatedList[index].startTime = "";
			updatedList[index].endTime = "";
			updatedList[index].breakTime = "";
			updatedList[index].nightBreakTime = "";
			updatedList[index].workTime = "";
			updatedList[index].overTime = "";
		}

		
	}
		// 時刻を変更した場合は勤務時間・残業時間を再計算
		const timeFieldList = [
			"startTime",
			"endTime",
			"breakTime",
			"nightBreakTime",
		];

		if (timeFieldList.includes(field)) {

			updatedList[index] =
				calculateWorkingHours(
					updatedList[index]
				);
		}
		setAttendanceList(updatedList);
	};

	/**
	 * 曜日による行の色
	 */
	const getRowClassName = (attendance) => {

		if(attendance.holiday) {
			return "table-danger";
		}


		if (attendance.dayOfWeek === "土") {
			return "table-info";
		}

		if (attendance.dayOfWeek === "日") {
			return "table-danger";
		}

		return "";
	};



	return (
		<div className="container mt-4">

			<h2 className="mb-3">
				勤怠入力
			</h2>

			{/* 年月入力 */}
			<div className="card mb-4">

				<div className="card-header bg-light">
					入力
				</div>

				<div className="card-body">

					<div className="row align-items-end">

						<div className="col-md-4">

							<label className="form-label">
								年月:
							</label>

							<input
								type="month"
								className="form-control"
								value={targetMonth}
								onChange={(event) =>
									setTargetMonth(
										event.target.value
									)
								}
							/>

						</div>

						<div className="col-md-8 text-end">

							<button
								type="button"
								className="btn btn-info"
								onClick={handleDisplay}
							>
								表示
							</button>

						</div>

					</div>

				</div>

			</div>

			{/* エラーメッセージ */}
			{errorMessage && (
				<div className="alert alert-danger">
					{errorMessage}
				</div>
			)}

			{/* カレンダー */}
			<div className="card">

				<div className="card-header bg-light">
					カレンダー
				</div>

				<div className="card-body">

					{/* 保存・申請ボタン */}
					<div className="text-end mb-3">

						<button
							type="button"
							className="btn btn-primary me-2"
						>
							保存
						</button>

						<button
							type="button"
							className="btn btn-success"
						>
							申請
						</button>

					</div>

					<div className="table-responsive">
						<table className="table table-bordered align-middle">

							<thead className="table-dark">

								<tr>
									<th>日</th>
									<th>曜日</th>
									<th>区分</th>
									<th>開始時刻</th>
									<th>終了時刻</th>
									<th>昼休憩時間</th>
									<th>夜休憩時間</th>
									<th>勤務時間</th>
									<th>残業時間</th>
									<th>備考</th>
								</tr>

							</thead>

							<tbody>

								{attendanceList.map(
									(attendance, index) => (

										<tr
											key={
												attendance.attendanceDate
											}
											className={
												getRowClassName(attendance)
											}
										>

											<td>
												{
													attendance
														.attendanceDate
														.slice(8)
												}
											</td>

											<td>
												{
													attendance.dayOfWeek
												}
											</td>

											<td>
												<select
													className="form-select"
													value={attendance.kbn}
													onChange={(event) =>
														handleAttendanceChange(
															index,
															"kbn",
															event.target.value
														)
													}
												>
													<option value="1">出勤</option>
													<option value="2">休日</option>
													<option value="3">有給</option>
													<option value="4">休出</option>
													<option value="5">欠勤</option>
													<option value="6">特休</option>
													<option value="7">代休</option>
													<option value="8">振休</option>
												</select>
											</td>

											<td>
												<input
													type="time"
													className="form-control"
													value={
														attendance.startTime
													}
													onChange={(event) =>
														handleAttendanceChange(
															index,
															"startTime",
															event.target.value
														)
													}
												/>
											</td>

											<td>
												<input
													type="time"
													className="form-control"
													value={
														attendance.endTime
													}
													onChange={(event) =>
														handleAttendanceChange(
															index,
															"endTime",
															event.target.value
														)
													}
												/>
											</td>

											<td>
												<input
													type="time"
													className="form-control"
													value={
														attendance.breakTime
													}
													onChange={(event) =>
														handleAttendanceChange(
															index,
															"breakTime",
															event.target.value
														)
													}
												/>
											</td>

											<td>
												<input
													type="time"
													className="form-control"
													value={
														attendance
															.nightBreakTime
													}
													onChange={(event) =>
														handleAttendanceChange(
															index,
															"nightBreakTime",
															event.target.value
														)
													}
												/>
											</td>

											<td>
												<input
													type="time"
													className="form-control"
													value={
														attendance.workTime
													}
													readOnly
												/>
											</td>

											<td>
												<input
													type="time"
													className="form-control"
													value={
														attendance.overTime
													}
													readOnly
												/>
											</td>

											<td>
												<input
													type="text"
													className="form-control"
													value={
														attendance.remarks
													}
													onChange={(event) =>
														handleAttendanceChange(
															index,
															"remarks",
															event.target.value
														)
													}
												/>
											</td>

										</tr>
									)
								)}

							</tbody>

						</table>

					</div>

				</div>

			</div>

		</div>
	);
};

export default AttendanceInput;