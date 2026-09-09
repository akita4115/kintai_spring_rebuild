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

	const approveAttendanceIds =
		document.getElementById("approveAttendanceIds");

	// 差戻ボタン
	const rejectButton =
		document.getElementById("rejectButton");

	// 差戻モーダル
	const rejectModalElement =
		document.getElementById("rejectModal");

	const rejectModal =
		bootstrap.Modal.getOrCreateInstance(
			rejectModalElement
		);

	// 差戻対象IDの設定場所
	const rejectAttendanceIds =
		document.getElementById("rejectAttendanceIds");

	// 差戻フォーム
	const rejectForm =
		document.getElementById("rejectForm");

	// 差戻理由
	const rejectReason =
		document.getElementById("rejectReason");

	// 差戻理由エラー
	const rejectReasonError =
		document.getElementById("rejectReasonError");

	/**
	 * 選択された勤怠IDを取得
	 */
	const getSelectedAttendanceIds = function() {

		const checkedElements =
			document.querySelectorAll(
				".attendance-checkbox:checked"
			);

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

	/**
 * 差戻ボタン
 */
	rejectButton.addEventListener(
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

			// 前回の値を初期化
			rejectAttendanceIds.replaceChildren();
			rejectReason.value = "";
			rejectReasonError.classList.add("d-none");

			// 選択したIDをhiddenへ設定
			selectedIds.forEach(function(selectedId) {

				const hiddenInput =
					document.createElement("input");

				hiddenInput.type = "hidden";
				hiddenInput.name = "attendanceHeadIds";
				hiddenInput.value = selectedId;

				rejectAttendanceIds.appendChild(
					hiddenInput
				);
			});

			// 差戻モーダルを表示
			rejectModal.show();
		}
	);

	/**
	 * 差戻フォーム送信
	 */
	rejectForm.addEventListener(
		"submit",
		function(event) {

			// 差戻理由が空欄
			if (rejectReason.value.trim() === "") {

				event.preventDefault();

				rejectReasonError.classList.remove(
					"d-none"
				);

				return;
			}

			rejectReasonError.classList.add(
				"d-none"
			);
		}
	);
});