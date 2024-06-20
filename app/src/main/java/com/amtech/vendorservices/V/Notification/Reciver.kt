package com.amtech.vendorservices.V.Notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Reciver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "action_id") {
            val soundServiceIntent = Intent(context, SoundService::class.java)
            context.stopService(soundServiceIntent)
//            val i = Intent(context, Appointments::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(i)
            // Handle the action when the button is clicked
           // Toast.makeText(context, "Action Button Clicked!", Toast.LENGTH_SHORT).show()

//        } else if (Objects.equals(intent.getAction(), "stop_id")) {
//            Intent soundServiceIntent = new Intent(context, SoundService.class);
//            context.stopService(soundServiceIntent);
//            Toast.makeText(context, "Action Button Clicked!", Toast.LENGTH_SHORT).show();
//        } else if (Objects.equals(intent.getAction(), "click_id")) {
//            Intent soundServiceIntent = new Intent(context, SoundService.class);
//            context.stopService(soundServiceIntent);
//            Toast.makeText(context, "Action Button Clicked!", Toast.LENGTH_SHORT).show();
//
        }
    }
}
