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
    //받은 객체에 따라 DB에 집어넣을 수 있는 문자열로 변경해서 리턴하는 함수
    function setTargetLanguage(){
        let lang = $(".targetLanguage").attr('src');
        if(lang === languageImages["한국어"]){
            lang = 'ko';
        }else if(lang === languageImages["일본어"]){
            lang = 'ja';
        }else if(lang === languageImages["영어"]){
            lang = 'en';
        }else{
            lang = 'ko';
        }
        return lang;
    }

    function selectDesiredLanguage() {
        // 선택된 언어에 따라 이미지 변경
        let selectedLanguage = $(this).data('language');
        changeLanguageImage(selectedLanguage);
        $('#languageModal').modal('hide');
    }
    
    
//해당 사용자가 현 페이지 주인계정과 동일한 ID값을 가지는지 확인하는 함수
function memberIdCheck(pageUserId){
    let currentUserid = null;
    $.ajax({
        url:'/ringo/member/currentUserIdSearch',
        type:"post",
        dataType:'json',
        success:function(id){
            currentUserid = id;
        },
        error:function(e){
            console.log(e);
        }
    })
    if(currentUserid === pageUserId){
        return true;
    }else {
        return false;
    }
}

$(document).ready(function() {   
	$(document).on('click', '#follower-btn a', followerSearch);
	$(document).on('click', '#follow-btn a', followeeSearch);
});