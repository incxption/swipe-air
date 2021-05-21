import { config } from "dotenv"
config()

import { Server } from "socket.io"
import logger from "./utils/logger"
import { addEventListeners } from "./events/events"

export const io = new Server()

io.on("connection", socket => {
    logger.info(
        "New connection established from %s (%s)",
        socket.handshake.address,
        socket.id
    )

    addEventListeners(socket)
})

io.listen(5785)
logger.info("Listening on port 5785")
