import { MODAL_CONFIG, getIsPublicValue } from './config/modalConfig.js';
import { ACTION_HANDLERS } from './services/modalActionHandler.js';

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

async function handleSubmit() {
    const name = elements.input.value.trim();
    const cfg = MODAL_CONFIG[currentType];
    if (!name && cfg.showInput !== false) return;

    const isPublic = getIsPublicValue();

    try {
        elements.submit.disabled = true;

        if (cfg.service && cfg.action) {
            await ACTION_HANDLERS[cfg.service][cfg.action](name, currentEditId, isPublic);
        } else {
            await cfg.action(name, currentEditId, isPublic);
        }

        if (currentType !== 'search' && currentType !== 'viewRequests') {
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
    const cfg = MODAL_CONFIG[type];
    if (!modal || !cfg) return;

    currentType = type;
    currentEditId = editId;

    elements.title.innerText = cfg.title;
    elements.desc.innerText = cfg.desc;

    elements.submit.style.display = cfg.showSubmit === false ? 'none' : 'block';
    elements.submit.innerText = cfg.buttonText || "作成";

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
        ACTION_HANDLERS.viewRequests.action(editId);
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