// console.log("gatewayScript.js загружен!");
//
// let stompClient = null;
//
// function connectGateway() {
//     console.log("Попытка подключения к WebSocket...");
//     const socket = new SockJS('/ws-chat');
//     stompClient = Stomp.over(socket);
//
//     stompClient.connect({}, function (frame) {
//         console.log('Gateway Connected: ' + frame);
//
//         // ПОДПИСКА НА ЛИЧНЫЙ ПОТОК (User Gateway)
//         // Spring Security сам поймет, кто ты, и подставит имя вместо /user
//         stompClient.subscribe('/user/queue/events', function (event) {
//             const data = JSON.parse(event.body);
//             console.log("Получено событие от сервера:", data);
//
//             handleGlobalEvent(data);
//         });
//     }, function(error) {
//         console.error("WebSocket Error: ", error);
//         // Реконнект через 5 секунд, если упало
//         setTimeout(connectGateway, 5000);
//     });
// }

// function handleGlobalEvent(data) {
//     if (data.type === 'MESSAGE') {
//         // Если у нас открыт этот канал — рисуем сообщение
//         if (typeof currentChannelId !== 'undefined' && data.channelId === currentChannelId) {
//             renderNewMessage(data);
//         } else {
//             showNotificationBadge(data.serverId, data.channelId);
//         }
//     }
// }

// function renderNewMessage(msg) {
//     const messagesDiv = document.getElementById('chat');
//     if (!messagesDiv) return;
//
//     const messageElement = document.createElement('div');
//
//     messageElement.innerHTML =  `
//         <span style="color: #3ba55c; font-weight: bold;">${msg.senderName}:</span>
//         <span style="color: white;">${msg.content}</span>
//     `;
//     messagesDiv.appendChild(messageElement);
//     messagesDiv.scrollTop = messagesDiv.scrollHeight; // Авто-скролл вниз
// }

// window.addEventListener('load', connectGateway);

// function sendMessage() {
//     const input = document.getElementById('message-input');
//     const text = input.value;
//
//     if (!text || !currentChannelId || !currentServerId) {
//         console.error("Нельзя отправить: нет текста или не выбран канал/сервер", {text, currentChannelId, currentServerId});
//         return;
//     }
//
//     const payload = {
//         content: text,
//         channelId: currentChannelId,
//         serverId: currentServerId
//     };
//
//     // Шлем на @MessageMapping("/send") -> префикс /app задан в конфиге
//     stompClient.send("/app/send", {}, JSON.stringify(payload));
//
//     input.value = ""; // Очищаем поле
// }
//
// document.getElementById('message-input').addEventListener('keypress', function (e) {
//     if (e.key === 'Enter') {
//         sendMessage();
//     }
// });