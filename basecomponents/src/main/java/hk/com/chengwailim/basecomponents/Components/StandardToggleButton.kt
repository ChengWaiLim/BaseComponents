package hk.com.chengwailim.basecomponents.Components

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import hk.com.chengwailim.basecomponents.R

class StandardToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.buttonStyle
) : androidx.appcompat.widget.AppCompatToggleButton(
    ContextThemeWrapper(
        context,
        R.style.toggleButton
    ), attrs, defStyleAttr
) {

    fun setOnStatusChangeListener(callback: (isChecked: Boolean)->Unit){
        this.setOnCheckedChangeListener { buttonView, isChecked ->
            callback.invoke(isChecked)
            if(this.isChecked) this.setBackgroundColor(context.getColor(R.color.green))
            else this.setBackgroundColor(context.getColor(R.color.red))
        }
    }
}