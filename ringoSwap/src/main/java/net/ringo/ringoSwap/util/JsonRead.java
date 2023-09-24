package net.ringo.ringoSwap.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

//json파일의 경로를 읽어 그 경로를 문자열로 출력하는 메서드
public class JsonRead {
	// main/resource/를 기본 경로로 하여 해당 json파일의 경로를 문자열로 출력하는 메서드
	public static String readJsonFile(String filePath) {
	    Resource resource = new ClassPathResource(filePath);
	    try (InputStream inputStream = resource.getInputStream(); Scanner scanner = new 
	    	Scanner(inputStream, StandardCharsets.UTF_8.name())) {
	        return scanner.useDelimiter("\\A").next();
	    } catch(IOException e) {
	    	e.printStackTrace();
	    	return "ERROR";
	    }
	}
}

