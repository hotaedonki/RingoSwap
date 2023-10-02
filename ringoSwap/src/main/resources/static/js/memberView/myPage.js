function bringMyName() {
	$.ajax({
		url: '../member/nicknamePrint'
		, type: 'post'
		, success: function(name) {
			myNickname = name;
		}, error: function(error) {
			console.log(error);
		}
	})
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
                console.log('./memberProfilePrint?user_id='+member.user_id);
                console.log('./memberBackPrint?user_id='+member.user_id);
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
    
    

function goToMyFeed() {
	const url = `../feed/feedMain?nickname=${myName}`;
	window.location.href = url;
}


 $(document).ready(function(){
   memberPrint();
   bringMyName();
   $(document).on('click', '.goToMyFeed', goToMyFeed);
 });

