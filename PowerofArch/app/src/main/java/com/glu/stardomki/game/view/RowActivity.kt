package com.glu.stardomki.game.view

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.content.getSystemService
import com.glu.stardomki.R
import com.glu.stardomki.game.utils.OnSwipeListener
import java.util.*
import kotlin.math.floor

class RowActivity : AppCompatActivity() {

    private var itemsRes = intArrayOf(
        R.drawable.coin_1,
        R.drawable.coin_2,
        R.drawable.coin_3,
        R.drawable.coin_4,
        R.drawable.coin_5,
        R.drawable.coin_6,
    )

    var noOfBlock = 8
    private var widthOfBlock = 0
    private var widthOfScreen = 0
    var itemToBeDragged = 0
    var itemToBeReplaced = 0

    // In ms
    private var interval = 100
    private var score = 0

    private var itemsVi = ArrayList<ImageView>()
    private var notCandy = R.drawable.ic_nothing

    var mHandler: Handler? = null
    private var scoreResult: TextView? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raw)

        scoreResult = findViewById(R.id.score)

        // Android 10+
        val displayMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            getSystemService<WindowManager>()?.defaultDisplay?.getMetrics(displayMetrics)
        } else {
            // Android <10
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
        }

        widthOfScreen = displayMetrics.widthPixels
        displayMetrics.heightPixels

        widthOfBlock = widthOfScreen / noOfBlock
        createBoard()

        for (imageView in itemsVi) {
            imageView.setOnTouchListener(object : OnSwipeListener(this@RowActivity) {
                override fun OnSwipeLeft() {
                    super.OnSwipeLeft()
                    itemToBeDragged = imageView.id
                    itemToBeReplaced = itemToBeDragged - 1
                    candyInterChange()
                }

                override fun OnSwipeRight() {
                    super.OnSwipeRight()
                    itemToBeDragged = imageView.id
                    itemToBeReplaced = itemToBeDragged + 1
                    candyInterChange()
                }

                override fun OnSwipeTop() {
                    super.OnSwipeTop()
                    itemToBeDragged = imageView.id
                    itemToBeReplaced = itemToBeDragged - noOfBlock
                    candyInterChange()
                }

                override fun OnSwipeBottom() {
                    super.OnSwipeBottom()
                    itemToBeDragged = imageView.id
                    itemToBeReplaced = itemToBeDragged + noOfBlock
                    candyInterChange()
                }
            })
        }

        mHandler = Handler(Looper.getMainLooper())
        startRepeat()
    }

    private fun checkRowThree() {
        for (i in 0..61) {
            val choseItem = itemsVi[i].tag as Int
            val isBlank = itemsVi[i].tag as Int == notCandy
            val notValid = arrayOf(6, 7, 14, 15, 22, 23, 30, 31, 39, 46, 47, 54, 55)
            val list = listOf(*notValid)

            if (!list.contains(i)) {
                var x = i
                if (itemsVi[x++].tag as Int == choseItem && !isBlank && itemsVi[x++].tag as Int == choseItem && itemsVi[x].tag as Int == choseItem) {
                    score += 3
                    scoreResult!!.text = score.toString()
                    itemsVi[x].setImageResource(notCandy)
                    itemsVi[x].tag = notCandy
                    x--
                    itemsVi[x].setImageResource(notCandy)
                    itemsVi[x].tag = notCandy
                    x--
                    itemsVi[x].setImageResource(notCandy)
                    itemsVi[x].tag = notCandy
                }
            }
        }
        moveDownItems()
    }

    private fun checkColumnThree() {
        for (i in 0..46) {
            val choseItem = itemsVi[i].tag as Int
            val isBlank = itemsVi[i].tag as Int == notCandy
            var x = i
            if (itemsVi[x].tag as Int == choseItem && !isBlank && itemsVi[x + noOfBlock].tag as Int == choseItem && itemsVi[x + 2 * noOfBlock].tag as Int == choseItem) {
                score += 3
                scoreResult!!.text = score.toString()
                itemsVi[x].setImageResource(notCandy)
                itemsVi[x].tag = notCandy
                x += noOfBlock
                itemsVi[x].setImageResource(notCandy)
                itemsVi[x].tag = notCandy
                x += noOfBlock
                itemsVi[x].setImageResource(notCandy)
                itemsVi[x].tag = notCandy
            }
        }
        moveDownItems()
    }

    private fun moveDownItems() {
        val firstRow = arrayOf(0, 1, 2, 3, 4, 5, 6, 7)
        val list = listOf(*firstRow)
        for (i in 55 downTo 0) {
            if (itemsVi[i + noOfBlock].tag as Int == notCandy) {
                itemsVi[i + noOfBlock].setImageResource(itemsVi[i].tag as Int)
                itemsVi[i + noOfBlock].tag = itemsVi[i].tag
                itemsVi[i].setImageResource(notCandy)
                itemsVi[i].tag = notCandy
                if (list.contains(i) && itemsVi[i].tag as Int == notCandy) {
                    val randomColor = floor(Math.random() * itemsRes.size).toInt()
                    itemsVi[i].setImageResource(itemsRes[randomColor])
                    itemsVi[i].tag = itemsRes[randomColor]
                }
            }
        }
        for (i in 0..7) {
            if (itemsVi[i].tag as Int == notCandy) {
                val randomColor = floor(Math.random() * itemsRes.size).toInt()
                itemsVi[i].setImageResource(itemsRes[randomColor])
                itemsVi[i].tag = itemsRes[randomColor]
            }
        }
    }

    private var repeatChecker: Runnable = object:Runnable {
        override fun run() {
            try {
                checkRowThree()
                checkColumnThree()
                moveDownItems()
            } finally {
                mHandler!!.postDelayed(this, interval.toLong())
            }
        }
    }

    private fun startRepeat() {
        repeatChecker.run()
    }

    private fun candyInterChange() {
        val background = itemsVi[itemToBeReplaced].tag as Int
        val background1 = itemsVi[itemToBeDragged].tag as Int

        itemsVi[itemToBeDragged].setImageResource(background)
        itemsVi[itemToBeReplaced].setImageResource(background1)

        itemsVi[itemToBeDragged].tag = background
        itemsVi[itemToBeReplaced].tag = background1
    }

    private fun createBoard() {
        val gridLayout = findViewById<GridLayout>(R.id.fieldItems)

        gridLayout.rowCount = noOfBlock
        gridLayout.columnCount = noOfBlock

        gridLayout.layoutParams.width = widthOfScreen
        gridLayout.layoutParams.height = widthOfScreen

        for (i in 0 until noOfBlock * noOfBlock) {
            val imageView = ImageView(this)
            imageView.id = i
            imageView.layoutParams = ViewGroup.LayoutParams(widthOfBlock, widthOfBlock)

            imageView.maxHeight = widthOfBlock
            imageView.maxWidth = widthOfBlock
            val randomCandy = floor(Math.random() * itemsRes.size).toInt()

            imageView.setImageResource(itemsRes[randomCandy])
            imageView.tag = itemsRes[randomCandy]

            itemsVi.add(imageView)
            gridLayout.addView(imageView)
        }
    }
}