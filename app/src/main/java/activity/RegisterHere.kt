package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.s.b.s.ideapad.food_delivery_app.R
import model.LoginInfo
import org.json.JSONObject

class RegisterHere : AppCompatActivity() {
    lateinit var rhName:EditText
    lateinit var rhEmail:EditText
    lateinit var rhMobileNo:EditText
    lateinit var rhAddress:EditText
    lateinit var rhNewPass:EditText
    lateinit var rhCnfPass:EditText
    lateinit var btnRH:Button
    lateinit var toolBar:Toolbar
    lateinit var sharePrefLogiDAta:SharedPreferences
    var list= arrayListOf<LoginInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_here)
        sharePrefLogiDAta = getSharedPreferences(getString(R.string.Login_DAta), Context.MODE_PRIVATE)
        rhAddress=findViewById(R.id.etRHDelivery)
        rhName=findViewById(R.id.etRHName)
        rhEmail=findViewById(R.id.etRHEmail)
        rhMobileNo=findViewById(R.id.etRHMobileNo)
        rhNewPass=findViewById(R.id.etRHNewPass)
        rhCnfPass=findViewById(R.id.etRHCnfPass)
        btnRH=findViewById(R.id.btnRHRegister)
       toolBar=findViewById(R.id.rhToolbar)
        setUpToolbar()

        btnRH.setOnClickListener {
            Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show()
            val name=rhName.text.toString()
            val email=rhEmail.text.toString()
            val mobileNo=rhMobileNo.text.toString()
            val address=rhAddress.text.toString()
            val newPass=rhNewPass.text.toString()
            val cnfPass=rhCnfPass.text.toString()
            //checking password
            if (newPass==cnfPass) {
                // json request
                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/register/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("name", name)
                jsonParams.put("mobile_number", mobileNo)
                jsonParams.put("password",newPass)
                jsonParams.put("address",address)
                jsonParams.put("email",email)
                val jsonObjectRequest=object:JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                    val data=it.getJSONObject("data")
                    val success=data.getBoolean("success")
                    println("Response is $data")
                    if (success){
                        val data1=data.getJSONObject("data")
                        val returnInfo=LoginInfo(
                                data1.getString("user_id"),
                                data1.getString("name"),
                                data1.getString("email"),
                                data1.getString("mobile_number"),
                                data1.getString("address")
                        )
                        list.add(returnInfo)

                        val userId=returnInfo.userid
                        val email1=returnInfo.email
                        val mobile=returnInfo.mobilenumber
                        val address1=returnInfo.address
                        val name1=returnInfo.name

                        savePreferences(list)
                        val intent=Intent(this,AllRestaurantPage::class.java)
                        startActivity(intent)
                        finish()
                        
                        
                        
                    }
                    else{val message=data.getString("errorMessage")
                        Toast.makeText(this, message,Toast.LENGTH_SHORT).show()
                    }

                },Response.ErrorListener {
                    Toast.makeText(this,"Some Error Occured",Toast.LENGTH_SHORT).show()

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
            else{

                Toast.makeText(this,"Password Doesn't Match",Toast.LENGTH_SHORT).show()
            }

        }

    }
    fun setUpToolbar() {

        setSupportActionBar(toolBar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun savePreferences(list: ArrayList<LoginInfo>)
    {
        sharePrefLogiDAta.edit().putBoolean("isloggedin", true).apply()
        sharePrefLogiDAta.edit().putString("user_id", list[0].userid).apply()
        sharePrefLogiDAta.edit().putString("name",list[0].name).apply()
        sharePrefLogiDAta.edit().putString("email",list[0].email).apply()
        sharePrefLogiDAta.edit().putString("address",list[0].address).apply()
        sharePrefLogiDAta.edit().putString("mobileNo",list[0].mobilenumber ).apply()


    }
}
