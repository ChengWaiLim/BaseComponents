package hk.com.chengwailim.basecomponents.Components.BaseTable.Interfaces

import java.util.*

interface SortInterface<T> {
    fun sortColumn(data:T):String
}