import { setCurrentServer, setCurrentChannel } from './state.js';

export function goToHome() {

    setCurrentServer(null);
    setCurrentChannel(null);

    const topBar = document.querySelector('.top-bar');
    if(topBar) topBar.innerText = "ホーム";

    const chat = document.getElementById('chat');
    if(chat) chat.innerHTML = "<h1>おかえりなさい！</h1>";

    const channelList = document.getElementById('channel-list');
    if(channelList) channelList.innerHTML = "";
}