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

	// 承認する勤怠IDの設定場所
	const approveAttendanceIds =
		document.getElementById("approveAttendanceIds");

	// 差戻ボタン
	const rejectButton =
		document.getElementById("rejectButton");

	// 差戻モーダル
	const rejectModal =
		bootstrap.Modal.getOrCreateInstance(
			document.getElementById("rejectModal")
		);

	// 差戻フォーム
	const rejectForm =
		document.getElementById("rejectForm");

	// 差戻する勤怠IDの設定場所
	const rejectAttendanceIds =
		document.getElementById("rejectAttendanceIds");

	// 差戻理由
	const rejectReason =
		document.getElementById("rejectReason");

	// 差戻理由未入力エラー
	const rejectReasonError =
		document.getElementById("rejectReasonError");


	//選択された勤怠IDを取得
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


	// エラーメッセージを表示
	const showErrorMessage = function(message) {

		errorMessage.textContent = message;
		errorMessage.classList.remove("d-none");
	};


	//エラーメッセージを非表示
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

			const selectedCheckboxes =
				document.querySelectorAll(
					".attendance-checkbox:checked"
				);

			// 申請中以外の勤怠は差戻不可
			const hasNotPendingAttendance =
				Array.from(selectedCheckboxes).some(
					function(checkbox) {

						return checkbox.dataset.status !== "1";
					}
				);

			if (hasNotPendingAttendance) {

				showErrorMessage(
					"申請中以外の勤怠は差戻できません。"
				);

				return;
			}

			// 前回設定したIDを削除
			rejectAttendanceIds.replaceChildren();

			// 選択したIDをhiddenへ設定
			selectedIds.forEach(
				function(selectedId) {

					const hiddenInput =
						document.createElement("input");

					hiddenInput.type = "hidden";
					hiddenInput.name = "attendanceHeadIds";
					hiddenInput.value = selectedId;

					rejectAttendanceIds.appendChild(
						hiddenInput
					);
				}
			);

			// 前回入力した差戻理由とエラーをクリア
			rejectReason.value = "";
			rejectReasonError.classList.add("d-none");

			// 差戻モーダルを表示
			rejectModal.show();
		}
	);

	/**
	 * 差戻フォーム送信時の入力確認
	 */
	rejectForm.addEventListener(
		"submit",
		function(event) {

			const trimmedReason =
				rejectReason.value.trim();

			// 差戻理由未入力
			if (trimmedReason === "") {

				event.preventDefault();

				rejectReasonError.classList.remove(
					"d-none"
				);

				rejectReason.focus();

				return;
			}

			// 前後の空白を除去して送信
			rejectReason.value = trimmedReason;

			rejectReasonError.classList.add(
				"d-none"
			);
		}
	);

	/**
	 * 差戻理由入力時に未入力エラーを消す
	 */
	rejectReason.addEventListener(
		"input",
		function() {

			if (rejectReason.value.trim() !== "") {

				rejectReasonError.classList.add(
					"d-none"
				);
			}
		}
	);


});