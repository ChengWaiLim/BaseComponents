package hk.com.chengwailim.basecomponents.Util

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty
import hk.com.chengwailim.basecomponents.R

class BaseToast {
    companion object{
        fun successToast(context: Context, message:String = context.getString(R.string.success)){
            Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
        }
        fun warningToast(context: Context, message:String){
            Toasty.warning(context, message, Toast.LENGTH_SHORT, true).show();
        }
    }
}