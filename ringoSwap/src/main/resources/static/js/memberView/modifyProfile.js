$(document).ready(function() {

    // 변수 및 설정
    const languageImages = {
        "한국어": "@{../img/한국어.jpg}",
        "일본어": "@{../img/일본어.jpg}",
        "영어": "@{../img/영어.jpg}"
    };

    // 이벤트 핸들러 바인딩
    bindEventHandlers();

    // 초기 데이터 로딩
    loadGameData();
    loadPurchasedItems();

    function bindEventHandlers() {
        // 각 이벤트에 대한 핸들러 바인딩
        $(document)
            .on('click', '.hobbyButton ~', toggleHobbyButtonClass) // 취미 버튼 클릭 이벤트
            .on('click', '.card-text', enableIntroductionEditing)  // 자기소개 수정 활성화 이벤트
            .on('blur', '.card-text textarea', updateIntroductionText)  // 자기소개 수정 완료 이벤트
            .on('click', '.modify', sendProfileModification)  // 프로필 수정 이벤트
            .on('click', '.languageSelect', selectDesiredLanguage);  // 언어 선택 이벤트

        $(window).on('beforeunload', saveChangesBeforeExit);  // 페이지 종료 전 변경 사항 저장 이벤트
    }

    function toggleHobbyButtonClass() {
        // 취미 버튼 클래스 전환
        $(this).toggleClass('btn-outline-primary btn-primary');
    }

    function enableIntroductionEditing() {
        // 자기소개 수정 활성화
        let currentText = $(this).text();
        $(this).html(`<textarea class="form-control">${currentText}</textarea>`);
    }

    function updateIntroductionText() {
        // 수정된 자기소개 텍스트 저장
        let newValue = $(this).val();
        $(this).closest('.card-text').text(newValue);
    }

    function sendProfileModification() {
        // 프로필 수정 정보 수집 및 전송
        let updatedTags = collectUpdatedTags();
        let introduction = $('.card-text').text();
        let desiredLanguage = $("#desiredLanguage").val();
        let profilePic = $("#profilePicInput")[0].files[0];
        let backgroundPic = $("#backgroundPicInput")[0].files[0];

        let formData = prepareFormData(updatedTags, introduction, desiredLanguage, profilePic, backgroundPic);

        $.ajax({
            url: 'modifyProfile',
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            dataType: 'json',
            success: function(response) {
                alert('수정이 완료되었습니다.');
                window.location.href = "/member/myPage"; 
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            }
        });
    }

    function collectUpdatedTags() {
        // 수정된 태그 수집
        let updatedTags = [];
        $('.hobbyButton').each(function() {
            updatedTags.push($(this).text());
        });
        return updatedTags;
    }

    function prepareFormData(updatedTags, introduction, desiredLanguage, profilePic, backgroundPic) {
        // 수정 데이터를 FormData 객체에 저장
        let formData = new FormData();
        formData.append("updatedTags", JSON.stringify(updatedTags));
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
                console.error(`AJAX call failed: ${textStatus}, ${errorThrown}`);
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
                console.error(`AJAX call failed: ${textStatus}, ${errorThrown}`);
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
        $('.profile-info img').attr('src', languageImages[language]);
    }
});

