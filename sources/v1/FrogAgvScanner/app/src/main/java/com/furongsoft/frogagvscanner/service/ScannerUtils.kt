package com.furongsoft.frogagvscanner.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter


/**
 * 扫描器工具
 *
 * @author alex
 */
object ScannerUtils {
    fun open(context: Context) {
        context.sendBroadcast(Intent("com.android.scanservice.scan.on"))
    }

    fun close(context: Context) {
        context.sendBroadcast(Intent("com.android.scanservice.scan.off"))
    }

    fun scan(context: Context) {
        context.sendBroadcast(Intent("android.intent.action.FUNCTION_BUTTON_DOWN", null))
    }

    fun register(context: Context, broadcastReceiver: BroadcastReceiver) {
        val scanDataIntentFilter = IntentFilter()
        scanDataIntentFilter.addAction("com.android.scancontext")
        scanDataIntentFilter.addAction("com.android.scanservice.scancontext")
        context.registerReceiver(broadcastReceiver, scanDataIntentFilter)
    }

    fun unregister(context: Context, broadcastReceiver: BroadcastReceiver) {
        context.unregisterReceiver(broadcastReceiver)
    }
}