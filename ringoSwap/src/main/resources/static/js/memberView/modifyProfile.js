$(document).ready(function() {

    // Variables and configurations
    const languageImages = {
        "한국어": "@{../img/한국어.jpg}",
        "일본어": "@{../img/일본어.jpg}",
        "영어": "@{../img/영어.jpg}"
    };

    // Event Handlers Binding
    bindEventHandlers();

    // Initial Data Loading
    loadGameData();
    loadPurchasedItems();

    function bindEventHandlers() {
        $(document)
            .on('click', '.hobbyButton ~', toggleHobbyButtonClass)
            .on('click', '.card-text', enableIntroductionEditing)
            .on('blur', '.form-control textarea', updateIntroductionText)
            .on('blur', '.modify', sendProfileModification)
            .on('click', '.languageSelect', selectDesiredLanguage);

        $(window).on('beforeunload', saveChangesBeforeExit);
    }

    function toggleHobbyButtonClass() {
        $(this).toggleClass('btn-outline-primary btn-primary');
    }

    function enableIntroductionEditing() {
        let currentText = $(this).text();
        $(this).html(`<textarea class="form-control">${currentText}</textarea>`);
    }

    function updateIntroductionText() {
        let newValue = $(this).val();
        console.log(newValue);
        $(this).closest('.card-text').text(newValue);
    }

    function sendProfileModification() {
        let updatedTags = collectUpdatedTags();
        let introduction = $('.card-text').text();
        let desiredLanguage = $("#desiredLanguage").val();
        let profilePic = $("#profilePicInput")[0].files[0];
        let backgroundPic = $("#backgroundPicInput")[0].files[0];

        let formData = prepareFormData(updatedTags, introduction, desiredLanguage, profilePic, backgroundPic);
		
		updateTags(updateTags);		//해당 함수로 멤버태그 수정을 실시합니다.
		
        $.ajax({
            url: '/modifyProfile',
            type: 'POST',
            data: formData,
            dataType: 'json',
            success: function(response) {
                alert('수정이 완료되었습니다.');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            }
        });
    }
	//클릭한 멤버태그를 전부 수집하는 함수입니다.
    function collectUpdatedTags() {
        let updatedTags = [];
        $('.hobbyButton').each(function() {
            updatedTags.push($(this).text());
        });
        return updatedTags;
    }
    //태그 수정 ajax 실행 함수
    function updateTags(updateTags){
		$.ajax({
			url: '/memberTagLinkInsert',
            type: 'POST',
            data: {updatedTags : updateTags},
            success: function() {
				console.log("수정성공");
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            }
		});
	}

    function prepareFormData(updatedTags, introduction, desiredLanguage, profilePic, backgroundPic) {
        let formData = new FormData();
        formData.append("updatedTags", JSON.stringify(updatedTags));
        formData.append("introduction", introduction);
        formData.append("desiredLanguage", desiredLanguage);
        formData.append("profilePic", profilePic);
        formData.append("backgroundPic", backgroundPic);
        return formData;
    }

    function selectDesiredLanguage() {
        let selectedLanguage = $(this).data('language');
        changeLanguageImage(selectedLanguage);
        $('#languageModal').modal('hide');
    }

    function loadGameData() {
        $.ajax({
            type: 'GET',
            url: '/getGameData',
            success: function(response) {
                $('.game-section .card-body').html(`나의 게임 랭크: ${response.gameRank}`);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            }
        });
    }

    function loadPurchasedItems() {
        $.ajax({
            type: 'GET',
            url: '/getPurchasedItems',
            success: function(response) {
                updatePurchasedItems(response.items);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            }
        });
    }

    function updatePurchasedItems(items) {
        let itemList = items.map(item => `<p>${item.name}: ${item.date} 구매</p>`).join('');
        $('.store-section .card-body').html(itemList);
    }

    function saveChangesBeforeExit() {
        $.ajax({
            type: 'POST',
            url: '/saveChanges',
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
        $('.profile-info img').attr('src', languageImages[language]);
    }
});
