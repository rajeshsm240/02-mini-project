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
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
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
		
		Document document1 = new Document(PageSize.A4);     //browser
		
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        PdfWriter.getInstance(document1, outputStream);
        document1.open();
        
        Document document2 = new Document(PageSize.A4);     //email-attachment
        
        File f = new File("data.pdf");
        FileOutputStream fos  = new FileOutputStream(f);
        PdfWriter.getInstance(document2, fos);
        document2.open();
        
        Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 22);
        fontHeader.setSize(20);
        Paragraph p = new Paragraph("Citizens Plan Info",fontHeader);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        
        document1.add(p);
        document2.add(p);
        
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new int[] {3,3,3,3,3,3});
        table.setSpacingBefore(5);
        
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD, 22);
        fontHeader.setColor(CMYKColor.WHITE);
        
        cell.setPhrase(new Phrase("Name",font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Email",font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Gender",font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("SSN",font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Plan Name",font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Plan Status",font));
        table.addCell(cell);
        
        List<CitizenPlan> citizenPlans = citizenPlanRepository.findAll();
        
        for(CitizenPlan citizenPlan : citizenPlans) {
        	table.addCell(citizenPlan.getName());
        	table.addCell(citizenPlan.getEmail());
        	table.addCell(citizenPlan.getGender());
        	table.addCell(String.valueOf(citizenPlan.getSsn()));
        	table.addCell(citizenPlan.getPlanName());
        	table.addCell(citizenPlan.getPlanStatus());
        }
        
        document1.add(table);
        document2.add(table);
        
        document1.close();
        outputStream.close();
        
        document2.close();
        fos.close();
        
        email.sendMail(f);
       
       
	}

}
