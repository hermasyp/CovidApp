package com.catnip.covidapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity


/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
object IntentUtils {
    fun openToBrowser(context : Context?,url : String){
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        context?.startActivity(i)
    }

}