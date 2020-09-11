package com.rmohd8788.helloji

import android.content.Context

class PreferenceHelper(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("com.rmohd8788.helloji",Context.MODE_PRIVATE)

    fun putBoolean(key : String,value : Boolean){
        sharedPreferences.edit().putBoolean(key,value).apply()
    }

    fun getBoolean(key : String) : Boolean{
        return sharedPreferences.getBoolean(key,false)
    }

    fun putString(key : String,value : String){
        sharedPreferences.edit().putString(key,value).apply()
    }

    fun getString(key : String) : String? {
        return sharedPreferences.getString(key,null)
    }

    fun clear(){
        sharedPreferences.edit().clear().apply()
    }
}