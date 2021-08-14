import { Socket } from "socket.io"
import logger from "../utils/logger"
import { search } from "../api/youtube-api"

export function addEventListeners(socket: Socket) {
    socket.on(
        "share youtube video",
        async (title: string, channel: string, timestamp: string) => {
            const snapshot = { title, channel, timestamp }
            logger.info("Shared youtube video: %s", JSON.stringify(snapshot))

            await search(snapshot)
        }
    )
}
