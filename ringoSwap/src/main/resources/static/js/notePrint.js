// 함수처리용 변수값 설정 (검색 및 정렬을 위한 변수)
let ca = 'kor';
let st = 'input';

// 문서 준비가 완료되면 실행
$(document).ready(function(){
    dirPrint();    
    // 정렬 방식 설정
    $('.sortBtn').click(sortEvent);
    $('#createFolder').click(dirCreate);
    // 노트, 단어장 생성
    $('#createNote').click(() => fileCreate('note'));
    $('#createWord').click(() => fileCreate('word'));
    $(document).on('click', '.dir-btn', highlightSelectedFolder);
    $(document).on('click', '.dirOpen', dirOpen);
    $(document).on('click', '.dirDelete', dirDelete);
    $(document).on('click', '.yes', closeModal);

    window.addEventListener('load', function () {
        const fileNum = getUrlParam('file');
        const fileType = getUrlParam('type');
        if (fileNum) {
            // 파일 번호가 URL에 있을 경우 해당 텍스트 객체 열기
            console.log('객체 열기');
            fileOpenUrl(fileNum, fileType);
        }
    });
    
	$(".dropdown-item.sortBtn").click(function() {
	    // sortBtnMain의 현재 텍스트를 가져옴
	    let currentMainText = $(".sortBtnMain").text();
	    
	    // 클릭한 .dropdown-item.sortBtn의 텍스트를 가져옴
	    let selectedText = $(this).text();
	    
	    // 두 텍스트를 서로 바꿈
	    $(".sortBtnMain").text(selectedText);
	    $(this).text(currentMainText);
	});
});

// 폴더 버튼을 클릭할 때의 이벤트 처리
function highlightSelectedFolder() {
    $('.btn-dark').removeClass('btn-dark').addClass('btn-outline-dark');
    $(this).removeClass('btn-outline-dark').addClass('btn-dark');
}

// 정렬 방식을 설정하는 함수
function sortEvent() {
    let s = $(this).text();
    s = s === '생성순' ? 'input' : s === '제목순' ? 'title' : 'modifie';
    st = s;
    dirPrint();
}

//URL로부터 파일 번호를 얻어오는 함수
function getUrlParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

// 폴더 정보를 출력하는 ajax 함수
function dirPrint(){
    console.log("폴더 출력하기");
    $.ajax({
        url: 'dirPrint',
        type: 'post',
        dataType: 'json',
        success: function(list){
            console.log("폴더 출력됐음");
            let str = "";
            $(list).each(function(i, item){
                str += `<li><i class="bi bi-folder"></i><button class="btn btn-outline-dark dir-btn dirOpen" 
                data-dir-num="${item.dir_num}">
                ${item.dir_name}</button>
            <span class="modifyAndDelete">
                <i data-dir-num="${item.dir_num}" class="bi bi-trash dirDelete"></i></span>
                <div id="dirPrint${item.dir_num}"></div><div id="filePrint${item.dir_num}"></div>
                </li>`;
            });
            $('.dirPrint').html(str);
        },
        error: function(e){
            console.log(JSON.stringify(e));  
        }
    });
}

// 해당 폴더를 부모 폴더로 설정하고 하위 폴더를 생성하는 함수
function dirCreate() {
    let dir_name = $("#folderNameInput").val(); // 폴더 이름 가져오기
    let parent_dir_num = $('.btn-dark').attr('data-dir-num') || -1; // 상위 폴더의 dir_num 값을 할당. 기본값은 -1입니다.
    console.log("create가 실행됨");
    $.ajax({
        url: "dirCreate",
        type: "POST",
        data: {
            dir_name: dir_name,
            parent_dir_num: parent_dir_num
        },
        success: function(response) {
            console.log("폴더가 생성되었습니다.");
            dirPrint();
            
            $("#folderNameInput").val("");
        },
        error: function(error) {
            // 에러 발생 시 처리
        }
    });
}

// 해당 폴더를 부모 폴더로 설정하고 파일을 생성하는 함수
function fileCreate(fileType){
    let dir_num = $('.btn-dark').attr('data-dir-num');
    let title = $("#fileNameInput").val();
    let file_type = fileType;
    console.log(dir_num);
    console.log(file_type);
    $.ajax({
        url: 'fileCreateOne',
        type: 'post',
        data: { dir_num: dir_num, title: title, file_type: file_type },
        success: function(response) {
            console.log(`${file_type}가 생성되었습니다.`);
            dirOpen.call($(`.dirOpen[data-dir-num=${dir_num}]`));
            
            $("#fileNameInput").val("");
        },
        error: function(e) {
            console.log("파일 생성 중 에러 발생");
        }
    });
}

