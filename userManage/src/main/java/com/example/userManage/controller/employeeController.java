package com.example.userManage.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
import com.example.userManage.DTO.EmployeeRequestDTO;
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

	// 社員管理画面
	@GetMapping("/list")
	public String getList(HttpSession session, Model model) {
		
		// 1．セッションからユーザー情報を取得する
		// ログインしていない場合はログインページにリダイレクトする
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);
		if (userDto == null) {
			return "redirect:/login";
		}

		// 2. 必要なデータを参照する。
		List<SettingDTO> syozokuKaisyaDTO = settingService.getSettingByCategoryList(1L, null, 1L);
		List<SettingDTO> syokugyoKindDTO = settingService.getSettingByCategoryList(3L, 4L, null);
		List<EmployeeDTO> allEmployeeDTO = employeeService.getAllEmployee();

		// 3. 画面表示用のデータをModelに格納する
		model.addAttribute("loginUser", userDto); // ログインユーザー情報
		model.addAttribute("kaisya", syozokuKaisyaDTO); // 所属会社情報
		model.addAttribute("syokugyo", syokugyoKindDTO); // 職業種類情報
		model.addAttribute("employees", allEmployeeDTO); //社員情報
		model.addAttribute("currentPage", "listPage"); // ページ確認用情報

		return "/user/list"; // 社員管理画面

	}

	// 社員情報参照
	@GetMapping("/employees")
	public String search(HttpSession session,
			@RequestParam Long company,
			@RequestParam Long jobType,
			@RequestParam String employeeName,
			@RequestParam(name = "status", required = false) List<String> statusList,
			Model model) {
		
		// 1．セッションからユーザー情報を取得する
		// ログインしていない場合はログインページにリダイレクトする
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);
		if (userDto == null) {
			return "redirect:/login";
		}
		
		// 2. 在職確認
		// 在職チェックがNullではない場合はチェックを行う
		// 両側チェックされている場合は、全体を条件で設定しておく
		// 在職だけチェックされている場合は、在職の変数の値を１Lで設定しておく
		// 逆の場合は０で設定しておく
		if (statusList != null) {
			if (statusList.contains("active") && statusList.contains("inactive")) {
				zaisyoku = null; 
			} else if (statusList.contains("active")) {
				zaisyoku = 1L; // 재직 중
			} else if (statusList.contains("inactive")) {
				zaisyoku = 0L; // 퇴사
			}
		}
		// 所属会社確認
		// 所属会社の値が０(全て)の場合はNullで設定しておく
		if (company == 0) {
			company = null;
		}
		
		// 3. 必要なデータを参照する。
		List<EmployeeDTO> employeesDTO = employeeService.getByFilter(employeeName, employeeName, company, jobType,
				zaisyoku);
		List<SettingDTO> syozokuKaisyaDTO = settingService.getSettingByCategoryList(1L, null, 1L);
		List<SettingDTO> syokugyoKindDTO = settingService.getSettingByCategoryList(3L, 4L, null);

		// 4. 画面表示用のデータをModelに格納する
		model.addAttribute("loginUser", userDto); // ログインユーザー情報
		model.addAttribute("kaisya", syozokuKaisyaDTO); // 所属会社情報
		model.addAttribute("syokugyo", syokugyoKindDTO); // 職業種類情報
		model.addAttribute("employees", employeesDTO); // 社員情報
		model.addAttribute("currentPage", "listPage"); // ページ確認用情報
		
		return "/user/list"; // 社員管理画面
	}

	// 社員情報登録画面へ遷移
	@GetMapping("/register")
	public String moveRegisterPage(HttpSession session, Model model) {
		
		// 1．セッションからユーザー情報を取得する
		// ログインしていない場合はログインページにリダイレクトする
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);
		if (userDto == null) {
			return "redirect:/login";
		}
		
		// 2. 必要なデータを参照する。
		List<SettingDTO> syozokuKaisyaDTO = settingService.getSettingByCategoryList(1L, null, 1L);
		List<SettingDTO> syokugyoKindDTO = settingService.getSettingByCategoryList(3L, 4L, null);
		List<SettingDTO> osDTO = settingService.getSettingByCategoryList(3L, 6L, null);

		// 3. 画面表示用のデータをModelに格納する
		model.addAttribute("loginUser", userDto); // ログインユーザー情報
		model.addAttribute("syokugyoList", syokugyoKindDTO); // 所属会社情報
		model.addAttribute("kaisyaList", syozokuKaisyaDTO); // 職業種類情報
		model.addAttribute("osList", osDTO); // OSデータリスト情報
		model.addAttribute("currentPage", "specialPage"); // ページ確認用情報
		
		return "/user/register"; // 社員情報登録画面
	}

	// 社員情報登録機能
	@PostMapping("/employeeRegister")
	public String actionRegister(@RequestParam("syainId") String syainId,
			@RequestParam("lastNameKanji") String lastNameKanji,
			@RequestParam("firstNameKanji") String firstNameKanji,
			@RequestParam("seibetu") Long seibetu,
			@RequestParam("syouzokuKaisya") Long syouzokuKaisya,
			@RequestParam("nyuusyaDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate nyuusyaDate,
			@RequestParam("taisyaDate") String taisyaDateStr,
			@RequestParam("syokugyoKind") Long syokugyoKind,
			HttpSession session,
			Model model) {
		
		// 1．セッションからユーザー情報を取得する
		// ログインしていない場合はログインページにリダイレクトする
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);
		if (userDto == null) {
			return "redirect:/login";
		}
		
		// 2．パラメーターtaisyaDateの値がnullでない場合
		// フォーマットを確認し、正しい形式のものだけを受け取る
		LocalDate taisyaDate = null;
	    if (taisyaDateStr != null && !taisyaDateStr.isBlank()) {
	        if (taisyaDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
	        	 // "0000-00-00"の場合はLocalDateに変換できないため、別途処理する
	            if (!taisyaDateStr.equals("0000-00-00")) {
	                try {
	                    taisyaDate = LocalDate.parse(taisyaDateStr);
	                } catch (DateTimeParseException e) {
	                	// 無効なフォーマットの場合はログのみ出力する
	                    System.out.println("Invalid date: " + taisyaDateStr);
	                }
	            }
	        }
	    }
		
		// 3. 受け取ったパラメーターをもとにDTOインスタンスを生成する
		// 生成したインスタンスを使ってDBのテーブルを登録する
		EmployeeRequestDTO employeeDto = new EmployeeRequestDTO(null, firstNameKanji, lastNameKanji, seibetu, syouzokuKaisya, nyuusyaDate, taisyaDate, syokugyoKind);
		employeeService.createSyain(employeeDto);

		// 4. 画面表示用のデータをModelに格納する
		model.addAttribute("loginUser", userDto); // ログインユーザー情報

		return "redirect:/list"; //社員管理画面へ遷移
	}

	// 社員情報更新画面へ遷移
	@GetMapping("/employee/edit/{id}")
	public String moveUpdatePage(@PathVariable("id") Long syainId, HttpSession session, Model model) {
		
		// 1．セッションからユーザー情報を取得する
		// ログインしていない場合はログインページにリダイレクトする
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);
		if (userDto == null) {
			return "redirect:/login";
		}
		
		// 2. 必要なデータを参照する。
		EmployeeDTO employeeDto = employeeService.findBySyainId(syainId);
		List<SettingDTO> syozokuKaisyaDTO = settingService.getSettingByCategoryList(1L, null, 1L);
		List<SettingDTO> syokugyoKindDTO = settingService.getSettingByCategoryList(3L, 4L, null);

		// 3. 画面表示用のデータをModelに格納する
		model.addAttribute("loginUser", userDto); // ログインユーザー情報
		model.addAttribute("syain", employeeDto); // 選択社員情報
		model.addAttribute("kaisya", syozokuKaisyaDTO); // 所属会社情報
		model.addAttribute("syokugyo", syokugyoKindDTO); // 職業種類情報
		model.addAttribute("currentPage", "specialPage"); // ページ確認用情報
		
		return "/user/update"; //社員情報更新画面へ遷移
	}
	
	// 社員情報更新機能
	@PostMapping("/employeeUpdate")
	public String actionUpdate(@RequestParam("syainId") Long syainId,
			@RequestParam("lastNameKanji") String lastNameKanji,
			@RequestParam("firstNameKanji") String firstNameKanji,
			@RequestParam("seibetu") Long seibetu,
			@RequestParam("syouzokuKaisya") Long syouzokuKaisya,
			@RequestParam("nyuusyaDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate nyuusyaDate,
			@RequestParam("taisyaDate") String taisyaDateStr,
			@RequestParam("syokugyoKind") Long syokugyoKind,
			HttpSession session,
			Model model) {
		
		// 1．セッションからユーザー情報を取得する
		// ログインしていない場合はログインページにリダイレクトする
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);
		if (userDto == null) {
			return "redirect:/login";
		}
		
		// 2．パラメーターtaisyaDateの値がnullでない場合
		// フォーマットを確認し、正しい形式のものだけを受け取る
		LocalDate taisyaDate = null;
	    if (taisyaDateStr != null && !taisyaDateStr.isBlank()) {
	        if (taisyaDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
	        	 // "0000-00-00"の場合はLocalDateに変換できないため、別途処理する
	            if (!taisyaDateStr.equals("0000-00-00")) {
	                try {
	                    taisyaDate = LocalDate.parse(taisyaDateStr);
	                } catch (DateTimeParseException e) {
	                	// 無効なフォーマットの場合はログのみ出力する
	                    System.out.println("Invalid date: " + taisyaDateStr);
	                }
	            }
	        }
	    }
		
		// 3．受け取ったパラメーターをもとにDTOインスタンスを生成する
		// 生成したインスタンスを使ってDBのテーブルを更新する
		EmployeeRequestDTO employeeDto = new EmployeeRequestDTO(syainId, firstNameKanji, lastNameKanji, seibetu, syouzokuKaisya, nyuusyaDate, taisyaDate, syokugyoKind);
		employeeService.updateSyain(employeeDto);
		
		// 4. 画面表示用のデータをModelに格納する
		model.addAttribute("loginUser", userDto); // ログインユーザー情報
		
		return  "redirect:/list"; //社員管理画面へ遷移
	}
	
	// 社員情報削除機能
	@PostMapping("/employee/delete")
	public String deleteEmployee(@RequestParam("syainId") Long syainId, RedirectAttributes redirectAttributes,
			HttpSession session, Model model) {
		// 1．セッションからユーザー情報を取得する
		// ログインしていない場合はログインページにリダイレクトする
		UserDTO userDto = (UserDTO) session.getAttribute("loginUser");
		session.setAttribute("loginUser", userDto);
		if (userDto == null) {
			return "redirect:/login";
		}
		
		// 2．受け取ったパラメーターを使ってDBのレコードを削除する
		// 3．処理結果にかかわらずメッセージを表示する
		try {
			employeeService.deleteBySyainId(syainId); // 실제 삭제 처리
			redirectAttributes.addFlashAttribute("msg", "削除しました。");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("msg", "削除に失敗しました。");
		}
		
		// 3. 画面表示用のデータをModelに格納する
		model.addAttribute("loginUser", userDto); // ログインユーザー情報
		
		return "redirect:/list"; //社員管理画面へ遷移
	}
}
