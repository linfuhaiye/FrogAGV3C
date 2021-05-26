package com.furongsoft.frogagvscanner.miscs

import android.content.Context
import android.content.pm.PackageManager

/**
 * 渠道工具
 *
 * @author alex
 */
object ChannelUtils {
    /**
     * 获取渠道
     */
    fun getChannel(context: Context): String {
        return try {
            context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            ).metaData.getString("CHANNEL") ?: ""
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }
}
