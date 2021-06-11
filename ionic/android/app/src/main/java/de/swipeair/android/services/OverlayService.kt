package de.swipeair.android.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.*
import android.view.MotionEvent.*

private const val TAG = "SwipeAir-Overlay"
private const val USE_EDGE_SWIPE = true

class OverlayService : Service(), View.OnTouchListener {
    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()

        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val view = View(this)
        view.setOnTouchListener(this)
//        view.setBackgroundColor(Color.GREEN)

        val topLeftParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.START or Gravity.BOTTOM
            x = 0
            y = 0

            if (USE_EDGE_SWIPE) width = 50
            else height = 50
        }

        wm.addView(view, topLeftParams)
        Log.i(TAG, "Created overlay!")
    }

    private fun fire() {
        Log.i(TAG, "Fired gesture")
        ExtractorService.instance?.gestureFired()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        val function = if (USE_EDGE_SWIPE) ::onTouchEdgeSwipe else ::onTouchBottomSwipe
        function(event)
        return false
    }

    // 0 = none             -> 1
    // 1 = down             -> 2, 0
    // 2 = pointer down     -> 3, 0
    // 3 = pointer up       -> 4, 0
    //     up               -> fire, 0
    private var swipeState = 0

    private fun onTouchBottomSwipe(event: MotionEvent) {
        swipeState = when (event.action) {
            ACTION_DOWN -> if (swipeState == 0) 1 else 0
            ACTION_POINTER_DOWN, ACTION_POINTER_1_DOWN,
            ACTION_POINTER_2_DOWN, ACTION_POINTER_3_DOWN -> if (swipeState == 1) 2 else 0
            ACTION_POINTER_UP, ACTION_POINTER_1_UP,
            ACTION_POINTER_2_UP, ACTION_POINTER_3_UP -> if (swipeState == 2) 3 else 0
            ACTION_UP -> {
                if (swipeState == 3 && event.y <= -400) fire()
                0
            }
            else -> return
        }
    }

    private var swipeStart: Float? = null

    private fun onTouchEdgeSwipe(event: MotionEvent) {
        swipeStart = when (event.action) {
            ACTION_DOWN -> event.y
            ACTION_UP -> {
                if (swipeStart != null && event.y <= swipeStart!! - 300F) fire()
                null
            }
            else -> return
        }
    }
}
