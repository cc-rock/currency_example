package com.example.currencies.common.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Amount(val currency: Currency, val value: Float): Parcelable