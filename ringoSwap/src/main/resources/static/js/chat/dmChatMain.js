let stompClient;

$(document).ready(function()
{
	init();
	connect();
});

function init()
{
	let socket = new SockJS('/ringo/ws-stomp');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function connect()
{
	
}


function onConnected()
{
	
}

function onError()
{
	console.log("dmChatMain - connect failed!");
}