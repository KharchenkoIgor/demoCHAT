import { request } from "./client.js";

export const fetchMe = () =>
    request('/api/auth/me', 'GET', null, "ユーザー情報の取得に失敗しました");