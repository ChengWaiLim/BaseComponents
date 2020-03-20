package hk.com.chengwailim.basecomponents

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
class StandardButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = androidx.appcompat.R.attr.buttonStyle) : androidx.appcompat.widget.AppCompatButton(ContextThemeWrapper(context, R.style.standardButton ), attrs, defStyleAttr) {}
