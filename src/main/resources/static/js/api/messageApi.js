import { request } from "./client.js";

export const fetchHistory = (channelId) =>
    request(`/api/messages/channel/${channelId}`, 'GET', null, "履歴の読み込みに失敗しました");

export const deleteMessage = (id) =>
    request(`/api/messages/${id}`, 'DELETE', null, "メッセージの削除に失敗しました");

export const updateMessage = (id, content) =>
    request(`/api/messages/${id}`, 'PATCH', { content }, "メッセージの更新に失敗しました");