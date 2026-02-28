async function loadChatHistory(channel) {
    const messagesArea = document.getElementById('chat');

    try {
        const response = await fetch('/api/messages/channel/' + channel.id);
        if (response.ok) {
            const history = await response.json();
            history.forEach(msg => renderNewMessage(msg));
        }
    } catch (e) {
        console.error("履歴の読み込みエラー:", e);
        messagesArea.innerHTML = "履歴の読み込みは失敗です";
    }
}

async function loadChannels(serverId) {
    const channelList = document.getElementById('channel-list');

    try{
        const response = await fetch('/api/channels/server/' + serverId);
        if (response.ok) {
            const channels = await response.json();
            channelList.innerHTML = `
                <div class="channel-header">
                    <span>チャンネル</span>
                    <span style="cursor:pointer; font-size:18px;" onclick="openModal('channel')">+</span>
                </div>
            `;

            if (channels.length === 0) {
                channelList.innerHTML += `<div style="padding:10px; color:gray; font-size:12px;">No channels yet</div>`;
            }

            channels.forEach(channel => {
                const div = document.createElement('div');
                div.className = 'channel-item';
                div.innerText = '#' + channel.name;
                div.onclick = () => {
                    currentChannelId = channel.id;

                    document.querySelectorAll('.channel-item').forEach(el => el.classList.remove('active'));
                    div.classList.add('active');

                    const chatTitle = document.getElementById('chat-channel-name');
                    if (chatTitle) {
                        chatTitle.innerText = '#' + channel.name;
                    }

                    document.getElementById('chat').innerHTML = `<div>ようこそへ #${channel.name}!</div>`;

                    loadChatHistory(channel);
                };
                channelList.appendChild(div);
            });
        }
    } catch (e) {
        console.error("チャンネル描画のエラーが発生しました", e);
    }
}

async function submitChannel() {
    const name = modalInput.value;
    if (!name || !currentServerId) return;

    const response = await fetch('/api/channels', {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify({ name, serverId: currentServerId })
    });

    if (response.ok) {
        closeModal();
        loadChannels(currentServerId);
    }
}