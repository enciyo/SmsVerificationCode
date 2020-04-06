package com.enciyo.smsretrieverap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.enciyo.smsretrieverap.SmsVerificationCodeReceiverManagement.OnSmsCodeReceiverListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {


    //Uses-1
    private val mSmsVerificationCodeReceiverManagement by lazy {
        SmsVerificationCodeBuilder(this)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
            .build(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Uses-1
        mSmsVerificationCodeReceiverManagement.init()

        //Uses-2
        //    SmsVerificationCodeBuilder(this)
        //    .build(this)
        //    .init()
        ////


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Uses-1
        mSmsVerificationCodeReceiverManagement.onActivityResult(requestCode, resultCode, data)
        //Uses-2
        //  val message = SmsVerificationCodeResultHelper.getMessage(requestCode,resultCode, data)
        //  textView.text = message
        //
        super.onActivityResult(requestCode, resultCode, data)
    }

}
