import { stompClient } from '../webSocket/webSocketConnection.js';
import { appState } from '../state.js';

export function sendMessage() {
    const input = document.getElementById('message-input');
    if (!input) return;

    const text = input.value.trim();
    const { currentChannelId, currentServerId } = appState;

    if (!text || !appState.currentChannelId || !appState.currentServerId) {
        console.warn("Cannot send: validation failed", { text, currentChannelId, currentServerId });
        return;
    }

    if (!stompClient || !stompClient.connected) {
        console.error("WebSocket is not connected");
        return;
    }

    const payload = {
        content: text,
        channelId: currentChannelId,
        serverId: currentServerId
    };

    stompClient.send("/app/send", {}, JSON.stringify(payload));
    input.value = "";
}

export function setupMessageInput() {
    const input = document.getElementById('message-input');
    const btn = document.getElementById('send-button');
    if (!input) return;

    const newInput = input.cloneNode(true);
    input.parentNode.replaceChild(newInput, input);

    newInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') sendMessage();
    });

    if (btn) {
        btn.onclick = sendMessage;
    }
}