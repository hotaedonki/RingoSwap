// 함수처리용 변수값 설정 (검색 및 정렬을 위한 변수)
let ca = 'kor';
let st = 'input';

// 문서 준비가 완료되면 실행
$(document).ready(function(){
    dirPrint();    
    // 파일 분류에 따른 검색 방식 설정
    $('.cateBtn').click(categoryEvent);
    // 정렬 방식 설정
    $('.sortBtn').click(sortEvent);
    $('#createFolder').click(dirCreate);
    // 노트, 단어장 생성
    $('#createNote').click(function() {
        fileCreate('note');
    });   
    $('#createWord').click(function() {
        fileCreate('word');
    });
    $(document).on('click', '.dirOpen', dirOpen);
    $(document).on('click', '.dirDelete', dirDelete);
    $(document).on('click', '.yes', closeModal);
});

// 폴더 버튼을 클릭할 때의 이벤트 처리
$(document).on('click', '.dir-btn', function() {
    $('.btn-dark').removeClass('btn-dark').addClass('btn-outline-dark');
    $(this).removeClass('btn-outline-dark').addClass('btn-dark');
});

// 파일 분류에 따른 검색 방식을 설정하는 함수
function categoryEvent() {
    let c = $(this).text();
    c = c === '메모장' ? 'note' : c === '단어장' ? 'word' : 'all';
    $('#category').val(c);
    dirPrint();
}

// 정렬 방식을 설정하는 함수
function sortEvent() {
    let s = $(this).text();
    s = s === '생성순' ? 'input' : s === '제목순' ? 'title' : 'modifie';
    $('#sort').val(s);
    dirPrint();
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
    $.ajax({
        url: 'fileCreateOne',
        type: 'post',
        data: { dir_num: dir_num, title: title, file_type: file_type },
        success: function(response) {
            console.log("파일이 성공적으로 생성되었습니다.");
            dirOpen.call($(`.dirOpen[data-dir-num=${dir_num}]`));
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
    if(num.isNaN){
        num = -1;
    }

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
            $('.dirDelete').click(dirDelete);
        },
        error: function(e) {
            console.log("error");
            alert("하위 폴더를 불러오는데 실패했습니다.");
        }
    });

    // 하위 파일 불러오기
    console.log(ca, st);
    $.ajax({
        url: 'dirOpenFile',
        type: 'post',
        data: { dir_num: num, category: ca, sort: st },
        dataType: 'json',
        success: function(list) {
            let str = '<ul>';
            $(list).each(function(n, item) {
                str += `<li style="position: relative;"><i class="bi bi-file"></i>
                            <span data-file-num="${item.file_num}" class="fileOpen">${item.title}</span>
                            <span id="fileType${item.file_num}">${item.file_type}</span>
                            <span>${item.lang_type}</span>
                              <span class="modifyAndDelete">
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

// 파일 이름을 클릭하면 fileWindow에 표시하는 함수
function fileOpen(){
    let num = $(this).data("file-num");
    let type = $('#fileType'+num).text();
    console.log(num, type);

    $.ajax({
        url: 'fileOpenNote',
        type: 'post',
        data: {file_num : num, file_type: type},
        dataType: 'json',
        success: function(notepad){
            console.log(notepad);
            let str =`<table>
                <tr>
                     <td rowspan="2">${notepad.title}</td>
                    <td rowspan="2">${notepad.file_num}</td>
                    <td>${notepad.input_date}</td>
                </tr>
                <tr>
                    <td>${notepad.modifie_date}</td>
                </tr>
                <tr>
                    <td colspan="3"><textarea  cols="60" rows="15" >${notepad.file_text}</textarea></td>
                </tr>
            </table>`;
                str += '</ul>';
                $('.tox-edit-area').text(str);
                console.log('프린트 완료 : '+notepad);
                $('.btn-close').click();
            },
            error: function(e){
                console.log("error");
            }
        });
    if(type == 'word'){
        // 클릭한 파일의 분류가 'word=단어장'일 때 실행하는 ajax
        $.ajax({
            url: 'fileOpenWord',
            type: 'post',
            data: {file_num : num},
            dataType: 'json',
            success: function(list){
                console.log(list);
                let str1 = '';
                let str2 = '';
                let cnt = 0;
                let cntleng = Math.floor(list.length / 2);
                $(list).each(function(i, item){
	                if(cnt < cntleng){
	                    str1 += `<li class="list-group-item word-card modifyWord" data-word-num="${item.word_num}">
	                        <span class="word">${item.word}</span>
	                        <div class="word-content">
	                            <span class="pronunciation">${item.pron}</span>
	                            <span class="meaning">${item.mean}</span>
	                            <span class="description" style="display:none;">${item.description}</span>
	                        </div> 
	                    </li>`;
	                }else{
	                    str2 += `<li class="list-group-item word-card modifyWord" data-word-num="${item.word_num}">
	                        <span class="word">${item.word}</span>
	                        <div class="word-content">
	                            <span class="pronunciation">${item.pron}</span>
	                            <span class="meaning">${item.mean}</span>
	                            <span class="description" style="display:none;">${item.description}</span>
	                        </div> 
	                    </li>`;
                    }
                    console.log(item.word_num);
                    cnt++;
                });
                $('.add-btn').attr('data-file-num', num);
                console.log('filePrint' + num);

                $('.list-group1').html(str1);
                $('.list-group2').html(str2);
                $('.btn-close').click();
            },
            error: function(e){
                console.log("error");
            }
        });
    }
}

/* 수정부 */
function fileModify() {
    // 현재 클릭한 수정 버튼의 ID를 가져옵니다.
    let file_num = $(this).attr('class').replace('fileModify', '');
    // 현재 파일 이름을 가져옵니다.
    let currentFileName = $("#fileOpen" + file_num).text();
    // 모달을 표시합니다.
    $("#modifyModal").modal('show');
    $("#newFileNameInput").val(currentFileName);
    
    // 수정 버튼을 클릭하면
    $("#modifyFileName").off().click(function() {
        let newFileName = $("#newFileNameInput").val(); // 새로운 파일 이름을 가져옵니다.

        // AJAX 호출로 서버에 파일 이름 변경을 요청합니다.
        $.ajax({
            url: 'modifyFileName',  // 해당 URL을 변경할 필요가 있을 수 있습니다.
            type: 'post',
            data: {
                file_num: file_num,
                title : newFileName
            },
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    // 이름 업데이트
                    $("#fileOpen" + file_num).text(newFileName);
                    $("#modifyModal").modal('hide'); // 모달을 숨깁니다.
                } else {
                    alert("파일 이름을 변경하는 데 실패했습니다.");
                }
            },
            error: function() {
                alert("파일 이름을 변경하는 데 실패했습니다.");
            }
        });
    });
}

function fileSave(){
    let content = tinymce.activeEditor.getContent();
    let num = $('#fileNum').data('file-num');

    $.post("fileSave", {
        file_num : num,
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

