import { createServer, createChannel, updateServer, updateChannel } from "./api/api.js";
import { initChatPage, refreshServersUI, refreshChannelsUI } from "./ui/chatLogic.js";
import { appState, setCurrentServer } from "./state.js";

const modal = document.getElementById('universal-modal');
const elements = {
    title: document.getElementById('modal-title'),
    desc: document.getElementById('modal-description'),
    input: document.getElementById('modal-input'),
    serverTypeContainer: document.getElementById("modal-server-type-container"),
    submit: document.getElementById('modal-submit-button'),
    cancel: document.querySelector('.btn-cancel')
};

let currentType = null;
let currentEditId = null;

const CONFIG = {
    server: {
        title: "サーバー作成",
        desc: "新しいサーバー名を入力して下さい",
        placeholder: "サーバー名",
        action: async (name, id, isPublicStatus) => {
            const newServer = await createServer(name, isPublicStatus);
            setCurrentServer(newServer.id);
            await initChatPage();
            await refreshChannelsUI(newServer.id);
        }
    },
    channel: {
        title: "チャンネル作成",
        desc: "新しいチャンネル名を入力して下さい",
        placeholder: "チャンネル名",
        action: async (name) => {
            if (!appState.currentServerId) throw new Error("まずサーバーを選択してください！");
            await createChannel(name, appState.currentServerId);
            await refreshChannelsUI(appState.currentServerId);
        }
    },
    editServer: {
        title: "サーバー編集",
        desc: "新しいサーバー名を入力して下さい",
        placeholder: "新しいサーバー名",
        action: async (name, id, isPublicStatus) => {
            await updateServer(id, name, isPublicStatus);
            await refreshServersUI();
        }
    },
    editChannel: {
        title: "チャンネル編集",
        desc: "新しいチャンネル名を入力して下さい",
        placeholder: "新しいチャンネル名",
        action: async (name, id) => {
            await updateChannel(id, name);
            await refreshChannelsUI(appState.currentServerId);
        }
    },
    viewRequests: {
        title: "加入申請",
        desc: "サーバーへの参加希望者リスト",
        showInput: false,
        action: async (val, id) => {}
    },
    search: {
        title: "サーバー検索",
        desc: "探したいサーバー名を入力してください",
        placeholder: "検索...",
        showInput: true,
        action: async (name) => {
            const { handleSearchServers } = await import('./ui/chatLogic.js');
            await handleSearchServers(name);
        }
    }
};

function getIsPublicValue() {
    const selected = document.querySelector('input[name="serverType"]:checked');
    return selected ? selected.value === 'public' : true;
}

async function handleSubmit() {
    const name = elements.input.value.trim();
    const cfg = CONFIG[currentType];
    if (!name || !cfg) return;

    const isPublic = getIsPublicValue()

    try {
        elements.submit.disabled = true;
        await cfg.action(name, currentEditId, isPublic);
        if (currentType !== 'search') {
            closeModal();
        }
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        elements.submit.disabled = false;
    }
}

export function openModal(type, editId = null, initialValue = "", initialPublic = true) {
    const cfg = CONFIG[type];
    if (!modal || !cfg) return;

    currentType = type;
    currentEditId = editId;

    elements.title.innerText = cfg.title;
    elements.desc.innerText = cfg.desc;

    if (type === 'server' || type === 'editServer') {
        elements.serverTypeContainer.style.display = 'flex';
        const radioToSelect = initialPublic ? 'public' : 'private';
        document.querySelector(`input[name="serverType"][value="${radioToSelect}"]`).checked = true;
    } else {
        elements.serverTypeContainer.style.display = 'none';
    }

    elements.input.style.display = cfg.showInput === false ? 'none' : 'block';

    if (cfg.showInput !== false) {
        elements.input.placeholder = cfg.placeholder || "";
        elements.input.value = initialValue;
    }

    let listCont = document.getElementById('modal-list-container');
    if (listCont) listCont.innerHTML = '';

    modal.style.display = 'flex';

    if (type === 'viewRequests') {
        import('./ui/chatLogic.js').then(m => m.refreshRequestsUI(editId));
    }
}

function closeModal() {
    if (modal) {
        modal.style.display = 'none';
        elements.input.value = "";
        currentEditId = null;
    }
}

if (elements.submit) elements.submit.onclick = handleSubmit;
if (elements.input) elements.input.onkeydown = (e) => { if (e.key === 'Enter') { e.preventDefault(); handleSubmit(); } };
if (elements.cancel) elements.cancel.onclick = closeModal;
window.addEventListener('keydown', (e) => { if (e.key === 'Escape') closeModal(); });
window.addEventListener('click', (e) => { if (e.target === modal) closeModal(); });