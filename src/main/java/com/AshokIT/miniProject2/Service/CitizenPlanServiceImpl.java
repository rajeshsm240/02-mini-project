package com.AshokIT.miniProject2.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.AshokIT.miniProject2.Entities.CitizenPlan;
import com.AshokIT.miniProject2.Repository.CitizenPlanRepository;
import com.AshokIT.miniProject2.binding.SearchCriteria;
import com.AshokIT.miniProject2.utils.EmailUtils;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CitizenPlanServiceImpl implements CitizenPlanService {
	
	@Autowired
	private CitizenPlanRepository citizenPlanRepository;
	
	@Autowired
	private EmailUtils email;
	
	@Override
	public List<String> getPlanNames() {
		 return citizenPlanRepository.getUniquePlanNames();
	}

	@Override
	public List<String> getPlanStatus() {
		return citizenPlanRepository.getUniquePlanStatus();
	}

	@Override
	public List<CitizenPlan> getCitizens(SearchCriteria criteria) {
		CitizenPlan citizenCriteria = new CitizenPlan();
		/*
		 * if(criteria.getPlanName()!=null && !criteria.getPlanName().equals("")) {
		 * citizenCriteria.setPlanName(criteria.getPlanName()); }
		 * if(criteria.getPlanStatus()!=null && !criteria.getPlanStatus().equals("")) {
		 * citizenCriteria.setPlanStatus(criteria.getPlanStatus()); }
		 */
		
		if(StringUtils.isNotBlank(criteria.getPlanName())){
			citizenCriteria.setPlanName(criteria.getPlanName());
		}
		if(StringUtils.isNotBlank(criteria.getPlanStatus())) {
			citizenCriteria.setPlanStatus(criteria.getPlanStatus());
		}
		if(StringUtils.isNotBlank(criteria.getGender())) {
			citizenCriteria.setGender(criteria.getGender());
		}
		
		//QBE
		Example<CitizenPlan> of = Example.of(citizenCriteria);
		return citizenPlanRepository.findAll(of);
	}

	@Override
	public void generateExcel(HttpServletResponse httpServletResponse) throws Exception {
		
		List<CitizenPlan> citizens =  citizenPlanRepository.findAll();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Data");
		HSSFRow headerRow = sheet.createRow(0);
		
		headerRow.createCell(0).setCellValue("Name");
		headerRow.createCell(1).setCellValue("Email");
		headerRow.createCell(2).setCellValue("Gender");
		headerRow.createCell(3).setCellValue("SSN");
		headerRow.createCell(4).setCellValue("Plan Name");
		headerRow.createCell(5).setCellValue("Plan Status");
		
		int index =1;
		
		for(CitizenPlan citizenPlan : citizens) {
			HSSFRow dataRow = sheet.createRow(index);
			
			dataRow.createCell(0).setCellValue(citizenPlan.getName());
			dataRow.createCell(1).setCellValue(citizenPlan.getEmail());
			dataRow.createCell(2).setCellValue(citizenPlan.getGender());
			dataRow.createCell(3).setCellValue(citizenPlan.getSsn());
			dataRow.createCell(4).setCellValue(citizenPlan.getPlanName());
			dataRow.createCell(5).setCellValue(citizenPlan.getPlanStatus());
			
			index++;
		}		
		
		//To send file in email attachment
		File file = new File("data.xls");
		FileOutputStream fos = new FileOutputStream(file);
		workbook.write(fos);
		email.sendMail(file);
		
		//To download file in browser
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();
		workbook.write(outputStream);
		
		workbook.close();
		outputStream.close();
		fos.close();
	}

	@Override
	public void generatePdf(HttpServletResponse httpServletResponse) throws Exception {
		
		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
 
        try {
 
            PdfWriter.getInstance(document, out);
            document.open();
 
            // Add Content to PDF file ->
            Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 22);
            Paragraph para = new Paragraph("Citizen Plans", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);
 
            PdfPTable table = new PdfPTable(6);
            // Add PDF Table Header ->
            Stream.of("Name", "Email", "Gender", "SSN", "Plan Name","Plan Status").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
                header.setBackgroundColor(Color.CYAN);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase
                		(headerTitle, headFont));
                table.addCell(header);
            });
 
            for (CitizenPlan citizen : citizenPlanRepository.findAll()) {
                PdfPCell name = new PdfPCell(new Phrase(citizen.getName().toString()));
                name.setPaddingLeft(4);
                name.setVerticalAlignment(Element.ALIGN_MIDDLE);
                name.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(name);
 
                PdfPCell email = new PdfPCell(new Phrase(citizen.getEmail().toString()));
                email.setPaddingLeft(4);
                email.setVerticalAlignment(Element.ALIGN_MIDDLE);
                email.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(email);
 
                PdfPCell gender = new PdfPCell(new Phrase(String.valueOf(citizen.getGender().toString())));
                gender.setVerticalAlignment(Element.ALIGN_MIDDLE);
                gender.setHorizontalAlignment(Element.ALIGN_CENTER);
                gender.setPaddingRight(4);
                table.addCell(gender);
 
                PdfPCell ssn = new PdfPCell(new Phrase(String.valueOf(citizen.getSsn()+"")));
                ssn.setVerticalAlignment(Element.ALIGN_MIDDLE);
                ssn.setHorizontalAlignment(Element.ALIGN_CENTER);
                ssn.setPaddingRight(4);
                table.addCell(ssn);
 
                PdfPCell planName = new PdfPCell(new Phrase(String.valueOf(citizen.getPlanName().toString())));
                planName.setVerticalAlignment(Element.ALIGN_MIDDLE);
                planName.setHorizontalAlignment(Element.ALIGN_CENTER);
                planName.setPaddingRight(4);
                table.addCell(planName);
                
                PdfPCell planStatus = new PdfPCell(new Phrase(String.valueOf(citizen.getPlanStatus().toString())));
                planStatus.setVerticalAlignment(Element.ALIGN_MIDDLE);
                planStatus.setHorizontalAlignment(Element.ALIGN_CENTER);
                planStatus.setPaddingRight(4);
                table.addCell(planStatus);
            }
            document.add(table);
 
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

       
	}

}
