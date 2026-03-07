import { request } from "./client.js";

export const fetchChannels = (serverId) =>
    request(`/api/channels/server/${serverId}`, 'GET', null, "チャンネルの読み込みに失敗しました");

export const createChannel = (name, serverId) =>
    request('/api/channels', 'POST', { name, serverId }, "チャンネルの作成に失敗しました");

export const deleteChannel = (id) =>
    request(`/api/channels/${id}`, 'DELETE', null, "チャンネルの削除に失敗しました");

export const updateChannel = (id, name) =>
    request(`/api/channels/${id}`, 'PATCH', { name }, "チャンネルの更新に失敗しました");