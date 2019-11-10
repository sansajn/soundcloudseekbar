package com.soundcloudseekbar

import android.content.Context
import android.graphics.Bitmap
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
		if (!_seeking)
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

		val duration = kotlin.system.measureNanoTime {
			drawSeekBar(canvas)
		}

		Log.d("SoundCloudSeekBar", "drawSeekBar takes ${duration/1e6}ms")
	}

	private fun drawSeekBar(canvas: Canvas) {
		val positiveHeights = Array<Float>(100, {n -> 1f})
		drawBars(positiveHeights, canvas)

		val negativeHeights = Array<Float>(100, {n -> -1f})
		drawBars(negativeHeights, canvas)
	}

	private fun drawBars(heights: Array<Float>, canvas: Canvas) {
		_paint.style = Paint.Style.FILL

		val barSize = dpToPx(4f)
		val barMargin = dpToPx(3f)

		_paint.strokeWidth = barSize

		// passed
		var seeked = 0
		if (_seek_pos != _pos)
			seeked = (Math.abs(_seek_pos - _pos) * 100).toInt()

		val width = measuredWidth

		var passed = 0
		var centerOffset = 0f
		if (_seek_pos < _pos) {  // back seek
			passed = (_seek_pos * 100).toInt()
			centerOffset = width/2f - passed * (barSize + barMargin)
		}
		else if (_seek_pos > _pos) {  // forward seek
			passed = (_pos * 100).toInt()
			centerOffset = width/2f - (passed + seeked) * (barSize + barMargin)
		}
		else {
			passed = (_pos * 100).toInt()
			centerOffset = width/2f - passed * (barSize + barMargin)
		}

		// passed
		_paint.color = _passedColor

		val height = measuredHeight

		for (n in 0 until passed) {
			val x = centerOffset + n * (barSize + barMargin) + barSize/2f
			canvas.drawLine(x, height/2f, x, (1f - heights[n]) * height/2f, _paint)
		}

		// seeked
		if (_seek_pos < _pos)
			_paint.color = _backSeekColor
		else
			_paint.color = _forwardSeekColor

		for (n in passed until (passed + seeked)) {
			val x = centerOffset + n * (barSize + barMargin) + barSize/2f
			canvas.drawLine(x, height/2f, x, (1f - heights[n]) * height/2f, _paint)
		}

		// remain
		_paint.color = _remainColor

		for (n in (passed + seeked) until 100) {
			val x = centerOffset + n * (barSize + barMargin) + barSize/2f
			canvas.drawLine(x, height/2f, x, (1f - heights[n]) * height/2f, _paint)
		}

		if (_seek_pos != _pos)
			drawPosition(_seek_pos, canvas)
		else
			drawPosition(_pos, canvas)
	}

	private fun drawPosition(pos: Float, canvas: Canvas) {
		val textSize = dpToPx(18f)  // TODO: use sp (not dp)
		_paint.color = Color.BLACK
		_paint.textAlign = Paint.Align.CENTER
		_paint.textSize = textSize
		val width = measuredWidth
		val height = measuredHeight
		val seconds = Math.ceil((pos * _length).toDouble()).toInt()
		canvas.drawText("${positionToString(seconds)}|${positionToString(_length)}",
			width/2f, (height + textSize)/2f, _paint)
	}

	/*override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
	}*/

	override fun onTouchEvent(event: MotionEvent): Boolean {
		Log.d("SoundCloudSeekBar",
			"${MotionEvent.actionToString(event.action)}, deviceId=${event.deviceId}")

		if (event.action == MotionEvent.ACTION_DOWN) {
			_down_pos = arrayOf(event.getX(0), event.getY(0))
			return true
		}
		else if (event.action == MotionEvent.ACTION_MOVE) {
			val pos = arrayOf(event.getX(0), event.getY(0))
			val seek = ((pos[0] - _down_pos[0]) / measuredWidth) * -1f  // revert direction
			_seek_pos = clamp(_seek_pos + seek, 0f, 1f)
			_down_pos = pos
			_seeking = true
			redraw()
		}
		else if (event.action == MotionEvent.ACTION_UP) {
			_pos = _seek_pos
			_seeking = false
			redraw()
		}

		return super.onTouchEvent(event)
	}

	private fun redraw() {
		invalidate()
	}

	// helpers
	private fun clamp(x: Float, minVal: Float, maxVal: Float): Float {
		return Math.max(Math.min(x, maxVal), minVal)
	}

	private fun positionToString(seconds: Int): String {
		return DateUtils.formatElapsedTime(seconds.toLong())
	}

	private fun dpToPx(dp: Float): Float {
		return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
	}

	private fun pxToDp(px: Float): Float {
		return px / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
	}

	private var _length = 1  // in s
	private var _pos = 0.0f  // from 0 to 1

	// seeking logic
	private var _seeking = false
	private var _seek_pos = _pos  // position when seeking (becomes _pos on ACTION_UP event)
	private var _down_pos = arrayOf(0f, 0f)  // x, y

	private val _paint = Paint()

	// style
	private val _passedColor: Int = Color.parseColor("#FF3400")
	private val _remainColor: Int = Color.parseColor("#F2F2F2")
	private val _forwardSeekColor: Int = Color.parseColor("#C62801")
	private val _backSeekColor: Int = Color.parseColor("#BBBBBB")
	private var _waveApprox: Bitmap? = null
}