import { request } from "./client.js";

export const fetchMyRole = (serverId) =>
    request(`/api/servers/${serverId}/my-role`, 'GET', null, "ロールの取得に失敗しました");

export const fetchServers = () =>
    request('/api/servers', 'GET', null, "サーバーの読み込みに失敗しました");

export const createServer = (name, publicStatus) =>
    request('/api/servers', 'POST', { name, publicStatus },"サーバーの作成に失敗しました");

export const deleteServer = (id) =>
    request(`/api/servers/${id}`, 'DELETE', null, "サーバーの削除に失敗しました");

export const updateServer = (id, name) =>
    request(`/api/servers/${id}`, 'PATCH', { name }, "サーバーの更新に失敗しました");

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