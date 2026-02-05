package com.git.Student.Entity;

import java.util.Arrays;

import com.git.Student.enumactivity.ActivityStudent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photofilename;
    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

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

    // Full Name
    private String fullName;

    // DOB
    private String dob;

    @Enumerated(EnumType.STRING)
    private ActivityStudent activityStudent = ActivityStudent.INACTIVE;

    // Password for student login (set after payment approval)
    private String password;

    public ActivityStudent getActivityStudent() {
        return activityStudent;
    }

    public void setActivityStudent(ActivityStudent activityStudent) {
        this.activityStudent = activityStudent;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentContact() {
        return parentContact;
    }

    public void setParentContact(String parentContact) {
        this.parentContact = parentContact;
    }

    public String getSchoolCollegeName() {
        return schoolCollegeName;
    }

    public void setSchoolCollegeName(String schoolCollegeName) {
        this.schoolCollegeName = schoolCollegeName;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getPreferredExamDate() {
        return preferredExamDate;
    }

    public void setPreferredExamDate(String preferredExamDate) {
        this.preferredExamDate = preferredExamDate;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    @Column(unique = true)
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    // Gender
    private String gender;

    // Email
    @Column(unique = true)
    private String email;

    // Contact No
    private String contactNumber;

    // Address
    private String address;

    // State (STRING ONLY)
    private String state;

    // City
    private String city;

    // Parent Details
    private String parentName;
    private String parentContact;

    // School / College
    private String schoolCollegeName;

    // Class
    private String studentClass;

    // Registration ID
    @Column(unique = true, nullable = true)
    private String registrationId;

    // Preferred Exam Date
    private String preferredExamDate;

    // Photo Path
    private String photoPath;

    // Subjects / Courses
    private String subjects;

    // Section
    private String section;

    @Override
    public String toString() {
        return "StudentEntity [id=" + id + ", photofilename=" + photofilename + ", photo=" + Arrays.toString(photo)
                + ", fullName=" + fullName + ", dob=" + dob + ", gender=" + gender + ", email=" + email
                + ", contactNumber=" + contactNumber + ", address=" + address + ", state=" + state + ", city=" + city
                + ", parentName=" + parentName + ", parentContact=" + parentContact + ", schoolCollegeName="
                + schoolCollegeName + ", studentClass=" + studentClass + ", registrationId=" + registrationId
                + ", preferredExamDate=" + preferredExamDate + ", photoPath=" + photoPath + ", subjects=" + subjects
                + ", section=" + section + "]";
    }

    public Student(Long id, String photofilename, byte[] photo, String fullName, String dob, String gender,
            String email, String contactNumber, String address, String state, String city, String parentName,
            String parentContact, String schoolCollegeName, String studentClass, String registrationId,
            String preferredExamDate, String photoPath, String subjects, String uid, String section) {
        super();
        this.id = id;
        this.photofilename = photofilename;
        this.photo = photo;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.state = state;
        this.city = city;
        this.parentName = parentName;
        this.parentContact = parentContact;
        this.schoolCollegeName = schoolCollegeName;
        this.studentClass = studentClass;
        this.registrationId = registrationId;
        this.preferredExamDate = preferredExamDate;
        this.photoPath = photoPath;
        this.subjects = subjects;
        this.uid = uid;
        this.section = section;
    }

    public Student() {
    }
}