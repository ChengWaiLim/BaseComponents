package hk.com.chengwailim.basecomponents.Components.BaseTable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import hk.com.chengwailim.basecomponents.R
import kotlinx.android.synthetic.main.table_header.view.*

class TableHeader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var status = SortStatus.NORMAL
    init {
        View.inflate(context, R.layout.table_header, this)
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.TableHeader, 0, 0)
            attributes.getString(R.styleable.TableHeader_title)?.let {title->
                setText(title.toString())
            }
        }
    }
    fun sortOnClickListener(callback: (status: SortStatus)->Unit){
        this.setOnClickListener {
            when(this.status){
                SortStatus.NORMAL -> this.status =
                    SortStatus.ASCENDING
                SortStatus.ASCENDING ->this.status =
                    SortStatus.DESCENDING
                SortStatus.DESCENDING ->this.status =
                    SortStatus.NORMAL
            }
            setSortStatus(this.status)
            callback.invoke(this.status)
        }
    }

    fun setSortStatus(status: SortStatus){
        when(status){
            SortStatus.NORMAL -> sort_icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sort))
            SortStatus.ASCENDING -> sort_icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sort_up))
            SortStatus.DESCENDING -> sort_icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sort_down))
        }
        this.status = status
    }

    fun setText(text:String){
        text_view.setText(text)
    }
    enum class SortStatus{
        NORMAL, ASCENDING, DESCENDING
    }

}