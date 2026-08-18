import React from "react";
import { createRoot } from "react-dom/client";

import UserIndex from "./components/user/UserIndex";
import AttendanceInput from "./components/attendanceInput/AttendanceInput";

const rootElement = document.getElementById("root");

if (rootElement) {

	const root = createRoot(rootElement);

	// 現在表示しているURLを取得
	const pathname = window.location.pathname;

	let component;

	// URLに応じて表示するReactコンポーネントを切り替える
	if (pathname === "/attendance/input") {

		component = <AttendanceInput />;

	} else if (pathname === "/user/index") {

		component = <UserIndex />;

	} else {

		component = null;
	}

	root.render(
		<React.StrictMode>
			{component}
		</React.StrictMode>
	);
}