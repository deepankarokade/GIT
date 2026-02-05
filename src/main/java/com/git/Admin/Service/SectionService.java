package com.git.Admin.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.git.Admin.Entity.Section;
import com.git.Admin.Repository.SectionRepository;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public Section addSection(String sectionName, String sectionDescription) {
        if (sectionRepository.existsBySectionName(sectionName)) {
            throw new RuntimeException("Section with name '" + sectionName + "' already exists");
        }

        Section section = new Section();
        section.setSectionName(sectionName);
        section.setSectionDescription(sectionDescription != null ? sectionDescription : "");
        section.setSectionId(generateUniqueSectionId(sectionName));

        return sectionRepository.save(section);
    }

    private String generateUniqueSectionId(String sectionName) {
        // Generate ID like SEC-SECTIONNAME (removing spaces and making uppercase)
        return "SEC-" + sectionName.toUpperCase().replaceAll("\\s+", "");
    }

    public void deleteSection(Long id) {
        if (id != null) {
            sectionRepository.deleteById(id);
        }
    }

    public Section updateSection(Long id, String sectionName, String sectionDescription) {
        if (id == null) {
            throw new RuntimeException("Section ID is required");
        }
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found with id: " + id));

        if (!existingSection.getSectionName().equals(sectionName)
                && sectionRepository.existsBySectionName(sectionName)) {
            throw new RuntimeException("Section with name '" + sectionName + "' already exists");
        }

        existingSection.setSectionName(sectionName);
        existingSection.setSectionDescription(sectionDescription != null ? sectionDescription : "");
        existingSection.setSectionId(generateUniqueSectionId(sectionName));

        return sectionRepository.save(existingSection);
    }
}
