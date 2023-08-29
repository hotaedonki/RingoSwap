$(document).ready(function(){
	
    //id체크 이벤트
    $('#idInput').click(idInput)
    $('#notInput').click(notInput)
})


function idInput(){
	let id = $('#searchid').val();
	alert(id);
	opener.document.getElementById('user_id').value =id;
	window.close();
}
function notInput(){
	let id = '';
	opener.document.getElementById('user_id').value = id;
	window.close();
}