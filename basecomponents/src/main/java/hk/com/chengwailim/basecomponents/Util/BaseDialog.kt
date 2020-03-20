package hk.com.chengwailim.basecomponents.Util

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import hk.com.chengwailim.basecomponents.R
import hk.com.chengwailim.fancyalertdialog.Animation
import hk.com.chengwailim.fancyalertdialog.FancyAlertDialog
import hk.com.chengwailim.fancyalertdialog.Icon

class BaseDialog {
    companion object{
        var  loadingDialog: AlertDialog? = null
        fun alertDialog(context: Activity, title:String, message:String = ""){
            FancyAlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setBackgroundColor(context.getColor(R.color.red))
                .setPositiveBtnBackground(context.getColor(R.color.blue))
                .setPositiveBtnText(context.getString(R.string.confirm))
                .setAnimation(Animation.SLIDE)
                .isCancellable(false)
                .setIcon(R.drawable.ic_clear, Icon.Visible)
                .build()
        }

        fun confirmDialog(context: Activity, title:String, message:String = "", confirmCallBack: ()->Unit ){
            FancyAlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setBackgroundColor(context.getColor(R.color.black))
                .setPositiveBtnBackground(context.getColor(R.color.blue))
                .setNegativeBtnText(context.getString(R.string.cancel))
                .OnNegativeClicked {  }
                .OnPositiveClicked { confirmCallBack.invoke() }
                .setPositiveBtnText(context.getString(R.string.confirm))
                .setAnimation(Animation.SLIDE)
                .isCancellable(false)
                .setIcon(R.drawable.ic_error, Icon.Visible)
                .build()
        }
        fun showLoading(context: Activity){
            if(loadingDialog == null) loadingDialog = AlertDialog.Builder(context, R.style.LoadingDialog).create()
            loadingDialog?.setView(LayoutInflater.from(context).inflate(R.layout.loading_dialog, null), 0, 0, 0, 0)
            loadingDialog?.setCancelable(false)
            loadingDialog?.show()
        }

        fun stopLoading(){

            loadingDialog?.dismiss()
            loadingDialog = null


        }
    }
}