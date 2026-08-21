import { Routes, Route } from "react-router-dom";

import AttendanceInput from "./components/attendanceInput/AttendanceInput.jsx";
import UserIndex from "./components/user/UserIndex.jsx";

export default function App() {

	return (

		<Routes>

			<Route
				path="/attendance/input"
				element={<AttendanceInput />}
			/>

			<Route
				path="/user/index"
				element={<UserIndex />}
			/>

		</Routes>

	);

}