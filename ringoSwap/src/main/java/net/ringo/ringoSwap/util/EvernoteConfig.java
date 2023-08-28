package net.ringo.ringoSwap.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EvernoteConfig {
	
	@Bean
	public EvernoteSession evernote() {
		EvernoteSession session = new EvernoteSession.Builder(context)
			    .setEvernoteService(EvernoteService.SANDBOX)
			    .setApiKey("YOUR_API_KEY")
			    .setApiSecret("YOUR_API_SECRET")
			    .build();
		
	}


}
