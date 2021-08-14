package de.swipeair.android.client

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

private const val TAG = "SwipeAir-Client"

object Client {
    private lateinit var socket: Socket

    fun initialize() {
        try {
            /*socket = IO.socket("http://10.0.2.2:5785").connect()*/ // Android Emulator
            socket = IO.socket("http://192.168.178.66:5785").connect() // Internal Ip-Address of pc
            Log.i(TAG, "initialize: Connected to websocket")
        } catch (e: Exception) {
            Log.e(TAG, "initialize: Failed to connect to websocket", e)
        }
    }

    fun interrupt() {
        socket.close()
    }

    fun emit(packet: Packet) {
        Log.i(TAG, "emit: Emitting $packet")
        socket.emit(packet.event, *packet.arguments)
    }
}
