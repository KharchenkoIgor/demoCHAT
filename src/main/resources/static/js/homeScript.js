import { setCurrentServer, setCurrentChannel } from './state.js';

export function goToHome() {
    // Сбрасываем стейт
    setCurrentServer(null);
    setCurrentChannel(null);

    // Обновляем UI
    const topBar = document.querySelector('.top-bar');
    if(topBar) topBar.innerText = "ホーム";

    const chat = document.getElementById('chat');
    if(chat) chat.innerHTML = "<h1>おかえりなさい！</h1>";

    const channelList = document.getElementById('channel-list');
    if(channelList) channelList.innerHTML = "";
}