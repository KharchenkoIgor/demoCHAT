export function renderServerIcon(server, onClick) {
    const iconDiv = document.createElement('div');
    iconDiv.className = 'server-icon';
    // Добавлена проверка на наличие имени, чтобы не падало
    iconDiv.innerText = server.name ? server.name.substring(0,2).toUpperCase() : '??';
    iconDiv.onclick = () => onClick(server);
    return iconDiv;
}

export function renderChannel(channel, onClick) {
    const div = document.createElement('div');
    div.className = 'channel-item';
    div.innerText = '#' + channel.name;
    div.onclick = () => onClick(channel);
    return div;
}

export function renderMessage(msg) {
    const chat = document.getElementById('chat');
    if (!chat) return; // Защита если элемента нет

    const div = document.createElement('div');
    div.className = 'message-item'; // Полезно для стилей
    // Используем textContent для защиты от XSS атак, вместо innerHTML
    const userBold = document.createElement('b');
    userBold.textContent = msg.senderName;

    const textSpan = document.createElement('span');
    textSpan.textContent = `: ${msg.content}`;

    div.appendChild(userBold);
    div.appendChild(textSpan);
    chat.appendChild(div);

    // Автоскролл вниз
    chat.scrollTop = chat.scrollHeight;
}