package com.otp.login.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otp.login.entity.User;
import com.otp.login.repository.UserRepository;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SmsService smsService; // ✅ Injected Twilio SMS sender

    public String generateOtp(String username) {
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String otp = String.format("%06d", new Random().nextInt(999999));
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(15));
            user.setVerified(false);
            userRepo.save(user);

            // ✅ Send OTP via SMS
            if (user.getPhone() != null) {
                smsService.sendOtp(user.getPhone(), otp);
            }

            return otp;
        }

        return null;
    }

    public boolean verifyOtp(String username, String otpInput) {
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getOtp().equals(otpInput)
                    && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                user.setVerified(true);
                userRepo.save(user);
                return true;
            }
        }

        return false;
    }

    public boolean validateLogin(String username, String password, String otpInput) {
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return user.getPassword().equals(password)
                    && user.getOtp().equals(otpInput)
                    && user.getOtpExpiry().isAfter(LocalDateTime.now())
                    && user.isVerified();
        }

        return false;
    }
}
