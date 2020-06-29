package hk.com.chengwailim.basecomponents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import hk.com.chengwailim.basecomponents.Components.BaseTable.Interfaces.CustomizedFilter
import hk.com.chengwailim.basecomponents.Components.BaseTable.Interfaces.SortInterface
import hk.com.chengwailim.basecomponents.RFID.RFIDReader

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var base_table = findViewById<BaseTable<Location>>(R.id.base_table)
        var testingList = TestingList(this, arrayListOf(Location("A", "E"),Location("B", "S"),Location("C", "V"),Location("D", "F"), Location("E", "A"), Location("F", "G")))
        base_table.setDataAdapter(testingList)
        base_table.setUpHeader(arrayListOf("C1", "C2"))
        RFIDReader.setUpReader(this){isConnected->}
        var a = object : SortInterface<Location> {
            override fun sortColumn(data: Location): String {
                return data.code
            }
        }
        var b = object :SortInterface<Location>{
            override fun sortColumn(data: Location): String {
                return data.name
            }
        }
        base_table.setSortInterface(a,b)
        base_table.setFilterInterface(object: CustomizedFilter<Location> {
            override fun filter(dataList: ArrayList<Location>): ArrayList<Location> {
                return ArrayList(dataList.filter { it.code.equals("A") })
            }
        })
    }
}
