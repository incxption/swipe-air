import { createLogger, format, transports } from "winston"
import chalk from "chalk"

const logger = createLogger({
    level: "debug",
    format: format.combine(
        format.timestamp({ format: "YYYY-MM-DD HH:mm:ss" }),
        format.errors({ stack: true }),
        format.splat(),
        format.json()
    ),
    transports: [
        new transports.File({ filename: "error.log", dirname: "logs", level: "error" }),
        new transports.File({ filename: "combined.log", dirname: "logs" }),
        new transports.Console({
            format: format.combine(
                format.colorize(),
                format.align(),
                format.splat(),
                format.printf(
                    ({ timestamp, level, message }) =>
                        chalk.gray("[") +
                        chalk.magenta(timestamp) +
                        chalk.gray("] [") +
                        level +
                        chalk.gray("] ") +
                        chalk.white(message)
                )
            )
        })
    ]
})

export default logger
