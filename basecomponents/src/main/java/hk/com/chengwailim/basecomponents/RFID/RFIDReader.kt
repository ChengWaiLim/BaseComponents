package hk.com.chengwailim.basecomponents.RFID

import android.app.Activity
import android.os.AsyncTask
import com.zebra.rfid.api3.*
import com.zebra.rfid.api3.RFIDReader
import hk.com.chengwailim.basecomponents.Util.BaseDialog
import hk.com.chengwailim.basecomponents.Util.BaseToast
import hk.com.chengwailim.basecomponents.R
import java.lang.Exception

class RFIDReader {
    companion object {
        var readers: Readers
        var rfidReader: RFIDReader? = null
        var readerDevice: ReaderDevice? = null
        var readTagEventsListener: RfidEventsListener? = null
        init { readers = Readers() }

        fun setUpReader(context: Activity, callback: (isConnected: Boolean) -> Unit = {}) {
            object : AsyncTask<Void, Void, Exception?>() {
                override fun onPreExecute() {
                    BaseDialog.showLoading(context)
                }

                override fun doInBackground(vararg params: Void?): Exception? {
                    try {
                        if (!isConnected()) {
                            val availableRFIDReaderList: List<ReaderDevice> =
                                readers.GetAvailableRFIDReaderList()
                            if (availableRFIDReaderList.size > 0) {
                                readerDevice = availableRFIDReaderList[0]
                                rfidReader = readerDevice!!.getRFIDReader()
                                rfidReader!!.connect()
                            }
                        }
                    } catch (e: Exception) {
                        return e
                    }
                    return null
                }

                override fun onPostExecute(result: Exception?) {
                    BaseDialog.stopLoading()
                    if (result != null || !isConnected()) {
//                        BaseDialog.alertDialog(
//                            context,
//                            context.resources.getString(R.string.failed),
//                            if (result?.message == null) context.getString(R.string.error_no_rfid_reader_found) else result.message!!
//                        )
                        BaseToast.warningToast(context,if (result?.message == null) context.getString(
                            R.string.error_no_rfid_reader_found) else result.message!! )
                        disconnect()
                    } else BaseToast.successToast(context, context.getString(R.string.reader_connected))
                    callback.invoke(isConnected())
                }
            }.execute()
        }

        private fun disconnect() {
            if (rfidReader != null && rfidReader!!.isConnected) rfidReader?.disconnect()
        }

        fun isConnected(): Boolean {
            if (rfidReader == null) return false
            return rfidReader!!.isConnected
        }

        fun setReadTagEvent(readTagEventsListener: RfidEventsListener) {
            if(isConnected()){
                rfidReader?.Events?.setTagReadEvent(true)
                Companion.readTagEventsListener = readTagEventsListener
                rfidReader?.Events?.addEventsListener(readTagEventsListener)
            }
        }

        fun startReadTag(){
            if(isConnected()){
                rfidReader?.Actions?.Inventory?.perform()
            }
        }

        fun stopReadTag(){
            if(isConnected()){
                rfidReader?.Actions?.Inventory?.stop()
            }
        }

        fun removeReadTagEvent(){
            if(isConnected()){
                rfidReader?.Events?.removeEventsListener(
                    readTagEventsListener
                )
            }
        }
    }
}