/* 출력부 */
// 해당 폴더 하위에 있는 폴더와 파일을 불러오는 함수
function dirOpen() {
    let num = $(this).data('dir-num');
    console.log(num);

    // 하위 폴더 불러오기
    $.ajax({
        url: 'dirOpenDirectory',
        type: 'post',
        data: { dir_num: num },
        dataType: 'json',
        success: function(list) {
            let str = '<ul>';
            $(list).each(function(n, item) {
                str += `<li style="position: relative;"><i class="bi bi-folder"></i>
                    <button class="btn btn-outline-dark dir-btn dirOpen" data-dir-num="${item.dir_num}">
                    ${item.dir_name}</button>
                    <i data-dir-num="${item.dir_num}" class="bi bi-trash dirDelete"></i>
                    <div id="dirPrint${item.dir_num}"></div><div id="filePrint${item.dir_num}"></div>
                </li>`;
            });
            str += '</ul>';
            $('#dirPrint' + num).html(str);
            
        },
        error: function(e) {
            console.log("error");
            alert("하위 폴더를 불러오는데 실패했습니다.");
        }
    });

    // 하위 파일 불러오기
    $.ajax({
        url: 'dirOpenFile',
        type: 'post',
        data: { dir_num: num, category: ca, sort: st },
        dataType: 'json',
        success: function(list) {
            let str = '<ul>';
            console.log(list.title);
            $(list).each(function(n, item) {
				let iconClass = (item.file_type === "note") ? "bi-journal" : "bi-file-word"
				let langClass = (item.lang_type === "kor") ? "(한국어)" : (item.lang_type === "jap") ? "(일본어)" : "(영어)"
                str += `<li style="position: relative; cursor:pointer;"><i class="bi ${iconClass} fileType" data-file-type="${item.file_type}"></i>
                            <span data-file-num="${item.file_num}" class="fileOpen">${item.title}</span>
                            <span>${langClass}</span>
                     <i data-file-num="${item.file_num}" data-dir-num="${item.dir_num}" class="bi bi-pencil fileModify" ></i>
                            <i data-file-num="${item.file_num}" data-dir-num="${item.dir_num}" class="bi bi-trash fileDelete"></i>
                       </span></li>`;
            });
            str += '</ul>';
            $('#filePrint' + num).html(str);
            $('.fileOpen').click(fileOpen);
            $('.fileModify').click(fileModify);   
            $('.fileDelete').click(fileDelete);
        },
        error: function(e) {
            console.log("error");
            alert("하위 파일을 불러오는데 실패했습니다.");
        }
    });
}

let file_num_saver;
// 파일 이름을 클릭하면 fileWindow에 표시하는 함수
function fileOpen(){
    let num = $(this).data("file-num");
    file_num_saver = num;
    let type = $(this).siblings('.fileType').data("file-type");
    console.log(num, type);

    //실제 출력 함수 실행
    fileOpenUrl(num, type);
}

function fileOpenUrl(fileNum, fileType, pageNumber = 1){
    file_num_saver = fileNum;
    
    if(fileType === 'note'){
		$('.wordMain-content').hide();
		$('.noteMain-content, noteOffcanvas').show();
        $.ajax({
            url: 'fileOpenNote',
            type: 'post',
            data: {file_num : fileNum},
            dataType: 'json',
            success: function(notepad){
                console.log(notepad);
                let str =`${notepad.file_text}`;
                tinymce.activeEditor.setContent(str);  
                console.log('프린트 완료 : '+notepad);
                history.pushState({ file_num: file_num_saver, file_type: fileType }, '', `?file=${file_num_saver}&type=${fileType}`);
                $('.btn-close').click();
                //$('.noteTitle').text("제목: " + notepad.title);
            },
            error: function(e){
                console.log("error");
            }
        });
    } else if (fileType === 'word'){
		$('.wordMain-content').show();
		$('.noteMain-content, .noteOffcanvas').hide();
        // 클릭한 파일의 분류가 'word=단어장'일 때 실행하는 ajax
        loadPage(1);
    }
}

