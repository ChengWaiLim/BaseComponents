package hk.com.chengwailim.basecomponents.Util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView

abstract class BaseListView<T>(private val context: Context, private var dataList: ArrayList<T>, private var list_view: ListView?, private val list_item_view: Int): BaseAdapter() {

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


}