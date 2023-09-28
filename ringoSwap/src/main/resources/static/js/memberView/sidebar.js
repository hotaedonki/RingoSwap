const body = document.querySelector('body'),
      sidebar = body.querySelector('nav'),
      toggle = body.querySelector(".toggle")

toggle.addEventListener("click" , () =>{
    sidebar.classList.toggle("close");
})

function printMyProfilePhoto() {
	$.ajax({
		url: '../member/printMyProfilePhoto'
		, type: 'post'
		, success: function(user_id) {
			console.log(user_id)
			if(user_id !== "로그인 중 아님") {
				 let imgElem = $('<img>', {
		            'src': '../member/memberProfilePrint?user_id=' + user_id,
		            'class': 'profile-pic-li',
		            'data-tooltip': '프로필 사진으로 이동합니다.'
       		});
        
        $('#profileImagePlaceholder').replaceWith(imgElem);
			}

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

