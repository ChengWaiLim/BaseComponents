package hk.com.chengwailim.basecomponents

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ListView
import com.baoyz.swipemenulistview.SwipeMenuListView
import hk.com.chengwailim.basecomponents.Components.BaseTable.Interfaces.CustomizedFilter
import hk.com.chengwailim.basecomponents.Components.BaseTable.Interfaces.SortInterface
import hk.com.chengwailim.basecomponents.Components.BaseTable.TableHeader
import hk.com.chengwailim.basecomponents.Util.BaseListView


class BaseTable<T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){
    var list: SwipeMenuListView
    private lateinit var listAdapter: BaseListView<T>
    private var headerLayout: LinearLayout
    private var tableHeaderList = ArrayList<TableHeader>()
    private var sortInterfaceList = ArrayList<SortInterface<T>>()
    private var customizedFilter: CustomizedFilter<T>? = null
    var dataList = ArrayList<T>()
    var showList = ArrayList<T>()
    init {
        orientation = VERTICAL
        headerLayout = LinearLayout(context)
        headerLayout.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        headerLayout.orientation = HORIZONTAL
        list = SwipeMenuListView(getContext(), attrs, defStyleAttr)
        list.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.addView(headerLayout)
        this.addView(list)
    }

    fun setDataAdapter(dataAdapter: BaseListView<T>) {
        dataList = dataAdapter.getData() as ArrayList<T>
        listAdapter = dataAdapter
        list.adapter = listAdapter
        refresh(dataAdapter.getData())
    }
    fun refresh(list: ArrayList<T>) {
        listAdapter.refresh(list)
    }

    fun setSortInterface(vararg sortInterfaces: SortInterface<T>){
        sortInterfaces.forEach {
            sortInterfaceList.add(it)
        }
    }

    fun setFilterInterface(filterInterface: CustomizedFilter<T>){
        customizedFilter = filterInterface
    }

    fun filter(){
        showList = dataList
        customizedFilter?.let {
            showList = it.filter(showList)
        }

        for(i in 0 until headerLayout.childCount){
            var tableHeader = headerLayout.getChildAt(i) as TableHeader
            when(tableHeader.status){
                TableHeader.SortStatus.ASCENDING -> showList.sortBy {
                    sortInterfaceList.get(i).sortColumn(it)
                }
                TableHeader.SortStatus.DESCENDING -> showList.sortByDescending {
                    sortInterfaceList.get(i).sortColumn(it)
                }
            }
        }
        refresh(showList)
    }

    fun setUpHeader(headerList: ArrayList<String>){
        headerLayout.weightSum = headerList.size.toFloat()
        headerList.forEach {
            val tableHeader = TableHeader(context)
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
            )
            params.weight = 1.0f
            tableHeader.layoutParams = params
            tableHeader.setText(it)
            tableHeader.setBackgroundColor(context.resources.getColor(R.color.pale_grey))
            headerLayout.addView(tableHeader)
            tableHeaderList.add(tableHeader)
        }
        tableHeaderList.forEach {header->
            header.sortOnClickListener{currentStatus ->
                tableHeaderList.filter { !it.equals(header) }.forEach { it.setSortStatus(TableHeader.SortStatus.NORMAL) }
                filter()
            }
        }
    }
}