export function getIsPublicValue() {
    const selected = document.querySelector('input[name="serverType"]:checked');
    return selected ? selected.value === 'public' : true;
}

export const MODAL_CONFIG = {
    server: {
        title: "サーバー作成",
        desc: "新しいサーバー名を入力して下さい",
        placeholder: "サーバー名",
        buttonText: "作成",
        service: 'server',
        action: 'create'
    },
    channel: {
        title: "チャンネル作成",
        desc: "新しいチャンネル名を入力して下さい",
        placeholder: "チャンネル名",
        buttonText: "作成",
        service: 'channel',
        action: 'create'
    },
    editServer: {
        title: "サーバー編集",
        desc: "新しいサーバー名を入力して下さい",
        placeholder: "新しいサーバー名",
        buttonText: "保存",
        service: 'server',
        action: 'update'
    },
    editChannel: {
        title: "チャンネル編集",
        desc: "新しいチャンネル名を入力して下さい",
        placeholder: "新しいチャンネル名",
        buttonText: "保存",
        service: 'channel',
        action: 'update'
    },
    search: {
        title: "サーバー検索",
        desc: "探したいサーバー名を入力してください",
        placeholder: "検索...",
        buttonText: "検索",
        showInput: true,
        service: 'search',
        action: 'action'
    },
    viewRequests: {
        title: "加入申請",
        desc: "サーバーへの参加希望者リスト",
        showInput: false,
        showSubmit: false,
        service: 'viewRequests',
        action: 'action'
    }
};