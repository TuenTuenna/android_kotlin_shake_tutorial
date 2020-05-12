package com.jeongdaeri.shake_practice

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import render.animations.Attention
import render.animations.Bounce
import render.animations.Render
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {


    val TAG: String = "로그"

    // 센서매니저
    private lateinit var sensorManager: SensorManager

    private var accel: Float = 0.0f
    private var accelCurrent: Float = 0.0f
    private var accelLast: Float = 0.0f



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "MainActivity - onCreate() called")

        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        accel = 10f
        accelCurrent = SensorManager.GRAVITY_EARTH
        accelLast = SensorManager.GRAVITY_EARTH

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d(TAG, "MainActivity - onAccuracyChanged() called")

    }

    override fun onSensorChanged(event: SensorEvent?) {
//        Log.d(TAG, "MainActivity - onSensorChanged() called")



        val x: Float = event?.values?.get(0) as Float
        val y: Float = event?.values?.get(1) as Float
        val z: Float = event?.values?.get(2) as Float

        //
        x_text.text = "X: " + x.toInt().toString()
        y_text.text = "Y: " + y.toInt().toString()
        z_text.text = "Z: " + z.toInt().toString()

        accelLast = accelCurrent

        accelCurrent = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        val delta: Float = accelCurrent - accelLast

        accel = accel * 0.9f + delta

        if (accel > 30){
            Log.d(TAG, "MainActivity - 흔들었음")
            Log.d(TAG, "MainActivity - accel : ${accel}")

            face_img.setImageResource(R.drawable.ic_face_smile)

            // Create Render Class
            val render = Render(this)

            // Set Animation
            render.setAnimation(Attention().Wobble(face_img))
            render.start()

            Handler().postDelayed({

                face_img.setImageResource(R.drawable.ic_face)

            }, 1000L)


        }


    }



    override fun onResume() {

        Log.d(TAG, "MainActivity - onResume() called")
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

        super.onResume()
    }

    override fun onPause() {

        Log.d(TAG, "MainActivity - onPause() called")
        sensorManager.unregisterListener(this)

        super.onPause()
    }

}
