const images = [
    { fileName: "1.png", placeName: "Kinkakuji" },
    { fileName: "2.png", placeName: "Minato Mirai" },
    { fileName: "3.png", placeName: "Daedunsan Mountain" },
    { fileName: "4.png", placeName: "Amsterdam" },
    { fileName: "5.png", placeName: "Sydney Opera House" },
    { fileName: "6.png", placeName: "Itsukushima Shrine" },
    { fileName: "7.png", placeName: "Arakurayama Sengen Park" },
    { fileName: "8.png", placeName: "Dubai" },
    { fileName: "9.png", placeName: "Kuala Lumpur" },
    { fileName: "10.png", placeName: "Hongkong" },
    { fileName: "11.png", placeName: "Singapore" },
    { fileName: "12.png", placeName: "Big Ben" },
    { fileName: "13.png", placeName: "Venice" },
    { fileName: "14.png", placeName: "Grand Canyon" },
    { fileName: "15.png", placeName: "Arc de Triomphe" },
    { fileName: "16.png", placeName: "Kyoto" }
];

let interval;

function setBackgroundImage() {
    const randomImageObj = images[Math.floor(Math.random() * images.length)];
    const imageUrl = `../img/background/${randomImageObj.fileName}`;
    
    const img = new Image();
    img.src = imageUrl;
    img.onload = function() {
        $(".show-name").text(randomImageObj.placeName);
        $('#home').css('background-image', `url('${imageUrl}')`);
    };
}

function startInterval() {
    if (!interval) {
        interval = setInterval(setBackgroundImage, 10000);
    }
}

function stopInterval() {
    clearInterval(interval);
    interval = null;
}

$(document).ready(function() {
    setBackgroundImage();
    startInterval();
	usernamePrint();
	
    $(document).on("visibilitychange", function() {
        if (document.visibilityState === 'visible') {
            setBackgroundImage();
            startInterval();
        } else {
            stopInterval();
        }
    });
});

function usernamePrint() {
    const kiminonawaInput = $("#kiminonawa");
    $.ajax({
        url: 'nicknamePrint',
        type: 'post',
        success: function(username) {
			if(username === "로그인 중 아님") {
				return false;
			}
			console.log(username);
            kiminonawaInput.text(" " + username);
        },
        error: function(error) {
            console.error(error);
        }
    });
}