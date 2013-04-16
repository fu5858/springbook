package springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {

	@Override
	public void send(SimpleMailMessage message) throws MailException {

	}

	@Override
	public void send(SimpleMailMessage[] messages) throws MailException {
	}

}
