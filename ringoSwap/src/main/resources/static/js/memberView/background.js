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

function setBackgroundImage() {
    const randomImageObj = images[Math.floor(Math.random() * images.length)];
    const imageUrl = `url('../img/background/${randomImageObj.fileName}')`;
    $(".show-name").text(randomImageObj.placeName);
    $('#home').css('background-image', imageUrl);
    
    setInterval(setBackgroundImage, 10000);
}


$(document).ready(function() {   
    setBackgroundImage();  
});