function bringMyName() {
	$.ajax({
		url: 'nicknamePrint'
		, type: 'post'
		, success: function(name) {
			nickname = name;
		}, error: function(error) {
			console.log(error);
		}
	})
}

function goToMyFeed() {
	const url = `../feed/feedMain?nickname=${myName}`;
	window.location.href = url;
}


 $(document).ready(function(){
   memberPrint();
   bringMyName();
   $(document).on('click', '.goToMyFeed', goToMyFeed);
 });

