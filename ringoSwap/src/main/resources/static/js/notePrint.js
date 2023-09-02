// 함수처리용 변수값 설정 (검색 및 정렬을 위한 변수)
let ca = null;
let st = null;
let txt = null;

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
    $(document).on('click', '[id^="dirOpen"]', dirOpen);
});


// 폴더 버튼을 클릭할 때의 이벤트 처리
$(document).on('click', '.dir-btn', function() {
    $('.btn-dark').removeClass('btn-dark').addClass('btn-outline-dark');
    $(this).removeClass('btn-outline-dark').addClass('btn-dark');
});

// 파일 분류에 따른 검색 방식을 설정하는 함수
function categoryEvent(){
    let c = $(this).text();
    if(c == '메모장'){
        c = 'note';
    } else if(c == '단어장'){
        c = 'word';
    } else {
        c = 'all';
    }
    $('#category').val(c);
    listPrint();
}

// 정렬 방식을 설정하는 함수
function sortEvent(){
    let s = $(this).text();
    if(s == '생성순'){
        s = 'input';
    } else if(s == '제목순'){
        s = 'title';
    } else if(s == '최신순'){
        s = 'modifie';
    }

    $('#sort').val(s);
    listPrint();
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
                str += `<li><i class="bi bi-folder"></i><button class="btn btn-outline-dark dir-btn" 
                data-dir-num="${item.dir_num}" id="dirOpen${item.dir_num}">
                ${item.dir_name}</button></li>`;
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
    let parent_dir_num = $('.btn-dark').data('dir-num') || -1; // 상위 폴더의 dir_num 값을 할당. 기본값은 -1입니다.
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
        },
        error: function(e) {
            console.log("파일 생성 중 에러 발생");
        }
    });
}

