package com.git.Admin.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.git.Activity;
import com.git.Admin.Entity.Faculty;
import com.git.Admin.Repository.FacultyRepository;

@Service
public class FacultyService {

	private final FacultyRepository facultyRepository;

	private final EmailService emailService;

	public FacultyService(FacultyRepository facultyRepository, EmailService emailService) {
		this.facultyRepository = facultyRepository;
		this.emailService = emailService;
	}

	public Faculty registerFaculty(Faculty faculty) {
		return facultyRepository.save(faculty);
	}

	// Generate Faculty Username
	private String generateFacultyUsername() {
		long count = facultyRepository.count();
		long nextNumber = count + 1;
		return String.format("F%03d", nextNumber);
	}

	// Register Faculty in Database
	public Faculty registerFaculty(Faculty faculty, MultipartFile photo) {

		if (photo == null || photo.isEmpty()) {
			throw new RuntimeException("Profile photo is mandatory");
		}

		String username = generateFacultyUsername();

		if (facultyRepository.existsByUsername(username)) {
			throw new RuntimeException("Faculty username already exists");
		}

		faculty.setUsername(username);
		faculty.setPhotofilename(photo.getOriginalFilename());
		faculty.setPassword(UUID.randomUUID().toString().substring(0, 6));
		try {
			faculty.setPhoto(photo.getBytes());
		} catch (IOException ie) {
			throw new RuntimeException("Failed to save profile photo");
		}
		try {
			emailService.sendProfessorCredentials(faculty.getEmail(), faculty.getUsername(), faculty.getPassword());
		} catch (Exception e) {
			System.err.println("Failed to send email: " + e.getMessage());
		}

		return facultyRepository.save(faculty);
	}

	// Fetch all faculties
	public List<Faculty> getAllFaculties() {
		return facultyRepository.findAll();
	}

	// GET Faculty by username
	public Faculty getFacultyByUsername(String username) {
		return facultyRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Faculty not found"));
	}

	// GET Faculty photo
	public byte[] getFacultyPhoto(String username) {
		return facultyRepository.findByUsername(username)
				.map(Faculty::getPhoto)
				.orElseThrow(() -> new RuntimeException("Faculty photo not found"));
	}

	// Delete Faculty
	public void deleteFaculty(String username) {

		Faculty faculty = facultyRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Faculty not found"));

		facultyRepository.delete(faculty);
	}

	// Update Faculty
	public Faculty updateFaculty(String username, Faculty updatedFaculty) {

		Faculty existing = facultyRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Faculty not found"));

		existing.setFullName(updatedFaculty.getFullName());
		existing.setEmail(updatedFaculty.getEmail());
		existing.setMobileNo(updatedFaculty.getMobileNo());
		existing.setAltMobileNo(updatedFaculty.getAltMobileNo());
		existing.setOfficeAddress(updatedFaculty.getOfficeAddress());
		existing.setDepartment(updatedFaculty.getDepartment());
		existing.setQualification(updatedFaculty.getQualification());
		existing.setsubject(updatedFaculty.getsubject());

		// Only update password if provided
		if (updatedFaculty.getPassword() != null && !updatedFaculty.getPassword().isEmpty()) {
			existing.setPassword(updatedFaculty.getPassword());
		}

		return facultyRepository.save(existing);
	}

	// GET Profile Photo
	public byte[] getProfilePicture(String username) {
		Faculty faculty = facultyRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		if (faculty.getPhoto() == null) {
			throw new RuntimeException("No Profile Picture found");
		}

		return faculty.getPhoto();
	}

	// Update Profile Photo
	public void updateProfilePicture(String username, MultipartFile file) {
		try {
			Faculty faculty = facultyRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("Admin not found"));

			faculty.setPhoto(file.getBytes());

			facultyRepository.save(faculty);

		} catch (IOException e) {
			throw new RuntimeException("Failed to store profile image ", e);
		}
	}

	// GET Faculty Activity Status
	public Activity getAccountStatus(String username) {
		return facultyRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Faculty not found"))
				.getActivity();
	}

	// Update Activity Status
	public void updateAccountStatus(String username, Activity activity) {
		Faculty faculty = facultyRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Faculty not found"));

		faculty.setActivity(activity);
		facultyRepository.save(faculty);
	}

	// Get total faculty count
	public long getTotalFacultyCount() {
		return facultyRepository.count();
	}

	// Get active faculty count
	public long getActiveFacultyCount() {
		return facultyRepository.countByActivity(Activity.ACTIVE);
	}

	public Faculty loginFaculty(String username, String password) {

		Faculty faculty = facultyRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Invalid username"));

		// Account status check
		if (faculty.getActivity() != Activity.ACTIVE) {
			throw new RuntimeException("Your account is not active. Please contact admin.");
		}

		// Password match (plain text – for now)
		if (!faculty.getPassword().equals(password)) {
			throw new RuntimeException("Invalid password");
		}

		return faculty; // Login successful
	}
}
