export let stompClient = null;

export function webSocketConnection(userServers, handleEvent) {
    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);

    stompClient.debug = null;

    stompClient.connect({}, frame => {
        console.log("✅ WebSocketの接続:", frame);

        stompClient.subscribe('/user/queue/events', event => {
            handleEvent(JSON.parse(event.body));
        });

        if (userServers) {
            const allChannels = userServers.flatMap(s => s.channels || []);

            allChannels.forEach(channel => {
                stompClient.subscribe(
                    `/topic/server.${channel.serverId}.channel.${channel.id}`,
                    event => handleEvent(JSON.parse(event.body))
                );
            });
        }
    }, error => {
        console.error("❌ WebSocketのエラー:", error);

        setTimeout(() => webSocketConnection(userServers, handleEvent), 5000);
    });
}