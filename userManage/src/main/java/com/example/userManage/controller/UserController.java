package com.example.userManage.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

import com.example.userManage.DTO.SettingDTO;
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
	
	@PostMapping("/login")
	public String login(@RequestParam String userCode,
           				@RequestParam String password,
           							   Model model,           						 
           						 HttpSession session,
           				 HttpServletResponse response) {
		// 1.
		UserDTO userDto = userService.getUserByUserCode(userCode);
		List<SettingDTO> syozokuKaisyaDTO = settingService.getSettingByCategoryList(1L, null, 1L);
		List<SettingDTO> syokugyoKindDTO = settingService.getSettingByCategoryList(3L,4L, null);
        // 2. 사용자 존재 여부 및 비밀번호 확인
		if (userDto == null) {
			// 로그인 실패 - 에러 메시지 전달
            model.addAttribute("loginError", "DBエラー");
            session.invalidate();  // 모든 세션 데이터를 제거
            return "/user/login";  
		} else if (!userDto.getPassword().equals(password)) {
			// 로그인 실패 - 에러 메시지 전달
            model.addAttribute("loginError", "パスワードが間違っています。");
            session.invalidate();  // 모든 세션 데이터를 제거
            return "/user/login";  
		}
		// 3. 로그인 성공 - 세션 저장
        session.setAttribute("loginUser", userDto);
        
        // ① 현재 시간 저장 (포맷: 2017年06月11日　11時22分36秒)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH時mm分ss秒");
        String formattedDateTime = now.format(formatter);
        session.setAttribute("loginTime", formattedDateTime); // 세션 저장
        
        // 3. 아이디를 쿠키에 자동 저장 (체크박스 없이 무조건 저장)
        Cookie idCookie = new Cookie("userCode", userCode);
        idCookie.setMaxAge(60 * 60 * 24 * 7); // 7일간 유지
        idCookie.setPath("/"); // 전역 경로
        response.addCookie(idCookie);
        
        // 3.
        model.addAttribute("loginUser", userDto);
        model.addAttribute("kaisya", syozokuKaisyaDTO);
        model.addAttribute("syokugyo",syokugyoKindDTO);
        
        // 4
        accessService.createAccess(userDto.getUserId(), "login.html");
        // 5.
        System.out.println("test");
        return "redirect:/list";
	}
	
    // 로그인 폼 GET (쿠키 값 읽어오기)
    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request, Model model) {
    	
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userCode".equals(cookie.getName())) {
                    model.addAttribute("userCode", cookie.getValue());
                    break;
                }
            }
        }
        return "/user/login";
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 완전 종료
        return "redirect:/login"; // 로그인 페이지로 이동
    }
	
}
