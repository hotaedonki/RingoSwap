const modalOptions = {
	backdrop: true
};


function chooseDictation(event) {
	const dictationSlideIndex = 2;
	
	if(event.to === dictationSlideIndex) {
		$('.more-option').html(`
			<div class="col-4 text-start">
				Match Pronuntation				
			</div>
			<div class="col-4 text-end">
			    <div class="form-check form-switch d-inline-block">
			        <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault">
			    </div>
			</div>`);
	} else {
		$('.more-option').empty();
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
		
		const modalElement = new bootstrap.Modal($("#wordlistModal")[0], modalOptions);
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
								<input type="radio" class="btn-check" name="form-radio"
									id="form-radio1" autocomplete="off" checked=""> <label
									class="btn btn-outline-primary" for="form-radio1">제목-의미
									순서로</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="form-radio"
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
								<input type="radio" class="btn-check" name="pronunciation-radio"
									id="pronunciation-radio1" autocomplete="off" checked="">
								<label class="btn btn-outline-primary" for="pronunciation-radio1">발음
									표시</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="pronunciation-radio"
									id="pronunciation-radio2" autocomplete="off"> <label
									class="btn btn-outline-primary" for="pronunciation-radio2">발음
									표시 안함</label>
							</div>
						</div>
						<h3>순서</h3>
						<div class="btn-group-vertical" role="group"
							aria-label="Basic radio toggle button group">
							<div>
								<input type="radio" class="btn-check" name="order-radio"
									id="order-radio1" autocomplete="off" checked=""> <label
									class="btn btn-outline-primary" for="order-radio1">랜덤</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="order-radio"
									id="order-radio2" autocomplete="off"> <label
									class="btn btn-outline-primary" for="order-radio2">등록순</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="order-radio"
									id="order-radio3" autocomplete="off"> <label
									class="btn btn-outline-primary" for="order-radio3">최신순</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="order-radio"
									id="order-radio4" autocomplete="off"> <label
									class="btn btn-outline-primary" for="order-radio4">A-Z</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="order-radio"
									id="order-radio5" autocomplete="off"> <label
									class="btn btn-outline-primary" for="order-radio5">Z-A</label>
							</div>
						</div>
						<h3>설명 표시</h3>
						<div class="btn-group-vertical" role="group"
							aria-label="Basic radio toggle button group">
							<div>
								<input type="radio" class="btn-check" name="explanation-radio"
									id="explanation-radio1" autocomplete="off" checked=""> <label
									class="btn btn-outline-primary" for="explanation-radio1">
									설명 표시</label>
							</div>
							<div>
								<input type="radio" class="btn-check" name="explanation-radio"
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
			
		gameSettingPrint();
		const modalElement = new bootstrap.Modal($("#formModal")[0], modalOptions);
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
							<input class="form-check-input" type="radio"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions1">
							<label class=1"form-check-label" for="selectNumberOfQuestions1">
								10 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions2"
								checked> <label class="form-check-label"
								for="selectNumberOfQuestions2"> 20 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions3"
								checked> <label class="form-check-label"
								for="selectNumberOfQuestions3"> 30 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions4"
								checked> <label class="form-check-label"
								for="selectNumberOfQuestions4"> 40 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio"
								name="selectNumberOfQuestions" id="selectNumberOfQuestions5"
								checked> <label class="form-check-label"
								for="selectNumberOfQuestions5"> 50 </label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio"
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
			
		const modalElement = new bootstrap.Modal($("#countOfItemsModal")[0], modalOptions);
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
        },
        error:function(e){
            console.log(e);
        }
    })
}

function gameSettingPrint(){
	
    $.ajax({
        url:'gameSettingOpen',
        type:'post',
        dataType:'json',
        success:function(setting){'', 
            console.log(setting);
			const form = setting.form_type
			const order = setting.order_type;
			let formNum = (form == 'title_tomean') ? 1 : (form == 'mean_totitle') ? 2: ( form == 'title_only') ? 3: 4;
			$('#form-radio'+formNum).click();
			if(!setting.pron_show){
				console.log("pron");
				$('pronunciation-radio2').click();
			}
			let orderNum = (order == 'random')? 1 : (order == 'first_in')? 2 : (order == 'latest') ? 3: (order == 'atoz')? 4 : 5;
			$('#order-radio'+orderNum).click();
			if(!setting.description_show){
				console.log("설명");
				$('explanation-radio2').click();
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
		$('#selectNumberOfQuestions'+(num / 10)).click();
	}
}

//
function wordListUpdate(){
	
}
function gameSettingUpdate(){
	
}
function questionNumUpdate(){

}