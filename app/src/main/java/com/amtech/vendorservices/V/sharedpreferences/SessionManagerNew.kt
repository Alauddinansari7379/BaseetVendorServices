//package com.amtech.vendorservices.V.sharedpreferences
//
//import android.content.Context
//import android.preference.PreferenceManager
//
//class SessionManagerNew(context: Context?) {
//    private val prefsNew = PreferenceManager.getDefaultSharedPreferences(context)
//
//    companion object {
//
//        private const val EMAIL = "emailNew"
//        private const val PASSWORD = "passwordNew"
//    }
//    var email: String?
//        get() = prefsNew.getString(SessionManagerNew.EMAIL, "")
//        set(email) {
//            prefsNew.edit().putString(SessionManagerNew.EMAIL, email).apply()
//        }
//
//    var password: String?
//        get() = prefsNew.getString(SessionManagerNew.PASSWORD, "")
//        set(password) {
//            prefsNew.edit().putString(SessionManagerNew.PASSWORD, password).apply()
//        }
//}