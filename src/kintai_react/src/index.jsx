import React from "react";
import { createRoot } from "react-dom/client";

import UserIndex from "./components/user/UserIndex";

const rootElement = document.getElementById("root");

if (rootElement) {
	const root = createRoot(rootElement);

	root.render(
		<React.StrictMode>
			<UserIndex />
		</React.StrictMode>
	);
}