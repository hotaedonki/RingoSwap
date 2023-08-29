$(document).ready(function() {

    // Variables and configurations
    const languageImages = {
        "한국어": "@{../img/한국어.jpg}",
        "일본어": "@{../img/일본어.jpg}",
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
            .on('blur', '.card-text textarea', updateIntroductionText)
            .on('click', '.modify', sendProfileModification)
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
        $(this).closest('.card-text').text(newValue);
    }

    function sendProfileModification() {
        let updatedTags = collectUpdatedTags();
        let introduction = $('.card-text').text();
        let desiredLanguage = $("#desiredLanguage").val();
        let profilePic = $("#profilePicInput")[0].files[0];
        let backgroundPic = $("#backgroundPicInput")[0].files[0];

        let formData = prepareFormData(updatedTags, introduction, desiredLanguage, profilePic, backgroundPic);

        $.ajax({
            url: '/modifyProfile',
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            dataType: 'json',
            success: function(response) {
                alert('수정이 완료되었습니다.');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error(`AJAX call failed: ${textStatus}, ${errorThrown}`);
            }
        });
    }

    function collectUpdatedTags() {
        let updatedTags = [];
        $('.hobbyButton').each(function() {
            updatedTags.push($(this).text());
        });
        return updatedTags;
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
