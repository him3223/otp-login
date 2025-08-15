package com.otp.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otp.login.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
