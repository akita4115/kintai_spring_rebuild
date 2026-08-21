import { useEffect, useState } from "react";

const UserIndex = () => {
  // 検索条件
  const emptySearchForm = {
    userNo: "",
    name: "",
    email: "",
    startDate: "",
    roleCd: "",
  };

  const [searchForm, setSearchForm] = useState(emptySearchForm);

  // 社員一覧
  const [userList, setUserList] = useState([]);

  // 現在のページ番号
  const [page, setPage] = useState(1);

  // 検索結果の総件数
  const [totalCount, setTotalCount] = useState(0);

  // 1ページあたりの件数（バックエンドと合わせる）
  const PAGE_SIZE = 5;

  // 総ページ数
  const totalPages = Math.max(1, Math.ceil(totalCount / PAGE_SIZE));

  // 選択中の社員（ラジオボタンで選択）
  const [selectedUser, setSelectedUser] = useState(null);

  // 入力内容を検索条件へ設定
  const handleChange = (event) => {
    const { name, value } = event.target;

    setSearchForm({
      ...searchForm,
      [name]: value,
    });
  };

  // 社員一覧を取得
  const searchUsers = async (targetPage = 1, formValue = searchForm) => {
    const params = new URLSearchParams();

    if (formValue.userNo) {
      params.append("userNo", formValue.userNo);
    }

    if (formValue.name) {
      params.append("name", formValue.name);
    }

    if (formValue.email) {
      params.append("email", formValue.email);
    }

    if (formValue.startDate) {
      params.append("startDate", formValue.startDate);
    }

    if (formValue.roleCd) {
      params.append("roleCd", formValue.roleCd);
    }

    params.append("page", targetPage);

    try {
      const response = await fetch(`/api/user?${params.toString()}`);

      if (!response.ok) {
        throw new Error("社員一覧の取得に失敗しました。");
      }

      const data = await response.json();
      setUserList(data.userList ?? []);
      setTotalCount(data.totalCount ?? 0);
      setPage(targetPage);
      setSelectedUser(null);
    } catch (error) {
      console.error(error);
    }
  };

  // 検索ボタン押下時は1ページ目から検索し直す
  const handleSearch = () => {
    searchUsers(1);
  };

  // クリアボタン押下時：検索条件を空にして再検索
  const handleClear = () => {
    setSearchForm(emptySearchForm);
  };

  // ページ番号ボタン押下時
  const handlePageChange = (targetPage) => {
    if (targetPage < 1 || targetPage > totalPages) {
      return;
    }
    searchUsers(targetPage);
  };

  // 初回表示時に社員一覧を取得
  useEffect(() => {
    searchUsers(1);
  }, []);

  // ラジオボタンで社員を選択
  const handleSelectUser = (user) => {
    setSelectedUser(user);
  };

  // ==== 新規登録・更新（共通モーダル） ====

  // モーダルの表示・非表示、モード（create / update）
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState("create");

  const emptyDetailForm = {
    id: null,
    userNo: "",
    name: "",
    startDate: "",
    email: "",
    password: "",
    confirmPassword: "",
    roleCd: "0",
  };

  const [detailForm, setDetailForm] = useState(emptyDetailForm);

  // バックエンドから返るエラーメッセージ（フィールド名 → メッセージ）
  const [formErrors, setFormErrors] = useState({});

  // 新規登録モーダルを開く
  const handleOpenCreateModal = () => {
    setModalMode("create");
    setDetailForm(emptyDetailForm);
    setFormErrors({});
    setShowModal(true);
  };

  // 更新モーダルを開く（選択中の社員の情報を入れる）
  const handleOpenUpdateModal = () => {
    if (!selectedUser) {
      return;
    }

    setModalMode("update");
    setDetailForm({
      id: selectedUser.id,
      userNo: selectedUser.userNo,
      name: selectedUser.name,
      startDate: selectedUser.startDate,
      email: selectedUser.email,
      password: "",
      confirmPassword: "",
      roleCd: selectedUser.roleCd,
    });
    setFormErrors({});
    setShowModal(true);
  };

  //  削除 

  // 削除確認モーダルの表示・非表示
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  // 削除確認モーダルを開く
  const handleOpenDeleteModal = () => {
    if (!selectedUser) {
      return;
    }
    setShowDeleteModal(true);
  };

  // 削除確認モーダルを閉じる
  const handleCloseDeleteModal = () => {
    setShowDeleteModal(false);
  };

  // 削除実行
  const handleConfirmDelete = async () => {
    try {
      const response = await fetch(`/api/user?id=${selectedUser.id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error("削除に失敗しました。");
      }

      setShowDeleteModal(false);
      searchUsers(page);
    } catch (error) {
      console.error(error);
    }
  };

  // モーダルを閉じる
  const handleCloseModal = () => {
    setShowModal(false);
  };

  // 登録・更新フォームの入力内容を反映
  const handleDetailChange = (event) => {
    const { name, value } = event.target;

    setDetailForm({
      ...detailForm,
      [name]: value,
    });
  };

  // 登録・更新ボタン押下
  const handleSubmit = async () => {
    const isUpdate = modalMode === "update";

    try {
      const response = await fetch("/api/user", {
        method: isUpdate ? "PATCH" : "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(detailForm),
      });

      const data = await response.json();

      // エラーがある場合はモーダル内に表示して閉じない
      if (data.errors && Object.keys(data.errors).length > 0) {
        setFormErrors(data.errors);
        return;
      }

      // 成功時はモーダルを閉じて一覧を再取得
      setShowModal(false);
      setFormErrors({});
      searchUsers(page);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-3">社員マスタ管理</h2>

      <div className="card page-card mb-4 ">
        <div className="card-header bg-light py-3 px-4">検索条件</div>

        <div className="card-body px-4">
          <div className="row g-3 align-items-end">
            <div className="col-md-2">
              <label className="form-label">社員番号:</label>
              <input
                type="text"
                className="form-control"
                name="userNo"
                value={searchForm.userNo}
                onChange={handleChange}
              />
            </div>

            <div className="col-md-3">
              <label className="form-label">社員名:</label>
              <input
                type="text"
                className="form-control"
                name="name"
                value={searchForm.name}
                onChange={handleChange}
              />
            </div>

            <div className="col-md-3">
              <label className="form-label">メールアドレス:</label>
              <input
                type="text"
                className="form-control"
                name="email"
                value={searchForm.email}
                onChange={handleChange}
              />
            </div>

            <div className="col-md-2">
              <label className="form-label">入社日:</label>
              <input
                type="date"
                className="form-control"
                name="startDate"
                value={searchForm.startDate}
                onChange={handleChange}
              />
            </div>

            <div className="col-md-2">
              <label className="form-label">権限:</label>
              <select
                className="form-select"
                name="roleCd"
                value={searchForm.roleCd}
                onChange={handleChange}
              >
                <option value=""></option>
                <option value="0">一般</option>
                <option value="1">管理者</option>
              </select>
            </div>
          </div>

          <div className="mt-3 text-end">
            <button
              type="button"
              className="btn btn-warning me-2"
              onClick={handleClear}
            >
              クリア
            </button>

            <button
              type="button"
              className="btn btn-info"
              onClick={handleSearch}
            >
              検索
            </button>
          </div>
        </div>
      </div>
      <div className="card page-card">
        <div className="card-header bg-light py-3 px-4">検索結果</div>

        <div className="card-body">
          <div className="text-end mb-3">
            <button
              type="button"
              className="btn btn-success me-2"
              onClick={handleOpenCreateModal}
            >
              新規
            </button>

            <button
              type="button"
              className="btn btn-primary me-2"
              disabled={!selectedUser}
              onClick={handleOpenUpdateModal}
            >
              更新
            </button>

            <button
              type="button"
              className="btn btn-danger"
              disabled={!selectedUser}
              onClick={handleOpenDeleteModal}
            >
              削除
            </button>
          </div>

          {userList.length === 0 ? (
            <div className="alert alert-warning">
              検索結果がありませんでした。
            </div>
          ) : (
            <table className="table table-hover align-middle">
              <thead className="table-dark">
                <tr>
                  <th>#</th>
                  <th>社員番号</th>
                  <th>社員名</th>
                  <th>メールアドレス</th>
                  <th>入社日</th>
                  <th>権限</th>
                </tr>
              </thead>

              <tbody>
                {userList.map((user) => (
                  <tr
                    key={user.id}
                    onClick={() => handleSelectUser(user)}
                    className={
                      selectedUser?.id === user.id ? "table-active" : ""
                    }
                    role="button"
                  >
                    <td>
                      <input
                        type="radio"
                        name="selectedUser"
                        checked={selectedUser?.id === user.id}
                        onChange={() => handleSelectUser(user)}
                        onClick={(event) => event.stopPropagation()}
                      />
                    </td>

                    <td>{user.userNo}</td>
                    <td>{user.name}</td>
                    <td>{user.email}</td>
                    <td>{user.startDate}</td>
                    <td>{user.roleCd === "1" ? "管理者" : "一般"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}

          {/* ページング */}
          <nav>
            <ul className="pagination justify-content-center mb-0">
              <li className={`page-item ${page <= 1 ? "disabled" : ""}`}>
                <button
                  type="button"
                  className="page-link"
                  onClick={() => handlePageChange(page - 1)}
                >
                  前
                </button>
              </li>

              {Array.from({ length: totalPages }, (_, i) => i + 1).map((p) => (
                <li
                  key={p}
                  className={`page-item ${p === page ? "active" : ""}`}
                >
                  <button
                    type="button"
                    className="page-link"
                    onClick={() => handlePageChange(p)}
                  >
                    {p}
                  </button>
                </li>
              ))}

              <li
                className={`page-item ${page >= totalPages ? "disabled" : ""}`}
              >
                <button
                  type="button"
                  className="page-link"
                  onClick={() => handlePageChange(page + 1)}
                >
                  次
                </button>
              </li>
            </ul>
          </nav>
        </div>
      </div>

      {/* 新規登録・更新モーダル */}
      {showModal && (
        <div className="modal d-block bg-dark bg-opacity-50">
          <div className="modal-dialog modal-xl">
            <div className="modal-content">
              {/* モーダルヘッダー */}
              <div className="modal-header bg-info text-white">        

                <h5 className="modal-title">
                  {modalMode === "update" ? "社員更新" : "社員登録"}
                </h5>

                <button
                  type="button"
                  className="btn-close"
                  onClick={handleCloseModal}
                />
              </div>

              {/* モーダル本文 */}
              <div className="modal-body p-4">
                <div className="border rounded">
                  <div className="bg-light border-bottom px-3 py-2">
                    社員情報
                  </div>

                  <div className="p-3">
                    {/* 1行目 */}
                    <div className="row g-4">
                      {/* 社員番号 */}
                      <div className="col-md-3">
                        <label className="form-label">社員番号:</label>

                        <input
                          type="text"
                          className={`form-control ${
                            formErrors.userNo ? "is-invalid" : ""
                          }`}
                          name="userNo"
                          value={detailForm.userNo}
                          onChange={handleDetailChange}
                        />

                        <div className="invalid-feedback">
                          {formErrors.userNo}
                        </div>
                      </div>

                      {/* 社員名 */}
                      <div className="col-md-4">
                        <label className="form-label">社員名:</label>

                        <input
                          type="text"
                          className={`form-control ${
                            formErrors.name ? "is-invalid" : ""
                          }`}
                          name="name"
                          value={detailForm.name}
                          onChange={handleDetailChange}
                        />

                        <div className="invalid-feedback">
                          {formErrors.name}
                        </div>
                      </div>

                      {/* 入社日 */}
                      <div className="col-md-3">
                        <label className="form-label">入社日:</label>

                        <input
                          type="date"
                          className={`form-control ${
                            formErrors.startDate ? "is-invalid" : ""
                          }`}
                          name="startDate"
                          value={detailForm.startDate}
                          onChange={handleDetailChange}
                        />

                        <div className="invalid-feedback">
                          {formErrors.startDate}
                        </div>
                      </div>

                      {/* 権限 */}
                      <div className="col-md-2">
                        <label className="form-label">権限:</label>

                        <select
                          className={`form-select ${
                            formErrors.roleCd ? "is-invalid" : ""
                          }`}
                          name="roleCd"
                          value={detailForm.roleCd}
                          onChange={handleDetailChange}
                        >
                          <option value="0">一般</option>
                          <option value="1">管理者</option>
                        </select>

                        <div className="invalid-feedback">
                          {formErrors.roleCd}
                        </div>
                      </div>
                    </div>

                    {/* 2行目 */}
                    <div className="row g-4 mt-1">
                      {/* メールアドレス */}
                      <div className="col-md-4">
                        <label className="form-label">メールアドレス:</label>

                        <input
                          type="email"
                          className={`form-control ${
                            formErrors.email ? "is-invalid" : ""
                          }`}
                          name="email"
                          value={detailForm.email}
                          onChange={handleDetailChange}
                          disabled={modalMode === "update"}
                        />

                        <div className="invalid-feedback">
                          {formErrors.email}
                        </div>
                      </div>

                      {/* パスワード */}
                      <div className="col-md-4">
                        <label className="form-label">
                          パスワード:
                          {modalMode === "update" && (
                            <span className="text-muted small ms-1">
                              （変更する場合のみ入力）
                            </span>
                          )}
                        </label>

                        <input
                          type="password"
                          className={`form-control ${
                            formErrors.password || formErrors.confirmPassword
                              ? "is-invalid"
                              : ""
                          }`}
                          name="password"
                          value={detailForm.password}
                          onChange={handleDetailChange}
                        />

                        <div className="invalid-feedback">
                          {formErrors.password}
                        </div>
                      </div>

                      {/* 確認用パスワード */}
                      <div className="col-md-4">
                        <label className="form-label">確認用パスワード:</label>

                        <input
                          type="password"
                          className={`form-control ${
                            formErrors.confirmPassword ? "is-invalid" : ""
                          }`}
                          name="confirmPassword"
                          value={detailForm.confirmPassword}
                          onChange={handleDetailChange}
                        />

                        <div className="invalid-feedback">
                          {formErrors.confirmPassword}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              {/* モーダルフッター */}
              <div className="modal-footer px-4 py-2">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={handleCloseModal}
                >
                  キャンセル
                </button>

                <button
                  type="button"
                  className={
                    modalMode === "update"
                      ? "btn btn-primary"
                      : "btn btn-success"
                  }
                  onClick={handleSubmit}
                >
                  {modalMode === "update" ? "更新" : "登録"}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* 削除確認モーダル */}
      {showDeleteModal && selectedUser && (
        <div className="modal d-block bg-dark bg-opacity-50">
          <div className="modal-dialog modal-md">
            <div className="modal-content">
              <div className="modal-header bg-info text-white">
                <h5 className="modal-title">社員削除</h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={handleCloseDeleteModal}
                />
              </div>

              <div className="modal-body">
                <p>選択した社員を削除しますか？</p>
              </div>

              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={handleCloseDeleteModal}
                >
                  キャンセル
                </button>
                <button
                  type="button"
                  className="btn btn-danger"
                  onClick={handleConfirmDelete}
                >
                  削除
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserIndex;
