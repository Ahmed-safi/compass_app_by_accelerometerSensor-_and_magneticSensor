package com.example.orientationsensor
import android.animation.ObjectAnimator
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometerSensor: Sensor? = null
    private var magneticSensor: Sensor? = null
    private var accelerometerFloatArray = FloatArray(3)
    private var magneticFloatArray = FloatArray(3)
    private var rotationArray = FloatArray(9)
    private var orientationValues = FloatArray(9)
    private lateinit var  com:ImageView
    private lateinit var  com1:ImageView
    private lateinit var  com2:ImageView
    private lateinit var  tvData:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        com = findViewById(R.id.imageView)
        com1 = findViewById(R.id.image1)
        com2 = findViewById(R.id.image2)
        tvData = findViewById(R.id.tvData)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    }

    override fun onResume() {
        super.onResume()
        if (accelerometerSensor != null && magneticSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI)
            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        if (accelerometerSensor != null && magneticSensor != null) {
            sensorManager.unregisterListener(this)
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelerometerFloatArray = p0.values
        }
        else if (p0.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFloatArray = p0.values
        }
        SensorManager.getRotationMatrix(rotationArray, null, accelerometerFloatArray, magneticFloatArray)
        SensorManager.getOrientation(rotationArray, orientationValues)
        val zAzimuth = orientationValues[0]
        val xPitch = orientationValues[1]
        val yRoll = orientationValues[2]
        compass(zAzimuth, xPitch, yRoll)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private fun compass(a: Float, p: Float, r: Float) {
        val degree = Math.toDegrees(a.toDouble())
        val degree1 = Math.toDegrees(p.toDouble())
        val degree2 = Math.toDegrees(r.toDouble())
        val objectAnimator = ObjectAnimator.ofFloat(com, "rotation", -degree.toFloat())
        val objectAnimator1 = ObjectAnimator.ofFloat(com1, "translationY", degree1.toFloat())
        val objectAnimator2 = ObjectAnimator.ofFloat(com1, "translationX", degree2.toFloat())
        objectAnimator.duration = 100
        objectAnimator1.duration = 200
        objectAnimator2.duration = 200
        objectAnimator.start()
        objectAnimator1.start()
        objectAnimator2.start()
        tvData.text = degree.toInt().toString()
        if (degree >= 0){
            tvData.toString()
        }else{
            tvData.text = (degree+360).toInt().toString()
        }

    }
}