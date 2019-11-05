package com.soundcloudseekbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
	}
}
