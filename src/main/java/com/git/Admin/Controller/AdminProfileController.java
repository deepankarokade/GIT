package com.git.Admin.Controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.git.Admin.Entity.Admin;
import com.git.Admin.Service.AdminService;

@RestController
@RequestMapping("/admin/profile")
@CrossOrigin
public class AdminProfileController {

	private final AdminService adminService;

	public AdminProfileController(AdminService adminService) {
		this.adminService = adminService;
	}

	// Get Profile
	@GetMapping("/{uid}")
	public Admin getProfile(@PathVariable String uid) {
		return adminService.getProfile(uid);
	}

	// Get Admin Name
	@GetMapping("/{uid}/name")
	public String getAdminName(@PathVariable String uid) {
		return adminService.getAdminName(uid);
	}

	// Update Admin Name
	@PutMapping("/{uid}/name")
	public void updateAdminName(
			@PathVariable String uid,
			@RequestBody Map<String, String> body) {
		adminService.updateAdminName(uid, body.get("name"));
	}

	// Get Admin Phone Number
	@GetMapping("/{uid}/phone-number")
	public Long getAdminPhoneNumber(@PathVariable String uid) {
		return adminService.getAdminPhoneNumber(uid);
	}

	// Get Admin Address
	@GetMapping("/{uid}/address")
	public String getAdminAddress(@PathVariable String uid) {
		return adminService.getAdminAddress(uid);
	}

	// Get Admin Email
	@GetMapping("/{uid}/email")
	public String getAdminEmail(@PathVariable String uid) {
		return adminService.getAdminEmail(uid);
	}

	// Update Admin Phone Number
	@PutMapping("/{uid}/phone-number")
	public void updatePhoneNumber(
			@PathVariable String uid,
			@RequestParam(required = false) Long phoneNumber,
			@RequestBody(required = false) Map<String, String> body) {
		// Accept either query parameter or JSON body (with non-digit chars sanitized)
		if (phoneNumber != null) {
			adminService.updatePhoneNumber(uid, phoneNumber);
			return;
		}

		if (body != null && body.get("phoneNumber") != null) {
			String cleaned = body.get("phoneNumber").replaceAll("\\D", "");
			adminService.updatePhoneNumber(uid, Long.parseLong(cleaned));
			return;
		}

		throw new RuntimeException("phoneNumber not provided");
	}

	// Update Admin Address
	@PutMapping("/{uid}/address")
	public void updateAdminAddress(
			@PathVariable String uid,
			@RequestBody Map<String, String> body) {
		adminService.updateAdminAddress(uid, body.get("address"));
	}

	// Update Admin Email
	@PutMapping("/{uid}/email")
	public void updateAdminEmail(
			@PathVariable String uid,
			@RequestBody Map<String, String> body) {
		adminService.updateAdminEmail(uid, body.get("email"));
	}

	// Change Admin Password
	@PostMapping("/{uid}/change-password")
	public void changePassword(
			@PathVariable String uid,
			@RequestBody Map<String, String> body) {

		adminService.changePassword(uid, body.get("currentPassword"), body.get("newPassword"));
	}

	// Get Admin Profile Picture
	@GetMapping("/{uid}/profile-picture")
	public ResponseEntity<byte[]> getAdminProfilePicture(@PathVariable String uid) {

		byte[] image = adminService.getProfilePicture(uid);

		return ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(image);
	}

	// Update Admin Profile Picture
	@PostMapping("/{uid}/profile-picture")
	public ResponseEntity<Void> updateProfilePicture(
			@PathVariable String uid,
			@RequestParam("file") MultipartFile file) {
		adminService.updateProfilePicture(uid, file);
		return ResponseEntity.ok().build();
	}
}
