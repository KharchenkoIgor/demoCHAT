import { createServer, createChannel, updateServer, updateChannel } from "./api/api.js";
import { initChatPage, refreshServersUI, refreshChannelsUI } from "./ui/chatLogic.js";
import { appState, setCurrentServer } from "./state.js";

const modal = document.getElementById('universal-modal');
const elements = {
    title: document.getElementById('modal-title'),
    desc: document.getElementById('modal-description'),
    input: document.getElementById('modal-input'),
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
        action: async (name) => {
            const newServer = await createServer(name);
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
        action: async (name, id) => {
            await updateServer(id, name);
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
    }
};

async function handleSubmit() {
    const name = elements.input.value.trim();
    const cfg = CONFIG[currentType];
    if (!name || !cfg) return;

    try {
        elements.submit.disabled = true;
        await cfg.action(name, currentEditId);
        closeModal();
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        elements.submit.disabled = false;
    }
}

export function openModal(type, editId = null, initialValue = "") {
    const cfg = CONFIG[type];
    if (!modal || !cfg) return;

    currentType = type;
    currentEditId = editId;

    elements.title.innerText = cfg.title;
    elements.desc.innerText = cfg.desc;
    elements.input.placeholder = cfg.placeholder;
    elements.input.value = initialValue;

    modal.style.display = 'flex';
    setTimeout(() => elements.input.focus(), 50);
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