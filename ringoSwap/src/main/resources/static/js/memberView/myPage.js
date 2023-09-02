/**
 * 
 */

 $(document).ready(function(){
	 printMyPage();
	 
 });

 function printMyPage(){
    $.ajax({
        url:'myPage',
			type:'post',
			dataType: 'json',
			success:function(member){
                $('#nickname').html(member.username);
                $('#introduction').html(member.introduction);
                $('#fr_count').html(member.fr_count);
                $('#fe_count').html(member.fe_count);
                let native = member.native_lang;
                let target = member.target_lang;
                let tag = member.tag_list;
                nativePrint(native);
                targetPrint(target);
                tagPrint(tag);
            }
    });
 }

 function nativePrint(native){

 }

 function targetPrint(target){

 }

 function tagPrint(tag){
	    let list= [];
	    let str ='';
	 if(tag){
	    list = tag.split(" ");
	    $(list).each(function(i, item){
	        str += `<button class="btn btn-light m-1">${item}</button>`;
	    })
	 }


    $('#tagBtnList').html(str);
 }