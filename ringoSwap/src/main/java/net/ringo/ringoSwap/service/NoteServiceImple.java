package net.ringo.ringoSwap.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ringo.ringoSwap.dao.NoteDAO;
import net.ringo.ringoSwap.domain.DirFile;
import net.ringo.ringoSwap.domain.DirPhoto;
import net.ringo.ringoSwap.domain.DirWord;
import net.ringo.ringoSwap.domain.Directory;
import net.ringo.ringoSwap.util.PageNavigator;

@Service
public class NoteServiceImple implements NoteService{
	@Autowired
	NoteDAO dao;
	
	//사용자의 아이디를 매개변수로 dao파일에 넘겨, dao로부터 해당 id로 검색하여 나온 모든 폴더 정보를 배열로 리턴받는 메서드
	@Override
	public ArrayList<Directory> userDirectorySelectAll(int user_num){
		return dao.userDirectorySelectAll(user_num);
	}
	//사용자의 아이디를 매개변수로 dao파일에 넘겨, dao로부터 해당 id로 검색하여 나온 모든 파일 정보를 배열로 리턴받는 메서드
	@Override
	public ArrayList<DirFile> userFileSelectAll(HashMap<String, Object> map){
		return dao.userFileSelectAll(map);
	}

	//매개변수로 주어지는 부모폴더 정보로 검색된 폴더목록을 리턴하는 메서드
	@Override
	public ArrayList<Directory> directorySelectByPDirNum(int dir_num){
		return dao.directorySelectByPDirNum(dir_num);
	}
	//매개변수로 주어지는 부모폴더 정보로 검색된 파일목록을 리턴하는 메서드
	@Override
	public ArrayList<DirFile> fileSelectByDirNum(HashMap<String, Object> map){
		return dao.fileSelectByDirNum(map);
	}
	
	//매개변수로 file_num을 받아 해당 번호의 파일 객체를 리턴하는 메서드
	@Override
	public DirFile fileSelectByFileNum(int file_num) {
		return dao.fileSelectByFileNum(file_num);
	}
	//매개변수로 file_num을 받아 해당 파일에 기록된 사진 목록을 배열로 리턴하는 메서드
	@Override
	public ArrayList<DirPhoto> filePhotoSelectByFileNum(int file_num){
		return dao.filePhotoSelectByFileNum(file_num);
	}
	
	//file_num을 매개변수로 검색해 나온 file객체에서 해당 파일의 title값만 리턴하는 메서드
	@Override
	public String fileSelectByFileNumReturnTitle(int file_num) {
		DirFile file = dao.fileSelectByFileNum(file_num);
		String title = file.getTitle();
		return title;
	}

	//file_num을 매개변수로 해당 파일에 속하는 단어목록 네비게이터를 생성하는 메서드
	@Override
	public PageNavigator wordSelectPageNavigator(int pagePerGroup, int countPerPage, int page, int file_num) {
		//단어장의 단어 갯수 총합을 검색
		int total = dao.wordSelectByFileNum(file_num);
		//검색한 단어를 기준으로 단어장에 사용할 네비게이터 navi 생성
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		
		return navi;
	}
	//file_num을 매개변수로 해당 파일에 속하는 word객체 목록을 리턴하는 메서드 
	@Override
	public ArrayList<DirWord> selectWordArrayByFileNum(PageNavigator navi, int file_num){

		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
		
		return dao.selectWordArrayByFileNum(file_num, rb);
	}

	//사용자의 가장 최근 수정한 파일을 출력하는 메서드
	@Override
	public DirFile fileSelectByModifieDate(int user_num) {
		return dao.fileSelectByModifieDate(user_num);
	}
	//-----------[ 노트 출력기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 생성기능 시작 ]-----------------------
	//폴더를 생성하는 메서드
	@Override
	public int dirCreateOne(Directory dir) {
		return dao.dirCreate(dir);
	}
	//파일을 생성하는 메서드
	@Override
	public int fileCreateOne(DirFile file) {
		return dao.fileCreateOne(file);
	}
	//특정 파일에 속한 사진 객체 배열을 생성하는 메서드
	@Override
	public int filePhotoArrayInsert(ArrayList<DirPhoto> photo) {
		return dao.filePhotoArrayInsert(photo);
	}
	
	//파일 부속 객체인 Word를 생성하는 메서드
	@Override
	public int wordCreateOne(DirWord word) {
		return dao.wordCreateOne(word);
	}
	//-----------[ 노트 생성기능 종료 ]-------------->>>>>>>>>>>>>>
	
	//<<<<<<<<<<<<-----[ 노트 삭제기능 시작 ]-----------------------
	//dir_num을 매개변수로 해당 폴더를 DB에서 삭제하는 기능
	@Override
	public int dirDeleteOne(HashMap<String, Integer> map) {
		return dao.dirDeleteOne(map);
	}
	
	//파일번호를 매개변수로 해당 파일을 DB에서 삭제하는 기능
	@Override
	public int fileDeleteOne(HashMap<String, Integer> map) {
		return dao.fileDeleteOne(map);
	}
	//-----------[ 노트 삭제기능 종료 ]-------------->>>>>>>>>>>>>>

	//<<<<<<<<<<<<-----[ 노트 수정기능 시작 ]-----------------------
	//수정된 파일번호를 보내 해당 파일번호의 파일의 이름을 수정하는 기능
	@Override
	public int fileUpdateOne(HashMap<String, Object> map) {
		return dao.fileUpdateOne(map);
	}
	//메모장 작성 완료 후 수정한 작성파일을 DB로 보내는 메서드
	@Override
	public int fileTextUpdateOne(HashMap<String, Object> map) {
		return dao.fileTextUpdateOne(map);
	}
	//단어 수정전 수정할 단어를 DB에서 검색해 리턴하는 메서드
	@Override
	public DirWord wordSearchByWordNum(HashMap<String, Integer> map) {
		return dao.wordSearchByWordNum(map);
	}
	//화살표 클릭시 해당 단어를 기준으로 단어장 내 직전/직후 단어를 리턴하는 메서드
	@Override
	public DirWord wordSearchByArrow(HashMap<String, Object> map) {
		return dao.wordSearchByArrow(map);
	}
	//수정한 단어 객체를 DB에 전달해 수정하는 메서드
	@Override
	public int wordUpdateOne(DirWord word) {
		return dao.wordUpdateOne(word);
	}
	@Override
	public int wordDeleteOne(HashMap<String, Integer> map) {
		return dao.wordDeleteOne(map);
	}
	@Override
	public int checkLastWord(HashMap<String, Object> checkMap) {
		return dao.checkLastWord(checkMap);
	}
	//단어장에서 단어를 추가/수정/삭제할때마다 단어장의 수정일자 정보를 현재 시각으로 수정하는 메서드
	@Override
	public int wordFileUpdate(int user_num, int file_num){
		HashMap<String, Integer> map = new HashMap<>();
		map.put("user_num", user_num);
		map.put("file_num", file_num);
		return dao.wordFileUpdate(map);
	}
}
