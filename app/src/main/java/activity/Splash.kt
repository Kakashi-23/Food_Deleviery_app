package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.s.b.s.ideapad.food_delivery_app.R

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title="Food Delivery"
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(
                {
                    val intent=Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                },1000)

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

