package com.soundcloudseekbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

	private val SIM_LENGTH = 5*60+32  // in seconds

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		timeLine0.position(0f)
		timeLine0.length(5*60 + 32)

		timeLine1.position(0.2f)
		timeLine1.length(5*60 + 32)

		timeLine2.position(0.5f)
		timeLine2.length(5*60 + 32)

		timeLine3.position(0.7f)
		timeLine3.length(5*60 + 32)

		timeLine4.position(1f)
		timeLine4.length(5*60 + 32)

		playButton.setOnClickListener { runPlaybackSimulation() }
		stopButton.setOnClickListener { stopPlaybackSimulation() }
	}

	private fun runPlaybackSimulation() {
		timeLine0.position(0f)
		timeLine0.length(SIM_LENGTH)

		_scheduler = Timer()

		val progressUpdate = object : TimerTask() {
			override fun run() {
				runOnUiThread {
					_seconds += 1
					val pos = Math.min(_seconds / SIM_LENGTH.toFloat(), 1f)
					timeLine0.position(pos)
					Log.d("SoundCloudSeekBar", "seconds=$_seconds, pos=$pos (${pos * SIM_LENGTH})")
				}
			}

			private var _seconds = 0
		}

		_scheduler?.schedule(progressUpdate, 0, 1000)
	}

	private fun stopPlaybackSimulation() {
		_scheduler?.cancel()
	}

	private var _scheduler: Timer? = null
}
