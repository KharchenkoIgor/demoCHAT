import { createServer, createChannel } from "./api/api.js";
import { initChatPage, refreshChannelsUI } from "./ui/chatLogic.js"; // <--- ДОБАВИЛ refreshChannelsUI
import { appState } from "./state.js";

const modal = document.getElementById('universal-modal');
const modalTitle = document.getElementById('modal-title');
const modalInput = document.getElementById('modal-input');
const modalSubmitButton = document.getElementById('modal-submit-button');
const modalDesc = document.getElementById('modal-description');
const cancelButton = document.querySelector('.btn-cancel');

let currentType = null; // Запоминаем тип (server или channel)

const modalActions = {
    server: async (name) => {
        await createServer(name);
    },
    channel: async (name) => {
        if (!appState.currentServerId) {
            alert("Сначала выберите сервер!");
            return;
        }
        await createChannel(name, appState.currentServerId);
    }
};

export function openModal(type) {
    if (!modal) return;
    modal.style.display = 'flex';
    modalInput.value = "";
    modalInput.focus();

    currentType = type; // Сохраняем тип, чтобы знать, что обновлять

    if (type === 'server') {
        modalTitle.innerText = "サーバー作成";
        modalDesc.innerText = "新しいサーバー名を入力して下さい";
        modalInput.placeholder = "サーバー名";
    }
    else if (type === 'channel') {
        modalTitle.innerText = "チャンネル作成";
        modalDesc.innerText = "新しいチャンネル名を入力して下さい";
        modalInput.placeholder = "チャンネル名";
    }
}

function closeModal() {
    if (modal) modal.style.display = 'none';
}

if (modalSubmitButton) {
    modalSubmitButton.onclick = async () => {
        const name = modalInput.value.trim();
        const action = modalActions[currentType];

        if (!name || !action) return;

        try {
            // 1. Создаем (сервер или канал)
            await action(name);

            // 2. Обновляем интерфейс в зависимости от типа
            if (currentType === 'server') {
                await initChatPage(); // Обновляем список серверов
            } else if (currentType === 'channel') {
                await refreshChannelsUI(); // Обновляем ТОЛЬКО список каналов
            }

            closeModal();
        } catch (error) {
            console.error(error);
            alert("Ошибка: " + error.message);
        }
    };
}

if (cancelButton) {
    cancelButton.onclick = closeModal;
}

window.onclick = function(event) {
    if (event.target == modal) closeModal();
}