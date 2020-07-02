package hk.com.chengwailim.basecomponents.Components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import hk.com.chengwailim.basecomponents.R

class RangeGraph : View {
    // Graphics
    protected var colorNeutral = resources.getColor(R.color.black)
    protected var colorInRange = resources.getColor(R.color.red)
    // State
    private var mMin = 0
    private var mMax = 100
    // Simple accessors
    var value = 50
    private val mFontSize = 10
    private var mPaint: Paint? = null

    // Layout
    private var mWidth = 200
    private var mHeight = 500

    constructor(context: Context) : super(context) {
        commonSetup()
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // Then allow overrides from XML
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RangeGraph,
            0, 0
        )
        try {
            colorNeutral = a.getColor(R.styleable.RangeGraph_colorNeutral, Color.BLACK)
            colorInRange = a.getColor(R.styleable.RangeGraph_colorInRange, Color.RED)
            mMin = a.getInteger(R.styleable.RangeGraph_minimum, 0)
            mMax = a.getInteger(R.styleable.RangeGraph_maximum, 100)
        } finally {
            a.recycle()
        }
        commonSetup()
    }

    private fun commonSetup() {
        mPaint = Paint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = colorNeutral
        // Scale the desired text size to match screen density
        mPaint!!.textSize = mFontSize * resources.displayMetrics.density
        //mPaint.setStrokeWidth(2f);
        setPadding(PADDING_5, PADDING_5, PADDING_5, PADDING_5)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //setMeasuredDimension(mWidth, mHeight);
        val parentWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        mWidth = parentWidth / 2
        val parentHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        mHeight = parentHeight
        this.setMeasuredDimension(parentWidth, parentHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val stringMax = Integer.toString(mMax)
        val stringMin = Integer.toString(mMin)
        val top = paddingTop
        val bot = mHeight - paddingBottom
        val leftSide = (mWidth * 0.75f).toInt()
        val rightSide = (mWidth * 1.25f).toInt()
        val oldStyle = mPaint!!.style
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        mPaint!!.color = colorNeutral
        canvas.drawRect(leftSide.toFloat(), top.toFloat(), rightSide.toFloat(), bot.toFloat(), mPaint!!)
        mPaint!!.style = oldStyle
        mPaint!!.color = colorInRange
        val barHeight = (value.toFloat() / mMax * mHeight).toInt()
        canvas.drawRect(leftSide.toFloat(), (bot - barHeight).toFloat(), rightSide.toFloat(), bot.toFloat(), mPaint!!)
        mPaint!!.style = oldStyle
        mPaint!!.color = colorInRange
        val fm = mPaint!!.fontMetrics
        val fontHeight = fm.ascent + fm.descent
        mPaint!!.color = FONT_COLOR
    }

    public fun refresh(value: Int){
        this.value = value;
        this.invalidate()
        this.requestLayout()
    }

    companion object {
        private val TAG = "RangeGraph"
        private val PADDING_5 = 5
        private val WIDTH_OF_LINE = 25
        private val FONT_COLOR = Color.BLACK
    }
}
