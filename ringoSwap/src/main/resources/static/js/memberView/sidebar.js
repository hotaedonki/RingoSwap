const body = document.querySelector('body'),
      sidebar = body.querySelector('nav'),
      toggle = body.querySelector(".toggle")

toggle.addEventListener("click" , () =>{
    sidebar.classList.toggle("close");
})

function printMyProfilePhoto() {
	$.ajax({
		url: 'printMyProfilePhoto'
		, type: 'post'
		, success: function(user_id) {
			console.log(user_id)
			$('.profile-pic-li').attr('src', '../member/memberProfilePrint?user_id=' + user_id);
		}, 
		error: function(error) {
			console.log(error);
		}
		
	})
}

function goHome() {
	window.location.href = "../member/home";
}

$(document).ready(function() {
	printMyProfilePhoto();
	$(document).on('click', '.logo-area', goHome);
});

