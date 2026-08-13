package com.example.demo.controller.api;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.entity.UserEntity;
import com.example.demo.domain.model.LoginUser;
import com.example.demo.domain.service.UserService;
import com.example.demo.form.LoginUserDetailForm;
import com.example.demo.form.LoginUserSearchForm;
import com.example.demo.form.LoginUserUpdateForm;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
@Slf4j
public class UserApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * GET 社員一覧取得
	 */
	@GetMapping
	public UserEntity getUserList(
			@ModelAttribute LoginUserSearchForm form) {

		try {
			List<LoginUser> userList = userService.getUserList(form);

			int totalCount = userService.getUserCount(form);

			UserEntity userEntity = new UserEntity();
			userEntity.setUserList(userList);
			userEntity.setTotalCount(totalCount);

			return userEntity;

		} catch (Exception ex) {
			log.error(ex.getMessage());

			return new UserEntity();
		}
	}

	/**
	 * PUT 社員登録
	 */
	@PutMapping
	public UserEntity create(
			@RequestBody @Valid LoginUserDetailForm form,
			BindingResult bindingResult,
			Locale locale) {

		UserEntity userEntity = new UserEntity();

		try {
			// パスワード一致チェック
			if (form.getPassword() != null
					&& !form.getPassword().equals(form.getConfirmPassword())) {

				bindingResult.rejectValue(
						"confirmPassword",
						null,
						"パスワードと確認用パスワードが一致しません。");
			}

			// メールアドレス重複チェック
			if (form.getEmail() != null && !form.getEmail().isBlank()) {

				LoginUser registeredUser =
						userService.findByEmail(form.getEmail());

				if (registeredUser != null) {
					bindingResult.rejectValue(
							"email",
							null,
							"このメールアドレスは既に登録されています。");
				}
			}

			// 入力チェックエラー
			if (bindingResult.hasErrors()) {

				Map<String, String> errors = new HashMap<>();

				for (FieldError error : bindingResult.getFieldErrors()) {
					errors.put(
							error.getField(),
							messageSource.getMessage(
									error,
									locale));
				}

				userEntity.setErrors(errors);
				return userEntity;
			}

			LoginUser user = new LoginUser();
			user.setUserNo(form.getUserNo());
			user.setName(form.getName());
			user.setEmail(form.getEmail());
			user.setStartDate(
					LocalDate.parse(form.getStartDate()));
			user.setPassword(form.getPassword());
			user.setRoleCd(form.getRoleCd());

			userService.createUser(user);

			return userEntity;

		} catch (Exception ex) {
			log.error("社員登録に失敗しました。", ex);

			Map<String, String> errors = new HashMap<>();

			errors.put(
					"userNo",
					"この社員番号は既に使用されています。");

			userEntity.setErrors(errors);

			return userEntity;
		}
	}

	/**
	 * PATCH 社員更新
	 */
	@PatchMapping
	public UserEntity update(
			@RequestBody @Valid LoginUserUpdateForm form,
			BindingResult bindingResult,
			Locale locale) {

		UserEntity userEntity = new UserEntity();

		try {
			// パスワードまたは確認用パスワードが入力されている場合は一致チェック
			boolean hasPassword =
					form.getPassword() != null
					&& !form.getPassword().isEmpty();

			boolean hasConfirmPassword =
					form.getConfirmPassword() != null
					&& !form.getConfirmPassword().isEmpty();

			if (hasPassword || hasConfirmPassword) {

				if (!java.util.Objects.equals(
						form.getPassword(),
						form.getConfirmPassword())) {

					bindingResult.rejectValue(
							"confirmPassword",
							null,
							"パスワードと確認用パスワードが一致しません。");
	
				}
			}

			// 入力チェックエラー
			if (bindingResult.hasErrors()) {

				Map<String, String> errors = new HashMap<>();

				for (FieldError error : bindingResult.getFieldErrors()) {

					errors.put(
							error.getField(),
							messageSource.getMessage(
									error,
									locale));
				}

				userEntity.setErrors(errors);
				return userEntity;
			}

			LoginUser user = new LoginUser();
			user.setId(form.getId());
			user.setUserNo(form.getUserNo());
			user.setName(form.getName());
			user.setStartDate(
					LocalDate.parse(form.getStartDate()));
			user.setPassword(form.getPassword());
			user.setRoleCd(form.getRoleCd());

			userService.updateUser(user);

			return userEntity;

		} catch (Exception ex) {
			log.error(ex.getMessage());

			Map<String, String> errors = new HashMap<>();

			errors.put(
					"userNo",
					"この社員番号は既に使用されています。");

			userEntity.setErrors(errors);

			return userEntity;
		}
	}

	/**
	 * DELETE 社員削除
	 */
	@DeleteMapping
	public UserEntity delete(
			@RequestParam Long id) {

		UserEntity userEntity = new UserEntity();

		try {
			userService.deleteUser(id);

			return userEntity;

		} catch (Exception ex) {
			log.error(ex.getMessage());

			Map<String, String> errors =
					new HashMap<>();

			errors.put(
					"delete",
					"削除に失敗しました。");

			userEntity.setErrors(errors);

			return userEntity;
		}
	}

}