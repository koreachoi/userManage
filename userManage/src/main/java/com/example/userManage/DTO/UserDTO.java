package com.example.userManage.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class UserDTO {
	
	private Long userId;
	private String userCode;
	private String userName;
	private String password;
	private String userRole;
	private String isYoukou;
	
}
