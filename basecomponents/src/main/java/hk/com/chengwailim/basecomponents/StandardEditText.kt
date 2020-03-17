package hk.com.chengwailim.basecomponents

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.standard_edit_text.view.*

open class StandardEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        initView(context, attrs, defStyleAttr)
        standard_edit_text.setSelectAllOnFocus(true)
        standard_edit_text.setOnFocusChangeListener({ view, isFocused ->
            if (isFocused)
                standard_edit_text.selectAll()
        })
    }

    open fun initView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0){
        View.inflate(context, R.layout.standard_edit_text, this)
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.StandardEditText, 0, 0)
            text_view_title.setText(attributes.getString(R.styleable.StandardEditText_title))
            standard_edit_text.inputType = context.obtainStyledAttributes(it, intArrayOf(android.R.attr.inputType) ,0, 0).getInt(0, InputType.TYPE_CLASS_TEXT)
            standard_edit_text.setText(context.obtainStyledAttributes(it, intArrayOf(android.R.attr.text) ,0, 0).getString(0))
        }
    }

    fun setMaxCharacters(maxCharacters: Int){
        standard_edit_text.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxCharacters))
    }
    fun setKeyListener(listener: OnKeyListener){
        standard_edit_text.setOnKeyListener(listener)
    }

    open fun editTextRequestFocus(){
        standard_edit_text.requestFocus()
    }

    open fun getText(): String{
        return standard_edit_text.text.toString()
    }

    open fun setText(text:String){
        standard_edit_text.setText(text)
    }

}