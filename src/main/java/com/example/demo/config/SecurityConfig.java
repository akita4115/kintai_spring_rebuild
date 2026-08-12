package com.example.demo.config;


import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(
			org.springframework.security.config.annotation.web.builders.HttpSecurity http)
			throws Exception {

		http.csrf(csrf -> csrf
				.ignoringRequestMatchers("/api/**"));

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login/**", "/error").permitAll()

				// 社員マスタ管理は管理者のみ
				.requestMatchers("/user/**", "/api/user/**")
				.hasRole("ADMIN")

				// 勤怠管理画面は管理者のみ
				.requestMatchers("/attendance/manage/**")
				.hasRole("ADMIN")

				// その他はログイン必須
				.anyRequest().authenticated());

		http.formLogin(form -> form
				.loginPage("/login")
				.failureUrl("/login?error")

				// ログイン成功後、権限ごとに遷移先を変更
				.successHandler(this::loginSuccessHandler)

				.permitAll());

		http.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
				.permitAll());

		return http.build();
	}

	/**
	 * ログイン成功後の遷移処理
	 */
	private void loginSuccessHandler(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication)
			throws IOException, ServletException {

		boolean isAdmin = authentication.getAuthorities()
				.stream()
				.anyMatch(authority ->
						"ROLE_ADMIN".equals(authority.getAuthority()));

		if (isAdmin) {
			// 管理ユーザ
			response.sendRedirect("/attendance/manage");

		} else {
			// 一般ユーザ
			response.sendRedirect("/attendance/input");
		}
	}
}