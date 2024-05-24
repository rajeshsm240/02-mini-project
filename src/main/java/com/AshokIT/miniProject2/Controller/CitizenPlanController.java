package com.AshokIT.miniProject2.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AshokIT.miniProject2.Entities.CitizenPlan;
import com.AshokIT.miniProject2.Service.CitizenPlanService;
import com.AshokIT.miniProject2.binding.SearchCriteria;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CitizenPlanController {
	
	@Autowired
	private CitizenPlanService citizenPlanService;
	
	@GetMapping("/")
	public String index(Model model) {
		
		extracted(model);
		model.addAttribute("search", new SearchCriteria());
		
		return "index";
	}


	private void extracted(Model model) {
		List<String> planNames = citizenPlanService.getPlanNames();
		List<String> planStatus = citizenPlanService.getPlanStatus();
		
		model.addAttribute("plan-names-list", planNames);
		model.addAttribute("plan-status-list", planStatus);
	}
	
	
	@PostMapping("/search")
	public String handleSearchButton(@ModelAttribute("search") SearchCriteria criteria,Model model) {
		
		List<CitizenPlan> citizenPlanList = citizenPlanService.getCitizens(criteria);
		model.addAttribute("search", citizenPlanList);
		return "index";
	}
	
	@GetMapping("/excel")
	public void downloadExcel(HttpServletResponse response) throws Exception{
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;fileName=data.xls";
		
		citizenPlanService.generateExcel(response);
	}
	
	@GetMapping(value = "/openpdf/citizens", produces = MediaType.APPLICATION_PDF_VALUE)
    public void employeeReport(HttpServletResponse response)  throws Exception {
 
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;fileName=data.pdf";
        
        citizenPlanService.generatePdf(response);
 
        
	}

}
