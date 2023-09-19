const offcanvaslanguage = {
   "한국어": "../img/한국어.jpg",
   "일본어": "../img/일본어.jpg",
   "영어": "../img/영어.jpg"
};

function showOffcanvasWithUserData() {
	const nickname = $(this).closest('[data-user-name]').data('user-name');
	const offcanvsElement = document.getElementById('offcanvasWithBothOptions');
	const offcanvas = new bootstrap.Offcanvas(offcanvsElement);
	
	console.log(nickname)
	
	$.ajax({
		url: 'showOffcanvasWithUserData'
		, type: 'post'
		, data: { nickname: nickname }
		, success: function(userInfo) {
			document.getElementById('nickname').textContent = userInfo.nickname;
		    document.getElementById('original_profile').src = "../member/memberProfilePrint?user_id=" + userInfo.user_id;
		    document.getElementById('target_lang_img').src = userInfo.target_lang; 
		    document.getElementById('native_lang_img').src = userInfo.native_lang; 
		    document.getElementById('introduction').textContent = userInfo.introduction ? userInfo.introduction : "자기소개 없음";
		    
		    let native = printLanguage(userInfo.native_lang);
            let target = printLanguage(userInfo.target_lang);
		    
		    $('#native_lang_img').attr('src', native);
            $('#target_lang_img').attr('src', target);

			followCheck(nickname);
			offcanvas.show();
		},
		error: function(error) {
			console.error(error)
		}
	})
}

function printLanguage(lang){
   if(lang === 'kor'){
       lang = offcanvaslanguage["한국어"];
   }else if(lang === 'jap'){
       lang = offcanvaslanguage["일본어"];
   }else if(lang === 'eng'){
       lang = offcanvaslanguage["영어"];
   }
   return lang;
}


function followCheck(nickname){
	//해당 회원을 팔로우 했는지 여부를 체크하여, 오프캔버스의 팔로우 관련 버튼 중 해당하는 기능의 버튼을 출력하는 함수

    $.ajax({
        url: "followCheck",
        type: "post",
        data: {nickname : nickname},
        dataType:'json',
        success:function(result){
            if(result == 0){
                console.log('팔로우');
				$('#follow_button').show();			//버튼을 출력함
				$('#unfollow_button').hide();		//버튼을 숨김
            }else{
                console.log('언팔로우');
				$('#follow_button').hide();			//버튼을 숨김
				$('#unfollow_button').show();		//버튼을 출력함
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}