import { appState } from '../state.js';

// Универсальный создатель элементов
const el = (tag, { cls = '', id = '', text = '', click = null } = {}, children =[]) => {
    const element = document.createElement(tag);
    if (cls) element.className = cls;
    if (id) element.id = id;
    if (text) element.textContent = text;
    if (click) element.onclick = click;
    children.forEach(child => child && element.appendChild(child));
    return element;
};

// НОВЫЙ ПОМОЩНИК: Стандартная кнопка (✏️ или 🗑️)
const createBtn = (icon, cls, onClick, stopProp = false) => {
    return el('button', {
        cls, text: icon, click: (e) => {
            if (stopProp) e.stopPropagation();
            onClick(e);
        }
    });
};

export function renderServerIcon(server, onClick) {
    return el('div', {
        cls: 'server-icon',
        id: `server-item-${server.id}`,
        text: server.name ? server.name.substring(0, 2).toUpperCase() : '??',
        click: () => onClick(server)
    });
}

export function renderServerHeader(serverName, onEdit = null, onDelete = null) {
    const elements = [el('span', { text: serverName })];
    // Используем помощника для кнопок
    if (onEdit) elements.push(createBtn('✏️', 'server-edit-btn', onEdit));
    if (onDelete) elements.push(createBtn('🗑️', 'server-delete-btn', onDelete));
    return elements;
}

export function renderChannel(channel, onClick, onDelete = null, onEdit = null) {
    const controls = el('div', { cls: 'channel-controls' });

    // Используем помощника (stopProp = true, чтобы клик по кнопке не открывал канал)
    if (onEdit) controls.appendChild(createBtn('✏️', 'edit-btn', () => onEdit(channel), true));
    if (onDelete) controls.appendChild(createBtn('🗑️', 'delete-btn', () => onDelete(channel.id), true));

    return el('div', { cls: 'channel-wrapper', id: `channel-item-${channel.id}` },[
        el('div', { cls: 'channel-item', text: `# ${channel.name}`, click: () => onClick(channel) }),
        controls
    ]);
}

export function renderMessage(msg, onDelete = null, onEdit = null) {
    const realId = msg.id || msg.messageId;
    const authorName = msg.senderName || msg.author || 'Unknown';
    const isAuthor = (window.currentUserUsername && authorName === window.currentUserUsername);
    const isAdmin = (appState.currentRole === 'OWNER' || appState.currentRole === 'ADMIN');

    const controls = el('div', { cls: 'msg-controls' });

    // Используем помощника
    if (isAuthor && onEdit) controls.appendChild(createBtn('✏️', 'msg-btn', () => onEdit(msg)));
    if ((isAuthor || isAdmin) && onDelete) controls.appendChild(createBtn('🗑️', 'msg-btn', () => onDelete(realId)));

    return el('div', { cls: 'message-item', id: `message-${realId}` },[
        el('div', { cls: 'msg-content' },[
            el('span', { cls: 'msg-author', text: `${authorName}: ` }),
            el('span', { cls: 'msg-text', id: `msg-text-${realId}`, text: msg.content || '' }),
            controls
        ])
    ]);
}