package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.s.b.s.ideapad.food_delivery_app.R
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {
    lateinit var mobileNo:TextView
    lateinit var email:TextView
    lateinit var nextBtn:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        mobileNo=findViewById(R.id.etFPMobileNo)
        email=findViewById(R.id.etFPEmail)
        nextBtn=findViewById(R.id.btnFPNext)


        nextBtn.setOnClickListener {
            val getMobileNO=mobileNo.text.toString()
            val getEmail=email.text.toString()
            val queue=Volley.newRequestQueue(this)
            val url="http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonParams=JSONObject()
            jsonParams.put("mobile_number",getMobileNO)
            jsonParams.put("email",getEmail)
            val jsonObjectRequest=object:JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                val dataObject=it.getJSONObject("data")
                val success=dataObject.getBoolean("success")
                if (success){
                    when(dataObject.getBoolean("first_try")){
                        true-> Toast.makeText(this,"OTP is sent to your registered Email",Toast.LENGTH_SHORT).show()
                        false->Toast.makeText(this,"OTP was sent to your registered Email",Toast.LENGTH_SHORT).show()
                    }
                    val intent=Intent(this,FPSecPage::class.java)
                    intent.putExtra("mobileNo", getMobileNO)
                    startActivity(intent)
                    finishAffinity()


                }
                else{
                    Toast.makeText(this,"Enter Correct Details",Toast.LENGTH_SHORT).show()


                }

            },Response.ErrorListener {

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="c1635bc1e3000c"
                    return headers
                }

            }
            queue.add(jsonObjectRequest)
        }

    }

    override fun onBackPressed() {
        finish()
    }

}
