package com.example.userManage.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Data
public class EmployeeDTO {

	private Long syainId;
	private String firstNameKanji;
	private String lastNameKanji;
	private String seibetu;
	private String syouzokuKaisya;
	private LocalDate nyuusyaDate;
	private LocalDate taisyaDate;
	private String syokugyoKind;
}
