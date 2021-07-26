package hk.com.cft.maybank.Util.CustomDataBase

import DataBaseInterface
import DataBaseModel

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.google.gson.Gson
import hk.com.cft.maybank.Model.BaseModel
import hk.com.chengwailim.basecomponents.DataBase.BaseEnum
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties


open class SQLiteDataBase(protected val context: Context) : SQLiteOpenHelper(context, context.packageName + ".db", null, 1) {
    protected lateinit var db: SQLiteDatabase

    @Throws(Exception::class)
    fun <T : DataBaseModel> rawQuery(table: Class<T>, options: MutableMap<String, Any> = mutableMapOf("joinClass" to ArrayList<String>())): ArrayList<T>? {
        isDBExisted()
        var joinClass = options["joinClass"] as ArrayList<String>
        if(joinClass.indexOf(table.simpleName) == -1){
            joinClass.add(table.simpleName)
            options.put("joinClass", joinClass)
            val cursor = db.query(table.simpleName, null, null, null, null, null, null)
            val jsonList = cursorToJSONObjectArray(cursor)
            cursor.close();
            return  castArrayListJSON(jsonList, table, options)
        }
        else
            return null;
    }

    @Throws(java.lang.Exception::class)
    fun <T : DataBaseModel> rawQueryByField(
        table: Class<T>,
        value: String,
        field: String,
        options: MutableMap<String, Any> = mutableMapOf("joinClass" to ArrayList<String>())
    ): ArrayList<T>? {
        var joinClass = options["joinClass"] as ArrayList<String>
        if(joinClass.indexOf(table.simpleName) == -1) {
            joinClass.add(table.simpleName)
            options.put("joinClass", joinClass)
            val cursor =
                db.query(table.simpleName, null, field + "=?", arrayOf(value), null, null, null)
            val jsonList = cursorToJSONObjectArray(cursor)
            cursor.close();
            return castArrayListJSON(jsonList, table, options)
        }else
            return null;
    }

    @Throws(java.lang.Exception::class)
    fun <T : DataBaseModel> rawUpdate(model: T, id: String, field: String, beforeUpdate: (Any) -> Any ) {
        isDBExisted()
        var contentValues = prepareContentValue(model)
        contentValues.put("isUpdated", 1)
        contentValues = beforeUpdate.invoke(contentValues) as ContentValues
        db.update(model::class.java.simpleName, contentValues, field + "=?", arrayOf(id))
    }

    @Throws(Exception::class)
    fun <T : DataBaseModel> rawInsert(model: T, isFromServer: Boolean = true,beforeInsert: (Any) -> Any) {
        isDBExisted()
        var contentValues = prepareContentValue(model)
        if(!isFromServer) contentValues.put("isInserted", 1)
        contentValues = beforeInsert.invoke(contentValues) as ContentValues
        db.insertOrThrow(model::class.java.simpleName, null, contentValues)
    }

    @Throws(Exception::class)
    fun <T : DataBaseModel> rawDelete(table: Class<T>, id: String, field: String) {
        isDBExisted()
        db.delete(table.simpleName, field + "=?", arrayOf(id))
    }

    fun <T : DataBaseModel> rawDelete(table: Class<T>) {
        isDBExisted()
        db.delete(table.simpleName, null, null)
    }

    fun <T : DataBaseModel> rawGetColumnNames(table: Class<T>) :ArrayList<String>{
        return db.query(table.simpleName, null, null, null, null, null, null).columnNames.toCollection(ArrayList())
    }

    @Throws(Exception::class)
    fun closeDB() {
        isDBExisted()
        db.close()
    }

    @Throws(Exception::class)
    fun openDB() {
        db = this.writableDatabase
    }

