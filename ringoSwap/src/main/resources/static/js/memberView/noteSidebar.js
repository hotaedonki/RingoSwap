$(document).ready(function () {
    const sidebar = $('nav.sidebar');
    const noteHome = $('.noteHome');
    
    $(".toggle").click(function () {
        sidebar.toggleClass("close");
    });


    $(".toggle-switch").click(function () {
        $('body').toggleClass("dark");
        const modeText = $(".mode-text");

        if ($('body').hasClass("dark")) {
            modeText.text("Light mode");
        } else {
            modeText.text("Dark mode");
        }
    });
    
    $('.toggle-card-btn').on('click', function() {
        let $card = $(this).closest('.card'); // 해당 버튼의 가장 가까운 '.card' 요소를 선택
        $card.toggle(); // 카드의 표시/숨김 상태를 토글합니다.

        // 아이콘 변경: on/off
        if ($card.is(':visible')) {
            $(this).removeClass('bi-toggle-off').addClass('bi-toggle-on');
        } else {
            $(this).removeClass('bi-toggle-on').addClass('bi-toggle-off');
        }
    });
    
    $(".bi-file-earmark-plus").click(function() {
        $("#chooseModal").modal('show');
    });

    // .bi-pencil 아이콘 클릭 시 이름 수정
    $(".bi-pencil").click(function() {
        let currentItem = $(this).closest('.folder, .note, .wordbook');
        let currentName = currentItem.text().trim();
        let newName = prompt("새 이름을 입력하세요:", currentName);
        if (newName) {
            currentItem.children(":first").text(newName);
        }
    });

    // .bi-trash 아이콘 클릭 시 삭제 확인 알림
    $(".bi-trash").click(function() {
        if (confirm("삭제하시겠습니까?")) {
            $(this).closest('.folder, .note, .wordbook').remove();
        }
    });
    
    $(".folder").click(function() {
        $(this).find('.note, .wordbook').toggle();
    });

    // 폴더 생성 모달 출력
    $(".bi-folder-plus").click(function() {
        let folderName = prompt("새 폴더의 이름을 입력하세요:");
        if (folderName) {
            // 폴더 생성 로직 (예: DOM 요소 추가 등)
            $(".card-body").append('<div class="folder"><i class="bi bi-folder"></i> ' + folderName + '<div class="action-icons"><i class="bi bi-pencil"></i><i class="bi bi-trash"></i></div></div>');
        }
    });

    // 노트/단어장 생성 모달에서 이름 입력 필드 추가
    $(".btn.btn-primary, .btn.btn-secondary").click(function() {
        let name = prompt("이름을 입력하세요:");
        if (name) {
            // 노트/단어장 생성 로직
        }
    });
});
