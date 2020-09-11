package com.rmohd8788.helloji

import android.app.Activity
import android.app.AlertDialog

class CustomProgressDialog(activity: Activity) {

    private var alertDialog = AlertDialog.Builder(activity)
        .setCancelable(false)
        .setView(R.layout.custom_progress_dialog).create()

    fun startProgress(){
        alertDialog.show()
    }

    fun stopProgress(){
        alertDialog.dismiss()
    }
}