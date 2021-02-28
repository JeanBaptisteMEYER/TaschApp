package com.jbm.intactchallenge.model

import com.google.gson.annotations.SerializedName

class Size {
    @SerializedName("H")  var height = ""
    @SerializedName("W") var width = ""
    @SerializedName("D") var depth = ""
}