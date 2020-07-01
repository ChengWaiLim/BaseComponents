package hk.com.chengwailim.basecomponents.RFID

import com.zebra.rfid.api3.RfidEventsListener
import com.zebra.rfid.api3.RfidReadEvents
import com.zebra.rfid.api3.RfidStatusEvents
import com.zebra.rfid.api3.TagDataArray
import java.util.*

abstract interface RFIDReadTagListener: RfidEventsListener {
    override fun eventStatusNotify(p0: RfidStatusEvents?) {}

    override fun eventReadNotify(p0: RfidReadEvents?) {
        val tagDataArray: TagDataArray? = RFIDReader.rfidReader?.Actions?.getReadTagsEx(100)
        if (tagDataArray != null) {
            for (i in 0 until tagDataArray.length) {
                var tagID = tagDataArray.tags[i].tagID
                onTagRead(tagID)
            }
        }
    }

    public abstract fun onTagRead(tagID: String)
}