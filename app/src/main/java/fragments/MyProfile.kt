package fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.s.b.s.ideapad.food_delivery_app.R


public  class MyProfile :Fragment() {
    lateinit var name:TextView
     lateinit var mobileNo:TextView
     lateinit var email:TextView
     lateinit var address:TextView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         val view=inflater.inflate(R.layout.fragment_my_profile, container, false)


        name=view.findViewById(R.id.userName)
         address=view.findViewById(R.id.userAddress)
         mobileNo=view.findViewById(R.id.userMobileNo)
         email=view.findViewById(R.id.userEmail)
        val get= activity?.getSharedPreferences(getString(R.string.Login_DAta),Context.MODE_PRIVATE)


        name.text=get?.getString("name",null)
         address.text=get?.getString("address",null)
         mobileNo.text=get?.getString("mobileNo",null)
         email.text=get?.getString("email",null)
         return view
    }




}
