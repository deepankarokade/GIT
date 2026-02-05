package com.git.Admin.Service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.git.Admin.Entity.Admin;
import com.git.Admin.Repository.AdminRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminService {

	private final AdminRepository adminRepository;
	private final EmailService emailService;

	@Value("${app.base-url}")
	private String baseUrl;

	// Constructor
	public AdminService(AdminRepository adminRepository, EmailService emailService) {
		this.adminRepository = adminRepository;
		this.emailService = emailService;
	}

	// Forgot Password
	public void processForgotPassword(String email) {
		Admin admin = adminRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("No admin found with email: " + email));

		String resetLink = baseUrl + "/admin/reset-password?uid=" + admin.getUid();
		emailService.sendForgotPasswordEmail(email, admin.getName(), resetLink);
	}

	public void resetPassword(String uid, String newPassword) {
		Admin admin = adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"));
		admin.setPassword(newPassword);
		adminRepository.save(admin);
	}

	// Login - AdminController.java
	public Admin login(String uid, String password) {

		Admin admin = adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found with UID " + uid));

		if (!admin.getPassword().equals(password)) {
			throw new RuntimeException("Invalid Password");
		}

		return admin;
	}

	// Get Profile - AdminProfileController.java
	public Admin getProfile(String uid) {
		return adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"));
	}

	// Change Password - AdminProfileController.java
	public void changePassword(String uid, String currentPassword, String newPassword) {

		Admin admin = adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		if (!admin.getPassword().equals(currentPassword)) {
			throw new RuntimeException("Current password incorrect");
		}

		admin.setPassword(newPassword);
		adminRepository.save(admin);
	}

	// Get Admin UID - AdminProfileController.java
	public String getAdminUid(String uid) {

		return adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"))
				.getUid();
	}

	// Get Admin name - AdminProfileController.java
	public String getAdminName(String uid) {

		return adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"))
				.getName();
	}

	// Get Admin Phone number - AdminProfileController.java
	public Long getAdminPhoneNumber(String uid) {
		return adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"))
				.getPhoneNumber();
	}

	// Get Admin Address
	public String getAdminAddress(String uid) {
		return adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"))
				.getAddress();
	}

	// Get Admin Email
	public String getAdminEmail(String uid) {
		return adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"))
				.getEmail();
	}

	// Update Admin name - AdminProfileController.java
	public void updateAdminName(String uid, String name) {
		Admin admin = adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"));
		System.out.println("Before update: " + admin.getName());
		admin.setName(name);
		System.out.println("After update: " + admin.getName());
		adminRepository.save(admin);
	}

	// Update Admin Phone Number - AdminProfileController.java
	public void updatePhoneNumber(String uid, Long phoneNumber) {
		Admin admin = adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		admin.setPhoneNumber(phoneNumber);

		adminRepository.save(admin);
	}

	// Update Admin Address
	public void updateAdminAddress(String uid, String address) {
		Admin admin = adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		admin.setAddress(address);

		adminRepository.save(admin);
	}

	// Update Admin Email
	public void updateAdminEmail(String uid, String email) {
		Admin admin = adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		admin.setEmail(email);

		adminRepository.save(admin);
	}

	// Get Admin Profile Pic
	public byte[] getProfilePicture(String uid) {
		Admin admin = adminRepository.findByUid(uid)
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		if (admin.getProfileImage() == null) {
			throw new RuntimeException("No Profile Picture found");
		}

		return admin.getProfileImage();
	}

	// Update Profile Picture
	public void updateProfilePicture(String uid, MultipartFile file) {
		try {
			Admin admin = adminRepository.findByUid(uid)
					.orElseThrow(() -> new RuntimeException("Admin not found"));

			admin.setProfileImage(file.getBytes());

			adminRepository.save(admin);
		} catch (IOException e) {
			throw new RuntimeException("Failed to store profile image ", e);
		}
	}
}
