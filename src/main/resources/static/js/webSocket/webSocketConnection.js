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

        stompClient.subscribe('/user/queue/reply', event => {
            const data = JSON.parse(event.body);
            handleEvent({ type: "JOIN_RESPONSE", ...data });
        });

        if (userServers && Array.isArray(userServers)) {
            userServers.forEach(server => {
                stompClient.subscribe(`/topic/server/${server.id}/requests`, event => {
                    const data = JSON.parse(event.body);
                    handleEvent({ type: "NEW_JOIN_REQUEST", ...data });
                });
            });
        }
    }, error => {
        stompClient = null;
        setTimeout(() => webSocketConnection(userServers, handleEvent), 5000);
    });
}