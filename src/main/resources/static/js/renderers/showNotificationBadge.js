export function showNotificationBadge(serverId, channelId) {

    const serverIcon = document.getElementById(`server-item-${serverId}`);
    if (serverIcon) {
        serverIcon.classList.add('has-notification');
    }

    const channelEl = document.getElementById(`channel-item-${channelId}`);
    if (channelEl) {
        channelEl.classList.add('unread');
    }
}