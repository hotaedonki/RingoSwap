const img = [
    "1.jpg",
    "2.jpg",
    "3.jpg",
    "4.jpg",
    "5.jpg",
    "6.jpg",
];


function setBackgroundImage() {
    const randomImage = img[Math.floor(Math.random() * img.length)];
    document.getElementById("home").style.backgroundImage = `url('../img/${randomImage}')`;
}

window.onload = setBackgroundImage;

