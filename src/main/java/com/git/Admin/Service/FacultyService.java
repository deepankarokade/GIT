package com.git.Admin.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

	// Generate Faculty UID
	private String generateFacultyUid() {
		return facultyRepository.findTopByOrderByFacidDesc()
				.map(lastFaculty -> {
					String lastUid = lastFaculty.getUsername();
					if (lastUid != null && lastUid.startsWith("FAC_")) {
						try {
							int lastNumber = Integer.parseInt(lastUid.substring(4));
							return String.format("FAC_%04d", lastNumber + 1);
						} catch (NumberFormatException e) {
							// Fallback if parsing fails
							return String.format("FAC_%04d", lastFaculty.getFacid() + 1);
						}
					}
					// Fallback if UID format is unexpected
					return String.format("FAC_%04d", lastFaculty.getFacid() + 1);
				})
				.orElse("FAC_0001"); // First faculty
	}

	// Register Faculty in Database
	public Faculty registerFaculty(Faculty faculty, MultipartFile photo) {

		if (photo == null || photo.isEmpty()) {
			throw new RuntimeException("Profile photo is mandatory");
		}

		String username = generateFacultyUid();

		if (facultyRepository.existsByUsername(username)) {
			throw new RuntimeException("Faculty username already exists");
		}

		faculty.setUsername(username);
		faculty.setPhotofilename(photo.getOriginalFilename());
		faculty.setPassword(UUID.randomUUID().toString().substring(0, 8));
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
		existing.setSubject(updatedFaculty.getSubject());
		existing.setPassword(updatedFaculty.getPassword());

		return facultyRepository.save(existing);
	}

	// GET Profile Photo
	public byte[] getProfilePicture(String username) {
		Faculty faculty = facultyRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Faculty not found"));

		if (faculty.getPhoto() == null) {
			throw new RuntimeException("No Profile Picture found");
		}

		return faculty.getPhoto();
	}

	// Update Profile Photo
	public void updateProfilePicture(String username, MultipartFile file) {
		try {
			Faculty faculty = facultyRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("Faculty not found"));

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

	public Optional<Faculty> findfacultybymail(String email) {
		return facultyRepository.findByEmail(email);
	}

	public void changePassword(Long facid, String oldPassword, String newPassword) {
		Faculty faculty = facultyRepository.findById(facid)
				.orElseThrow(() -> new RuntimeException("Faculty not found"));

		if (!faculty.getPassword().equals(oldPassword)) {
			throw new RuntimeException("Old password is incorrect");
		}

		faculty.setPassword(newPassword);
		faculty.setFirstLogin(false);
		facultyRepository.save(faculty);
	}

	public Faculty getFacultyById(Long id) {
		return facultyRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Faculty not found"));
	}

	public Faculty updateOwnProfile(Long facid, Faculty formFaculty) {
		Faculty existing = facultyRepository.findById(facid)
				.orElseThrow(() -> new RuntimeException("Faculty not found"));

		// ✅ Allowed fields only
		existing.setFullName(formFaculty.getFullName());
		existing.setEmail(formFaculty.getEmail());
		existing.setMobileNo(formFaculty.getMobileNo());
		existing.setAltMobileNo(formFaculty.getAltMobileNo());
		existing.setOfficeAddress(formFaculty.getOfficeAddress());
		existing.setDepartment(formFaculty.getDepartment());
		existing.setQualification(formFaculty.getQualification());
		existing.setSubject(formFaculty.getSubject());

		return facultyRepository.save(existing);
	}

	// ================= PROFESSOR SELF PROFILE UPDATE WITH IMAGE =================
	public Faculty updateOwnProfile(Long facid, Faculty formFaculty, MultipartFile profileImage) {
		Faculty existing = facultyRepository.findById(facid)
				.orElseThrow(() -> new RuntimeException("Faculty not found"));

		// Update text info
		existing.setFullName(formFaculty.getFullName());
		existing.setEmail(formFaculty.getEmail()); // Including email as it was in the other version
		existing.setMobileNo(formFaculty.getMobileNo());
		existing.setAltMobileNo(formFaculty.getAltMobileNo());
		existing.setOfficeAddress(formFaculty.getOfficeAddress());
		existing.setDepartment(formFaculty.getDepartment());
		existing.setQualification(formFaculty.getQualification());
		existing.setSubject(formFaculty.getSubject());

		// Update photo only if a new one is selected
		if (profileImage != null && !profileImage.isEmpty()) {
			try {
				existing.setPhoto(profileImage.getBytes());
				existing.setPhotofilename(profileImage.getOriginalFilename());
			} catch (IOException e) {
				throw new RuntimeException("Failed to process image");
			}
		}

		return facultyRepository.save(existing);
	}
}
