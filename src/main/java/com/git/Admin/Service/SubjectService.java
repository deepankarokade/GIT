package com.git.Admin.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Admin.Entity.Subject;
import com.git.Admin.Repository.SubjectRepository;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // GET All Subjects
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Add a subject
    public Subject addSubject(String subjectName, String subjectCode, Subject subject) {
        if (subjectRepository.existsBySubjectName(subjectName)) {
            throw new RuntimeException("Subject Name Already Exists");
        } else if (subjectRepository.existsBySubjectCode(subjectCode)) {
            throw new RuntimeException("Subject Code Already Exists");
        }
        subject.setSubjectName(subjectName);
        subject.setSubjectCode(subjectCode);
        return subjectRepository.save(subject);
    }

    // Update subject
    public Subject updateSubject(long id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Optional: Check if the new name/code exists in OTHER rows
        subject.setSubjectName(subjectDetails.getSubjectName());
        subject.setSubjectCode(subjectDetails.getSubjectCode());

        return subjectRepository.save(subject);
    }

    // Delete a subject by string code
    public void deleteSubject(String subjectCode) {
        Subject subject = subjectRepository.findBySubjectCode(subjectCode)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        subjectRepository.delete(subject);
    }

    // Delete a subject by ID
    public void deleteSubjectById(long id) {
        if (!subjectRepository.existsById(id)) {
            throw new RuntimeException("Subject not found");
        }
        subjectRepository.deleteById(id);
    }

    // GET Total subjects count
    public Long totalSubjects() {
        return subjectRepository.count();
    }
}