function loadPage(pageNumber) {
    $.ajax({
        url: 'fileOpenWord',
        type: 'post',
        data: {file_num: file_num_saver, page: pageNumber}, // 페이지 번호 추가
        dataType: 'json',
        success: function(res) {
            let wordList = res.wordList;
            let str1 = '';
            let str2 = '';
            let cnt = 0;
            $(wordList).each(function(i, item) {
                if (cnt < 7) {
                    str1 += `<li class="list-group-item word-card modifyWord" data-word-num="${item.word_num}" data-pron="${item.pron}" data-mean="${item.mean}">
                        <span class="word">${item.word}</span>
                        <div class="word-content">
                            <span class="pronunciation">${(item.pron && item.pron !== '') ? '발음: ' + item.pron : ''}</span>
                            <span class="meaning">${'의미: ' + item.mean}</span>
                            <span class="description" style="display:none;">${item.description}</span>
                        </div> 
                    </li>`;
                } else {
                    str2 += `<li class="list-group-item word-card modifyWord" data-word-num="${item.word_num}" data-pron="${item.pron}" data-mean="${item.mean}">
                        <span class="word">${item.word}</span>
                        <div class="word-content">
                            <span class="pronunciation">${(item.pron && item.pron !== '') ? '발음: ' + item.pron : ''}</span>
                            <span class="meaning">${'의미: ' + item.mean}</span>
                            <span class="description" style="display:none;">${item.description}</span>
                        </div> 
                    </li>`;
                }
                cnt++;
            });

			$('.add-btn').attr('data-file-num', file_num_saver);
            $('.list-group1').html(str1);
            $('.list-group2').html(str2);						
            history.pushState({ file_num: file_num_saver, file_type: "word" }, '', `?file=${file_num_saver}&type=word`);
            $('.btn-close').click();
			//단어장 이름 출력
			$(".wordListName").text(res.title);
            // Pagination 처리 부분
            let paginationHtml = '';
            let navi = res.navi;

			
            
            paginationHtml += '<li class="page-item"><a class="page-link" href="#" onclick="loadPage(' + (navi.currentPage - 1) + ')">&laquo;</a></li>';
  
            
            for (let i = navi.startPageGroup; i <= navi.endPageGroup; i++) {
                if (i === navi.currentPage) {
                    paginationHtml += '<li class="page-item active"><a class="page-link" href="#" onclick="loadPage(' + i + ')">' + i + '</a></li>';
                } else {
                    paginationHtml += '<li class="page-item"><a class="page-link" href="#" onclick="loadPage(' + i + ')">' + i + '</a></li>';
                }
            }
            
            paginationHtml += '<li class="page-item"><a class="page-link" href="#" onclick="loadPage(' + (navi.currentPage + 1) + ')">&raquo;</a></li>';


            $('.pagination').html(paginationHtml);
        },
        error: function(e) {
            console.log("error");
        }
    });
}
/* 수정부 */
function fileModify() {
    // 현재 클릭한 수정 버튼의 ID를 가져옵니다.
    let file_num =  $(this).data("file-num");
    // 모달을 표시합니다.
    $("#modifyModal").modal('show'); 
    // 수정 버튼을 클릭하면
    $("#modifyFileName").off().click(function() {
        let newFileName = $("#newFileNameInput").val(); // 새로운 파일 이름을 가져옵니다.
		console.log(newFileName);
        // AJAX 호출로 서버에 파일 이름 변경을 요청합니다.
        $.ajax({
            url: 'fileModify',  // 해당 URL을 변경할 필요가 있을 수 있습니다.
            type: 'post',
            data: {
                file_num: file_num,
                title : newFileName
            },
            dataType: 'json',
            success: function(response) {
                alert(`파일 이름이 ${newFileName}으로 변경되었습니다.`);
                dirPrint();
            }, 
            error: function(jqXHR, textStatus, errorThrown) {
			    console.log("Status: ", textStatus);
			    console.log("Error: ", errorThrown);
			    alert("파일 이름을 변경하는 데 실패했습니다.");
			}
        });
    });
}

function fileSave(){
    let content = tinymce.activeEditor.getContent();

    $.post("fileTextModifie", {
        file_num : file_num_saver,
        file_text : content
    }).done(function(txt){
        console.log(txt);
    }).fail(function() {
        // 실패했을 때 실행할 코드
        console.log("error");
    })
    .always(function() {
        // 항상 실행할 코드
    });
}




/* Delete 함수 목록 시작부 */
//폴더 삭제기능
function dirDelete(){
    let num = $(this).attr('data-dir-num');
    console.log(num);

    $.post("dirDeleteOne", {
        dir_num : num
    })
    .done(function(txt) {
        // 성공했을 때 실행할 코드
        console.log("success"+txt);
        
        dirPrint();
    })
    .fail(function() {
        // 실패했을 때 실행할 코드
        console.log("error");
    })
    .always(function() {
        // 항상 실행할 코드
    });
}
//파일 삭제기능
function fileDelete(){
    let fnum = $(this).data('file-num');
    let dnum = $(this).data('dir-num');
    console.log(fnum, dnum);
    
	$("#deleteModal").modal('show');	
	
		$("#confirmDelete").off().click(function() {
		    $.ajax({
		    url: 'fileDeleteOne',
		    type: 'post',
		    data: {file_num : fnum},
		    dataType: 'text',
		    success: function(txt){
		        console.log("success"+txt);
		        dirPrint();
		    },
		    error: function(e){
		        console.log("error");
		    }
	    });
    });
}


function closeModal() {
   $(this).closest(".modal").modal("hide");
}
