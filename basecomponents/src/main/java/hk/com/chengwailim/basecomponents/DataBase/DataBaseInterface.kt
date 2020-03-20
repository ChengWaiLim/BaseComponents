package hk.com.cft.sfc_inventory_application.Util.Database

import java.lang.Exception

interface DataBaseInterface {
    @Throws(Exception::class)
    fun < T:DataBaseModel> rawQuery(table: Class<T>): ArrayList<T>
    @Throws(Exception::class)
    fun < T:DataBaseModel> rawQueryByField(table: Class<T>, value:String, field:String): ArrayList<T>
    @Throws(Exception::class)
    fun < T:DataBaseModel> rawUpdate(model:T, id:String, field:String, beforeUpdate: (Any)->Any)
    @Throws(Exception::class)
    fun < T:DataBaseModel> rawInsert(model:T, beforeInsert: (Any)->Any)
    @Throws(Exception::class)
    fun < T:DataBaseModel> rawDelete(table: Class<T>, id:String, field:String)
    @Throws(Exception::class)
    fun < T:DataBaseModel> rawDelete(table: Class<T>)
    @Throws(Exception::class)
    fun < T:DataBaseModel> rawGetColumnNames(table: Class<T>): ArrayList<String>
    @Throws(Exception::class)
    fun closeDB()
    @Throws(Exception::class)
    fun openDB()
}