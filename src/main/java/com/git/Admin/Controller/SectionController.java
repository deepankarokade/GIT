package com.git.Admin.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.git.Admin.Entity.Section;
import com.git.Admin.Service.SectionService;

@RestController
@RequestMapping("/admin/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping("/all")
    public ResponseEntity<List<Section>> getAllSections() {
        return ResponseEntity.ok(sectionService.getAllSections());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSection(@RequestBody SectionRequest request) {
        try {
            Section savedSection = sectionService.addSection(request.getSectionName(),
                    request.getSectionDescription(), request.getClassId(), request.getCourseId());
            return ResponseEntity.ok(savedSection);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.ok("Section deleted successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSection(@PathVariable Long id, @RequestBody SectionRequest request) {
        try {
            Section updatedSection = sectionService.updateSection(id, request.getSectionName(),
                    request.getSectionDescription(), request.getClassId(), request.getCourseId());
            return ResponseEntity.ok(updatedSection);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static class SectionRequest {
        private String sectionName;
        private String sectionDescription;
        private Long classId;
        private Long courseId;

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public String getSectionDescription() {
            return sectionDescription;
        }

        public void setSectionDescription(String sectionDescription) {
            this.sectionDescription = sectionDescription;
        }

        public Long getClassId() {
            return classId;
        }

        public void setClassId(Long classId) {
            this.classId = classId;
        }

        public Long getCourseId() {
            return courseId;
        }

        public void setCourseId(Long courseId) {
            this.courseId = courseId;
        }
    }
}
