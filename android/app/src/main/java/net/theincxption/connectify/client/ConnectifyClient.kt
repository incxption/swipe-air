package net.theincxption.connectify.client

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

private const val TAG = "SwipeAirNetworking"

object ConnectifyClient {
    private lateinit var socket: Socket

    fun initialize() {
        try {
            socket = IO.socket("http://10.0.2.2:5785").connect()
            Log.i(TAG, "initialize: Connected to websocket")
        } catch (e: Exception) {
            Log.e(TAG, "initialize: Failed to connect to websocket", e)
        }
    }

    fun interrupt() {
        socket.close()
    }

    fun emit(packet: Packet) {
        socket.emit(packet.event, *packet.arguments)
    }
}