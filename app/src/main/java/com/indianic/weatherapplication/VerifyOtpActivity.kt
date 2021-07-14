package com.indianic.weatherapplication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.indianic.weatherapplication.databinding.ActivityVerifyOtpBinding
import java.util.concurrent.TimeUnit

class VerifyOtpActivity : AppCompatActivity() {


    private lateinit var binding : ActivityVerifyOtpBinding
    private  var forcedForceResendingToken: PhoneAuthProvider.ForceResendingToken?=null
    private lateinit var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private  var mVerificationId: String?=null
    private  val TAG="VerifyOtpActivity"
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit  var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.phoneLl.visibility=View.VISIBLE
        binding.codeLl.visibility=View.GONE

        firebaseAuth= FirebaseAuth.getInstance()


        mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                signInWithPhoneAuthCredentials(phoneAuthCredential)

            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerifyOtpActivity,e.message,Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                mVerificationId=verificationId
                binding.phoneLl.visibility=View.GONE
                binding.codeLl.visibility=View.VISIBLE
                Toast.makeText(this@VerifyOtpActivity,"Verification code sent....",Toast.LENGTH_SHORT).show()
            }

        }

        binding.phoneContinueBtn.setOnClickListener{
            val phone =binding.phoneEt.text.toString().trim()
            if (TextUtils.isEmpty(phone)){
                Toast.makeText(this@VerifyOtpActivity,"Please enter phone numvber...",Toast.LENGTH_SHORT).show()

            }else{
                startPhoneNumberVerification(phone)
            }

        }
        binding.codeSubmitBtn.setOnClickListener{
            val code =binding.codeEt.text.toString().trim()
            if (TextUtils.isEmpty(code)){
                Toast.makeText(this@VerifyOtpActivity,"Please enter OTP...",Toast.LENGTH_SHORT).show()

            }else{
                mVerificationId?.let { it1 -> verifyPhoneNumberCode(it1,code) }
            }
        }


    }

    private fun startPhoneNumberVerification(phone : String){
        val option = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallBacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    private fun verifyPhoneNumberCode(verificationId : String,code : String){
        val credential=PhoneAuthProvider.getCredential(verificationId,code)
        signInWithPhoneAuthCredentials(credential)
    }

    private fun signInWithPhoneAuthCredentials(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                //Success
                val phone= firebaseAuth.currentUser?.phoneNumber
                Toast.makeText(this@VerifyOtpActivity,"Mobile No $phone in verified",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@VerifyOtpActivity,DashBoardActivity::class.java))
                finish()
            }
            .addOnFailureListener{
                //Failure
                Toast.makeText(this@VerifyOtpActivity,"Something went wrong...",Toast.LENGTH_SHORT).show()
            }
    }
}