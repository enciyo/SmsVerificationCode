package com.enciyo.smsretrieverap

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

class SmsVerificationCodeBuilder(private val mActivity: Activity) {


    private var mOnSuccessListener: ((String?) -> Unit)? = null
    private var mOnFailedListener: ((Exception?) -> Unit)? = null


    fun addOnSuccessListener(message: (String?) -> Unit) = apply {
        mOnSuccessListener = message
    }

    fun addOnFailureListener(exception: (Exception?) -> Unit) = apply {
        mOnFailedListener = exception
    }

    fun build(mLifecycleOwner: LifecycleOwner): SmsVerificationCodeReceiverManagement {
        return SmsVerificationCodeReceiverManagementImp(
            WeakReference(mActivity),
            mLifecycleOwner
        ).also {
            it.mOnSmsCodeReceiverListener = object : SmsVerificationCodeReceiverManagement.OnSmsCodeReceiverListener {
                override fun onSuccess(message: String?) {
                    mOnSuccessListener?.invoke(message)
                }

                override fun onFailure(it: Exception?) {
                    mOnFailedListener?.invoke(it)
                }
            }
        }
    }

}