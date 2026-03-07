import {acceptedJoinRequest, fetchJoinRequests, joinServer,
    rejectJoinRequest, searchServers} from "../api/serverApi.js";
import {showPush} from "../utils/showPush.js";
import {appState} from "../state.js";

const JOIN_STATUS_MAP = {
    'JOINED': { text: "サーバーに参加しました！", type: 'success', reload: true },
    'PENDING': { text: "参加申請を送信しました。管理者の承認をお待ちください", type: 'info' },
    'ALREADY_MEMBER': { text: "既にこのサーバーの参加者です", type: 'info' },
    'PENDING_EXISTS': { text: "既に申請済みです。承認をお待ち下さい", type: 'info' }
};

const REQUEST_ACTIONS = {
    'approve': acceptedJoinRequest,
    'reject': rejectJoinRequest
};

export async function handleJoinServer(serverId) {
    try {
        const { status } = await joinServer(serverId);
        const result = JOIN_STATUS_MAP[status];

        if (result) {
            showPush(result.text, result.type);
            if (result.reload) {
                setTimeout(() => window.location.reload(), 1500);
            }
        } else {
            showPush("不明なステータスです", 'error');
        }
    } catch (e) {
        showPush("エラーが発生しました", 'error');
    }
}

export async function handleSearchServers(query) {
    if (!query) return;

    const container = document.getElementById('modal-list-container');
    if (!container) return;

    const results = await searchServers(query);
    container.innerHTML = results.length ? '' : 'サーバーが見つかりません。';

    results.forEach(server => {
        const div = document.createElement('div');
        div.className = 'search-result-item';
        div.innerHTML = `
            <span>${server.name}</span>
            <button class="btn-join" onclick="handleJoinServerClick(${server.id})">参加</button>
        `;
        container.appendChild(div);
    });
}

export async function refreshRequestsUI(serverId) {
    const container = document.getElementById('modal-list-container');
    if (!container) return;

    const requests = await fetchJoinRequests(serverId);
    container.innerHTML = requests.length ? '' : '待機中の申請はありません';

    requests.forEach(req => {
        const item = document.createElement('div');
        item.className = 'modal-list-item';
        item.innerHTML = `
            <span>${req.username}</span>
            <button class="btn-approve" onclick="processRequest(${req.id}, 'approve')">✅</button>
            <button class="btn-reject" onclick="processRequest(${req.id}, 'reject')">❌</button>
        `;
        container.appendChild(item);
    });
}

async function handleProcessRequest(requestId, action) {
    try {
        const apiCall = REQUEST_ACTIONS[action];
        if (apiCall) {
            await apiCall(requestId);
            if (appState.currentServerId) {
                refreshRequestsUI(appState.currentServerId);
            }
        }
    } catch (e) {
        showPush(e.message, 'error');
    }
}

window.handleJoinServerClick = handleJoinServer;
window.processRequest = handleProcessRequest;
window.refreshRequestsUI = refreshRequestsUI;