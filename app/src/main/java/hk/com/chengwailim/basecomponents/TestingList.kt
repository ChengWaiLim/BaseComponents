package hk.com.chengwailim.basecomponents

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenuListView
import hk.com.chengwailim.basecomponents.Components.BaseTable.TableHeader
import hk.com.chengwailim.basecomponents.Util.BaseListView

class TestingList(private val context: Context, private val dataList: ArrayList<Location>, private var list_view: SwipeMenuListView? = null): BaseListView<Location>(context, dataList, list_view, R.layout.testing_list) {
    override fun setMenu(list_view: SwipeMenuListView) {
        list_view.setMenuCreator(SwipeMenuCreator() {
            it.addMenuItem(createSwipeItem(context.resources.getColor(R.color.blue), R.drawable.ic_clear))
            it.addMenuItem(createSwipeItem(context.resources.getColor(R.color.grey), R.drawable.ic_error))
        })
    }
    override fun getView(view: View, instance: Location, position: Int): View {
        view.findViewById<TextView>(R.id.text_view_code).setText(instance.code)
        return view
    }
}