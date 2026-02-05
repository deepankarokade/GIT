package com.git.Admin.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.git.Activity;
import com.git.Admin.Entity.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
	boolean existsByUsername(String username);

	Optional<Faculty> findByUsername(String username);

	List<Faculty> findAllByActivity(Activity activity);

	// Count all faculty
	long countBy();

	// Count faculty by activity status
	long countByActivity(Activity activity);
}
