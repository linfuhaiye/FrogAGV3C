package com.furongsoft.frogagvscanner.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * 原料
 *
 * @author alex
 */
@Parcelize
class Material(
    @SerializedName("code") var code: String,
    @SerializedName("name") var name: String,
    @SerializedName("count") var count: Int
) : Parcelable {
    interface OnMaterialClickListener {
        fun onClick(material: Material)
    }

    @IgnoredOnParcel
    var listener: OnMaterialClickListener? = null
}
