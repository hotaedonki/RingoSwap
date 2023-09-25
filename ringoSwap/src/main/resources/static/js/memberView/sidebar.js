const body = document.querySelector('body'),
      sidebar = body.querySelector('nav'),
      toggle = body.querySelector(".toggle")

toggle.addEventListener("click" , () =>{
    sidebar.classList.toggle("close");
})

function goHome() {
	window.location.href = "../member/home";
}

$(document).ready(function() {
	$(document).on('click', '.logo-area', goHome);
});