$(document).ready(function () {
    const sidebar = $('nav.sidebar');
    const noteHome = $('.noteHome');
    setToggleBtnPosition();
    $(window).resize(setToggleBtnPosition);
    
    function setToggleBtnPosition() {
    const sidebarWidth = $(".sidebar").width();
    const directoryWidth = $(".toggleDirectory").width();
    const btnPosition = sidebarWidth + directoryWidth + 50;  // 25px 추가
	console.log(sidebarWidth, directoryWidth, btnPosition)
    $(".toggle-card-btn").css("left", btnPosition + "px");
};
    
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
    const toggleDirectory = $('.toggleDirectory');
    const toggleBtn = $('.toggle-card-btn');
    
    // toggleDirectory의 표시 상태를 변경
    toggleDirectory.toggle();

    // 아이콘 변경: on/off
    if (toggleDirectory.is(':visible')) {
        toggleBtn.removeClass('bi-toggle-on').addClass('bi-toggle-off');
    } else {
        toggleBtn.removeClass('bi-toggle-off').addClass('bi-toggle-on');
    }
});
    
    $(".bi-file-earmark-plus").click(function() {
        $("#chooseModal").modal('show');
    });

    // .bi-pencil 아이콘 클릭 시 이름 수정
    $(".bi-pencil").click(function() {
		console.log("이름 수정")
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
        let folderName;
        if (folderName) {
            // 폴더 생성 로직 (예: DOM 요소 추가 등)
            $(".card-body").append('<div class="folder"><i class="bi bi-folder"></i> ' + folderName + '<div class="action-icons"><i class="bi bi-pencil"></i><i class="bi bi-trash"></i></div></div>');
        }
    });

    // 노트/단어장 생성 모달에서 이름 입력 필드 추가
    //$(".btn.btn-primary, .btn.btn-secondary").click(function() {
   //     let name = prompt("이름을 입력하세요:");
   //     if (name) {
            // 노트/단어장 생성 로직
    //    }
   // });
});
