package net.ringo.ringoSwap.util;

import lombok.Data;

/**
 * 게시판 페이징 처리 클래스
 */
@Data
public class PageNavigator {
	//페이지 관련 정보 
	private int countPerPage;		//페이지당 글목록 수
	private int pagePerGroup;		//그룹당 페이지 수 
	private int currentPage;		//현재 페이지 (최근 글이 1부터 시작)
	private int totalRecordsCount;	//전체 글 수
	private int totalPageCount;		//전체 페이지 수
	private int currentGroup;		//현재 그룹 (최근 그룹이 0부터 시작)
	private int startPageGroup;		//현재 그룹의 첫 페이지
	private int endPageGroup;		//현재 그룹의 마지막 페이지
	private int startRecord;		//전체 결과 중 현재 페이지 첫 글의 위치 (0부터 시작)
	
	/*
	 * 생성자
	 */
	public PageNavigator(int pagePerGroup, int countPerPage, int currentPage, int totalRecordsCount) {
		//그룹당 페이지 수, 페이지당 글 수, 현재 페이지, 전체 글 수를 전달받음
		this.countPerPage = countPerPage;
		this.pagePerGroup = pagePerGroup;
		this.totalRecordsCount = totalRecordsCount  < 1 ? 1 : totalRecordsCount;
		
		//전체 페이지 수
		totalPageCount = (totalRecordsCount + countPerPage - 1) / countPerPage;

		//전달된 현재 페이지가 1보다 작으면 현재페이지를 1페이지로 지정
		if (currentPage < 1)	currentPage = 1;
		//전달된 현재 페이지가 마지막 페이지보다 크면 현재페이지를 마지막 페이지로 지정
		if (currentPage > totalPageCount)	currentPage = totalPageCount;

		//현재 페이지가 1보다 작으면 1로 처리
		currentPage = currentPage < 1 ? 1 : currentPage;
		this.currentPage = currentPage;

		//현재 그룹
		currentGroup = (currentPage - 1) / pagePerGroup;
		//현재 그룹의 첫 페이지
		startPageGroup = currentGroup * pagePerGroup + 1;
		//현재 그룹의 마지막 페이지
		endPageGroup = startPageGroup + pagePerGroup - 1;
		//현재 그룹의 마지막 페이지가 전체 페이지 수보다 작으면 전체페이지 수를 마지막으로.
		endPageGroup = endPageGroup < totalPageCount ? endPageGroup : totalPageCount;

		//전체 결과 중 현재 페이지 첫 글의 위치

		startRecord = (currentPage - 1) * countPerPage;	
		
        	
		//현재 페이지를 기준으로 주변 페이지 출력
		int naviCount = currentPage - totalPageCount;
		if(naviCount <= -3 && currentPage > 3) {
			startPageGroup = (currentPage - 2);
		}else if(currentPage -totalPageCount >= -2) {
			startPageGroup = (totalPageCount - 4);
			//현재 그룹의 첫 페이지가 1보다 작으면 1로 처리
			startPageGroup = startPageGroup < 1 ? 1 : startPageGroup;
		}
		
		//페이지 수가 5 이하인 경우
		if(currentPage < 4 && totalRecordsCount > 70) endPageGroup = 5;
		else if(naviCount >= -3 && naviCount <= -2) endPageGroup = (currentPage + 2);
		else if(totalRecordsCount % 14 == 0) endPageGroup = totalRecordsCount / 14;
        else endPageGroup = totalRecordsCount / 14 + 1;
		
	}
}