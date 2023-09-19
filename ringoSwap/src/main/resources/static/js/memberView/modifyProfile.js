// 변수 및 설정
const languageImages = {
    "한국어": "../img/한국어.jpg",
    "일본어": "../img/일본어.jpg",
    "영어": "../img/영어.jpg"
};

 $(document).ready(function() {

    $('#profilePicInput').click(profileFileInput);  //프로필 클릭시 원하는 사진을 업로드해서 해당 사진으로 프로필 사진을 변경하는 이벤트
    $('.card-img-top').click(backgroundFileInput);   //배경사진 클릭시 원하는 사진을 업로드해서 해당 사진으로 프로필 사진을 변경하는 이벤트
    // 이벤트 핸들러 바인딩
    bindEventHandlers();

    // 초기 데이터 로딩
    //loadGameData();
    //loadPurchasedItems();

    function bindEventHandlers() {
        // 각 이벤트에 대한 핸들러 바인딩
        $(document)
            .on('click', '.hobbyButton button', toggleHobbyButtonClass) // 취미 버튼 클릭 이벤트
            .on('click', '.card-text', enableIntroductionEditing)  // 자기소개 수정 활성화 이벤트
            .on('click', '.modify', sendProfileModification)  // 프로필 수정 이벤트
            .on('click', '.languageSelect', selectDesiredLanguage);  // 언어 선택 이벤트

        $(window).on('beforeunload', saveChangesBeforeExit);  // 페이지 종료 전 변경 사항 저장 이벤트
    }
    memberPrint();
});
/* 멤버정보를 출력하는 함수 */
function memberPrint(){
    $.ajax({
        url: 'myMemberPrint',
        type: 'POST',
        dataType: 'json',
        success: function(member) {
            console.log('member출력');
            $('.nickname').html(member.nickname);
            $('.introduction').html(member.introduction);
            $('.follower-cnt').text(member.fr_count);
            $('.followee-cnt').text(member.fe_count);
             let native = printLanguage(member.native_lang);
             let target = printLanguage(member.target_lang);
            let tagArr = member.tagList;
            $('#profilePicInput').attr('src', './memberProfilePrint?user_id='+member.user_id);
            $('.nativeLanguage').attr('src', native);
            $('.targetLanguage').attr('src', target);
            
            if(tagArr && Array.isArray(tagArr)){
                for(let i=0;i<tagArr.length;i++){
                    console.log(tagArr[i]);
                    $(`.hobbyButton button:contains("${tagArr[i]}")`).attr('class', 'btn btn-primary btn-sm');
                    console.log($(`.hobbyButton button:contains("${tagArr[i]}")`).text());
                }
            }else if(tagArr){
                console.log(tagArr);
                $(`.hobbyButton button[value="${tagArr}"]`).attr('class', 'btn btn-primary btn-sm');
                console.log($(`.hobbyButton button[value="${tagArr}"]`).val());
            }
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

    function toggleHobbyButtonClass() {
        // 취미 버튼 클래스 전환
        $(this).toggleClass('btn-outline-primary btn-primary');
    }

    function enableIntroductionEditing() {
        // 자기소개 수정 활성화
        let currentText = $(this).text();
        let form = $('.form-control').text();
        if(!form){
            $(this).html(`<textarea class="form-control" onkeypress="enterPress(event)">${currentText}</textarea>
            `);
        }
    }

    function enterPress(event){
        if (event.key === 'Enter') {        // 자기소개 수정 완료 이벤트
            updateIntroductionText();
        }
    }
    function updateIntroductionText() {
        // 수정된 자기소개 텍스트 저장
        let newValue = $('.form-control').val();
        console.log(newValue);
        $('.form-control').closest('.card-text').text(newValue);
    }

    function profileFileInput(){
        //profilePicInput 객체 클릭시 profileFileInput객체에 사진 파일을 input하고 profilePicInput에 input한 사진을 띄우는 메서드
            // profileFileInput 객체 클릭
            const fileInput = document.getElementById('profileFileInput');
            fileInput.click();

            // 파일 선택 시 이벤트 처리
            fileInput.addEventListener("change", function () {
                const profilePicInput = document.getElementById("profilePicInput");
                // 선택한 파일을 읽기
                const selectedFile = fileInput.files[0];
                if (selectedFile) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        // 읽어온 이미지를 profilePicInput에 표시
                        profilePicInput.src = e.target.result;
                    };
                reader.readAsDataURL(selectedFile);
                }
            });
    }
    function backgroundFileInput(){
        //배경 사진을 변경하는 함수
        const backInput = document.getElementById('backgroundFileInput');
        backInput.click();

        // 파일 선택 시 이벤트 처리
        backInput.addEventListener("change", function () {
            const backPicInput = document.getElementById('backPicInput');
            // 선택한 파일을 읽기
            const selectedFile = backInput.files[0];
            if (selectedFile) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    // 읽어온 이미지를 profilePicInput에 표시
                    backPicInput.src = e.target.result;
                };
            reader.readAsDataURL(selectedFile);
            }
        });
    }


    /* 수정한 프로필 정보를 수집 및 전송하는 함수부 */
    function sendProfileModification() {
        // 프로필 수정 정보 수집 및 전송
        let updatedTags = collectUpdatedTags();
        let introduction = $('.introduction').text();              //자기소개
        let desiredLanguage = setTargetLanguage();      //배우고 싶은 언어
        let profileData = new FormData();            //프로필 사진 사진객체
        let profileFileInput = document.querySelector('#profileFileInput');
        profileData.append('profileUpload', profileFileInput.files[0]);
        profileData.append('introduction', introduction);
        profileData.append('target_lang', desiredLanguage);

        let backFileInput = document.querySelector("#backgroundFileInput");           //배경사진
		updateTags(updatedTags);		//해당 함수로 멤버태그 수정을 실시합니다.
		let modify = '';
        console.log('태그 : '+updatedTags);
        $.ajax({
            url: 'memberModifyProfile',
            type: 'POST',
            data: profileData,
            contentType: false,
            processData: false,
            success: function() {
                console.log('수정이 완료되었습니다.');
                modify = '100';
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            }
        });
            window.location.href = "../member/myPage"; 
        
    }
	//클릭한 멤버태그를 전부 수집하는 함수입니다.
    function collectUpdatedTags() {
        // 수정된 태그 수집
        let updatedTags = [];
        $('.hobbyButton button').each(function() {
            let tag = $(this).attr('class').split(' ');
            if(tag[1] === 'btn-primary' || tag[2] === 'btn-primary'){
                console.log($(this).text());
                updatedTags.push($(this).text());
            }
        });
        return updatedTags;
    }
    //태그 수정 ajax 실행 함수
    function updateTags(updatedTags){
		$.ajax({
			url: 'memberTagLinkInsert',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(updatedTags),
            success: function() {
				console.log("수정성공");
            },
            error: function(error) {
                console.log(`AJAX call failed:`+JSON.stringify(error));
            }
		});
	}

    //받은 객체에 따라 DB에 집어넣을 수 있는 문자열로 변경해서 리턴하는 함수
    function setTargetLanguage(){
        let lang = $(".targetLanguage").attr('src');
        if(lang === languageImages["한국어"]){
            lang = 'kor';
        }else if(lang === languageImages["일본어"]){
            lang = 'jap';
        }else if(lang === languageImages["영어"]){
            lang = 'eng';
        }else{
            lang = 'kor';
        }
        return lang;
    }

    function prepareFormData(introduction, desiredLanguage, profilePic, backgroundPic) {
        // 수정 데이터를 FormData 객체에 저장
        let formData = new FormData();
        formData.append("introduction", introduction);
        formData.append("desiredLanguage", desiredLanguage);
        formData.append("profilePic", profilePic);
        formData.append("backgroundPic", backgroundPic);
        return formData;
    }

    function selectDesiredLanguage() {
        // 선택된 언어에 따라 이미지 변경
        let selectedLanguage = $(this).data('language');
        changeLanguageImage(selectedLanguage);
        $('#languageModal').modal('hide');
    }


    function loadGameData() {
        // 게임 데이터 로드
        $.ajax({
			url: 'getGameData',
            type: 'POST',
            success: function(response) {
                $('.game-section .card-body').html(`나의 게임 랭크: ${response.gameRank}`);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            }
        });
    }

    function loadPurchasedItems() {
        // 구매한 아이템 데이터 로드
        $.ajax({
			url: 'getItemList',
            type: 'POST',
            success: function(response) {
                updatePurchasedItems(response.items);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            } 
            //jqXHR : XMLHttpRequest의 jquery버전 오류
            //textStatus : 요청의 결과를 나타내는 문자열
            //HTTP 오류가 발생했을 떄의 예외 텍스트
        });
    }

    function updatePurchasedItems(items) {
        // 구매한 아이템 목록 업데이트
        let itemList = items.map(item => `<p>${item.name}: ${item.date} 구매</p>`).join('');
        $('.store-section .card-body').html(itemList);
    }

    function saveChangesBeforeExit() {
        // 페이지 종료 전 변경 사항 저장
        $.ajax({
			url: 'saveBeforeExit',
            type: 'POST',
            data: {
                // 수정 내용 데이터
            },
            async: false,
            success: function(response) {
                // 필요한 경우 응답 처리
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            }
        });
    }

    function changeLanguageImage(language) {
        // 선택된 언어에 따라 프로필 이미지 변경
        $('.profile-info .targetLanguage').attr('src', languageImages[language]);
    }

