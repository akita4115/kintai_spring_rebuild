const Attendance = () => {

    return (
        <div className="container mt-4">

            <h2 className="mb-3">
                勤怠管理
            </h2>

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
							/>

						</div>

						<div className="col-md-8 text-end">

							<button
								type="button"
								className="btn btn-info">
								表示
							</button>

						</div>

					</div>

				</div>

			</div>

		</div>
	);
};

export default AttendanceInput;