package net.ringo.ringoSwap.service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import lombok.extern.slf4j.Slf4j;
import net.ringo.ringoSwap.util.JsonRead;

@Service
@Slf4j
public class TranslationServiceImple implements TranslationService{
	private static final String PROJECT_ID = "key-range-399607";		//해당 API의 프로젝트ID를 담은 상수
	private static final String KEY = JsonRead.readJsonFile("key-range-399607-17ea03595a21.json");	//해당 API의 서비스 계정의 키값을 담은 상수
	
	@Override
	public String translateDetection(String text) {
		log.debug("키값{}", KEY.getBytes(StandardCharsets.UTF_8));
		log.debug("변환값{}", StandardCharsets.UTF_8);
		try {
			//인증 및 설정
			GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(KEY.getBytes(StandardCharsets.UTF_8))); 
			log.debug("키값{}", KEY.getBytes(StandardCharsets.UTF_8));
			log.debug("변환값{}", StandardCharsets.UTF_8);
			
			TranslateOptions options = TranslateOptions.newBuilder()
					.setProjectId(PROJECT_ID)
	                .setCredentials(credentials)
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
			GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(KEY.getBytes(StandardCharsets.UTF_8)));
			
			TranslateOptions options = TranslateOptions.newBuilder()
					.setProjectId(PROJECT_ID)
	                .setCredentials(credentials)
					.build();
			//Translate API 클라이언트 생성
			Translate translate = options.getService();
			
			//번역설정
			Translation translation = translate.translate(text
					, Translate.TranslateOption.sourceLanguage(sourceLang)
					, Translate.TranslateOption.targetLanguage(targetLang));
			
			//번역 텍스트 반환 
			return translation.getTranslatedText();
		} catch (Exception e) {
			e.printStackTrace();
			return "번역오류: "+e.getMessage();
		}
	}

}
