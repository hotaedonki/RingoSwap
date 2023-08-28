(document).ready(function(){
})

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
			data: {file_num : arr[1], file_type: type},
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
			data: {file_num : arr[1], file_type: type},
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