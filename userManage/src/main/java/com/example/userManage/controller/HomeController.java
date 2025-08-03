package com.example.userManage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	// メイン画面へ遷移
	@GetMapping("/")
	public String home(Model model) {
		return "redirect:/login"; 
	}
}