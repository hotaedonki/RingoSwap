// 함수처리용 변수값 설정 (검색 및 정렬을 위한 변수)
let ca = 'ko';
let st = 'input';

$(document).ready(function() { 
    let originalWordList = ""; 
    let currentWordNum;

	// 문서 준비가 완료되면 실행
    dirPrint();
    offCanvasButtonPrint();
    latestFilePrint();  //가장 최근 수정한 메모장or단어장 출력 함수
    printNote();    
    // 정렬 방식 설정
    $('.sortBtn').click(sortEvent);
    $('#createFolder').click(dirCreate);
    // 노트, 단어장 생성
    $('#createNote').click(() => sendFileInfo('note'));
    $('#createWord').click(() => sendFileInfo('word'));
    $(document).on('click', '.dir-btn', highlightSelectedFolder);
    $(document).on('click', '.dirOpen', dirOpen);
    $(document).on('click', '.dirDelete', dirDelete);
    $(document).on('click', '.yes', closeModal);
    $(document).on('click', '.dropdown-item.sortBtn', sortBtnNameChange);
    $(document).on('click', '.offcanvas-body', folderClose);
    $(document).on('click', '.offcanvas-body *', function(event) {
	    event.stopPropagation();
	});

    window.addEventListener('load', function () {
        const fileNum = getUrlParam('file');
        const fileType = getUrlParam('type');
        if (fileNum) {
            // 파일 번호가 URL에 있을 경우 해당 텍스트 객체 열기
            fileOpenUrl(fileNum, fileType);
        }
    });
    
    // "추가" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.add-btn', handleAddButtonClick);
    // "저장" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.save-btn', handleSaveButtonClick);
    // "돌아가기" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.back-btn', handleBackButtonClick);
    // 단어 수정
    $(document).on('click', '.modifyWord', handleModifyWordClick);
    // "수정" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.word-modify-btn', handleWordModifyButtonClick);
    // "삭제" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.word-delete-btn', handleWordDeleteButtonClick);
    // 앞뒤 단어로 이동
    $(document).on('click', '.carousel-control-next, .carousel-control-prev', handleWordNavigation);
});