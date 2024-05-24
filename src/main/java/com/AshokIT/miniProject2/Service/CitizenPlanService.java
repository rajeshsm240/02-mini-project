package com.AshokIT.miniProject2.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.AshokIT.miniProject2.Entities.CitizenPlan;
import com.AshokIT.miniProject2.binding.SearchCriteria;

import jakarta.servlet.http.HttpServletResponse;

public interface CitizenPlanService {
	
	public List<String> getPlanNames();
	
	public List<String> getPlanStatus();
	
	public List<CitizenPlan> getCitizens(SearchCriteria criteria);
	
	public void generateExcel(HttpServletResponse httpServletResponse) throws Exception;
	
	public void generatePdf(HttpServletResponse httpServletResponse) throws Exception;
}
