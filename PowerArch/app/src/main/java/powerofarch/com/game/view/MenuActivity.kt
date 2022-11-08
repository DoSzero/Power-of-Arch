package powerofarch.com.game.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import powerofarch.com.R
import android.view.animation.AnimationUtils
import powerofarch.com.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {

            binding.imageView2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.am_smallbigforth))
            binding.imageView2.animate().alpha(0f).duration = 1700

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, RowActivity::class.java)
                startActivity(intent)
                finish()
            }, 1750)

        }

    }
}

