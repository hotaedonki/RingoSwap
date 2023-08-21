$(document).ready(function()
{
	$('#checkEmailBtn').click(emailConfirm);
});


function emailConfirm()
{
		let email = $('#email').val();
	
		$.ajax({
			url:'emailConfirm'
			, type:'post'
			, data:{email: email}
			, success: function()
			{
				alert("성공");
			}
		});
}