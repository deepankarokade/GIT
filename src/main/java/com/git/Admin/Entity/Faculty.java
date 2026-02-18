package com.git.Admin.Entity;

import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.git.Activity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Faculty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long facid; 

	private String fullName;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String username;

	private String password;
	
	@Column(name = "first_login")
	private boolean firstLogin = true; 

	private boolean mustResetPassword = true; 

	private String department;
	private String mobileNo;
	private String altMobileNo;
	private String officeAddress;
	private String qualification;
	private String subject;

	private String photofilename;
	@Lob
	@Column(name = "photo", columnDefinition = "LONGBLOB")
	@JsonIgnore
	private byte[] photo;

	// Signature Fields (New addition from 2nd file)
	private String signatureFilename;
	@Lob
	@Column(name = "signature", columnDefinition = "LONGBLOB")
	@JsonIgnore
	private byte[] signature;

	private boolean termsAccepted;

	@Enumerated(EnumType.STRING)
	@Column(name = "activity", columnDefinition = "VARCHAR(20) DEFAULT 'ACTIVE'")
	private Activity activity = Activity.ACTIVE;

	// No-argument constructor
	public Faculty() {
		super();
	}

	// Full Constructor (Merged fields)
	public Faculty(Long facid, String fullName, String email, String username, String password, String department,
			String mobileNo, String altMobileNo, String officeAddress, String qualification, String subject,
			String photofilename, byte[] photo, String signatureFilename, byte[] signature, boolean termsAccepted) {
		super();
		this.facid = facid;
		this.fullName = fullName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.department = department;
		this.mobileNo = mobileNo;
		this.altMobileNo = altMobileNo;
		this.officeAddress = officeAddress;
		this.qualification = qualification;
		this.subject = subject;
		this.photofilename = photofilename;
		this.photo = photo;
		this.signatureFilename = signatureFilename;
		this.signature = signature;
		this.termsAccepted = termsAccepted;
	}

	// Getters and Setters
	public Long getFacid() { return facid; }
	public void setFacid(Long facid) { this.facid = facid; }

	public String getFullName() { return fullName; }
	public void setFullName(String fullName) { this.fullName = fullName; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public boolean isFirstLogin() { return firstLogin; }
	public void setFirstLogin(boolean firstLogin) { this.firstLogin = firstLogin; }

	public boolean isMustResetPassword() { return mustResetPassword; }
	public void setMustResetPassword(boolean mustResetPassword) { this.mustResetPassword = mustResetPassword; }

	public String getDepartment() { return department; }
	public void setDepartment(String department) { this.department = department; }

	public String getMobileNo() { return mobileNo; }
	public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

	public String getAltMobileNo() { return altMobileNo; }
	public void setAltMobileNo(String altMobileNo) { this.altMobileNo = altMobileNo; }

	public String getOfficeAddress() { return officeAddress; }
	public void setOfficeAddress(String officeAddress) { this.officeAddress = officeAddress; }

	public String getQualification() { return qualification; }
	public void setQualification(String qualification) { this.qualification = qualification; }

	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }

	public String getPhotofilename() { return photofilename; }
	public void setPhotofilename(String photofilename) { this.photofilename = photofilename; }

	public byte[] getPhoto() { return photo; }
	public void setPhoto(byte[] photo) { this.photo = photo; }

	public String getSignatureFilename() { return signatureFilename; }
	public void setSignatureFilename(String signatureFilename) { this.signatureFilename = signatureFilename; }

	public byte[] getSignature() { return signature; }
	public void setSignature(byte[] signature) { this.signature = signature; }

	public boolean isTermsAccepted() { return termsAccepted; }
	public void setTermsAccepted(boolean termsAccepted) { this.termsAccepted = termsAccepted; }

	public Activity getActivity() { return activity; }
	public void setActivity(Activity activity) { this.activity = activity; }

	@Override
	public String toString() {
		return "Faculty [facid=" + facid + ", fullName=" + fullName + ", email=" + email + ", username=" + username + "]";
	}
}