package de.swipeair.android.client

data class YouTubeVideoPacket(val title: String, val channel: String, val timestamp: String) :
    Packet("share youtube video", title, channel, timestamp)
