package net.ringo.ringoSwap.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class JsonRead {
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

