package net.ringo.ringoSwap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

@Service
public class TranslationServiceImple implements TranslationService{
	static String key = "17ea03595a210241d6b26b2bbbb711fd5e693513";
	static String location = "global";
	static String projectId = "key-range-399607";
	
	@Override
	public String translateDetection(String text) {
		try {
			//인증 및 설정
			TranslateOptions options = TranslateOptions.newBuilder()
					.setProjectId(projectId)
					.build();
			//Translate API 클라이언트 생성
			Translate translate = options.getService();
			
			//언어감지
			Detection detection = translate.detect(text);
            String detectedLanguage = detection.getLanguage();
            //감지한 언어 코드를 반환
            return detectedLanguage;
		} catch(Exception e) {
			e.printStackTrace();
			return "ko";	//기본 설정 언어코드인 ko를 반환
		}
	}
	
	//피드글에 작성된 글을 받아와, 해당글을 번역해 리턴하는 메서드
	@Override
	public String translateFeed(String text, String sourceLang, String targetLang) {
		try {
			//인증설정
			TranslateOptions options = TranslateOptions.newBuilder()
					.setProjectId(projectId)
					.build();
			//Translate API 클라이언트 생성
			Translate translate = options.getService();
			
			//번역설정
			Translation translation = translate.translate(text
					, Translate.TranslateOption.sourceLanguage(text)
					, Translate.TranslateOption.targetLanguage(text));
			
			//번역 텍스트 반환 
			return translation.getTranslatedText();
		} catch (Exception e) {
			e.printStackTrace();
			return "번역오류: "+e.getMessage();
		}
	}

}
