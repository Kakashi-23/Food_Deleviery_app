package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.s.b.s.ideapad.food_delivery_app.R
import model.LoginInfo
import org.json.JSONObject
import kotlin.collections.HashMap

public class Login : AppCompatActivity() {
    lateinit var mobileNo: EditText
    lateinit var password: EditText
    lateinit var loginBtn: Button
    lateinit var forgotPassword: Button
    lateinit var registerHere: Button
    lateinit var toolBar: androidx.appcompat.widget.Toolbar

    lateinit var sharePrefLogiDAta: SharedPreferences





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePrefLogiDAta = getSharedPreferences(getString(R.string.Login_DAta), Context.MODE_PRIVATE)
        val isLoggedIn=sharePrefLogiDAta.getBoolean("isloggedin",false)

        setContentView(R.layout.activity_login)
        if (isLoggedIn){
            val intent=Intent(this,AllRestaurantPage::class.java)
            startActivity(intent)
            finish()
        }

        mobileNo = findViewById<EditText>(R.id.etloginMobileNo)
        password = findViewById(R.id.etloginPassword)
        toolBar = findViewById(R.id.loginToolbar)
        forgotPassword = findViewById(R.id.loginForgotPassword)
        registerHere = findViewById(R.id.loginregister)
        loginBtn = findViewById(R.id.btnLogin)
        var userId:String
        setUpToolbar()

        //posting request



        loginBtn.setOnClickListener {
            val getMobileNo = mobileNo.text.toString()
            val getPassword = password.text.toString()
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/login/fetch_result/"
            val jsonparams = JSONObject()
            jsonparams.put("mobile_number",getMobileNo)
            jsonparams.put("password",getPassword)

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url,jsonparams,Response.Listener {
                val loginJsonObject= it.getJSONObject("data")
                val success=loginJsonObject.getBoolean("success")

                if (success) {

                    val loginJsonObject1=loginJsonObject.getJSONObject("data")
                    val loginObject = LoginInfo(
                            loginJsonObject1.getString("user_id"),
                            loginJsonObject1.getString("name"),
                            loginJsonObject1.getString("email"),
                            loginJsonObject1.getString("mobile_number"),
                            loginJsonObject1.getString("address")
                    )
                    userId=loginObject.userid
                    val email=loginObject.email

                    val address=loginObject.address
                    val name=loginObject.name

                    savePreferences(userId,email,address,getMobileNo,name)

                    val intent=Intent(this,AllRestaurantPage::class.java)
                    startActivity(intent)
                    finish()




                }
                else {Toast.makeText(this,"Data not found!! login again",Toast.LENGTH_SHORT).show()
                mobileNo.text.clear()
                password.text.clear()}

            },
                    Response.ErrorListener {
                        Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                    }


            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="c1635bc1e3000c"
                    return headers
                }




            }
            queue.add(jsonObjectRequest)

        }
// forgot password
        forgotPassword.setOnClickListener {
            val intent=Intent(this,ForgotPassword::class.java)
            finish()
            startActivity(intent)
        }
        // register here
        registerHere.setOnClickListener {
            val intent=Intent(this,RegisterHere::class.java)
            startActivity(intent)
        }

    }

    fun setUpToolbar() {

        setSupportActionBar(toolBar)
        supportActionBar?.title = "Login"
    }

    fun savePreferences(userid:String,email:String,address:String,mobileNo:String,name:String)
    {
        sharePrefLogiDAta.edit().putBoolean("isloggedin", true).apply()
        sharePrefLogiDAta.edit().putString("user_id",userid).apply()
        sharePrefLogiDAta.edit().putString("name",name).apply()
        sharePrefLogiDAta.edit().putString("email",email).apply()
        sharePrefLogiDAta.edit().putString("address",address).apply()
        sharePrefLogiDAta.edit().putString("mobileNo",mobileNo).apply()


    }

    override fun onBackPressed() {
        finish()
    }


}
