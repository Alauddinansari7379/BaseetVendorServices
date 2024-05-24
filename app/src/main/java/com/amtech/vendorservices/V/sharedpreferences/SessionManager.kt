package com.amtech.vendorservices.V.sharedpreferences

import android.content.Context
import android.preference.PreferenceManager

class SessionManager(context: Context?) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val prefsNew = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {

        private const val IS_LOGIN = "islogin"
        private const val BLOOD_GROUP="blood_group"
        private const val CREATED_AT="created_at"
        private const val CUSTOMER_NAME="customer_name"
        private const val LATITUDE="latitude"
        private const val LONGITUDE="longitude"
        private const val EMAIL="email"
        private const val FCM_TOKEN="fcm_token"
        private const val GENDER="gender"
        private const val USERID="userId"
        private const val USERTYPE="usertype"
        private const val GROUP="group"
        private const val HOSPITAL_ID="HOSPITALID"
        private const val ID_TOKEN="ID_TOKEN"
        private const val ION_ID="ION_ID"
        private const val OBJECTIVE_INFORMATION="objective_information"
        private const val ASSESSMENT="assessment"
        private const val DOCTOR_NOTE="doctor_notes"
        private const val TEST_NAME="test_name"
        private const val INSTRUCATIONS="instructions"
        private const val ID="id"
        private const val LAST_ACTIVE_ADDRESS="last_active_address"
        private const val NO_OF_RATINGS="no_of_ratings"
        private const val OVERALL_RATING="overall_ratings"
        private const val PASSWORD="password"
        private const val PHONE_NUMBER="phone_number"
        private const val PHONE_WITH_CODE="phone_with_code"
        private const val BASE_URL="phone_with_code"
        private const val PRE_EXISTING_DESEASE="pre_existing_desease"
        private const val PROFILE_PICTURE="profile_picture"
        private const val STATUS="status"
        private const val UPDATED_AT="updated_at"
        private const val WALLET="wallet"
        private const val BOOKING_TYPE="bookingType"
        private const val PRICE="pricing"
        private const val SELECTED_DATE="selected_date"
    }

    var isLogin: Boolean
        get() = prefs.getBoolean(IS_LOGIN, false)
        set(isLogin) {
            prefs.edit().putBoolean(IS_LOGIN, isLogin).apply()
        }

    fun logout() {
        prefs.edit().clear().apply()
    }

    var userId: String?
        get() = prefs.getString(USERID, "")
        set(userId) {
            prefs.edit().putString(USERID, userId).apply()
        }

    var usertype: String?
        get() = prefs.getString(USERTYPE, "")
        set(usertype) {
            prefs.edit().putString(USERTYPE, usertype).apply()
        }

    var group: String?
        get() = prefs.getString(GROUP, "")
        set(group) {
            prefs.edit().putString(GROUP, group).apply()
        }

    var hospitalId: String?
        get() = prefs.getString(HOSPITAL_ID, "")
        set(hospitalId) {
            prefs.edit().putString(HOSPITAL_ID, hospitalId).apply()
        }

    var idToken: String?
        get() = prefs.getString(ID_TOKEN, "")
        set(idToken) {
            prefs.edit().putString(ID_TOKEN, idToken).apply()
        }

    var imageURL: String?
        get() = prefs.getString(BASE_URL, "")
        set(baseURL) {
            prefs.edit().putString(BASE_URL, baseURL).apply()
        }

    var ionId: String?
        get() = prefs.getString(ION_ID, "")
        set(ionId) {
            prefs.edit().putString(ION_ID, ionId).apply()
        }

    var objectiveInformation: String?
        get() = prefs.getString(OBJECTIVE_INFORMATION, "")
        set(objectiveInformation) {
            prefs.edit().putString(OBJECTIVE_INFORMATION, objectiveInformation).apply()
        }
    var assessment: String?
        get() = prefs.getString(ASSESSMENT, "")
        set(assessment) {
            prefs.edit().putString(ASSESSMENT, assessment).apply()
        }
    var doctorNotes: String?
        get() = prefs.getString(DOCTOR_NOTE, "")
        set(doctorNotes) {
            prefs.edit().putString(DOCTOR_NOTE, doctorNotes).apply()
        }

    var instructions: String?
        get() = prefs.getString(INSTRUCATIONS, "")
        set(instructions) {
            prefs.edit().putString(INSTRUCATIONS, instructions).apply()
        }

    var testName: String?
        get() = prefs.getString(TEST_NAME, "")
        set(testName) {
            prefs.edit().putString(TEST_NAME, testName).apply()
        }

    var fcmToken: String?
        get() = prefs.getString(FCM_TOKEN, "")
        set(fcmToken) {
            prefs.edit().putString(FCM_TOKEN, fcmToken).apply()
        }
    var latitude: String?
        get() = prefs.getString(LATITUDE, "")
        set(latitude) {
            prefs.edit().putString(LATITUDE, latitude).apply()
        }
    var longitude: String?
        get() = prefs.getString(LONGITUDE, "")
        set(longitude) {
            prefs.edit().putString(LONGITUDE, longitude).apply()
        }
    var bookingType: String?
        get() = prefs.getString(BOOKING_TYPE, "")
        set(bookingType) {
            prefs.edit().putString(BOOKING_TYPE, bookingType).apply()
        }
    var pricing: String?
        get() = prefs.getString(PRICE, "")
        set(pricing) {
            prefs.edit().putString(PRICE, pricing).apply()
        }
    var password: String?
        get() = prefs.getString(PASSWORD, "")
        set(password) {
            prefs.edit().putString(PASSWORD, password).apply()
        }
    var  customerName: String?
        get() = prefs.getString(CUSTOMER_NAME, "")
        set(customerName) {
            prefs.edit().putString(CUSTOMER_NAME, customerName).apply()
        }
    var  email: String?
        get() = prefs.getString(EMAIL, "")
        set(email) {
            prefs.edit().putString(EMAIL, email).apply()
        }

    var  phoneNumber: String?
        get() = prefs.getString(PHONE_NUMBER, "")
        set(phoneNumber) {
            prefs.edit().putString(PHONE_NUMBER, phoneNumber).apply()
        }
    var  phoneWithCode: String?
        get() = prefs.getString(PHONE_WITH_CODE, "")
        set(phoneWithCode) {
            prefs.edit().putString(PHONE_WITH_CODE, phoneWithCode).apply()
        }

    var id: Int
        get() = prefs.getInt(ID, -1)
        set(id) {
                prefs.edit().putInt(ID, id).apply()
        }

    var gender: String?
        get() = prefs.getString(GENDER, "")
        set(gender) {
            prefs.edit().putString(GENDER, gender).apply()
        }

    var selectedDate: String?
        get() = prefs.getString(SELECTED_DATE, "")
        set(selectedDate) {
            prefs.edit().putString(SELECTED_DATE, selectedDate).apply()
        }
    var profilePic: String?
        get() = prefs.getString(PROFILE_PICTURE, "")
        set(profilePic) {
            prefs.edit().putString(PROFILE_PICTURE, profilePic).apply()
        }

}