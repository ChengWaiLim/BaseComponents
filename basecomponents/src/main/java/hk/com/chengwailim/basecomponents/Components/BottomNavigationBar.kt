package hk.com.chengwailim.basecomponents.Components

import android.content.Context
import android.util.AttributeSet
import android.view.Menu
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class BottomNavigationBar@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr){
    fun init(controller: NavController, modelList: ArrayList<NavigationModel>){
        for (i in 0 until modelList.size ){
            addNavigationModel(i, modelList.get(i))
        }
        this.setOnNavigationItemSelectedListener {
            controller.navigate(modelList.get(it.itemId).fragment_id)
            true
        }
    }

    private fun addNavigationModel(itemID:Int, model: NavigationModel){
        this.getMenu().add(Menu.NONE, itemID, Menu.NONE, model.title).setIcon(model.icon);
    }

    data class NavigationModel(var title:String, var icon:Int, var fragment_id: Int)
}