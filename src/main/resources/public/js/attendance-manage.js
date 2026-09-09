"use strict";

document.addEventListener("DOMContentLoaded", function() {

	// エラーメッセージ
	const errorMessage =
		document.getElementById("attendanceErrorMessage");

	// 承認ボタン
	const approveButton =
		document.getElementById("approveButton");

	// 承認モーダル
	const approveModalElement =
		document.getElementById("approveModal");

	const approveModal =
		bootstrap.Modal.getOrCreateInstance(
			approveModalElement
		);

	/**
	 * 選択された勤怠IDを取得
	 */
	const getSelectedAttendanceIds = function() {

		const checkedElements =
			document.querySelectorAll(
				".attendance-checkbox:checked"
			);

		const approveAttendanceIds =
			document.getElementById("approveAttendanceIds");

		return Array.from(checkedElements).map(
			function(checkbox) {
				return checkbox.value;
			}
		);
	};

	/**
	 * エラーメッセージを表示
	 */
	const showErrorMessage = function(message) {

		errorMessage.textContent = message;
		errorMessage.classList.remove("d-none");
	};

	/**
	 * エラーメッセージを非表示
	 */
	const clearErrorMessage = function() {

		errorMessage.textContent = "";
		errorMessage.classList.add("d-none");
	};

	/**
	 * 承認ボタン
	 */
	approveButton.addEventListener(
		"click",
		function() {

			clearErrorMessage();

			const selectedIds =
				getSelectedAttendanceIds();

			// 勤怠未選択
			if (selectedIds.length === 0) {

				showErrorMessage(
					"勤怠が選択されていません。"
				);

				return;
			}

			//前回設定したIDを削除
			approveAttendanceIds.replaceChildren();

			//選択したIDをhiddenへ設定
			selectedIds.forEach(function(selectedId) {

				const hiddenInput =
					document.createElement("input");

				hiddenInput.type = "hidden";
				hiddenInput.name = "attendanceHeadIds";
				hiddenInput.value = selectedId;

				approveAttendanceIds.appendChild(hiddenInput);
			});

			// 承認モーダルを表示
			approveModal.show();
		}
	);
});