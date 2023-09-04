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
                          <input type="text" class="form-control" placeholder="Pronunciation" id="pronunciation-input">
                      </div>
                      <div class="form-group">
                          <label class="col-form-label mt-4" for="inputDefault">설명</label>
                          <input type="text" class="form-control" placeholder="Description" id="description-input">
                      </div>
                  </div>
              </div>
          </div>`;

        // 현재 카드의 내용을 wordCard로 교체
        $(".card-body").replaceWith(wordCard);

        // "저장" 버튼 생성
        const saveButton = '<button class="btn btn-primary save-btn">저장</button>';
        
        // 만약 wordCard가 화면에 존재하면 "저장" 버튼을 "추가" 버튼 왼쪽에 생성
        $(".wordButton").prepend(saveButton);
          
        // "돌아가기" 버튼 생성
        const backButton = '<button class="btn btn-secondary back-btn" style="margin-right: 5px;">돌아가기</button>';
        
        // "저장" 버튼 왼쪽에 "돌아가기" 버튼 추가
        $(".wordButton").prepend(backButton);
        
        $("button:contains('추가'), .card-footer").hide();
        
    });

    // "저장" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.save-btn', function() {
        // 데이터 수집
        let num = $('.add-btn').data('file-num');
        const word = $("#word-input").val();
        const meaning = $("#meaning-input").val();
        const pronunciation = $("#pronunciation-input").val();
        const description = $("#description-input").val();

        // 데이터를 서버로 전송
        $.post("wordCreate", {
            file_num : num,
            word: word,
            mean: meaning,
            pron: pronunciation,
            description: description
        }).done(function(response) {
            // Toast 메시지 표시
            const toast = `
            <div class="toast toast-center show" role="alert" aria-live="assertive" aria-atomic="true">
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
            $(".toast").toast({ delay: 2000 });

            setTimeout(function() {
                $(".toast").remove();
            }, 2000);
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
       $("button:contains('추가'), .card-footer").show();
   
   });
   
   let currentWordNum;
   
   //단어 수정
   $(document).on('click', '.modifyWord', function() {
	  const word = $(this).find('.word').text();
	  const pronunciation = $(this).find('.pronunciation').text();
	  const meaning = $(this).find('.meaning').text();
	  const description = $(this).find('.description').text();
	  currentWordNum = $(this).data('word-num');
	  
	  console.log(currentWordNum + "수정번호 받아오기");
	   originalWordList = $(".wordList").html();
        const wordCard = `
          <div class = "card-body wordCard">
              <div class="card border-primary mb-3"  style="width: 400px;"">
                  <div class="card-body">
                      <div class="form-group">
                          <label class="col-form-label mt-4" for="inputDefault">단어</label>
                          <input type="text" class="form-control" placeholder="Word" id="word-input" value="${word}">
                      </div>
                      <div class="form-group">
                          <label class="col-form-label mt-4" for="inputDefault">의미</label>
                          <input type="text" class="form-control" placeholder="Meaning" id="meaning-input" value="${meaning}">
                      </div>
                      <div class="form-group">
                          <label class="col-form-label mt-4" for="inputDefault">발음</label>
                          <input type="text" class="form-control" placeholder="Pronunciation" id="pronunciation-input" value="${pronunciation}">
                      </div>
                      <div class="form-group">
                          <label class="col-form-label mt-4" for="inputDefault">설명</label>
                          <input type="text" class="form-control" placeholder="Description" id="description-input" value="${description}">
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

        
        // "저장" 버튼 생성
        const modifyButton = '<button class="btn btn-primary word-modify-btn">수정</button>';
        
        // 만약 wordCard가 화면에 존재하면 "저장" 버튼을 "추가" 버튼 왼쪽에 생성
        $(".wordButton").prepend(modifyButton);
        
        //"삭제" 버튼 생성
        const deleteButton = '<button class="btn btn-primary word-delete-btn" style="margin-right: 5px;">삭제</button>';
        //"수정"버튼 왼쪽에 "삭제" 버튼 추가
        $(".wordButton").prepend(deleteButton);
        
        // "돌아가기" 버튼 생성
        const backButton = '<button class="btn btn-secondary back-btn" style="margin-right: 5px;">돌아가기</button>';
        
        // "수정" 버튼 왼쪽에 "돌아가기" 버튼 추가
        $(".wordButton").prepend(backButton);
                
        $("button:contains('추가'), .card-footer").hide();
    });

    // "수정" 버튼 클릭 이벤트 핸들러
    $(document).on('click', '.word-modify-btn', function() {
        // 데이터 수집
        const word = $("#word-input").val();
        const meaning = $("#meaning-input").val();
        const pronunciation = $("#pronunciation-input").val();
        const description = $("#description-input").val();
        
        console.log(word);
        console.log(meaning);
        console.log(pronunciation);
        console.log(description);
        console.log(currentWordNum);
        // 데이터를 서버로 전송
        $.post("wordModify", {
            word_num: currentWordNum,
            word: word,
            mean: meaning,
            pron: pronunciation,
            description: description
        }).done(function(response) {
            // Toast 메시지 표시
            const toast = `
            <div class="toast toast-center show" role="alert" aria-live="assertive" aria-atomic="true">
              <div class="toast-header">
                <strong class="me-auto">Bootstrap</strong>
                <small>Just now</small>
                <button type="button" class="btn-close ms-2 mb-1" data-bs-dismiss="toast" aria-label="Close">
                  <span aria-hidden="true"></span>
                </button>
              </div>
              <div class="toast-body">
                수정되었습니다!
              </div>
            </div>`;

            $("body").append(toast);
            $(".toast").toast({ delay: 2000 });

            setTimeout(function() {
                $(".toast").remove();
            }, 2000);
        });
    });
    
    $(document).on('click', '.word-delete-btn', function() {
		console.log(currentWordNum + "삭제 카드 번호");
		$.post("wordDeleteOne", {
			word_num: currentWordNum
			}).done(function(response) {
				window.location.href = '../note/noteMain';
				
				const toast = `
	            <div class="toast toast-center show" role="alert" aria-live="assertive" aria-atomic="true">
	              <div class="toast-header">
	                <strong class="me-auto">Bootstrap</strong>
	                <small>Just now</small>
	                <button type="button" class="btn-close ms-2 mb-1" data-bs-dismiss="toast" aria-label="Close">
	                  <span aria-hidden="true"></span>
	                </button>
	              </div>
	              <div class="toast-body">
	                삭제되었습니다!
	              </div>
	            </div>`;
	
	            $("body").append(toast);
	            $(".toast").toast({ delay: 2000 });
	
	            setTimeout(function() {
	                $(".toast").remove();
	            }, 2000);
	            
			}).fail(function(response) {
   				console.log("단어 삭제 실패: ", response);
			})
		});

    // "돌아가기" 버튼 클릭 이벤트 핸들러
     $(document).on('click', '.back-btn', function() {
       // wordCard + navigationButtons에서 originalWordList로 다시 출력
       $(".wordCard").replaceWith('<div class="card-body wordList">' + originalWordList + '</div>');
   
       // "수정" 버튼 제거
       $(".word-modify-btn").remove();
   
       // "돌아가기" 버튼 제거
       $(".back-btn").remove();
   
       // "추가" 버튼 다시 표시
       $("button:contains('추가'), .card-footer").show();
       
       // navigationButtons 제거
       $(".carousel-control-prev").remove();
       $(".carousel-control-next").remove();
   
   });
});
   //단어 클릭 시 해당 단어의 내용 보이는 js만들기 > 수정가능 // 아직 controller없음.
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
        
        