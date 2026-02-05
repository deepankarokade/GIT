package com.git.Professor.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.git.Professor.Entity.Demoquestions;

public interface DemoQuestionreposirty extends JpaRepository<Demoquestions, Long> {

}
