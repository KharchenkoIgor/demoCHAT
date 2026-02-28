export async function fetchServers() {
    const response = await fetch('/api/servers');
    if (!response.ok) throw new Error("Failed to load servers");
    return await response.json();
}

export async function createServer(name) {
    const response = await fetch('/api/servers', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify({ name })
    });
    if (!response.ok) throw new Error("Failed to create server");
    return await response.json();
}

export async function fetchChannels(serverId) {
    const response = await fetch('/api/channels/server/' + serverId);
    if (!response.ok) throw new Error("Failed to load channels");
    return await response.json();
}

export async function createChannel(name, serverId) {
    const response = await fetch('/api/channels', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, serverId })
    });
    if (!response.ok) throw new Error("Failed to create channel");
    return await response.json();
}

export async function fetchHistory(channelId) {
    const response = await fetch('/api/messages/channel/' + channelId);
    if (!response.ok) throw new Error("Failed to load history");
    return await response.json();
}