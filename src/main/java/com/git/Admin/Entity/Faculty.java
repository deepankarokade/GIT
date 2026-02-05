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

	private boolean termsAccepted;

	@Enumerated(EnumType.STRING)
	@Column(name = "activity", columnDefinition = "VARCHAR(20) DEFAULT 'ACTIVE'")
	private Activity activity = Activity.ACTIVE;

	// No-argument constructor required by JPA
	public Faculty() {
		super();
	}

	public Faculty(Long facid, String fullName, String email, String username, String password, String department,
			String mobileNo, String altMobileNo, String officeAddress, String qualification, String subject,
			String photofilename, byte[] photo, boolean termsAccepted) {
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
		this.termsAccepted = termsAccepted;
	}

	@Override
	public String toString() {
		return "Faculty [facid=" + facid + ", fullName=" + fullName + ", email=" + email + ", username=" + username
				+ ", password=" + password + ", department=" + department + ", mobileNo=" + mobileNo + ", altMobileNo="
				+ altMobileNo + ", officeAddress=" + officeAddress + ", qualification=" + qualification
				+ ", subject=" + subject + ", photofilename=" + photofilename + ", photo="
				+ Arrays.toString(photo) + ", termsAccepted=" + termsAccepted + "]";
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Long getFacid() {
		return facid;
	}

	public void setFacid(Long facid) {
		this.facid = facid;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAltMobileNo() {
		return altMobileNo;
	}

	public void setAltMobileNo(String altMobileNo) {
		this.altMobileNo = altMobileNo;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getsubject() {
		return subject;
	}

	public void setsubject(String subject) {
		this.subject = subject;
	}

	public String getPhotofilename() {
		return photofilename;
	}

	public void setPhotofilename(String photofilename) {
		this.photofilename = photofilename;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public boolean isTermsAccepted() {
		return termsAccepted;
	}

	public void setTermsAccepted(boolean termsAccepted) {
		this.termsAccepted = termsAccepted;
	}
}
