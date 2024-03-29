package com.amod2android.weatherapplication.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.amod2android.weatherapplication.R
import com.amod2android.weatherapplication.databinding.FragmentSlideshowBinding
import com.amod2android.weatherapplication.ui.VerifyOtpActivity

class SlideshowFragment : Fragment() {
    lateinit var bingding:FragmentSlideshowBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View{
        bingding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideshow, container, false)
       firebaseAuth= FirebaseAuth.getInstance()

        checkUser()

        bingding.buttonLogot.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        return bingding.root
    }

    private fun checkUser() {
        val firebaseUser=firebaseAuth.currentUser
        if (firebaseUser!=null){
            startActivity(Intent(requireActivity(), VerifyOtpActivity::class.java))
            requireActivity().finish()
        }else{
            val phone=firebaseUser?.phoneNumber
            bingding.tvPhone.text=phone
        }
    }
}