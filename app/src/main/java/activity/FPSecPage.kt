package activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.s.b.s.ideapad.food_delivery_app.R
import org.json.JSONObject
import java.util.concurrent.TimeoutException

class FPSecPage : AppCompatActivity() {
    lateinit var otp:TextView
    lateinit var newPass:TextView
    lateinit var cnfPass:TextView
    lateinit var btnSubmit:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_f_p_sec_page)

        otp=findViewById(R.id.etOTP)
        newPass=findViewById(R.id.etNewPass)
        cnfPass=findViewById(R.id.etCnfPass)
        btnSubmit=findViewById(R.id.btnSubmit)
        val mobileNo=intent.getStringExtra("mobileNo")


        btnSubmit.setOnClickListener {
            var getnewPass = newPass.text.toString()
            var getCnfPass = cnfPass.text.toString()
            var getOTP=otp.text.toString()

            if (getCnfPass==getnewPass) {
                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", mobileNo)
                jsonParams.put("password", getnewPass)
                jsonParams.put("otp", getOTP)
                println("Response is $mobileNo,$getCnfPass,$getOTP,$getnewPass")
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    val data = it.getJSONObject("data")
                    val check = data.getBoolean("success")
                    println("Response is $data")
                    if (check) {
                        Toast.makeText(this, "Password Changed", Toast.LENGTH_LONG).show()
                        val sharedPreferences = getSharedPreferences(getString(R.string.Login_DAta), Context.MODE_PRIVATE).edit().clear().apply()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)



                    } else {
                        Toast.makeText(this, "Some Error Occured", Toast.LENGTH_LONG).show()
                        finishAffinity()


                    }


                }, Response.ErrorListener {

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "c1635bc1e3000c"
                        return headers
                    }

                }
                queue.add(jsonObjectRequest)


            } else {
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_LONG).show()
            }

        }
    }
}
