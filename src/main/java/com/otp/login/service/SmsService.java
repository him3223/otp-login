package com.otp.login.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {
	@Value("${twilio.phone.number}")
	private String fromNumber;
	public void sendOtp(String toNumber, String otp) {
		Message.creator(
				new PhoneNumber(toNumber),
				new PhoneNumber(fromNumber),
				"Your OTP is: "+ otp
		).create();
		System.out.println("✅ SMS Sent to " + toNumber);
	}
}
