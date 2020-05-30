package hk.com.cft.sfc_inventory_application.Util.Database

import org.json.JSONObject
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

abstract class DataBaseModel(){
    companion object{
        fun getJSONObjectValue(jsonObject: JSONObject, field:String, type:Type):Any?{
            if(jsonObject.has(field)){
                when(type){
                    Type.STRING -> {
                        if(jsonObject.getString(field) == "null") return null
                        return jsonObject.getString(field)
                    }
                    Type.INTEGER -> return jsonObject.getInt(field)
                    Type.DOUBLE -> return jsonObject.getDouble(field)
                    Type.DATE -> return  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString(field))
                    Type.BOOLEAN -> return if(jsonObject.getInt(field).equals(1)) true else false
                }
            }
            return null
        }
    }
    fun getDate(date: Date):String{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
    }

    enum class Type{
        STRING, INTEGER, DOUBLE, DATE, BOOLEAN
    }

}