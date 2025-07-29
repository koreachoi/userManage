package com.example.userManage.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class AccessDTO {
	
	private Long id;
	private Long userId;
	private String gamenId;
	private LocalDateTime startTime;
	
	public AccessDTO(Long userId, String gamenId, LocalDateTime startTime){
		this.userId = userId;
		this.gamenId = gamenId;
		this.startTime = startTime;
	}
}
