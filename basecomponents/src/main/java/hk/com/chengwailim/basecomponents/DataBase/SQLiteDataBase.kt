package hk.com.cft.sfc_inventory_application.Util.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


open class SQLiteDataBase(protected val context: Context) : SQLiteOpenHelper(context, "sfc_inventory.db", null, 1), DataBaseInterface {
    protected lateinit var db: SQLiteDatabase

    @Throws(Exception::class)
    override fun <T : DataBaseModel> rawQuery(table: Class<T>): ArrayList<T> {
        isDBExisted()
        val cursor = db.query(table.simpleName, null, null, null, null, null, null)
        val jsonList = cursorToJSONObjectArray(cursor)
        return  castArrayListJSON(jsonList, table)
    }

    override fun <T : DataBaseModel> rawQueryByField(
        table: Class<T>,
        value: String,
        field: String
    ): ArrayList<T> {
        val cursor = db.query(table.simpleName, null, field + "=?", arrayOf(value), null, null, null)
        val jsonList = cursorToJSONObjectArray(cursor)
        return  castArrayListJSON(jsonList, table)
    }

    @Throws(java.lang.Exception::class)
    override fun <T : DataBaseModel> rawUpdate(model: T, id: String, field: String, beforeUpdate: (Any) -> Any ) {
        isDBExisted()
        var contentValues = prepareContentValue(model)
        contentValues = beforeUpdate.invoke(contentValues) as ContentValues
        db.update(model::class.java.simpleName, contentValues, field + "=?", arrayOf(id))
    }

    @Throws(Exception::class)
    override fun <T : DataBaseModel> rawInsert(model: T, beforeInsert: (Any) -> Any) {
        isDBExisted()
        var contentValues = prepareContentValue(model)
        contentValues = beforeInsert.invoke(contentValues) as ContentValues
        db.insertOrThrow(model::class.java.simpleName, null, contentValues)
    }

    @Throws(Exception::class)
    override fun <T : DataBaseModel> rawDelete(table: Class<T>, id: String, field: String) {
        isDBExisted()
        db.delete(table.simpleName, field + "=?", arrayOf(id))
    }

    override fun <T : DataBaseModel> rawDelete(table: Class<T>) {
        isDBExisted()
        db.delete(table.simpleName, null, null)
    }

    override fun <T : DataBaseModel> rawGetColumnNames(table: Class<T>) :ArrayList<String>{
        return db.query(table.simpleName, null, null, null, null, null, null).columnNames.toCollection(ArrayList())
    }

    @Throws(Exception::class)
    override fun closeDB() {
        isDBExisted()
        db.close()
    }

    @Throws(Exception::class)
    override fun openDB() {
        db = this.writableDatabase
    }

    private fun <T : DataBaseModel> prepareContentValue(model: T): ContentValues {
        val contentValues = ContentValues()
        for (prop in model::class.memberProperties) {
            if (prop.getUnsafed(model) != null) {
                when (prop.getUnsafed(model)!!::class.simpleName) {
                    "String" -> contentValues.put(prop.name, prop.getUnsafed(model) as String)
                    "Int" -> contentValues.put(prop.name, prop.getUnsafed(model) as Int)
                    "Boolean" -> contentValues.put(prop.name, if(prop.getUnsafed(model) as Boolean) 1 else 0)
                    "Date" -> contentValues.put(
                        prop.name,
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prop.getUnsafed(model) as Date)
                    )
                }
            }
        }
        return contentValues
    }

    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    private fun <T : DataBaseModel>castArrayListJSON(jsonList: ArrayList<JSONObject>, table: Class<T>):ArrayList<T>{
        val result = ArrayList<T>()
        jsonList.forEach {
            result.add(castJSON(it,table))
        }
        return result
    }

    @Throws(Exception::class)
    private fun <T: DataBaseModel> castJSON(json:JSONObject, table: Class<T>):T{
        return table.constructors.find {
           it.parameterCount.equals(1) && it.parameters[0].type.equals(JSONObject::class.java)
        }!!.newInstance(json) as T
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
}

