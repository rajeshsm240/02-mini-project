package com.AshokIT.miniProject2.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
	@Autowired
	private JavaMailSender mailSender;

	public boolean sendMail(File file) {
		boolean status =  false;
		
		Logger logger = LoggerFactory.getLogger("EmailUtils.java");
		
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			
			helper.setTo("rajeshsm235@gmial.com");
			helper.setSubject("Your Report");
			
			helper.setText("<h2>Please download your report</h2>",true);
			
			helper.addAttachment(file.getName(), file);
			
			mailSender.send(mimeMessage);		
			
			status = true;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
