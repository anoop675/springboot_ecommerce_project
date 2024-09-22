package com.anoopsen.SpringProject.dto;

import com.anoopsen.SpringProject.model.OtpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetResponseDto {

	private OtpStatus status;
	
	private String message;
}
