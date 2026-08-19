document.addEventListener("DOMContentLoaded", function() {

	const radioButtons =
		document.querySelectorAll(
			'input[name="selectedHoliday"]'
		);

	const updateButton =
		document.getElementById("updateButton");

	const deleteButton =
		document.getElementById("deleteButton");


	// 一覧選択
	radioButtons.forEach(function(radio) {

		radio.addEventListener(
			"change",
			function() {

				updateButton.disabled = false;
				deleteButton.disabled = false;

			}
		);


		// 行をクリックしてもラジオボタンを選択
		radio.closest("tr").addEventListener(
			"click",
			function() {

				radio.checked = true;

				radio.dispatchEvent(
					new Event("change")
				);

			}
		);

	});


	// 新規登録
	const createButton =
		document.getElementById(
			"createHolidayButton"
		);

	const createForm =
		document.getElementById(
			"createHolidayForm"
		);

	const createYyyymmdd =
		document.getElementById(
			"createYyyymmdd"
		);

	const createHolidayName =
		document.getElementById(
			"createHolidayName"
		);


	createButton.addEventListener(
		"click",
		function() {

			let hasError = false;

			// エラー表示を初期化
			createYyyymmdd.classList.remove(
				"is-invalid"
			);

			createHolidayName.classList.remove(
				"is-invalid"
			);


			// 日付必須チェック
			if (createYyyymmdd.value === "") {

				createYyyymmdd.classList.add(
					"is-invalid"
				);

				hasError = true;

			}


			// 祝日名のエラー表示
			const createHolidayNameError =
				createHolidayName.nextElementSibling;

			// 祝日名必須チェック
			if (
				createHolidayName.value.trim()
				=== ""
			) {

				createHolidayName.classList.add(
					"is-invalid"
				);

				createHolidayNameError.textContent =
					"必須入力です。";

				hasError = true;

				// 祝日名文字数チェック
			} else if (
				createHolidayName.value.length > 10
			) {

				createHolidayName.classList.add(
					"is-invalid"
				);

				createHolidayNameError.textContent =
					"祝日名は10文字以内で入力してください。";

				hasError = true;
			}


			// エラーの場合は送信しない
			if (hasError) {
				return;
			}


			// 正常の場合のみ登録
			createForm.submit();

					}
			);


			// 更新モーダル表示
			updateButton.addEventListener(
				"click",
				function() {

			const selectedHoliday =
				document.querySelector(
					'input[name="selectedHoliday"]:checked'
				);

			if (selectedHoliday === null) {
				return;
			}


			// ID
			document.getElementById(
				"updateId"
			).value =
				selectedHoliday.value;


			// 日付
			document.getElementById(
				"updateYyyymmdd"
			).value =
				selectedHoliday.dataset.yyyymmdd;


			// 祝日名
			document.getElementById(
				"updateHolidayName"
			).value =
				selectedHoliday.dataset.holidayName;


			// モーダル表示
			const updateModalElement =
				document.getElementById(
					"updateModal"
				);

			const updateModal =
				bootstrap.Modal
					.getOrCreateInstance(
						updateModalElement
					);

			updateModal.show();

		}
	);


	// 更新処理
	const updateHolidayButton =
		document.getElementById(
			"updateHolidayButton"
		);

	const updateHolidayForm =
		document.getElementById(
			"updateHolidayForm"
		);

	const updateYyyymmdd =
		document.getElementById(
			"updateYyyymmdd"
		);

	const updateHolidayName =
		document.getElementById(
			"updateHolidayName"
		);


	updateHolidayButton.addEventListener(
		"click",
		function() {

			let hasError = false;


			// エラー表示を初期化
			updateYyyymmdd.classList.remove(
				"is-invalid"
			);

			updateHolidayName.classList.remove(
				"is-invalid"
			);


			// 日付必須チェック
			if (updateYyyymmdd.value === "") {

				updateYyyymmdd.classList.add(
					"is-invalid"
				);

				hasError = true;

			}


			// 祝日名のエラー表示
			const updateHolidayNameError =
				updateHolidayName.nextElementSibling;

			// 祝日名必須チェック
			if (
				updateHolidayName.value.trim()
				=== ""
			) {

				updateHolidayName.classList.add(
					"is-invalid"
				);

				updateHolidayNameError.textContent =
					"必須入力です。";

				hasError = true;

				// 祝日名文字数チェック
			} else if (
				updateHolidayName.value.length > 10
			) {

				updateHolidayName.classList.add(
					"is-invalid"
				);

				updateHolidayNameError.textContent =
					"祝日名は10文字以内で入力してください。";

				hasError = true;
			}


			// エラーの場合は更新しない
			if (hasError) {
				return;
			}


			// 正常の場合のみ更新
			updateHolidayForm.submit();

		}
	);


	// 削除モーダル表示
	deleteButton.addEventListener(
		"click",
		function() {

			const selectedHoliday =
				document.querySelector(
					'input[name="selectedHoliday"]:checked'
				);

			if (selectedHoliday === null) {
				return;
			}


			// 削除対象IDを設定
			document.getElementById(
				"deleteId"
			).value =
				selectedHoliday.value;


			// 削除モーダルを表示
			const deleteModalElement =
				document.getElementById(
					"deleteModal"
				);

			const deleteModal =
				bootstrap.Modal
					.getOrCreateInstance(
						deleteModalElement
					);

			deleteModal.show();

		}
	);

});