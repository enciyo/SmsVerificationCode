package com.enciyo.smsretrieverap

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever


object SmsVerificationCodeResultHelper {

    fun getMessage(requestCode: Int, resultCode: Int, data: Intent?): String? {
        return when (requestCode) {
            SmsVerificationCodeReceiverManagementImp.KEY_SMS_CODE_VERIFICATION_RESULT -> {
                if (resultCode==Activity.RESULT_OK && data!=null) {
                    data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)?.let {
                        return it
                    }
                } else {
                    null
                }
            }
            else -> null
        }
    }
}