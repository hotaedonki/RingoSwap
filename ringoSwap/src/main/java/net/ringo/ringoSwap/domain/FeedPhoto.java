package net.ringo.ringoSwap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedPhoto {
	int photo_num;							//사진 번호
	int feed_num;							//사진을 업로드한 피드 번호
	String origin_file;						//사진의 실제 이름
	String saved_file;						//사진의 저장경로에서의 이름
	String inputdate;						//사진 업로드 날짜 및 시간
	String description;						//사진에 대한 설명이나 캡션
	int photo_size;							//사진 파일의 크기 (바이트 단위)
	String photo_format;					//사진의 확장자나 형식 (예: JPEG, PNG, GIF 등)
	String resolution;						//사진의 해상도 (예: 1920x1080)
	
	public String toString()
	{
		return String.format("photo_num : {%d} / feed_num : {%d} / origin_file : {%s} / saved_file : {%s} / inputdate : {%s} / description : {%s} / photo_size : {%d} / photo_format : {%s} / resolution : {%s}"
				, photo_num, feed_num, origin_file, saved_file, inputdate, description, photo_size, photo_format, resolution);
	}
}
