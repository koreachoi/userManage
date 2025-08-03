package com.example.userManage.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.userManage.DTO.UserDTO;
import com.example.userManage.service.AccessService;
import com.example.userManage.service.SettingService;
import com.example.userManage.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private AccessService accessService;

	// ユーザーログイン機能
	@PostMapping("/login")
	public String login(@RequestParam String userCode,
			@RequestParam String password,
			Model model,
			HttpSession session,
			HttpServletResponse response) {

		// 1. 受け取ったパラメーターをもと必要なデータを参照する。
		UserDTO userDto = userService.getUserByUserCode(userCode);

		// 2. 参照したデータが存在しているか確認する
		// 存在していない場合はエラーメッセージを表示する
		// データは存在しているが、パスワードが間違っている場合もエラーメッセージを表示する
		// ＊セッションを解除する
		if (userDto == null) {
			model.addAttribute("loginError", "DBエラー");
			session.invalidate();
			return "/user/login";
		} else if (!userDto.getPassword().equals(password)) {
			model.addAttribute("loginError", "パスワードが間違っています。");
			session.invalidate();
			return "/user/login";
		}

		// 3. ログイン成功の場合セッションを格納する
		session.setAttribute("loginUser", userDto);

		// 3-1. 現在時間(登録時間)を格納(フォーマット: 2017年06月11日　11時22分36秒)
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH時mm分ss秒");
		String formattedDateTime = now.format(formatter);
		session.setAttribute("loginTime", formattedDateTime); // 세션 저장

		// 3-2. ログインIDをクッキーで格納
		// 一週間保存
		Cookie idCookie = new Cookie("userCode", userCode);
		idCookie.setMaxAge(60 * 60 * 24 * 7);
		idCookie.setPath("/");
		response.addCookie(idCookie);

		// 3. 画面表示用のデータをModelに格納する
		model.addAttribute("loginUser", userDto); // ログインユーザー情報

		// 4.ユーザーの利用logを記録する
		accessService.createAccess(userDto.getUserId(), "login.html");

		return "redirect:/list"; //社員情報管理画面へ遷移
	}

	// ログイン画面へ遷移(クッキーを受け取る)
	@GetMapping("/login")
	public String showLoginForm(HttpServletRequest request, Model model) {

		// 1. クッキーからユーザーIDを取得して、入力欄にセットする
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("userCode".equals(cookie.getName())) {
					model.addAttribute("userCode", cookie.getValue());
					break;
				}
			}
		}

		return "/user/login"; //ログイン画面
	}

	// ログアウト機能
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		// 1. セッションを解除する
		session.invalidate();
		return "redirect:/login"; //ログイン画面へ遷移
	}

}
