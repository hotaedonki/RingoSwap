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
    

let myName;
    /* 멤버정보를 출력하는 함수 */
    function memberPrint(){
       $.ajax({
           url: '../member/myMemberPrint',
           type: 'POST',
           dataType: 'json',
           success: function(member) {
               myName = member.nickname;
               $('#userId').val(member.user_id);               //회원 ID
               $('.nickname').html(member.nickname);           //회원 닉네임
               $('.introduction').html(member.introduction);    //자기소개
               $('.follower-cnt').html(member.fr_count);        //팔로워 수
               $('.followee-cnt').html(member.fe_count);        //팔로우 수
               let native = printLanguage(member.native_lang);  //모국어
               let target = printLanguage(member.target_lang);  //학습 희망 언어
               let tagArr = member.tagList;                     //태그목록
	           $('#profilePicInput').attr('src', '../member/memberProfilePrint?user_id='+member.user_id);   //사진위치 지정후 출력
	           $('#backPicInput').attr('src', '../member/memberBackPrint?user_id='+member.user_id);
	           $('#followOpen').val(member.follow_open);       //타인에게 팔로우를 공개하는지 여부
               $('.nativeLanguage').attr('src', native);        //모국어 출력
               $('.targetLanguage').attr('src', target);        //학습 희망 언어 출력
               $('#translateLang').val(member.trans_lang);      //번역할 언어 지정
         
               let str = '<h5 class="card-title">정보/취미</h5>';
               tagArr.forEach(tag => {
                str += `<button type="button" class="btn btn-outline-primary btn-sm">${tag}</button>`;
               })
                if(tagArr.length == 0){
                    str += `<p>설정되지 않았습니다.</p>`;
                }
               $(`.hobbyButton`).html(str);
               console.log('member출력완료');
           },
           error: function(jqXHR, textStatus, errorThrown) {
               console.log('에러남 ㅅㄱ');
           }
       });
    }

$(document).ready(function() {
	$(document).on('show.bs.modal', '.followModalClass', followeeSearch);
    $(document).on('show.bs.modal', '.followerModalClass', followerSearch);
})