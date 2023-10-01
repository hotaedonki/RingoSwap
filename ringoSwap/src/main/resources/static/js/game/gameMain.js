function chooseDictation(event) {
	const dictationSlideIndex = 2;
	
	if(event.to === dictationSlideIndex) {
		$('.more-option').show();
	} else {
		$('.more-option').hide();
	}
}

function showWordListModal() {
	const modalHTML = `
		<div class="modal fade" id="wordlistModal" tabindex="-1" aria-labelledby="wordlistModalLabel" aria-hidden="true">
		    <div class="modal-dialog">
		        <div class="modal-content">
		            <div class="modal-header">
		                <h1 class="modal-title fs-5" id="wordlistModalLabel">Select a Wordlist</h1>
		                <span class="d-flex align-items-frt">
		                    <button type="button" class="btn btn-primary btn-sm me-3 select-all">Select All</button>
		                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		                </span>
		            </div>
		            <div class="modal-body">
		                <ul class="word-list-group"></ul>
		            </div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
		                <button type="button" class="btn btn-primary wordList-update">Select</button>
		            </div>
		        </div>
		    </div>
		</div>
		`;
		
		$('body').append(modalHTML);
		
		const modalElement = new bootstrap.Modal($("#wordlistModal")[0]);
		modalElement.show();

        $('#wordlistModal').on('hidden.bs.modal', function () {
            $(this).remove();
        });
        wordFilePrint();
}

function showFormModal() {
	const modalHTML = `
		<div class="modal fade" id="formModal" tabindex="-1" aria-labelledby="formModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h1 class="modal-title fs-5" id="formModalLabel">Presentation
							Form</h1>
	
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<div class="modal-body">
						<h3 class="notMargin">형태</h3>
						<div class="btn-group-vertical" role="group"
							aria-label="Basic radio toggle button group">
							<div>
								<input type="radio" class="btn-check" name="form-radio" data-form-type="title"
									id="form-radio1" autocomplete="off" checked=""> <label
									class="btn btn-outline-primary" for="form-radio1">제목-의미
									순서로</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="form-radio" data-form-type="mean"
									id="form-radio2" autocomplete="off"> <label
									class="btn btn-outline-primary" for="form-radio2">의미-제목
									순서로</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="form-radio"
									id="form-radio3" autocomplete="off"> <label
									class="btn btn-outline-primary" for="form-radio3">제목만</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="form-radio"
									id="form-radio4" autocomplete="off"> <label
									class="btn btn-outline-primary" for="form-radio4">의미만</label>
							</div>
						</div>
						<h3>발음</h3>
						<div class="btn-group-vertical" role="group"
							aria-label="Basic radio toggle button group">
							<div>
								<input type="radio" class="btn-check" name="pronunciation-radio" data-pron-show="true"
									id="pronunciation-radio1" autocomplete="off" checked="">
								<label class="btn btn-outline-primary" for="pronunciation-radio1">발음
									표시</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="pronunciation-radio" data-pron-show="false"
									id="pronunciation-radio2" autocomplete="off"> <label
									class="btn btn-outline-primary" for="pronunciation-radio2">발음
									표시 안함</label>
							</div>
						</div>
						<h3>순서</h3>
						<div class="btn-group-vertical" role="group"
							aria-label="Basic radio toggle button group">
							<div>
								<input type="radio" class="btn-check" name="order-radio" data-order-type="random"
									id="order-radio1" autocomplete="off" checked=""> <label
									class="btn btn-outline-primary" for="order-radio1">랜덤</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="order-radio" data-order-type="first_in"
									id="order-radio2" autocomplete="off"> <label
									class="btn btn-outline-primary" for="order-radio2">등록순</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="order-radio" data-order-type="latest"
									id="order-radio3" autocomplete="off"> <label
									class="btn btn-outline-primary" for="order-radio3">최신순</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="order-radio" data-order-type="atoz"
									id="order-radio4" autocomplete="off"> <label
									class="btn btn-outline-primary" for="order-radio4">A-Z</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="order-radio" data-order-type="ztoa"
									id="order-radio5" autocomplete="off"> <label
									class="btn btn-outline-primary" for="order-radio5">Z-A</label>
							</div>
						</div>
						<h3>설명 표시</h3>
						<div class="btn-group-vertical" role="group"
							aria-label="Basic radio toggle button group">
							<div>
								<input type="radio" class="btn-check" name="explanation-radio" data-desc-show="true"
									id="explanation-radio1" autocomplete="off" checked=""> <label
									class="btn btn-outline-primary" for="explanation-radio1">
									설명 표시</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="explanation-radio" data-desc-show="false"
									id="explanation-radio2" autocomplete="off"> <label
									class="btn btn-outline-primary" for="explanation-radio2">
									설명 표시 안함</label>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Cancel</button>
						<button type="button" class="btn btn-primary form-update">Select</button>
					</div>
				</div>
			</div>
		</div>`;
	
		$('body').append(modalHTML);
			
		gameSettingOpen();
		const modalElement = new bootstrap.Modal($("#formModal")[0]);
		modalElement.show();
	
	    $('#formModal').on('hidden.bs.modal', function () {
	        $(this).remove();
	    });
}

