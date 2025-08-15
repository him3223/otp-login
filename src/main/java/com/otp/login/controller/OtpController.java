package com.otp.login.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otp.login.service.OtpService;

@RestController
@RequestMapping("/api")
public class OtpController {
	@Autowired
	private OtpService otpService;
	@PostMapping("/send-otp")
	public Map<String, String> sendOtp(@RequestParam String username) {
		String otp = otpService.generateOtp(username);
		Map<String, String> response = new HashMap<>();
		if (otp!=null) {
			System.out.println("OTP for username"+username+": "+otp);
			response.put("message", "Otp Sent Successfully");
		}
		else {
			response.put("message", "User not found");
		}
		return response;
	}
	@PostMapping("verify-otp")
	public Map<String, String>verifyOtp(@RequestParam String username, @RequestParam String otp) {
		boolean inValid = otpService.verifyOtp(username,otp);
		Map<String, String> response = new HashMap<>();
		response.put("status",inValid ? "verified":"invalid or expired otp");
		return response;
	}
	@PostMapping("/login")
	public Map<String, String> login(@RequestParam String username,
			@RequestParam String password,
			@RequestParam String otp){
			boolean isLoggedIn = otpService.validateLogin(username, password, otp);
			Map<String, String> response = new HashMap<>();
			response.put("status",isLoggedIn ? "success":"login failed");
			return response;
	}
}
