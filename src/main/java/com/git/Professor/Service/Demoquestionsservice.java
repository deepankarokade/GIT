package com.git.Professor.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Professor.Entity.Demoquestions;
import com.git.Professor.Repository.DemoQuestionreposirty;

@Service
public class Demoquestionsservice {

	@Autowired
	DemoQuestionreposirty dq;

	public void savequestions(Demoquestions question) {

		dq.save(question);

	}

}
