package com.git.Professor.Entity;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="demoquestionpaper")
public class Demoquestionpaper {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long qid;
	private String coursename;
	private String subject;
	private String chapter;
    private int duration;
    private String difficulty;
    private String selectset;

    
    // question paper image 
    private String questionpaperphotoname;
	@Lob
	@Column(name = "questionpaperimage", columnDefinition="LONGBLOB")
	private byte [] questionpaperimage;

	
	
    private String explanation;   
 
    
    // attachment 

    private String attachmentname;
	@Lob
	@Column(name = "attachment", columnDefinition="LONGBLOB")
	private byte [] attachment;

	
	@OneToMany(mappedBy = "demoquestionpaper")
	private List<Demoquestions> lq;


	public Demoquestionpaper() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Demoquestionpaper(long qid, String coursename, String subject, String chapter, int duration,
			String difficulty, String selectset, String questionpaperphotoname, byte[] questionpaperimage,
			String explanation, String attachmentname, byte[] attachment, List<Demoquestions> lq) {
		super();
		this.qid = qid;
		this.coursename = coursename;
		this.subject = subject;
		this.chapter = chapter;
		this.duration = duration;
		this.difficulty = difficulty;
		this.selectset = selectset;
		this.questionpaperphotoname = questionpaperphotoname;
		this.questionpaperimage = questionpaperimage;
		this.explanation = explanation;
		this.attachmentname = attachmentname;
		this.attachment = attachment;
		this.lq = lq;
	}


	@Override
	public String toString() {
		return "Demoquestionpaper [qid=" + qid + ", coursename=" + coursename + ", subject=" + subject + ", chapter="
				+ chapter + ", duration=" + duration + ", difficulty=" + difficulty + ", selectset=" + selectset
				+ ", questionpaperphotoname=" + questionpaperphotoname + ", questionpaperimage="
				+ Arrays.toString(questionpaperimage) + ", explanation=" + explanation + ", attachmentname="
				+ attachmentname + ", attachment=" + Arrays.toString(attachment) + ", lq=" + lq + "]";
	}


	public long getQid() {
		return qid;
	}


	public void setQid(long qid) {
		this.qid = qid;
	}


	public String getCoursename() {
		return coursename;
	}


	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getChapter() {
		return chapter;
	}


	public void setChapter(String chapter) {
		this.chapter = chapter;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public String getDifficulty() {
		return difficulty;
	}


	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}


	public String getSelectset() {
		return selectset;
	}


	public void setSelectset(String selectset) {
		this.selectset = selectset;
	}


	public String getQuestionpaperphotoname() {
		return questionpaperphotoname;
	}


	public void setQuestionpaperphotoname(String questionpaperphotoname) {
		this.questionpaperphotoname = questionpaperphotoname;
	}


	public byte[] getQuestionpaperimage() {
		return questionpaperimage;
	}


	public void setQuestionpaperimage(byte[] questionpaperimage) {
		this.questionpaperimage = questionpaperimage;
	}


	public String getExplanation() {
		return explanation;
	}


	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}


	public String getAttachmentname() {
		return attachmentname;
	}


	public void setAttachmentname(String attachmentname) {
		this.attachmentname = attachmentname;
	}


	public byte[] getAttachment() {
		return attachment;
	}


	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}


	public List<Demoquestions> getLq() {
		return lq;
	}


	public void setLq(List<Demoquestions> lq) {
		this.lq = lq;
	}


	

	

	
}
