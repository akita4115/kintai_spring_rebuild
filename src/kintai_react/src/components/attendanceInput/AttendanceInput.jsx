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
	 * 勤怠一覧の入力内容を変更
	 */
	const handleAttendanceChange = (
		index,
		field,
		value
	) => {

		const updatedList = [...attendanceList];

		updatedList[index] = {
			...updatedList[index],
			[field]: value,
		};

		// 区分を変更した場合
		if (field === "attendanceType") {

			if (value === "休日") {
				updatedList[index].startTime = "";
				updatedList[index].endTime = "";
				updatedList[index].breakTime = "";
				updatedList[index].nightBreakTime = "";
				updatedList[index].workTime = "";
				updatedList[index].overTime = "";

			} else if (value === "出勤") {
				updatedList[index].startTime = "09:00";
				updatedList[index].endTime = "18:00";
				updatedList[index].breakTime = "01:00";
				updatedList[index].nightBreakTime = "00:00";
			}
		}

		setAttendanceList(updatedList);
	};

	/**
	 * 曜日による行の色
	 */
	const getRowClassName = (dayOfWeek) => {

		if (dayOfWeek === "土") {
			return "table-info";
		}

		if (dayOfWeek === "日") {
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

						<table
							className="table table-bordered align-middle"
							style={{ minWidth: "1250px" }}
						>

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
												getRowClassName(
													attendance.dayOfWeek
												)
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
													value={
														attendance
															.attendanceType
													}
													onChange={(event) =>
														handleAttendanceChange(
															index,
															"attendanceType",
															event.target.value
														)
													}
												>
													<option value="出勤">
														出勤
													</option>

													<option value="休日">
														休日
													</option>
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