// 해당 폴더 하위에 있는 폴더와 파일을 불러오는 함수
function dirOpen() {
    let num = $(this).data('dir-num');
    console.log("폴더 열기");
    // 하위 폴더 불러오기
    $.ajax({
        url: 'dirOpenDirectory',
        type: 'post',
        data: { dir_num: num },
        dataType: 'json',
        success: function(list) {
            let str = '<ul>';
            $(list).each(function(i, item) {
                str += `<li><span data-dir-num="${item.dir_num}" id="dirOpen${item.dir_num}">${item.dir_name}
                    </span></li>
                <li id="dirPrint${item.dir_num}"></li>
                <li id="filePrint${item.dir_num}"></li>`;
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
    let ca = $('#category').val();
    let st = $('#sort').val();
    let txt = $("#searchText").val();
	console.log("하위파일")
    $.ajax({
        url: 'dirOpenFile',
        type: 'post',
        data: { dir_num: num, category: ca, sort: st, text: txt },
        dataType: 'json',
        success: function(list) {
            let str = '<ul>';
            $(list).each(function(i, item) {
                str += `<li><span id="fileType${item.file_num}">${item.file_type}</span> / </li>`;
            });
            str += '</ul>';
            $('#filePrint' + num).html(str);
        },
        error: function(e) {
            console.log("error");
            alert("하위 파일을 불러오는데 실패했습니다.");
        }
    });
}

// 파일 이름을 클릭하면 fileWindow에 표시하는 함수
function fileOpen(){
    let num = $(this).attr("id");
    let arr = num.split('n', 2);
    let type = $('#fileType'+arr[1]).text();
    console.log(type);

    if(type == 'note'){
        // 클릭한 파일의 분류가 'note=메모장'일 때 실행하는 ajax
        $.ajax({
            url: 'fileOpenNote',
            type: 'post',
            data: {file_num : arr[1], file_type: type},
            dataType: 'json',
            success: function(notepad){
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
                        <td colspan="3"><textarea  cols="60" rows="15" >${notepad.text}</textarea></td>
                    </tr>
                </table>`;
                str += '</ul>';
                $('#windowPrint').html(str);
            },
            error: function(e){
                console.log("error");
            }
        });
    } else if(type == 'word'){
        // 클릭한 파일의 분류가 'word=단어장'일 때 실행하는 ajax
        $.ajax({
            url: 'fileOpenWord',
            type: 'post',
            data: {file_num : arr[1]},
            dataType: 'json',
            success: function(list){
                let str = '<ul>';
                $(list).each(function(i, item){
                    let inputDate = new Date(item.inputdate).toLocaleString();
                    str += `<li>
                    <span>${item.word_num}</span> / <span>${item.file_num}</span> / 
                    <span id="wordOpen${item.word_num}">${item.word}</span> / 
                    <span>${item.pron}</span> / <span>${item.mean}</span> /
                    <span>${inputDate}</span> / <span>${item.user_num}</span>
                    </li>`;
                    console.log(item.word_num);
                });
                str += `<li><button id="wordInsert${arr[1]}"> + 단어 추가 + </button></li></ul>`;
                console.log('filePrint' + arr[1]);
                $('#windowPrint').html(str);

                $('[id^="wordInsert"]').click(wordInsert);
            },
            error: function(e){
                console.log("error");
            }
        });
    }

    function wordInsert(){
        // 단어 추가 로직
    }
}

let originalWordList = ""; // 임시 변수로 원래 wordList의 내용을 저장

$(document).ready(function() {
    // "추가" 버튼 클릭 이벤트 핸들러
    $("button:contains('추가')").click(function() {
		originalWordList = $(".wordList").html();
        const wordCard = `
		    <div class = "card-body wordCard">
		        <div class="card border-primary mb-3"  style="width: 400px;"">
		            <div class="card-body">
		                <div class="form-group">
		                    <label class="col-form-label mt-4" for="inputDefault">단어</label>
		                    <input type="text" class="form-control" placeholder="Word" id="word-input">
		                </div>
		                <div class="form-group">
		                    <label class="col-form-label mt-4" for="inputDefault">의미</label>
		                    <input type="text" class="form-control" placeholder="Meaning" id="meaning-input">
		                </div>
		                <div class="form-group">
		                    <label class="col-form-label mt-4" for="inputDefault">발음</label>
		                    <input type="text" class="form-control" placeholder="pronunciation" id="pronunciation-input">
		                </div>
		                <div class="form-group">
		                    <label class="col-form-label mt-4" for="inputDefault">설명</label>
		                    <input type="text" class="form-control" placeholder="description" id="description-input">
		                </div>
		            </div>
		        </div>
		    </div>`;
	        
        const navigationButtons = `
            <button class="carousel-control-prev" type="button" style="font-size: 9rem; color: white; display: flex; justify-content: center; align-items: center;">
                <span class="carousel-control-prev-icon" aria-hidden="true" style="width: 200px; height: 200px;"><</span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next" type="button" style="font-size: 9rem; color: white; display: flex; justify-content: center; align-items: center;">
                <span class="carousel-control-next-icon" aria-hidden="true" style="width: 200px; height: 200px;">></span>
                <span class="visually-hidden">Next</span>
            </button>
        `;

        // 현재 카드의 내용을 wordCard로 교체
        $(".card-body").replaceWith(wordCard + navigationButtons);
        
        $("button:contains('추가')").hide();
        
        // "저장" 버튼 생성
        const saveButton = '<button class="btn btn-primary save-btn">저장</button>';
        
        // 만약 wordCard가 화면에 존재하면 "저장" 버튼을 "추가" 버튼 왼쪽에 생성
        $(".wordButton").append(saveButton);
          
        // "돌아가기" 버튼 생성
        const backButton = '<button class="btn btn-secondary back-btn">돌아가기</button>';
        
        // "저장" 버튼 왼쪽에 "돌아가기" 버튼 추가
        $(".wordButton").prepend(backButton);
    });

    // "저장" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.save-btn', function() {
        // 데이터 수집
        const word = $("#word-input").val();
        const meaning = $("#meaning-input").val();
        const pronunciation = $("#pronunciation-input").val();
        const description = $("#description-input").val();

        // 데이터를 서버로 전송
        $.post("/wordCreate", {
            word: word,
            meaning: meaning,
            pronunciation: pronunciation,
            description: description
        }).done(function(response) {
            // Toast 메시지 표시
            const toast = `
            <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
              <div class="toast-header">
                <strong class="me-auto">Bootstrap</strong>
                <small>Just now</small>
                <button type="button" class="btn-close ms-2 mb-1" data-bs-dismiss="toast" aria-label="Close">
                  <span aria-hidden="true"></span>
                </button>
              </div>
              <div class="toast-body">
                저장되었습니다!
              </div>
            </div>`;

            $("body").append(toast);
            $(".toast").toast({ delay: 2000 }).toast('show');

            setTimeout(function() {
                $(".toast").remove();
            }, 2100);
        });
    });

    // "돌아가기" 버튼 클릭 이벤트 핸들러
	  $(document).on('click', '.back-btn', function() {
	    // wordCard + navigationButtons에서 originalWordList로 다시 출력
	    $(".wordCard").replaceWith('<div class="card-body wordList">' + originalWordList + '</div>');
	
	    // "저장" 버튼 제거
	    $(".save-btn").remove();
	
	    // "돌아가기" 버튼 제거
	    $(".back-btn").remove();
	
	    // "추가" 버튼 다시 표시
	    $("button:contains('추가')").show();
	
	    // navigationButtons 제거
	    $(".carousel-control-prev").remove();
	    $(".carousel-control-next").remove();
	});
});

/*
function loadNoteContent(file_num) {
            $.ajax({
                type: "POST",
                url: "/fileOpenNote",
                data: {
                    file_num: file_num
                },
                success: function(response) {
                    tinymce.get('noteTextarea').setContent(response.content);
                },
                error: function(error) {
                    console.error("노트를 불러오는데 실패했습니다.", error);
                }
            });
        }

        $(document).on('click', '.note-item', function() {
            var file_num = $(this).data('file-num');
            loadNoteContent(file_num);
        });
        tinymce에 저장된 note값을 가져오는 코드
        */
        
        
