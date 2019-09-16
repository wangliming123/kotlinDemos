package com.twtims.mapboxdemo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IssModel{
    @SerializedName("iss_position")
    @Expose
    var issPosition: IssPosition? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("timestamp")
    @Expose
    var timestamp: Int? = null
}

class IssPosition {

    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null

    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null

}
