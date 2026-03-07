import { appState } from './state.js';
import { fetchMe } from './api/authApi.js';
import { webSocketConnection } from './webSocket/webSocketConnection.js';
import { eventHandlers } from './handlers/eventHandlers.js';
import { initChatPage, displayNewMessage } from './services/chatService.js';
import { setupMessageInput } from './utils/sendMessage.js';
import { showNotificationBadge, clearNotificationBadge } from './utils/showNotificationBadge.js';
import { openModal } from './modalScript.js';
import './services/joinService.js';

window.showNotificationBadge = showNotificationBadge;
window.clearNotificationBadge = clearNotificationBadge;

function handleWebSocketEvent(data) {
    const handlerObject = eventHandlers.find(h => h.type === data.type);
    if (!handlerObject) return;

    handlerObject.handler(data, {
        currentServerId: appState.currentServerId,
        currentChannelId: appState.currentChannelId,
        displayNewMessage,
        showNotificationBadge,
        clearNotificationBadge: window.clearNotificationBadge,
        refreshRequestsUI: window.refreshRequestsUI
    });
}

window.addEventListener('load', async () => {
    try {
        const userData = await fetchMe();

        window.currentUserUsername = userData.username;
        console.log("ユーザー名は", window.currentUserUsername);

        await initChatPage();
        setupMessageInput();

        const serverButton = document.getElementById('add-server-button');
        if (serverButton) serverButton.addEventListener('click', () => openModal('server'));

        const channelButton = document.getElementById('add-channel-button');
        if (channelButton) channelButton.addEventListener('click', () => openModal('channel'));

        webSocketConnection(window.userServers || [], handleWebSocketEvent);
    } catch (e) {
        console.error("ログインが出来ませんでした", e);
    }
});