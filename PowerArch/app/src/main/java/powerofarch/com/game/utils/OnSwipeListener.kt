package powerofarch.com.game.utils

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View
import powerofarch.com.game.model.SwipeConst
import powerofarch.com.game.view.RowActivity
import kotlin.math.abs


open class OnSwipeListener(rowActivity: RowActivity) : View.OnTouchListener {

    private var gestureDetector: GestureDetector

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    init {
        gestureDetector = GestureDetector(rowActivity, OnSwipeListener())
    }

    private inner class OnSwipeListener : SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent,
            velocityX: Float, velocityY: Float): Boolean {
            var result = false
            val yDiff = e2.y - e1.y
            val xDiff = e2.x - e1.x

            if (abs(xDiff) > abs(yDiff)) {
                if (abs(xDiff) > SwipeConst.SWIPE_THRESHOLD && abs(velocityX) > SwipeConst.SWIPE_VELOCITY_THRESHOLD) {
                    if (xDiff > 0) {
                        OnSwipeRight()
                    } else {
                        OnSwipeLeft()
                    }
                    result = true
                }
            } else if (abs(yDiff) > SwipeConst.SWIPE_THRESHOLD && abs(velocityY) > SwipeConst.SWIPE_VELOCITY_THRESHOLD) {
                if (yDiff > 0) {
                    OnSwipeBottom()
                } else {
                    OnSwipeTop()
                }
                result = true
            }
            return result
        }


    }

    open fun OnSwipeLeft() {

    }
    open fun OnSwipeRight() {

    }
    open fun OnSwipeTop() {

    }
    open fun OnSwipeBottom() {

    }
}