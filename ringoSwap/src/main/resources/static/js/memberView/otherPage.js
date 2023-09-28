let nickname;  // Global variable to hold the nickname value

function getnicknameFromUrl() {
  const urlParams = new URLSearchParams(window.location.search);
  nickname = urlParams.get('nickname');  // Updating the global variable
  console.log('nickname:', nickname);
}

function printOtherPeoplePage() {
  if (!nickname) {
    console.error('nickname is not defined');
    return;
  }

  $.ajax({
    url: '../member/goToOtherProfile',
    type: 'post',
    data: { nickname: nickname },
    success: function(member) {
       $('.nickname').html(member.nickname);
       $('.introduction').html(member.introduction);
       $('.follower-cnt').html(member.fr_count);
       $('.followee-cnt').html(member.fe_count);
       $('.goToOtherPeopleFeed').html(member.nickname + "님의 피드가기");
       let native = printOtherPeopleLanguage(member.native_lang);
       let target = printOtherPeopleLanguage(member.target_lang);
       let tagArr = member.tagList;
       $('#profilePicInput').attr('src', '../member/memberProfilePrint?user_id='+member.user_id);
       $('#backPicInput').attr('src', '../member/memberBackPrint?user_id='+member.user_id);
       $('.nativeLanguage').attr('src', native);
       $('.targetLanguage').attr('src', target);
       
       let str = '<h5 class="card-title">정보/취미</h5>';
       if(tagArr && Array.isArray(tagArr)){
           for(let i=0;i<tagArr.length;i++){
               str += `<button type="button" class="btn btn-outline-primary btn-sm">${tagArr[i]}</button>`;
           }
       }else if(tagArr){
        str = `<button type="button" class="btn btn-outline-primary btn-sm">${tagArr}</button>`;
       }
       $(`.hobbyButton `).html(str);
       console.log('member출력완료');
    	},
    error: function(jqXHR, textStatus, errorThrown) {
      console.log('에러남 ㅅㄱ');
    }
  });
}


function goToOtherPeopleFeed() {
	const url = `../feed/feedMain?nickname=${nickname}`;
	window.location.href = url;
}

$(document).ready(function() {
  getnicknameFromUrl();
  printOtherPeoplePage();
  printOtherPeopleLanguage();
  $(document).on('click', '.goToOtherPeopleFeed', goToOtherPeopleFeed);
  $(document).on('click', '#follower-btn a', followerSearch);
  $(document).on('click', '#follow-btn a', followeeSearch);
	
  memberPrint();
});

