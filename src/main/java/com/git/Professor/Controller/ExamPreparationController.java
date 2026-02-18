package com.git.Professor.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.git.Professor.Entity.ExamPreparationForm;
import com.git.Professor.Service.ExamPreparationService;

@Controller
@RequestMapping("/exam-preparation")
public class ExamPreparationController {

    @Autowired
    private ExamPreparationService service;

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page) {

        int pageSize = 3;
        Page<ExamPreparationForm> papersPage;

        if (search != null && !search.isEmpty()) {
            List<ExamPreparationForm> list = service.search(search);
            papersPage = new PageImpl<>(list);
        } else {
            papersPage = service.getAllPaginated(page, pageSize);
        }

        model.addAttribute("papers", papersPage.getContent());
        model.addAttribute("totalFiles", papersPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", papersPage.getTotalPages());

        return "professor/professor-exam-preparation";
    }

    // ================= UPLOAD =================
    @PostMapping("/upload")
    public String upload(@RequestParam String subjectName,
            @RequestParam int academicYear,
            @RequestParam String fileFormat,
            @RequestParam MultipartFile file) throws IOException {

        service.save(subjectName, academicYear, fileFormat, file);
        return "redirect:/exam-preparation/dashboard";
    }

    // ================= UPDATE (FIXED) =================
    // Removed {id} from URL because HTML sends ID as a hidden input parameter
    @PostMapping("/update")
    public String update(@RequestParam("id") Long id,
            @RequestParam("subjectName") String subjectName,
            @RequestParam("academicYear") int academicYear,
            @RequestParam("fileFormat") String fileFormat,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        service.update(id, subjectName, academicYear, fileFormat, file);
        return "redirect:/exam-preparation/dashboard";
    }

    // ================= EDIT JSON DATA (FIXED) =================
    @GetMapping("/edit/{id}")
    @ResponseBody
    public ExamPreparationForm editExam(@PathVariable Long id) {
        ExamPreparationForm form = service.getById(id);

        // IMPORTANT: Set fileData to null before sending to frontend.
        // Otherwise, heavy file binary data will slow down the JSON response.
        if (form != null) {
            form.setFileData(null);
        }
        return form;
    }

    // ================= DOWNLOAD =================
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        ExamPreparationForm paper = service.getById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + paper.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(paper.getFileData());
    }

    // ================= VIEW =================
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> view(@PathVariable Long id) {
        ExamPreparationForm paper = service.getById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + paper.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(paper.getFileData());
    }

    // ================= DELETE =================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/exam-preparation/dashboard";
    }

    // ================= EXPORT CSV =================
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToCsv() {

        byte[] csvData = service.exportToCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exam_papers.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csvData);
    }

}