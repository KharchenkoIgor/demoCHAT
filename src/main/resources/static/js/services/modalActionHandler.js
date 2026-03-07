import { appState, setCurrentServer } from "../state.js";
import { initChatPage, refreshServersUI, refreshChannelsUI } from "../services/chatService.js";
import { createServer, updateServer } from "../api/serverApi.js";
import { createChannel, updateChannel } from "../api/channelApi.js";
import { handleSearchServers, refreshRequestsUI } from "./joinService.js";

export const ACTION_HANDLERS = {
    server: {
        create: async (name, id, isPublicStatus) => {
            const newServer = await createServer(name, isPublicStatus);
            setCurrentServer(newServer.id);
            await initChatPage();
            await refreshChannelsUI(newServer.id);
        },
        update: async (name, id, isPublicStatus) => {
            await updateServer(id, name, isPublicStatus);
            await refreshServersUI();
        }
    },
    channel: {
        create: async (name) => {
            if (!appState.currentServerId) throw new Error("まずサーバーを選択してください！");
            await createChannel(name, appState.currentServerId);
            await refreshChannelsUI(appState.currentServerId);
        },
        update: async (name, id) => {
            await updateChannel(id, name);
            await refreshChannelsUI(appState.currentServerId);
        }
    },
    search: {
        action: async (name) => {
            await handleSearchServers(name);
        }
    },
    viewRequests: {
        action: async (id) => {
            await refreshRequestsUI(id);
        }
    }
};