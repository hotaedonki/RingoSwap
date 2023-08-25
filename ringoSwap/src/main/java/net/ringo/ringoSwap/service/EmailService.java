package net.ringo.ringoSwap.service;

public interface EmailService 
{
	public String sendVerifyMessage(String to) throws Exception;
}