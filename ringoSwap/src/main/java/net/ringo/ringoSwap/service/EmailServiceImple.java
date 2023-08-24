package net.ringo.ringoSwap.service;

import java.util.Random;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImple implements EmailService 
{
	@Autowired
	JavaMailSender emailSender;
	
	private String createKey()
	{
		StringBuilder key = new StringBuilder();
		Random rand = new Random();
		
		for (int i = 0; i < 6; i++)	// length 6 code.
		{
			int idx = rand.nextInt(3);
			
			switch (idx)
			{
				case 0:	// create a ~ z
					key.append((char) ((int)((rand.nextInt(26))) + 97));
					break;
				case 1:	// create A ~ Z
					key.append((char) ((int)((rand.nextInt(26))) + 65));
					break;
				case 2:	// create number
					key.append( (rand.nextInt(10)) );
					break;
			}
		}
			
		return key.toString();
	}
	
    @Override
    public String sendVerifyMessage(String to) throws Exception 
    {
        String ePw = createKey();
        MimeMessage message = emailSender.createMimeMessage();
        
        message.addRecipients(RecipientType.TO, to); // set target
        message.setSubject("[ 이메일 인증 ]"); // set title
        
        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 H4운영진입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";
        
        message.setText(msgg, "utf-8", "html"); // set contents
        message.setFrom(new InternetAddress("h4teamproject@gmail.com", "H4 운영자" )); // set sender
        
        try
        {
        	emailSender.send(message); // send email
        }
        catch (MailException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        
        return ePw;
    }
}
