async function loadServers() {
    const response = await fetch('/api/servers');
    if (response.ok) {
        const servers = await response.json();
        servers.forEach(server => renderServerIcon(server));
    }
}

async function submitServer() {
    const name = modalInput.value;
    if (!name) return;

    const response = await fetch('/api/servers', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify({ name })
    });

    if (response.ok) {
        const newServer = await response.json();
        renderServerIcon(newServer);
        closeModal();
    }
}

function renderServerIcon(server) {
    const iconDiv = document.createElement('div');
    iconDiv.className = 'server-icon';
    iconDiv.innerText = server.name.substring(0,2).toUpperCase();
    iconDiv.onclick = () => {
        currentServerId = server.id;
        document.querySelector('.top-bar').innerText = server.name;
        loadChannels(server.id);
    };
    document.getElementById('server-list').insertBefore(iconDiv, document.getElementById('add-server-button'));
}

window.addEventListener('load', loadServers);