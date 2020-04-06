package com.enciyo.smsretrieverap

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.lang.Exception

internal class SmsVerificationCodeReceiver : BroadcastReceiver() {

    var listener: SmsVerificationCodeReceiverListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action==SmsRetriever.SMS_RETRIEVED_ACTION) {
            intent.extras?.let { extras ->
                (extras.get(SmsRetriever.EXTRA_STATUS) as? Status)?.let { status ->
                    when (status.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT).also {
                                listener?.onSuccess(it)
                            }
                        }
                        else -> listener?.onFailure(Exception(status.statusMessage))
                    }
                }
            }
        }
    }


    interface SmsVerificationCodeReceiverListener {
        fun onSuccess(it: Intent?)
        fun onFailure(throwable: Exception?)
    }


}