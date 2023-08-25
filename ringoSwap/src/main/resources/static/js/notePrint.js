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
	list();
}

function list(){
	let ca = $('#category').val();
	let st = $('#sort').val();
	let txt = $("#searchText").val();
	$.ajax({
		url:'list',
		type:'post',
		data: {category: ca, sort: st, text: txt},
		dataType: 'json',
		success:function(list){
			let str = `<tr>
					<td>번호</td>
					<td>카테고리</td>
					<td>제목</td>
					<td>작성자</td>
					<td>조회수</td>
					<td>추천수</td>
					<td>작성일</td>
				</tr>`;
			$(list).each(function(i, item){
				str += '<tr>';
				str += '<td>'+item.boardnum+'</td>';
				str += '<td>'+categoryChange(item.category)+'</td>';
				str += '<td><a href="read?boardnum='+item.boardnum+'">'+item.title+'</a></td>';
				str += '<td>'+item.memberid+'</td>';
				str += '<td>'+item.hits+'</td>';
				str += '<td>'+item.recommend+'</td>';
				str += '<td>'+item.inputdate+'</td>';
				str += '</tr>';
			});
			$('#listTable').html(str);
		},
		error:function(e){
			console.log(JSON.stringify(e));
		}
	});
}