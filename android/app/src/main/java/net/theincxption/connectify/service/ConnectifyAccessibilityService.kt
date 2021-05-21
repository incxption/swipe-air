package net.theincxption.connectify.service

import android.accessibilityservice.*
import android.graphics.Path
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import net.theincxption.connectify.client.YouTubeVideoPacket
import net.theincxption.connectify.client.ConnectifyClient

private const val TAG = "ConnectifyExtractor"

class ConnectifyAccessibilityService : AccessibilityService() {

    companion object {
        var instance: ConnectifyAccessibilityService? = null
            private set
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
    }

    override fun onInterrupt() {
        Log.e(TAG, "Interrupted!")
        ConnectifyClient.interrupt()
    }

    override fun onCreate() {
        Log.i(TAG, "Created accessibility service!")
        instance = this
        ConnectifyClient.initialize()
    }

    fun gestureFired() {
        Log.i(TAG, "Received gesture fired")
        extractVideoData { video ->
            if (video != null) {
                Toast.makeText(this, video.toString(), Toast.LENGTH_LONG).show()
                ConnectifyClient.emit(video)
            }
        }
    }

    private fun extractVideoData(callback: (YouTubeVideoPacket?) -> Unit) {
        val title = findTitle()
        val timestamp = findTimestamp()

        findChannel { channel ->
            if (title != null && timestamp != null && channel != null) {
                callback(YouTubeVideoPacket(title.toString(), channel.toString(), timestamp.toString()))
            } else {
                Toast.makeText(this, "Couldn't extract video data!", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Failed to extract video data!")
                Log.e(TAG, "title = $title")
                Log.e(TAG, "channel = $channel")
                Log.e(TAG, "timestamp = $timestamp")
                callback(null)
            }
        }
    }

    private fun findTitle(): CharSequence? {
        val titleNode = id("title").getOrNull(0)
        if (titleNode != null) {
            return titleNode.text.also { titleNode.recycle() }
        }

        val playerTitleNode = id("player_video_title_view").getOrNull(0)
        if (playerTitleNode != null) {
            return playerTitleNode.text.also { playerTitleNode.recycle() }
        }

        // click on watch player and try again
        id("watch_player").getOrNull(0)?.run {
            performAction(AccessibilityNodeInfo.ACTION_CLICK)
            recycle()
        }

        return id("player_video_title_view").getOrNull(0)?.let { node ->
            node.text.also { node.recycle() }
        }
    }

    private fun findChannel(callback: (CharSequence?) -> Unit) {
        val channelNode = id("channel_navigation_container").find { it.contentDescription != null }
        if (channelNode != null) {
            return callback(channelNode.contentDescription).also { channelNode.recycle() }
        }

        val gestureBuilder = GestureDescription.Builder().apply {
            val path = Path().apply {
                val height = resources.displayMetrics.heightPixels
                moveTo(100F, height - 300F)
                lineTo(100F, height - 1000F)
            }
            addStroke(GestureDescription.StrokeDescription(path, 100, 100))
        }

        dispatchGesture(gestureBuilder.build(), object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                val channel = id("channel_title")?.getOrNull(0)?.let { node ->
                    node.text.also { node.recycle() }
                }
                callback(channel)
            }
        }, null)
    }

    private fun findTimestamp(): CharSequence? {
        val timestampNode = id("time_bar_current_time").getOrNull(0)

        if (timestampNode != null) {
            return timestampNode.text.also { timestampNode.recycle() }
        }

        // click on watch player and try again
        id("watch_player").getOrNull(0)?.run {
            performAction(AccessibilityNodeInfo.ACTION_CLICK)
            recycle()
        }
        return id("time_bar_current_time").getOrNull(0)?.let { node ->
            node.text.also { node.recycle() }
        }
    }

    private fun id(id: String) = rootInActiveWindow.findAccessibilityNodeInfosByViewId(
        "com.google.android.youtube:id/$id")
}