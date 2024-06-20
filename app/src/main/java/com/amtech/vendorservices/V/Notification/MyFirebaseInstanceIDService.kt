package com.amtech.vendorservices.V.Notification

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    lateinit var name: Array<String>
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val token = FirebaseInstanceId.getInstance().token
        println("my firebase token $token")
    }

    private fun sendRegistrationToServer(token: String) {

    }
    companion object {
        private const val TAG = "FirebaseIIDServiceDemo"
    }
}