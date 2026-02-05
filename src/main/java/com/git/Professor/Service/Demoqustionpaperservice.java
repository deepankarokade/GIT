package com.git.Professor.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.Professor.Entity.Demoquestionpaper;
import com.git.Professor.Repository.DemoQuestionpaperrepositry;

@Service
public class Demoqustionpaperservice {

	@Autowired
	DemoQuestionpaperrepositry dp;

	public void savequestionpaper(Demoquestionpaper questionpaper) {

		dp.save(questionpaper);

	}

}
