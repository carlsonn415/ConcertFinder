package com.example.lineup_app.data.local

import android.content.Context
import javax.inject.Inject

interface StringResourceProvider {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg formatArgs: Any): String
}

class AndroidStringResourceProvider @Inject constructor(private val context: Context) : StringResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)
    override fun getString(resId: Int, vararg formatArgs: Any): String = context.getString(resId, *formatArgs)
}