package hk.com.chengwailim.basecomponents.RFID

import android.app.Activity
import android.os.AsyncTask
import com.zebra.rfid.api3.*
import com.zebra.rfid.api3.RFIDReader
import hk.com.chengwailim.basecomponents.Util.BaseDialog
import hk.com.chengwailim.basecomponents.Util.BaseToast
import hk.com.chengwailim.basecomponents.R
import java.lang.Exception

class RFIDReaderTest {
    companion object {
        var readers: Readers
        var rfidReader: RFIDReader? = null
        var readerDevice: ReaderDevice? = null
        var readTagEventsListener: RfidEventsListener? = null
        var locateTagEventsListener: RfidEventsListener? = null
        init { readers = Readers() }

        fun setUpReader(context: Activity, callback: (isConnected: Boolean) -> Unit = {}) {
            object : AsyncTask<Void, Void, Exception?>() {
                override fun onPreExecute() {
                    readers = Readers(context, ENUM_TRANSPORT.SERVICE_SERIAL)
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
                        BaseToast.warningToast(
                            context, if (result?.message == null) context.getString(
                                R.string.error_no_rfid_reader_found
                            ) else result.message!!
                        )
                        disconnect()
                    } else BaseToast.successToast(
                        context,
                        context.getString(R.string.reader_connected)
                    )
                    callback.invoke(isConnected())
                }
            }.execute()
        }
        public fun locateTag(tag:String, onTagRead: (distance: String) -> Unit){
            if(isConnected()){
                rfidReader?.Events?.setTagReadEvent(true)
                val eventsListener = object : RfidEventsListener{
                    override fun eventStatusNotify(p0: RfidStatusEvents?) {}
                    override fun eventReadNotify(p0: RfidReadEvents?) {
                        val scannedTags = rfidReader?.Actions?.getReadTags(100)
                        if (scannedTags != null) {
                            for (index in scannedTags.indices) {
                                if(scannedTags[index].isContainsLocationInfo()){
                                    onTagRead.invoke(scannedTags[index].LocationInfo.relativeDistance.toString())
                                }
                            }
                        }
                    }
                }
                locateTagEventsListener = eventsListener;
                this.rfidReader?.Events?.addEventsListener(locateTagEventsListener)
                this.rfidReader?.Actions?.TagLocationing?.Perform(tag, null, null);
            }
        }

        public fun stopLocateTag(){
            if(isConnected()){
                rfidReader?.Actions?.Inventory?.stop()
                rfidReader?.Events?.removeEventsListener(
                    locateTagEventsListener
                )
            }
        }


        private fun disconnect() {
            if (rfidReader != null && rfidReader!!.isConnected) rfidReader?.disconnect()
        }

        fun isConnected(): Boolean {
            if (rfidReader == null) return false
            return rfidReader!!.isConnected
        }

        fun startReadTag(onTagRead: (tag:String) -> Unit){
            if(isConnected()){
                rfidReader?.Events?.setTagReadEvent(true)
                val readTagEventsListener = object : RfidEventsListener{
                    override fun eventStatusNotify(p0: RfidStatusEvents?) {}
                    override fun eventReadNotify(p0: RfidReadEvents?) {
                        val tagDataArray: TagDataArray? = rfidReader?.Actions?.getReadTagsEx(100)
                        if (tagDataArray != null) {
                            for (i in 0 until tagDataArray.length) {
                                var tagID = tagDataArray.tags[i].tagID
                                onTagRead(tagID)
                            }
                        }
                    }
                }
                Companion.readTagEventsListener = readTagEventsListener
                rfidReader?.Events?.addEventsListener(readTagEventsListener)
                rfidReader?.Actions?.Inventory?.perform()
            }
        }

        fun stopReadTag(){
            if(isConnected()){
                rfidReader?.Actions?.Inventory?.stop()
                rfidReader?.Events?.removeEventsListener(
                    readTagEventsListener
                )
            }
        }
    }
}
