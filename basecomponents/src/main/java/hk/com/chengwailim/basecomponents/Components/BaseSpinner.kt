package hk.com.chengwailim.basecomponents.Components

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import hk.com.chengwailim.basecomponents.R

class BaseSpinner<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.buttonStyle
) : SmartMaterialSpinner<T>(
    ContextThemeWrapper(
        context,
        R.style.standardSpinner
    ), attrs, defStyleAttr)