export const appState = {
    currentServerId: null,
    currentChannelId: null,
    currentRole: null,
    currentUser: null
};

export function setCurrentServer(id) {
    appState.currentServerId = id;
}

export function setCurrentChannel(id) {
    appState.currentChannelId = id;
}