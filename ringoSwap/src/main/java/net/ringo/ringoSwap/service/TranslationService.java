package net.ringo.ringoSwap.service;

public interface TranslationService {
	//피드글의 내용이 어떤언어인지 감지하는 메서드
	public String translateDetection(String text);
	//피드글의 내용을 번역하는 메서드
	String translateFeed(String text, String sourceLang, String targetLang);

}
