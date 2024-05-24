package com.AshokIT.miniProject2.binding;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class SearchCriteria {
	
	private String planName;
	
	private String planStatus;
	
	private String gender;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate endDate;
	
	public SearchCriteria() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SearchCriteria(String planName, String planStatus, String gender, LocalDate startDate, LocalDate endDate) {
		super();
		this.planName = planName;
		this.planStatus = planStatus;
		this.gender = gender;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	
	
	
}
