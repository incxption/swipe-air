import { config } from "dotenv"
config()

import { Server } from "socket.io"
import logger from "./utils/logger"
import { addEventListeners } from "./events/events"

export const io = new Server()
const port = 5785

io.on("connection", socket => {
    logger.info(
        "New connection established from %s (%s)",
        socket.handshake.address,
        socket.id
    )

    addEventListeners(socket)
})

io.listen(port)
logger.info("Listening on port %d", port)
