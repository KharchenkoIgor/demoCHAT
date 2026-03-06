async function request(url,
                       method = 'GET',
                       body = null,
                       errorMessage = 'API Error') {
    const options = {
        method,
        headers: body ? { 'Content-Type': 'application/json' } : {}
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);

    if (!response.ok) {
        throw new Error(errorMessage);
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

export const fetchMe = () =>
    request('/api/auth/me', 'GET', null, "ユーザー情報の取得に失敗しました");

export const fetchMyRole = (serverId) =>
    request(`/api/servers/${serverId}/my-role`, 'GET', null, "ロールの取得に失敗しました");

export const fetchServers = () =>
    request('/api/servers', 'GET', null, "サーバーの読み込みに失敗しました");

export const createServer = (name, publicStatus) =>
    request('/api/servers', 'POST', { name, publicStatus },"サーバーの作成に失敗しました");

export const fetchChannels = (serverId) =>
    request(`/api/channels/server/${serverId}`, 'GET', null, "チャンネルの読み込みに失敗しました");

export const createChannel = (name, serverId) =>
    request('/api/channels', 'POST', { name, serverId }, "チャンネルの作成に失敗しました");

export const fetchHistory = (channelId) =>
    request(`/api/messages/channel/${channelId}`, 'GET', null, "履歴の読み込みに失敗しました");

export const deleteServer = (id) =>
    request(`/api/servers/${id}`, 'DELETE', null, "サーバーの削除に失敗しました");

export const deleteChannel = (id) =>
    request(`/api/channels/${id}`, 'DELETE', null, "チャンネルの削除に失敗しました");

export const deleteMessage = (id) =>
    request(`/api/messages/${id}`, 'DELETE', null, "メッセージの削除に失敗しました");

export const updateServer = (id, name) =>
    request(`/api/servers/${id}`, 'PATCH', { name }, "サーバーの更新に失敗しました");

export const updateChannel = (id, name) =>
    request(`/api/channels/${id}`, 'PATCH', { name }, "チャンネルの更新に失敗しました");

export const updateMessage = (id, content) =>
    request(`/api/messages/${id}`, 'PATCH', { content }, "メッセージの更新に失敗しました");

export const searchServers = (name) =>
    request(`/api/servers/search?name=${encodeURIComponent(name)}`, 'GET', null, "サーバーの検索に失敗しました");

export const joinServer = (serverId) =>
    request(`/api/servers/${serverId}/join`, 'POST', null, "サーバーの参加に失敗しました");

export const fetchJoinRequests = (serverId) =>
    request(`/api/servers/${serverId}/requests`, 'GET', null, "参加申請の取得に失敗しました");

export const acceptedJoinRequest = (requestId) =>
    request(`/api/servers/requests/${requestId}/accepted`, 'POST', null, "申請の承認に失敗しました");

export const rejectJoinRequest = (requestId) =>
    request(`/api/servers/requests/${requestId}/reject`, 'POST', null, "申請の拒否に失敗しました");