export let stompClient = null;

export function webSocketConnection(userServers, handleEvent) {

    if (stompClient && stompClient.connected) return;

    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, frame => {
        console.log("✅ WebSocket Connected");

        stompClient.subscribe('/user/queue/events', event => {
            handleEvent(JSON.parse(event.body));
        });
    }, error => {
        stompClient = null;
        setTimeout(() => webSocketConnection(userServers, handleEvent), 5000);
    });
}