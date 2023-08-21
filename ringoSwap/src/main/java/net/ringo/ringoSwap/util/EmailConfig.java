package net.ringo.ringoSwap.util;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource(PathHandler.EMAILPROPERTIES)
public class EmailConfig 
{    
    @Value("${mail.smtp.host}")
    private String host;
    
    @Value("${mail.smtp.ssl.protocols}")
    private String protocols;
    
	@Value("${mail.smtp.port}")
    private int port;
    
    @Value("${mail.smtp.auth}")
    private boolean auth;
    
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;
    
    @Value("${mail.smtp.starttls.required}")
    private boolean startlls_required;
    
    @Value("${AdminMail.id}")
    private String id;
    
    @Value("${AdminMail.password}")
    private String password;
	
	@Bean
	public JavaMailSender javaMailService()
	{
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(host);
		javaMailSender.setUsername(id);
		javaMailSender.setPassword(password);
		javaMailSender.setPort(port);
		javaMailSender.setJavaMailProperties(getMailProperties());
		javaMailSender.setDefaultEncoding("UTF-8");
		return javaMailSender;
	}
	
	private Properties getMailProperties()
	{
		Properties pt = new Properties();
		pt.put("mail.smtp.host", host);
		pt.put("mail.smtp.port", port);
		pt.put("mail.smtp.auth", auth);
        pt.put("mail.smtp.starttls.enable", starttls);
        pt.put("mail.smtp.ssl.protocols", protocols);
        pt.put("mail.smtp.starttls.required", startlls_required);
        pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		return pt;
	}
}