package com.git.Admin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.git.Admin.Entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByUid(String uid);

	Optional<Admin> findByEmail(String email);
}
