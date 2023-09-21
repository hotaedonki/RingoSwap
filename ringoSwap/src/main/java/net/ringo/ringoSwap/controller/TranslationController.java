package net.ringo.ringoSwap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.ringo.ringoSwap.service.TranslationService;

@Controller
@RequestMapping("translate")
public class TranslationController {
	
	@Autowired
    private TranslationService translationService;  // 실제 Translation API를 호출하는 서비스
	
	@GetMapping("feed")
    public String translateFeed(@RequestParam String text, String targetLang) {
        // 로그인한 사용자만 이 메서드를 호출할 수 있도록 Spring Security 설정
		//해당 문자열이 어떤 언어인지 감지해, 해당 언어코드를 리턴하는 메서드 실행
		String sourceLang=translationService.translateDetection(text);
		//문자열, 해당 문자열의 언어코드, 번역할 언어코드를 매개변수로 받아 번역을 실행하는 메서드를 실행하며 그 결과값을 리턴
        return translationService.translateFeed(text, sourceLang, targetLang);
    }
	
}
