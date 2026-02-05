package com.git.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.git.Admin.Entity.Section;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findBySectionName(String sectionName);

    boolean existsBySectionName(String sectionName);

    Optional<Section> findBySectionId(String sectionId);
}
