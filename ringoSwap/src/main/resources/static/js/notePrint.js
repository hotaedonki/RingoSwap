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
	let a= $(this).text();

	$('#category').val(a);
	listPrint();
}
function sortEvent(){
	let a= $(this).text();
	$('#sort').val(a);
	listPrint();
}

function listPrint(){
	dirPrint();
	filePrint();
};

function dirPrint(){
	$.ajax({
		url:'dirPrint',
		type:'post',
		data: {category: ca, sort: st, text: txt},
		dataType: 'json',
		success:function(list){
			let str = `<tr>
					<td>폴더번호</td>
					<td>카테고리</td>
					<td>제목</td>
					<td>작성일</td>
					<td>부모폴더</td>
				</tr>`;
			$(list).each(function(i, item){
				str += '<tr>';
				str += '<td>'+item.dir_num+'</td>';
				str += '<td>폴더</td>';
				str += '<td><p id="dir'+item.dir_num+'">'+item.dir_name+'</p></td>';
				str += '<td>폴더</td>';
				str += '<td>'+item.parent_dir_num+'</td>';
				str += '</tr>';
			});
			$('#dirList').html(str);
		},
		error:function(e){
			console.log(JSON.stringify(e));
		}
	});
}
	
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
				str += '<tr>';
				str += '<td>'+item.file_num+'</td>';
				str += '<td>'+item.file_type+'</td>';
				str += '<td>'+item.title+'</td>';
				str += '<td>'+item.modifie_date+'</td>';
				str += '<td>'+item.dir_num+'</td>';
				str += '</tr>';
			});
			$('#fileList').html(str);
		},
		error:function(e){
			console.log(JSON.stringify(e));
		}
	});
}