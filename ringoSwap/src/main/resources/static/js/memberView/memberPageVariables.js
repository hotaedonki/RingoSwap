// 변수 및 설정
const languageImages = {
    "한국어": "../img/한국어.jpg",
    "일본어": "../img/일본어.jpg",
    "영어": "../img/영어.jpg"
};

$(document).ready(function() {   
	
	$(document).on('click', '', followerSearch);
	$(document).on('click', '', followeeSearch);
    memberPrint();
});