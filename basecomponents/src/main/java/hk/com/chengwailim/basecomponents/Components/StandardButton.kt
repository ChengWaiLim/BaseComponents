package hk.com.chengwailim.basecomponents.Components

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import hk.com.chengwailim.basecomponents.R

open class StandardButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = androidx.appcompat.R.attr.buttonStyle) : androidx.appcompat.widget.AppCompatButton(ContextThemeWrapper(context,
    R.style.standardButton
), attrs, defStyleAttr) {}