    private fun <T : DataBaseModel> prepareContentValue(model: T): ContentValues {
        val contentValues = ContentValues()
        for (prop in model::class.memberProperties) {
            if (prop.getUnsafed(model) != null) {
                when (prop.getUnsafed(model)!!::class.simpleName) {
                    "String" -> contentValues.put(prop.name, prop.getUnsafed(model) as String)
                    "Int" -> contentValues.put(prop.name, prop.getUnsafed(model) as Int)
                    "Double" -> contentValues.put(prop.name, prop.getUnsafed(model) as Double)
                    "Boolean" -> contentValues.put(prop.name, if(prop.getUnsafed(model) as Boolean) 1 else 0)
                    "Date" -> contentValues.put(
                        prop.name,
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prop.getUnsafed(model) as Date)
                    )
                    "ArrayList" -> {
                        var arrayList = prop.getUnsafed(model) as ArrayList<*>
                        var result = arrayList.map {
                            if(it is Date) return@map SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(it)
                            it
                        }.toList()
                        contentValues.put(prop.name, Gson().toJson(result))
                    }
                    else -> {
                        val enum = prop.getUnsafed(model) as BaseEnum
                        contentValues.put(prop.name, enum.toValue())
                    }
                }
            }
        }
        return contentValues
    }

    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    private fun <T : DataBaseModel>castArrayListJSON(jsonList: ArrayList<JSONObject>, table: Class<T>, options: MutableMap<String, Any>):ArrayList<T>{
        val result = ArrayList<T>()
        jsonList.forEach {
            result.add(castJSON(it,table, options))
        }
        return result
    }

    fun <T : BaseModel> update(model: T) {
        this.rawUpdate(model, model.ID.toString(), "ID", {it})
    }

    fun <T : BaseModel> insert(model: T, isFromServer: Boolean = true) {
        this.rawInsert(model,isFromServer,{it})
    }

    fun <T : BaseModel> delete(table: Class<T>) {
        this.rawDelete(table)
    }

    fun <T : BaseModel> delete(table: Class<T>, id: String) {
        this.rawDelete(table, id, "ID")
    }

    @Throws(Exception::class)
    private fun <T: DataBaseModel> castJSON(json:JSONObject, table: Class<T>, options: MutableMap<String, Any>):T{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            return table.constructors.find {
                it.parameterCount.equals(3) && it.parameters[0].type.equals(JSONObject::class.java)
            }!!.newInstance(json, this, options) as T
        else
            return table.constructors.get(table.constructors.size - 1).newInstance(json, this) as T
    }

    private fun <T, R> KProperty1<T, R>.getUnsafed(receiver: Any): R {
        return get(receiver as T)
    }

    private fun  cursorToJSONObjectArray(cursor:Cursor):ArrayList<JSONObject>{
        val jsonList = ArrayList<JSONObject>()
        while (cursor.moveToNext()) {
            val json = JSONObject()
            for(i in 0 until cursor.columnCount){
                when(cursor.getType(i)){
                    Cursor.FIELD_TYPE_STRING -> json.put(cursor.getColumnName(i), cursor.getStringOrNull(i))
                    Cursor.FIELD_TYPE_INTEGER -> json.put(cursor.getColumnName(i), cursor.getIntOrNull(i))
                    Cursor.FIELD_TYPE_FLOAT -> json.put(cursor.getColumnName(i), cursor.getFloatOrNull(i))
                    else -> json.put(cursor.getColumnName(i), cursor.getStringOrNull(i))
                }
            }
            jsonList.add(json)
        }
        return jsonList
    }

    @Throws(Exception::class)
    private fun isDBExisted(){
        if(db == null) throw Exception("DB is null")
    }

    protected fun createTableByModel(tableName :String, model : KClass<Any>) :String{
        var sqlQuery = "CREATE TABLE `" + tableName + "` (\n"
        sqlQuery += " `ID` int(11) PRIMARY KEY NOT NULL, \n"
        sqlQuery += " `isUpdated` tinyint(1) NOT NULL DEFAULT 0, \n"
        sqlQuery += " `isInserted` tinyint(1) NOT NULL DEFAULT 0, \n"
        for(property in model.declaredMemberProperties) {
            if(!property.name.equals("ID"))
                sqlQuery += convertToSqlType(property.name, property.returnType.toString())
        }
        sqlQuery = sqlQuery.dropLast(2)
        sqlQuery += ")"
        return sqlQuery
    }

    private fun convertToSqlType(name :String, type :String) :String{
        var sql = "`" + name + "`" + " ";
        var typeQuery = "";
        when (type){
            "kotlin.Int" -> typeQuery = "int(11) NOT NULL,"
            "kotlin.Int?" -> typeQuery = "int(11) NULL,"
            "java.util.Int" -> typeQuery = "int(11) NOT NULL,"
            "java.util.Int?" -> typeQuery = "int(11) NULL,"
            "kotlin.Date" -> typeQuery = "datetime NOT NULL,"
            "kotlin.Date?" -> typeQuery = "datetime NULL,"
            "java.util.Date" -> typeQuery = "datetime NOT NULL,"
            "java.util.Date?" -> typeQuery = "datetime NULL,"
            "kotlin.String" -> typeQuery = "VARCHAR(255) NOT NULL,"
            "kotlin.String?" -> typeQuery = "VARCHAR(255) NULL,"
            "java.util.String" -> typeQuery = "VARCHAR(255) NOT NULL,"
            "java.util.String?" -> typeQuery = "VARCHAR(255) NULL,"
            "kotlin.Double" -> typeQuery = "decimal(11,3) NOT NULL,"
            "kotlin.Double?" -> typeQuery = "decimal(11,3) NULL,"
            "java.util.Double" -> typeQuery = "decimal(11,3) NOT NULL,"
            "java.util.Double?" -> typeQuery = "decimal(11,3) NULL,"
            "kotlin.Boolean" -> typeQuery = "tinyint(1) NOT NULL,"
            "kotlin.Boolean?" -> typeQuery = "tinyint(1) NULL,"
            "java.util.Boolean" -> typeQuery = "tinyint(1) NOT NULL,"
            "java.util.Boolean?" -> typeQuery = "tinyint(1) NULL,"
        }
        if(type.contains("ArrayList") && (type.contains("java.util.Double") || type.contains("kotlin.Double") || type.contains("kotlin.Int") || type.contains("java.util.Int") || type.contains("kotlin.Date")  || type.contains("java.util.Date") || type.contains("java.util.String") || type.contains("kotlin.String") || type.contains("kotlin.Boolean") || type.contains("java.util.Boolean"))) typeQuery = "VARCHAR(2046) NOT NULL,"
        if(typeQuery != ""){
            return sql + typeQuery + "\n";
        }else
            return "";
    }
}


