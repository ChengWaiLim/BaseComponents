package hk.com.chengwailim.basecomponents.Components

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.baoyz.swipemenulistview.SwipeMenuListView
import hk.com.chengwailim.basecomponents.R
import hk.com.chengwailim.basecomponents.Util.BaseListView

abstract class BaseCheckBoxList<T>(
    private val context: Context,
    private val dataList: ArrayList<T>,
    private val list_view: SwipeMenuListView,
    private var list_item_view: Int
) : BaseListView<T>(context, dataList, list_view, list_item_view) {

    override fun getView(view: View, instance: T, position: Int): View {
        val checkBox = getCheckBox(view)
        checkBox.isChecked = checkOrNot(position)
        dimedCheckBox(checkBox, position)
        checkBox.setOnCheckedChangeListener({ compoundButton, checked ->
            if (compoundButton.isPressed) {
                if (checked) {
                    onCheckBoxClickAddListener(instance)
                    addCheck(checkBox, position)
                } else {
                    removeCheck(checkBox, position)
                    onCheckBoxRemoveAddListener(instance)
                }
            }
            onCheckBoxClickListener(view, instance, position, checkBox)
        })
        return getView(view, instance, position, checkBox)
    }

    abstract fun getView(view: View, instance: T, position: Int, checkBox: CheckBox): View

    abstract fun getCheckBox(view: View): CheckBox

    abstract fun onCheckBoxClickAddListener(instance: T)

    abstract fun onCheckBoxRemoveAddListener(instance: T)

    abstract fun onCheckBoxClickListener(view: View, instance: T, position: Int, checkBox: CheckBox)


    var map = HashMap<Int, Boolean>()
    var dimMap = HashMap<Int, Boolean>()

    protected fun addCheck(checkBox: CheckBox, position: Int) {
        checkBox.isChecked = true
        map.put(position, true)
    }

    protected fun checkOrNot(position: Int): Boolean {
        return if (map.get(position) != null) map.get(position)!! else false
    }

    protected fun removeCheck(checkBox: CheckBox, position: Int) {
        checkBox.isChecked = false
        map.remove(position)
    }

    protected fun dimedCheckBox(checkBox: CheckBox, position: Int) {
        checkBox.isEnabled = (!dimMap.get(position)!!)
        if(!checkBox.isEnabled) checkBox.isChecked = dimMap.get(position)!!
    }

}