package com.enciyo.smsretrieverap

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.lang.ref.WeakReference


internal class SmsVerificationCodeReceiverManagementImp(
    private var mActivity: WeakReference<Activity>,
    mLifecycle: LifecycleOwner
) : SmsVerificationCodeReceiverManagement, LifecycleObserver {

    companion object {
        const val KEY_SMS_CODE_VERIFICATION_RESULT = 23
    }


    var mOnSmsCodeReceiverListener: SmsVerificationCodeReceiverManagement.OnSmsCodeReceiverListener? = null


    private val mSmsVerificationCodeReceiverListener by lazy {
        WeakReference(object : SmsVerificationCodeReceiver.SmsVerificationCodeReceiverListener {
            override fun onSuccess(it: Intent?) {
                it?.let { mActivity.get()?.startActivityForResult(it, KEY_SMS_CODE_VERIFICATION_RESULT) }
            }

            override fun onFailure(throwable: Exception?) {
                mOnSmsCodeReceiverListener?.onFailure(throwable)
            }
        })
    }


    private val mSmsVerificationCodeReceiver by lazy {
        SmsVerificationCodeReceiver().also {
            it.listener = mSmsVerificationCodeReceiverListener.get()
        }
    }

    override fun init() {
        SmsRetriever.getClient(mActivity.get() ?: throw NullPointerException("Not found activity!")).also {
            it.startSmsUserConsent(null)
                .addOnSuccessListener {
                    Log.d("MyLogger", "SmsRetriever on success listener")
                }
                .addOnFailureListener { exception ->
                    mOnSmsCodeReceiverListener?.onFailure(exception)
                }

        }
    }


    init {
        mLifecycle.lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerSmsVerificationCodeReceiver() {
        IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION).also {
            mActivity.get()?.registerReceiver(mSmsVerificationCodeReceiver, it)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        SmsVerificationCodeResultHelper.getMessage(requestCode, resultCode, data).also {
            if (it!=null)
                mOnSmsCodeReceiverListener?.onSuccess(it)
            else
                mOnSmsCodeReceiverListener?.onFailure(Exception("Message is null"))
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clearListenersAndReceivers() {
        mActivity.get()?.unregisterReceiver(mSmsVerificationCodeReceiver)
        mSmsVerificationCodeReceiver.listener = null
        mSmsVerificationCodeReceiverListener.clear()
    }


}