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
    $('#home').css('background-image', `url('../img/${randomImage}')`);
}

// usernamePrint 함수 안에서 username을 가져온 후 밑줄 길이를 조절하는 코드 추가
function usernamePrint() {
    const kiminonawaInput = $("#kiminonawa");
    kiminonawaInput.prop('readonly', true);

    $.ajax({
        url: 'nicknamePrint',
        type: 'post',
        success: function(username) {
            kiminonawaInput.val(username);
            adjustUnderlineLength(kiminonawaInput, username);
        },
        error: function(error) {
            console.error(error);
        }
    });
}

// 밑줄 길이를 조절하는 함수
function adjustUnderlineLength(inputField, text) {
    const textWidth = getTextWidth(text, inputField.css('font'));
    inputField.css('width', textWidth + 'px');
}

// 텍스트의 너비를 계산하는 함수
function getTextWidth(text, font) {
    const canvas = document.createElement('canvas');
    const context = canvas.getContext('2d');
    context.font = font;
    const metrics = context.measureText(text);
    return metrics.width;
}

// 페이지 로드 후 실행
$(document).ready(function() {
    usernamePrint();
    setBackgroundImage();
});
