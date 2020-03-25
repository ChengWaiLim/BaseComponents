package hk.com.chengwailim.basecomponents.Util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenuListView
import hk.com.chengwailim.basecomponents.R

abstract class BaseListView<T>(private val context: Context, private var dataList: ArrayList<T>, private var list_view: SwipeMenuListView?, private var list_item_view: Int): BaseAdapter() {
    var a = 10

    init {
        list_view?.adapter = this
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = convertView
        if (v == null) {
            val vi: LayoutInflater
            vi = LayoutInflater.from(context)
            v = vi.inflate(list_item_view, parent, false)
        }
        return getView(v!!, getItem(position), position);
    }

    abstract fun getView(view: View, instance: T, position: Int): View

    override fun getItem(index: Int): T {
        return dataList.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    fun getData():ArrayList<T>{
        return dataList
    }

    fun refresh(data: ArrayList<T>){
        dataList = data
        notifyDataSetChanged()
    }

    fun setListView(listView: SwipeMenuListView){
        this.list_view = listView
        setMenu(this.list_view!!)
    }

    open fun setMenu(list_view: SwipeMenuListView){}

    fun createSwipeItem(color:Int, drawable: Int, width : Int = 180): SwipeMenuItem{
        val item = SwipeMenuItem(context)
        item.background = ColorDrawable(color)
        item.setIcon(drawable)
        item.width = width
        return item
    }


}