// 변수 및 설정
const languageImages = {
   "한국어": "../img/한국어.jpg",
   "일본어": "../img/일본어.jpg",
   "영어": "../img/영어.jpg"
};

let myName;


 $(document).ready(function(){
   memberPrint();
   $(document).on('click', '.goToMyFeed', goToMyFeed);
 });

/* 멤버정보를 출력하는 함수 */
function memberPrint(){
   $.ajax({
       url: '../member/myMemberPrint',
       type: 'POST',
       dataType: 'json',
       success: function(member) {
		   myName = member.nickname;
           $('.nickname').html(member.nickname);
           $('.introduction').html(member.introduction);
           $('.follower-cnt').html(member.fr_count);
           $('.followee-cnt').html(member.fe_count);
           let native = printLanguage(member.native_lang);
           let target = printLanguage(member.target_lang);
           let tagArr = member.tagList;
           $('#profilePicInput').attr('src', '../member/memberProfilePrint?user_id='+member.user_id);
           $('.nativeLanguage').attr('src', native);
           $('.targetLanguage').attr('src', target);
           
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
/*  */
function printLanguage(lang){
   if(lang === 'kor'){
       lang = languageImages["한국어"];
   }else if(lang === 'jap'){
       lang = languageImages["일본어"];
   }else if(lang === 'eng'){
       lang = languageImages["영어"];
   }
   return lang;
}

function goToMyFeed() {
	const url = `../feed/feedMain?nickname=${myName}`;
	window.location.href = url;
}
