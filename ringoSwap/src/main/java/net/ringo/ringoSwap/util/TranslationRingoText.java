package net.ringo.ringoSwap.util;
import java.io.IOException;

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;

public class TranslationRingoText {
	public String translationFeed(String text, String sourceLang, String targetLang) throws IOException {
		String txt;
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            String projectId = "key-range-399607";  // 여기에 실제 프로젝트 ID를 입력하세요
            String location = "us-central1";
            LocationName locationName = LocationName.of(projectId, location);

            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent(locationName.toString())
                    .setModel("projects/key-range-399607/locations/us-central1/models/model-id")
                    .setSourceLanguageCode(sourceLang)		//언어코드를 기재하며, 기재하는 언어코드로 ko, ja, en를 사용한다
                    .setTargetLanguageCode(targetLang)			//sourceLang과 동일
                    .addContents(text)
                    .build();

            TranslateTextResponse response = client.translateText(request);
            
    		txt = response.getTranslationsList().get(0).getTranslatedText();
        }
		return txt;
	}
	public static void main(String[] args) throws Exception {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            String projectId = "key-range-399607";  // 여기에 실제 프로젝트 ID를 입력하세요
            String location = "us-central1";
            LocationName locationName = LocationName.of(projectId, location);

            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent(locationName.toString())
                    .setModel("projects/your_project_id/locations/us-central1/models/model-id")
                    .setSourceLanguageCode("en")
                    .setTargetLanguageCode("ru")
                    .addContents("Dr. Watson, please discard your trash.")
                    .build();

            TranslateTextResponse response = client.translateText(request);
            System.out.println("Translated Text: " + response.getTranslationsList().get(0).getTranslatedText());
        }
    }
}
