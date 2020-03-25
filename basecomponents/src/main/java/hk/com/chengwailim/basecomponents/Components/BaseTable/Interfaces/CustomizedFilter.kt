package hk.com.chengwailim.basecomponents.Components.BaseTable.Interfaces

interface CustomizedFilter <T>{
    fun filter(dataList: ArrayList<T>):ArrayList<T>
}