package com.furongsoft.frogagvscanner.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product(
    @SerializedName("code") var code: String,
    @SerializedName("name") var name: String,
    @SerializedName("materials") var materials: List<Material>
) : Parcelable {
    interface OnProductClickListener {
        fun onClick(product: Product)
        fun onShowDetail(product: Product)
    }

    @IgnoredOnParcel
    var listener: OnProductClickListener? = null
}
