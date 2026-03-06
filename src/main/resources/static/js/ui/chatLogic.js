import { fetchServers, fetchChannels, fetchHistory, fetchMyRole,
    deleteMessage, deleteChannel, deleteServer, updateMessage,
    searchServers, joinServer, fetchJoinRequests, acceptedJoinRequest,
    rejectJoinRequest} from '../api/api.js';
import { showPush } from '../api/showPush.js';
import * as UI from './ui.js';
import { appState, setCurrentServer, setCurrentChannel } from '../state.js';
import { openModal } from '../modalScript.js';

const getChat = () => document.getElementById('chat');

async function confirmAndDelete(confirmMsg, apiCall, onSuccess) {
    if (!confirm(confirmMsg)) return;
    try {
        await apiCall();
        if (onSuccess) onSuccess();
    } catch (e) { alert(e.message); }
}

function updateServerHeaderUI(server) {
    const header = document.getElementById('server-header-container');
    if (!header) return;

    header.innerHTML = '';
    const isOwner = appState.currentRole === 'OWNER';

    const headerNodes = UI.renderServerHeader(
        server.name,
        isOwner ? () => openModal('editServer', server.id, server.name) : null,
        isOwner ? () => handleDeleteServer(server.id) : null,
        isOwner ? () => openModal('viewRequests', server.id) : null
    );
    header.append(...headerNodes);
}

export async function initChatPage() {
    try {
        const servers = await fetchServers();
        window.userServers = servers;
        const serverList = document.getElementById('server-list');
        if (!serverList) return;

        Array.from(serverList.children).forEach(c => !['add-server-button', 'home-button', 'search-server-button'].includes(c.id) && c.remove());
        document.getElementById('home-button').onclick = () => window.location.reload();

        servers.forEach(server => {
            serverList.insertBefore(UI.renderServerIcon(server, handleServerClick), document.getElementById('add-server-button'));
        });
    } catch (e) { console.error("Init Error:", e); }
}

async function handleServerClick(server) {
    const { role } = await fetchMyRole(server.id);
    appState.currentRole = role;
    setCurrentServer(server.id);
    setCurrentChannel(null);

    updateServerHeaderUI(server);

    document.querySelector('#chat-channel-name').textContent = 'チャンネルを選択してください';
    getChat().innerHTML = '';
    await refreshChannelsUI();
}

export async function refreshServersUI() {
    await initChatPage();

    if (!appState.currentServerId) return;
    const server = window.userServers.find(s => s.id === appState.currentServerId);
    if (server) updateServerHeaderUI(server);
}

async function handleDeleteServer(id) {
    await confirmAndDelete("サーバーを削除しますか？", () => deleteServer(id), () => window.location.reload());
}

export async function refreshChannelsUI() {
    const list = document.getElementById('channel-list');
    if (!list || !appState.currentServerId) return;
    list.innerHTML = '';

    const channels = await fetchChannels(appState.currentServerId);
    const isAdmin = ['OWNER', 'ADMIN'].includes(appState.currentRole);

    channels.forEach(channel => {
        list.appendChild(UI.renderChannel(
            channel,
            handleChannelClick,
            isAdmin ? handleDeleteChannel : null,
            isAdmin ? (ch) => openModal('editChannel', ch.id, ch.name) : null
        ));
    });

    list.appendChild(UI.renderChannel({ name: 'チャンネルを作成' }, () => openModal('channel')));
}

async function handleChannelClick(channel) {
    setCurrentChannel(channel.id);
    document.getElementById('chat-channel-name').textContent = `# ${channel.name}`;
    getChat().innerHTML = '';

    const history = await fetchHistory(channel.id);
    history.forEach(displayNewMessage);
}

async function handleDeleteChannel(id) {
    await confirmAndDelete("チャンネルを削除しますか？", () => deleteChannel(id), refreshChannelsUI);
}

export function displayNewMessage(msgData) {
    const chat = getChat();
    if (!chat) return;

    chat.appendChild(UI.renderMessage(msgData, handleDeleteMessage, handleEditMessage));
    chat.scrollTop = chat.scrollHeight;
}

async function handleEditMessage(msg) {
    const id = msg.id || msg.messageId;
    const textSpan = document.getElementById(`msg-text-${id}`);
    if (!textSpan) return;

    const originalText = textSpan.textContent;
    const input = document.createElement('input');
    input.value = originalText;
    input.className = 'inline-edit-input';

    textSpan.replaceWith(input);
    input.focus();

    let isSubmitting = false;

    const finishEdit = async () => {
        if (isSubmitting) return;
        isSubmitting = true;

        const newText = input.value.trim();
        if (newText && newText !== originalText) {
            try {
                await updateMessage(id, newText);
                textSpan.textContent = newText;
            } catch (e) { alert(e.message); }
        }
        input.replaceWith(textSpan);
    };

    input.onkeydown = (e) => {
        if (e.key === 'Enter') finishEdit();
        if (e.key === 'Escape') {
            isSubmitting = true;
            input.replaceWith(textSpan);
        }
    };

    input.onblur = () => finishEdit(true);
}

async function handleDeleteMessage(id) {
    await confirmAndDelete("削除しますか？", () => deleteMessage(id), () => document.getElementById(`message-${id}`)?.remove());
}

export async function handleSearchServers(query) {
    if (!query) return;
    const results = await searchServers(query);
    const container = document.getElementById('modal-list-container');
    if (!container) return;

    container.innerHTML = results.length ? '' : 'サーバーが見つかりません。';
    results.forEach(server => {
        const div = document.createElement('div');
        div.className = 'search-result-item';
        div.innerHTML = `
            <span>${server.name}</span>
            <button onclick="handleJoinServerClick(${server.id})">参加</button>
        `;
        container.appendChild(div);
    });
}

window.handleJoinServerClick = async (serverId) => {
    await handleJoinServer(serverId);
};

export async function handleJoinServer(serverId) {
    try {
        const response = await joinServer(serverId);
        const status = response.status;
        if (status === 'JOINED') {
            showPush("サーバーに参加しました！", 'success');
            setTimeout(() => window.location.reload(), 1500);
        }
        else if (status === 'PENDING'){
            showPush("参加申請を送信しました。管理者の承認をお待ちください", 'info');
        }
        else if (status === 'ALREADY_MEMBER') {
            showPush("既にこのサーバーの参加者です", 'info');
        }
        else if (status === 'PENDING_EXISTS') {
            showPush("既に申請済みです。承認をお持ち下さい");
        }
    } catch (e) {
        showPush("エラーが発生しました", 'error');
    }
}

export async function refreshRequestsUI(serverId) {
    const container = document.getElementById('modal-list-container');
    if (!container) return;

    const requests = await fetchJoinRequests(serverId);
    container.innerHTML = requests.length ? '' : '待機中の申請はありません。';

    requests.forEach(req => {
        const item = document.createElement('div');
        item.className = 'modal-list-item';
        item.innerHTML = `
            <span>${req.username}</span>
            <button onclick="processRequest(${req.id}, 'approve')">✅</button>
            <button onclick="processRequest(${req.id}, 'reject')">❌</button>
        `;
        container.appendChild(item);
    });
}

window.refreshRequestsUI = refreshRequestsUI;

window.processRequest = async (requestId, action) => {
    try {
        if (action === 'approve') await acceptedJoinRequest(requestId);
        else await rejectJoinRequest(requestId);
        if (appState.currentServerId) refreshRequestsUI(appState.currentServerId);
    } catch (e) { alert(e.message); }
};