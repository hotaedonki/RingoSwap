

function goToMyFeed() {
	const url = `../feed/feedMain?nickname=${myName}`;
	window.location.href = url;
}

 $(document).ready(function(){
   memberPrint();
   $(document).on('click', '.goToMyFeed', goToMyFeed);
 });

