import { google } from "googleapis"
import open from "open"
import logger from "../utils/logger"

export const youtube = google.youtube({
    version: "v3",
    auth: process.env.YOUTUBE_API_KEY
})

export async function search(snapshot: {
    title: string
    channel: string
    timestamp: string
}) {
    const response = await youtube.search.list({
        q: `${snapshot.title} ${snapshot.channel}`,
        part: ["snippet"],
        type: ["video"]
    })

    const firstItem = response.data.items?.find(
        item =>
            item.snippet?.title == snapshot.title &&
            item.snippet?.channelTitle == snapshot.channel
    )

    if (firstItem != null) {
        const timestamp = parseTimestamp(snapshot.timestamp)
        const link = `https://youtube.com/watch?v=${firstItem.id!.videoId}&t=${timestamp}`
        logger.info("Generated video link %s", link)
        await open(link)
    } else {
        logger.warn("Couldn't find a video in the first 5 results:")
        logger.warn(JSON.stringify(response.data.items))
    }
}

function parseTimestamp(input: string): number {
    try {
        const split = input.split(":")
        const hours = split.length == 3 ? parseInt(split[0]) : 0
        const minutes = parseInt(split[split.length - 2])
        const seconds = parseInt(split[split.length - 1])
        return hours * 60 * 60 + minutes * 60 + seconds
    } catch (e) {
        return 0
    }
}
