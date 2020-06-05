package com.mvvm.intelegenciamachinetest.dashboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.greenrobot.eventbus.EventBus

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        EventBus.getDefault().post("Reload")
    }
}