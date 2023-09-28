// 변수 및 설정
const languageImages = {
    "한국어": "../img/한국어.jpg",
    "일본어": "../img/일본어.jpg",
    "영어": "../img/영어.jpg"
};

/* 입력된 언어코드 문자열을 해당 언어의 이미지로 변환하는 함수 */
function printLanguage(lang){
    if(lang === 'ko'){
        lang = languageImages["한국어"];
    }else if(lang === 'ja'){
        lang = languageImages["일본어"];
    }else if(lang === 'en'){
        lang = languageImages["영어"];
    }
    return lang;
}

$(document).ready(function() {   
	$(document).on('click', '#follower-btn a', followerSearch);
	$(document).on('click', '#follow-btn a', followeeSearch);
	
    memberPrint();
});