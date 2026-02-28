export const eventHandlers = [
    {
        type: "MESSAGE",
        handler: (data, context) => {
            const isCurrentChannel = String(data.channelId) === String(context.currentChannelId);

            if (isCurrentChannel) {
                context.displayNewMessage(data);
            } else {
                context.showNotificationBadge(data.serverId, data.channelId);
            }
        }
    },
    {
        type: "DELETE_SERVER",
        handler: (data, context) => {
            const serverEl = document.getElementById(`server-item-${data.id}`);
            if (serverEl) serverEl.remove();
        }
    },
    {
        type: "DELETE_CHANNEL",
        handler: (data, context) => {
            const chanEl = document.getElementById(`channel-item-${data.id}`);
            if (chanEl) chanEl.remove();
        }
    },
    {
        type: "DELETE_MESSAGE",
        handler: (data, context) => {
            const msgEl = document.getElementById(`message-${data.id}`);
            if (msgEl) msgEl.remove();
        }
    },
    {
        type: "UPDATE_SERVER",
        handler: (data, context) => {
            const serverIcon = document.getElementById(`server-item-${data.id}`);

            if (serverIcon) serverIcon.textContent = data.name.substring(0, 2).toUpperCase();

            if (String(data.id) === String(context.currentServerId)) {
                const headerSpan = document.querySelector('#server-header-container span');
                if (headerSpan) headerSpan.textContent = data.name;
            }
        }
    },
    {
        type: "UPDATE_CHANNEL",
        handler: (data, context) => {
            const chanText = document.querySelector(`#channel-item-${data.id} .channel-item`);

            if (chanText) chanText.textContent = `# ${data.name}`;

            if (String(data.id) === String(context.currentChannelId)) {
                const headerTitle = document.getElementById('chat-channel-name');
                if (headerTitle) headerTitle.textContent = `# ${data.name}`;
            }
        }
    },
    {
        type: "UPDATE_MESSAGE",
        handler: (data) => {
            const msgText = document.getElementById(`msg-text-${data.id}`);

            if (msgText) msgText.textContent = data.content;
        }
    }
];