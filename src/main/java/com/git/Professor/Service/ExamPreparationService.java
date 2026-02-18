package com.git.Professor.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort; // Ha import mahatvacha ahe
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.git.Professor.Entity.ExamPreparationForm;
import com.git.Professor.Repository.ExamPreparationRepository;

@Service
public class ExamPreparationService {

    @Autowired
    private ExamPreparationRepository repo;

    // ================= SAVE =================
    public void save(String subjectName,
                     int academicYear,
                     String fileFormat,
                     MultipartFile file) throws IOException {

        ExamPreparationForm form = new ExamPreparationForm();
        form.setSubjectName(subjectName);
        form.setAcademicYear(academicYear);
        form.setFileFormat(fileFormat);
        form.setFileName(file.getOriginalFilename());
        form.setFileData(file.getBytes());

        repo.save(form);
    }

    // ================= GET ALL =================
    public List<ExamPreparationForm> getAll() {
        // Jari list havi asel tari descending pahije
        return repo.findAll(Sort.by(Sort.Direction.DESC, "id")); 
    }

    // ================= SEARCH =================
    public List<ExamPreparationForm> search(String keyword) {
        return repo.findBySubjectNameContainingIgnoreCase(keyword);
    }

    // ================= GET BY ID =================
    public ExamPreparationForm getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ================= DELETE =================
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ================= PAGINATION (MAIN CHANGE HERE) =================
    public Page<ExamPreparationForm> getAllPaginated(int page, int size) {
        // 'id' var DESC sort kela, mhanje last insert kelela id (Ex: 105) pahila disel
        return repo.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    // ================= TOP 3 LATEST =================
    public List<ExamPreparationForm> getTop3() {
        return repo.findTop3ByOrderByIdDesc();
    }

    // ================= UPDATE =================
    public void update(Long id,
                       String subjectName,
                       int academicYear,
                       String fileFormat,
                       MultipartFile file) throws IOException {

        ExamPreparationForm paper = repo.findById(id).orElse(null);

        if (paper == null) {
            return;
        }

        paper.setSubjectName(subjectName);
        paper.setAcademicYear(academicYear);
        paper.setFileFormat(fileFormat);

        // File optional during update
        if (file != null && !file.isEmpty()) {
            paper.setFileName(file.getOriginalFilename());
            paper.setFileData(file.getBytes());
        }

        repo.save(paper);
    }
    
    
    public byte[] exportToCsv() {

        List<ExamPreparationForm> list = repo.findAll();

        StringBuilder csv = new StringBuilder();

        // CSV Header
        csv.append("ID,Subject Name,Academic Year,File Name,File Format\n");

        for (ExamPreparationForm form : list) {
            csv.append(form.getId()).append(",")
               .append(form.getSubjectName()).append(",")
               .append(form.getAcademicYear()).append(",")
               .append(form.getFileName()).append(",")
               .append(form.getFileFormat()).append("\n");
        }

        return csv.toString().getBytes();
    }
    
 // ================= STUDENT PAGE PAGINATION =================
    public Page<ExamPreparationForm> findAllPaged(org.springframework.data.domain.Pageable pageable) {
        // Id nusar Sort kela mhanje paging barobar kam karel
        return repo.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<String> getDistinctSubjectNames() {
        return repo.findDistinctSubjectNames();
    }
}