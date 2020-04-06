package com.enciyo.smsretrieverap

import android.content.Intent
import java.lang.Exception


interface SmsVerificationCodeReceiverManagement {

    fun init()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)


    interface OnSmsCodeReceiverListener {
        fun onSuccess(message: String?) {}
        fun onFailure(it: Exception?) {}
    }

}