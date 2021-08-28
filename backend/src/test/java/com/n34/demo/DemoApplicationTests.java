package com.n34.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		PasswordEncoder passwordEncoder =
				new BCryptPasswordEncoder();
		String password = "123456";
		String hash1 = passwordEncoder.encode(password);
		String hash2 = passwordEncoder.encode(password);
		System.out.println("hash1: " + hash1);
		System.out.println("hash2: " + hash2);
		System.out.println("matches: " + passwordEncoder.matches(password, hash1));
		System.out.println("matches: " + passwordEncoder.matches(password, hash2));
	}

}
