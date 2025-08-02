package com.example.userManage.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.userManage.DTO.EmployeeDTO;
import com.example.userManage.DTO.EmployeeInsertDTO;
import com.example.userManage.DTO.SettingDTO;
import com.example.userManage.DTO.UserDTO;
import com.example.userManage.service.EmployeeService;
import com.example.userManage.service.SettingService;
import com.example.userManage.service.UserService;

@Controller
public class employeeController {

	@Autowired
	private UserService userService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private EmployeeService employeeService;

	private Long zaisyoku;

	@GetMapping("/list")
	public String getList(HttpSession session, Model model) {
		// 1. 세션에서 로그인 사용자 정보 꺼내기
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);

		if (userDto == null) {
			// 로그인 안 했으면 로그인 페이지로 이동!
			return "redirect:/login";
		}

		// 2. 필요한 데이터 조회 (회사정보, 직종정보 등)
		List<SettingDTO> syozokuKaisyaDTO = settingService.getSettingByCategoryList(1L, null, 1L);
		List<SettingDTO> syokugyoKindDTO = settingService.getSettingByCategoryList(3L, 4L, null);
		List<EmployeeDTO> allEmployeeDTO = employeeService.getAllEmployee();

		// 4. 화면에 쓸 데이터 model에 담기
		model.addAttribute("loginUser", userDto); // 로그인 사용자 정보
		model.addAttribute("kaisya", syozokuKaisyaDTO); // 소속 회사 정보
		model.addAttribute("syokugyo", syokugyoKindDTO); // 직종 정보
		model.addAttribute("employees", allEmployeeDTO);

		return "/user/list"; // 목록 화면 템플릿

	}

	@GetMapping("/employees")
	public String search(HttpSession session,
			@RequestParam Long company,
			@RequestParam Long jobType,
			@RequestParam String employeeName,
			@RequestParam(name = "status", required = false) List<String> statusList,
			Model model) {

		if (statusList != null) {
			if (statusList.contains("active") && statusList.contains("inactive")) {
				// 둘 다 체크됨 → 전체 검색
				zaisyoku = null; // 조건 생략
			} else if (statusList.contains("active")) {
				zaisyoku = 1L; // 재직 중
			} else if (statusList.contains("inactive")) {
				zaisyoku = 0L; // 퇴사
			}
		}
		if (company == 0) {
			company = null;
		}

		List<EmployeeDTO> employeesDTO = employeeService.getByFilter(employeeName, employeeName, company, jobType,
				zaisyoku);
		List<SettingDTO> syozokuKaisyaDTO = settingService.getSettingByCategoryList(1L, null, 1L);
		List<SettingDTO> syokugyoKindDTO = settingService.getSettingByCategoryList(3L, 4L, null);
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);

		model.addAttribute("loginUser", userDto);
		model.addAttribute("kaisya", syozokuKaisyaDTO);
		model.addAttribute("syokugyo", syokugyoKindDTO);
		model.addAttribute("employees", employeesDTO);
		return "/user/list";
	}

	@GetMapping("/register")
	public String moveRegisterPage(HttpSession session, Model model) {

		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		List<SettingDTO> syozokuKaisyaDTO = settingService.getSettingByCategoryList(1L, null, 1L);
		List<SettingDTO> syokugyoKindDTO = settingService.getSettingByCategoryList(3L, 4L, null);
		List<SettingDTO> osDTO = settingService.getSettingByCategoryList(3L, 6L, null);
		if (userDto == null) {
			System.out.println("null");
		}

		session.setAttribute("loginUser", userDto);

		model.addAttribute("osList", osDTO);
		model.addAttribute("syokugyoList", syokugyoKindDTO);
		model.addAttribute("kaisyaList", syozokuKaisyaDTO);
		model.addAttribute("loginUser", userDto);

		return "/user/register";
	}

	@PostMapping("/employeeRegister")
	public String actionRegister(@RequestParam("syainId") String syainId,
			@RequestParam("lastNameKanji") String lastNameKanji,
			@RequestParam("firstNameKanji") String firstNameKanji,
			@RequestParam("seibetu") Long seibetu,
			@RequestParam("company") Long company,
			@RequestParam("nyuusyaDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate nyuusyaDate,
			@RequestParam("syokugyoKind") Long syokugyoKind,
			HttpSession session,
			Model model) {

		EmployeeInsertDTO employeeInsertDto = new EmployeeInsertDTO(lastNameKanji, firstNameKanji, seibetu, company,
				nyuusyaDate, nyuusyaDate, syokugyoKind);
		employeeService.createSyain(employeeInsertDto);

		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);

		List<SettingDTO> syozokuKaisyaDTO = settingService.getSettingByCategoryList(1L, null, 1L);
		List<SettingDTO> syokugyoKindDTO = settingService.getSettingByCategoryList(3L, 4L, null);
		List<EmployeeDTO> allEmployeeDTO = employeeService.getAllEmployee();

		// 4. 화면에 쓸 데이터 model에 담기
		model.addAttribute("loginUser", userDto); // 로그인 사용자 정보
		model.addAttribute("kaisya", syozokuKaisyaDTO); // 소속 회사 정보
		model.addAttribute("syokugyo", syokugyoKindDTO); // 직종 정보
		model.addAttribute("employees", allEmployeeDTO);

		return "/user/list";
	}

	@GetMapping("/employee/edit/{id}")
	public String moveUpdatePage(@PathVariable("id") Long syainId, HttpSession session, Model model) {
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		if (userDto == null) {
			System.out.println("null");
		}
		session.setAttribute("loginUser", userDto);
		model.addAttribute("loginUser", userDto);
		// 3. 수정 화면(view)으로 이동
		return "/user/update";
	}

	@PostMapping("/employee/delete")
	public String deleteEmployee(@RequestParam("syainId") Long syainId, RedirectAttributes redirectAttributes,
			HttpSession session, Model model) {
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		if (userDto == null) {
			System.out.println("null");
		}
		session.setAttribute("loginUser", userDto);
		model.addAttribute("loginUser", userDto);

		try {
			employeeService.deleteBySyainId(syainId); // 실제 삭제 처리
			redirectAttributes.addFlashAttribute("msg", "削除しました。");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("msg", "削除に失敗しました。");
		}
		return "redirect:/list"; // 삭제 후 목록 화면 등으로 이동
	}
}