function showCountOfItemsModal() {
	const modalHTML = `
		<div class="modal fade" id="countOfItemsModal" tabindex="-1" aria-labelledby="countOfItemsModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h1 class="modal-title fs-5" id="countOfItemsModalLabel">Number
							of Questions</h1>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<div class="modal-body">
						<div class="form-check">
							<input class="form-check-input" type="radio" data-question-num="10"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions1">
							<label class=1"form-check-label" for="selectNumberOfQuestions1">
								10 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" data-question-num="20"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions2">
							<label class="form-check-label"
								for="selectNumberOfQuestions2"> 20 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" data-question-num="30"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions3">
							<label class="form-check-label"
								for="selectNumberOfQuestions3"> 30 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" data-question-num="40"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions4">
							<label class="form-check-label"
								for="selectNumberOfQuestions4"> 40 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" data-question-num="50"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions5">
							<label class="form-check-label"
								for="selectNumberOfQuestions5"> 50 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" data-question-num="0"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions6"
								checked> <label class="form-check-label"
								for="selectNumberOfQuestions6"> All </label>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Cancel</button>
						<button type="button" class="btn btn-primary questionNum-update">Select</button>
					</div>
				</div>
			</div>
		</div>
		`;
		
		$('body').append(modalHTML);

		countOfItemPrint();
			
		const modalElement = new bootstrap.Modal($("#countOfItemsModal")[0]);
		modalElement.show();
	
	    $('#countOfItemsModal').on('hidden.bs.modal', function () {
	        $(this).remove();
	    });
}

//wordlistModal 모달에 해당 사용자가 작성한 단어장 목록을 출력하는 메서드
function wordFilePrint(){
    $.ajax({
        url:'fileOpenWordNote',
        type:'post',
        dataType:'json',
        success:function(wordList){
            console.log(wordList);
                wordList.forEach(file => {
                $('#wordlistModal .word-list-group').append(`
                    <li>
                        <input type="checkbox" data-file-num="${file.file_num}">
                        <span>${file.title}</span>
                    </li>
                `);
            });
			$('#wordlistModal .word-list-group').append(`
			<li>
				<input type="checkbox" data-file-num="-10">
				<span>오답노트</span>
			</li>
		`);
        },
        error:function(e){
            console.log(e);
        }
    })
}

function gameSettingOpen(){
    $.ajax({
        url:'gameSettingOpen',
        type:'post',
        dataType:'json',
        success:function(setting){'', 
            console.log(setting);
			const form = setting.form_type
			const order = setting.order_type;
			let formNum = (form == 'title') ? 1 : 2;
			$('#form-radio'+formNum).click();
			if(!setting.pron_show){
				console.log("pron");
				$('#pronunciation-radio2').click();
			}
			let orderNum = (order == 'random')? 1 : (order == 'first_in')? 2 : (order == 'latest') ? 3: (order == 'atoz')? 4 : 5;
			$('#order-radio'+orderNum).click();
			if(!setting.description_show){
				console.log("설명");
				$('#explanation-radio2').click();
			}
		},
        error:function(e){
            console.log(e);
        }
    })
}

function countOfItemPrint(){
	let num = $('.btn[data-bs-target="#countOfItemsModal"]').data('question-num');
	if(num === '0'){
		$('#selectNumberOfQuestions6').click();
	}else{
		$('#selectNumberOfQuestions'+Math.floor(num / 10)).click();
	}
}

//게임에 사용할 단어장 정보를 수정하는 함수
function wordListUpdate(){
	let num = $('#wordlistModal .word-list-group').find('input[type="checkbox"]:checked').data('file-num');
	let select = "none";
	if($('.select-all').prop('checked')){
		select = "all";
	}
	
	console.log(num);
	$.ajax({
		url:'fileWordUpdate',
		type:'post',
		data:{file_num : num, select : select},
		success:function(res){
			console.log('성공');
		},
		error:function(e){
			console.error(e);
		}
	});
}
function gameSettingUpdate(){
	let form = $('[id^=form-radio]:checked').data('form-type');
	let pron = $('[id^=pronunciation-radio]:checked').data('pron-show');
	let order = $('[id^=order-radio]:checked').data('order-type');
	let description = $('[id^=explanation-radio]:checked').data('desc-show');
	$.ajax({
		url:'gameSettingUpdate',
		type:'post',
		data:{form_type : form, pron_show : pron, order_type : order, description_show: description},
		success:function(res){
			console.log('성공');
		},
		error:function(e){
			console.error(e);
		}
	});
}
function questionNumUpdate(){
	let num = $('input[name="selectNumberOfQuestions"]:checked').data('question-num');
	let checkedRadio = $('input[name="selectNumberOfQuestions"]:checked');
	console.log(num);
	console.log(checkedRadio); // 0이면 선택된 것이 없는 것입니다.
	
	// 선택된 radio 버튼의 data-Qnum 값 확인
	if (checkedRadio.length > 0) {
	  console.log(checkedRadio.data('question-num')); // 이 값이 undefined면 data-Qnum 속성이 없는 것입니다.
	}
	$.ajax({
		url:'gameSettingUpdateQuestionNum',
		type:'post',
		data:{question_num : num},
		success:function(res){
			console.log('성공');
		},
		error:function(e){
			console.error(e);
		}
	});
}
function matchUseUpdate(){
	let check = $('#flexSwitchCheckDefault');
	let match = check.prop('checked');

	$.ajax({
        url:'matchUseUpdate',
        type:'post',
        data: {match_use : match},
        success:function(res){
            console.log('match_use 변경 : ' + res);
        },
        error:function(e){
            console.log(e);
        }
    })
}