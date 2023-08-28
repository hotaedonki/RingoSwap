//검색 및 정렬을 위한 함수처리용 변수값
let ca = null;
let st = null;
let txt = null;

$(document).ready(function(){
	listPrint();
    //file분류에 따른 검색방식 지정
    $('.cateBtn').click(categoryEvent)
    //정렬방식 지정
    $('.sortBtn').click(sortEvent)
})

function categoryEvent(){
	let c= $(this).text();
	if(c == '메모장'){
		c = 'note';
	}else if(c == '단어장'){
		c = 'word';
	}else{
		c='all'
	}
	$('#category').val(c);
	listPrint();
}
function sortEvent(){
	let s= $(this).text();
	if(s == '생성순'){
		s = 'input';
	}else if(s == '제목순'){
		s = 'title';
	}else if(s == '최신순'){
		s = 'modifie';
	}

	$('#sort').val(s);
	listPrint();
}

function listPrint(){
	dirPrint();
	filePrint();
};
	//폴더정보를 출력하는 ajax함수
function dirPrint(){
	$.ajax({
		url:'dirPrint',
		type:'post',
		data: {category: ca, sort: st, text: txt},
		dataType: 'json',
		success:function(list){
			let str = `<li>폴더번호 / 카테고리 / 제목 / 작성일 / 부모폴더 / 폴더/파일생성</li>
			`;
			$(list).each(function(i, item){
				str += `
					<li>${item.dir_num} / 폴더 / <span id="dirOpen${item.dir_num}">${item.dir_name}
					</span> / 폴더 / ${item.parent_dir_num} / <span id="dirMaker${item.dir_num}">폴더+</span>
					<span id="fileMaker${item.dir_num}">파일</span>
					</li>
					<li id="dirPrint${item.dir_num}"></li>
					<li id="filePrint${item.dir_num}"></li>
				`;
			});
			$('#dirList').html(str);
			$('[id^="dirMaker"').click(dirCreate);
			$('[id^="fileMaker"').click(fileCreate);
			$('[id^="dirOpen"]').click(dirOpen);
		},
		error:function(e){
			console.log(JSON.stringify(e));
		}
	});
}
	//파일정보를 출력하는 ajax함수
function filePrint(){
	//검색제한, 정렬방식 결정용 변수 설정
	ca = $('#category').val();
	st = $('#sort').val();
	txt = $("#searchText").val();
	//파일검색-출력 ajax실행
	$.ajax({
		url:'filePrint',
		type:'post',
		data: {category: ca, sort: st, text: txt},
		dataType: 'json',
		success:function(list){
			let str = ``;
			$(list).each(function(i, item){
				str += `
					<li>${item.file_num} / ${item.file_type} / 
					${item.title} / ${item.modifie_date} / ${item.dir_num}</li>
					`;
			});
			$('#fileList').html(str);
		},
		error:function(e){
			console.log(JSON.stringify(e));
		}
	});
}
//해당 폴더를 부모폴더로 하여 하위폴더를 생성하는 테이블
function dirCreate(){

	console.log("yes1");
}
//해당 폴더를 부모폴더로 하여 파일을 생성하는 테이블
function fileCreate(){
	console.log("yes3");
	
}

//해당 폴더 하위에 존재하는 폴더,파일을 불러온다.
function dirOpen(){
	let num = $(this).attr("id");
	let arr = num.split('n', 2);
	num = arr[1];
	console.log(num);
	//하위 폴더 열기
	$.ajax({
		url:'dirOpenDirectory',
		type:'post',
		data: {dir_num : num},
		dataType: 'json',
		success:function(list){
			str ='<ul>'
			$(list).each(function(i, item){
				str+=`<li>
					<span>${item.dir_num}</span> / 폴더 / <span id="dirOpen${item.dir_num}">${item.dir_name}
					</span> / 폴더 / <span>${item.parent_dir_num}</span> / <span id="dirMaker${item.dir_num}">
					폴더+</span><span id="fileMaker${item.dir_num}">파일</span>
				</li>
				<li id="dirPrint${item.dir_num}"></li>
				<li id="filePrint${item.dir_num}"></li>
				`;
			})
			str +='</ul>'
			$('#dirPrint'+num).html(str);
			console.log('dirPrint'+num);
			$('[id^="dirOpen"]').click(dirOpen);
			$('[id^="dirMaker"').click(dirCreate);
			$('[id^="fileMaker"').click(fileCreate);
		},
		error:function(e){
			console.log("error");
		}
	});
	//검색제한, 정렬방식 결정용 변수 설정
	ca = $('#category').val();
	st = $('#sort').val();
	txt = $("#searchText").val();
	//하위 파일 열기
	$.ajax({
		url:'dirOpenFile',
		type:'post',
		data: {dir_num : num, category: ca, sort: st, text: txt},
		dataType: 'json',
		success:function(list){
			str ='<ul>'
			$(list).each(function(i, item){
				str+=`<li>
				<span>${item.file_num}</span> / <span id="fileType${item.file_num}">${item.file_type}</span> / 
				<span id="fileOpen${item.file_num}">${item.title}</span> / <span>${item.modifie_date}</span> /
				<span>${item.dir_num}</span>
				</li>`;
			})
			str +='</ul>'
			console.log('filePrint'+num);
			$('#filePrint'+num).html(str);
			$('[id^="fileOpen"]').click(fileOpen);
		},
		error:function(e){
			console.log("error");
		}
	});
}

function fileOpen(){
	//매개변수 dir_num 획득을 위한 처리
	let num = $(this).attr("id");
	let arr = num.split('n', 2);
	//파일이 어떤 분류인지 확인을 위한 변수정의
	let type = $('#fileType'+arr[1]).text();
	console.log(type);
	
	if(type == 'note'){
		//클릭한 파일의 분류가 'note=메모장'일때 실행하는 ajax
		$.ajax({
			url:'fileOpenNote',
			type:'post',
			data: {file_num : num, file_type: type},
			dataType: 'json',
			success:function(list){
				str ='<ul>'
				$(list).each(function(i, item){
					str+=`<li>
					<span>${item.file_num}</span> / <span id="fileType${item.file_num}">${item.file_type}</span> / 
					<span id="fileOpen${item.file_num}">${item.title}</span> / <span>${item.modifie_date}</span> /
					<span>${item.dir_num}</span>
					</li>`;
				})
				str +='</ul>'
				console.log('filePrint'+num);
				$('#filePrint'+num).html(str);
				$('[id^="fileOpen"]').click(fileOpen);
			},
			error:function(e){
				console.log("error");
			}
		});
	}else if(type == 'word'){
		//클릭한 파일의 분류가 'word=단어장'일때 실행하는 ajax
		$.ajax({
			url:'fileOpenWord',
			type:'post',
			data: {file_num : num, file_type: type},
			dataType: 'json',
			success:function(list){
				str ='<ul>'
				$(list).each(function(i, item){
					str+=`<li>
					<span>${item.file_num}</span> / <span id="fileType${item.file_num}">${item.file_type}</span> / 
					<span id="fileOpen${item.file_num}">${item.title}</span> / <span>${item.modifie_date}</span> /
					<span>${item.dir_num}</span>
					</li>`;
				})
				str +='</ul>'
				console.log('filePrint'+num);
				$('#filePrint'+num).html(str);
				$('[id^="fileOpen"]').click(fileOpen);
			},
			error:function(e){
				console.log("error");
			}
		});
	}
}