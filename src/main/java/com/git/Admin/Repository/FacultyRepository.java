package com.git.Admin.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.git.Activity;
import com.git.Admin.Entity.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
	boolean existsByUsername(String username);

	Optional<Faculty> findByUsername(String username);

	Optional<Faculty> findByEmail(String email);

	List<Faculty> findAllByActivity(Activity activity);

	// Count all faculty
	long countBy();

	// Count faculty by activity status
	long countByActivity(Activity activity);

	@Query("SELECT MAX(CAST(SUBSTRING(f.username, 2) AS long)) FROM Faculty f")
	Long findMaxUsernameSuffix();

	Optional<Faculty> findTopByOrderByFacidDesc();
}
