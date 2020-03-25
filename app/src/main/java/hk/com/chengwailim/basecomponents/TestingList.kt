package hk.com.chengwailim.basecomponents

import android.content.Context
import android.view.View
import android.widget.ListView
import android.widget.TextView
import hk.com.chengwailim.basecomponents.Components.BaseTable.TableHeader
import hk.com.chengwailim.basecomponents.Util.BaseListView

class TestingList(private val context: Context, private val dataList: ArrayList<Location>, private val list_view: ListView? = null): BaseListView<Location>(context, dataList, list_view, R.layout.testing_list) {
    override fun getView(view: View, instance: Location, position: Int): View {
        view.findViewById<TextView>(R.id.text_view_code).setText(instance.code)
        return view
    }
//    fun setUpHeader(headerList: ArrayList<String>, vararg sortCallback:  ()-> Unit){
//        headerList.forEach {
//        }
//    }

//    private fun initTableHeader() {
//        table_header_bin.sortOnClickListener {
//            table_header_location.setSortStatus(TableHeader.SortStatus.NORMAL)
//            table_header_item.setSortStatus(TableHeader.SortStatus.NORMAL)
//            table_header_quantity.setSortStatus(TableHeader.SortStatus.NORMAL)
//            filter()
//        }
//        table_header_location.sortOnClickListener {
//            table_header_item.setSortStatus(TableHeader.SortStatus.NORMAL)
//            table_header_bin.setSortStatus(TableHeader.SortStatus.NORMAL)
//            table_header_quantity.setSortStatus(TableHeader.SortStatus.NORMAL)
//            filter()
//        }
//        table_header_item.sortOnClickListener {
//            table_header_location.setSortStatus(TableHeader.SortStatus.NORMAL)
//            table_header_bin.setSortStatus(TableHeader.SortStatus.NORMAL)
//            table_header_quantity.setSortStatus(TableHeader.SortStatus.NORMAL)
//            filter()
//        }
//        table_header_quantity.sortOnClickListener {
//            table_header_item.setSortStatus(TableHeader.SortStatus.NORMAL)
//            table_header_location.setSortStatus(TableHeader.SortStatus.NORMAL)
//            table_header_bin.setSortStatus(TableHeader.SortStatus.NORMAL)
//            filter()
//        }
//        when (table_header_bin.status) {
//            TableHeader.SortStatus.ASCENDING -> data.sortBy { it.bin }
//            TableHeader.SortStatus.DESCENDING -> data.sortByDescending { it.bin }
//        }
//        when (table_header_location.status) {
//            TableHeader.SortStatus.ASCENDING -> data.sortBy { it.location }
//            TableHeader.SortStatus.DESCENDING -> data.sortByDescending { it.location }
//        }
//        when (table_header_item.status) {
//            TableHeader.SortStatus.ASCENDING -> data.sortBy { it.itemCode }
//            TableHeader.SortStatus.DESCENDING -> data.sortByDescending { it.itemCode }
//        }
//        when (table_header_quantity.status) {
//            TableHeader.SortStatus.ASCENDING -> data.sortBy { it.quantity }
//            TableHeader.SortStatus.DESCENDING -> data.sortByDescending { it.quantity }
//        }
//    }
}