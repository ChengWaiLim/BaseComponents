package hk.com.chengwailim.basecomponents.Components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import hk.com.chengwailim.basecomponents.R
import kotlinx.android.synthetic.main.card_button.view.*

class CardButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context,
            R.layout.card_button, this)
        attrs?.let {
            val attributes = context.obtainStyledAttributes(it,
                R.styleable.CardButton, 0, 0)
            roundedIcon.setCardBackgroundColor(attributes.getInt(R.styleable.CardButton_iconColor, 0))
            textView.setText(attributes.getString(R.styleable.CardButton_text))
            imageIcon.setImageDrawable(attributes.getDrawable(R.styleable.CardButton_iconPath))
            roundedIcon.radius = (roundedIcon.layoutParams.width/2).toFloat()
        }
    }

    public fun setOnClickLinstener(listener: OnClickListener){
        cardView.setOnClickListener(listener)
    }
}