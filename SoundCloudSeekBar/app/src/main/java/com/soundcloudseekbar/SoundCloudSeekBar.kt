package com.soundcloudseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.format.DateUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View

class SoundCloudSeekBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

	fun position(p: Float) {
		_pos = p
		_seek_pos = _pos
		redraw()
	}

	fun position(): Float {
		return _pos
	}

	fun length(seconds: Int) {
		_length = seconds
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

		_paint.style = Paint.Style.FILL

		if (_pos == _seek_pos) {
			drawPassed(_pos, canvas)
			drawRemain(_pos, canvas)
			drawPosition(_pos, canvas)
		}
		else {
			drawPassed(_seek_pos, canvas)
			drawRemain(_seek_pos, canvas)
			drawSeek(canvas)
			drawPosition(_seek_pos, canvas)
		}
	}

	private fun drawPosition(pos: Float, canvas: Canvas) {
		val textSize = dpToPx(18f)  // TODO: use sp (not dp)
		_paint.color = Color.BLACK
		_paint.textAlign = Paint.Align.CENTER
		_paint.textSize = textSize
		val width = measuredWidth
		val seconds = Math.ceil((pos * _length).toDouble()).toInt()
		canvas.drawText("${positionToString(seconds)}|${positionToString(_length)}",
			width/2f, 100 - (100 - textSize)/2f, _paint)
	}

	private fun positionToString(seconds: Int): String {
		return DateUtils.formatElapsedTime(seconds.toLong())
	}

	private fun drawPassed(pos: Float, canvas: Canvas) {
		_paint.color = _passedColor
		val width = measuredWidth
		val passedWidth = pos * width
		val left = Math.max(width/2f - passedWidth, 0f)
		canvas.drawRect(left, 0f, width/2f, 100f, _paint)
	}

	private fun drawRemain(pos: Float, canvas: Canvas) {
		_paint.color = _remainColor
		val width = measuredWidth
		val passedWidth = pos * width
		val remainWidth = Math.min(width - passedWidth, width/2f)
		canvas.drawRect(width/2f, 0f, width/2f + remainWidth, 100f, _paint)
	}

	private fun drawSeek(canvas: Canvas) {
		val width = measuredWidth

		if (_seek_pos < _pos) {  // back seek
			_paint.color = _backSeekColor
			val dt = _pos - _seek_pos
			canvas.drawRect(width/2f, 0f, width/2f + dt*width, 100f, _paint)
		}
		else {  // forward seek
			_paint.color = _forwardSeekColor
			val dt = _seek_pos - _pos
			canvas.drawRect(width/2f - dt*width, 0f, width/2f, 100f, _paint)
		}
	}

	/*override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
	}*/

	override fun onTouchEvent(event: MotionEvent): Boolean {
		if (event.action == MotionEvent.ACTION_DOWN) {
			_down_pos = arrayOf(event.getX(0), event.getY(0))
			return true
		}
		else if (event.action == MotionEvent.ACTION_MOVE) {
			val pos = arrayOf(event.getX(0), event.getY(0))
			val seek = ((pos[0] - _down_pos[0]) / measuredWidth) * -1f  // revert direction
			_seek_pos = clamp(_seek_pos + seek, 0f, 1f)
			_down_pos = pos
			redraw()
		}
		else if (event.action == MotionEvent.ACTION_UP) {
			_pos = _seek_pos
			redraw()
		}

		return super.onTouchEvent(event)
	}

	private fun clamp(x: Float, minVal: Float, maxVal: Float): Float {
		return Math.max(Math.min(x, maxVal), minVal)
	}

	private fun redraw() {
		invalidate()
	}

	private fun dpToPx(dp: Float): Float {
		return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
	}

	private var _length = 1  // in s
	private var _pos = 0.0f  // from 0 to 1
	private var _seek_pos = _pos  // position when seeking (becomes _pos on ACTION_UP event)
	private val _paint = Paint()
	private var _down_pos = arrayOf(0f, 0f)  // x, y

	// style
	private val _passedColor: Int = Color.RED
	private val _remainColor: Int = Color.LTGRAY
	private val _forwardSeekColor: Int = Color.YELLOW
	private val _backSeekColor: Int = Color.GRAY
}