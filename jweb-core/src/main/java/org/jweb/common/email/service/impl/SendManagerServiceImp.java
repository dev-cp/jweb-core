package org.jweb.common.email.service.impl;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jweb.common.email.service.SendManagerService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class SendManagerServiceImp implements SendManagerService{

	private JavaMailSender mailSender;
	private SimpleMailMessage message;
	
	@Override
	public void sendMail(final String email) {
		MimeMessagePreparator preparator = new  MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));;
				mimeMessage.setFrom(new InternetAddress(message.getFrom()));;
				mimeMessage.setSubject(message.getText(), "gbk");
				mimeMessage.setText(message.getText(), "gbk");
				
			}
		};
		
		mailSender.send(preparator);
		
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public SimpleMailMessage getMessage() {
		return message;
	}

	public void setMessage(SimpleMailMessage message) {
		this.message = message;
	}
	
	

}
