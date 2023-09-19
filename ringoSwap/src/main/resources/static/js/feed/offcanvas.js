const offcanvaslanguage = {
   "한국어": "../img/한국어.jpg",
   "일본어": "../img/일본어.jpg",
   "영어": "../img/영어.jpg"
};

let username

function showOffcanvasWithUserData() {
	const name = $(this).closest('[data-user-name]').data('user-name');
	username = name;
	const offcanvsElement = document.getElementById('offcanvasWithBothOptions');
	const offcanvas = new bootstrap.Offcanvas(offcanvsElement);
	
	console.log(username)
	
	$.ajax({
		url: 'showOffcanvasWithUserData'
		, type: 'post'
		, data: { username: username }
		, success: function(userInfo) {
			document.getElementById('username').textContent = userInfo.username;
		    document.getElementById('original_profile').src = "../member/memberProfilePrint?user_id=" + userInfo.user_id;
		    document.getElementById('target_lang_img').src = userInfo.target_lang; 
		    document.getElementById('native_lang_img').src = userInfo.native_lang; 
		    document.getElementById('introduction').textContent = userInfo.introduction ? userInfo.introduction : "자기소개 없음";
		    
		    let native = printLanguage(userInfo.native_lang);
            let target = printLanguage(userInfo.target_lang);
		    
		    $('#native_lang_img').attr('src', native);
            $('#target_lang_img').attr('src', target);

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

function goToOtherProfile(){
	const url = `../member/otherPage?username=${encodeURIComponent(username)}`;
	alert(url);
	window.location.href = url;
}

