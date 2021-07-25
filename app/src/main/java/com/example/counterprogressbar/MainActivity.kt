package com.example.counterprogressbar

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.counterprogressbar.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var timeCountInMilliSeconds = (1 * 60000).toLong()

    private enum class TimerStatus {
        STARTED, STOPPED
    }
private lateinit var binding:ActivityMainBinding
    private var timerStatus = TimerStatus.STOPPED
    private var progressBarCircle: ProgressBar? = null
    private var editTextMinute: EditText? = null
    private var textViewTime: TextView? = null
    private var imageViewReset: ImageView? = null
    private var imageViewStartStop: ImageView? = null

    private var countDownTimer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)


        // method call to initialize the views
        initViews()
        // method call to initialize the listeners
       initListeners()
    }



    /**
     * method to initialize the views
     */

    private fun initViews() {
        progressBarCircle =binding.progressBarCircle
        editTextMinute = binding.editTextMinute
        textViewTime = binding.textViewTime
        imageViewReset = binding.imageViewReset
        imageViewStartStop =binding.imageViewStartStop
    }



    /**
     * method to initialize the click listeners
     */
    private fun initListeners() {
        imageViewReset?.setOnClickListener(this)
        imageViewStartStop?.setOnClickListener(this)
    }

    /**
     * implemented method to listen clicks
     *
     * @param view
     */
    override fun onClick(view: View) {
        when (view.id) {
            R.id.imageViewReset -> reset()
            R.id.imageViewStartStop -> startStop()
        }
    }

    /**
     * method to reset count down timer
     */
    private fun reset() {
        stopCountDownTimer()
        startCountDownTimer()
    }

    /**
     * method to start and stop count down timer
     */
    private fun startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            setTimerValues()
            // call to initialize the progress bar values
            setProgressBarValues()
            // showing the reset icon
            imageViewReset!!.visibility  = View.VISIBLE
            // changing play icon to stop icon
            imageViewStartStop?.setImageResource(R.drawable.icon_stop)
            // making edit text not editable
            editTextMinute!!.isEnabled = false
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED
            // call to start the count down timer
            startCountDownTimer()
        } else {

            // hiding the reset icon
            imageViewReset!!.visibility = View.GONE
            // changing stop icon to start icon
            imageViewStartStop!!.setImageResource(R.drawable.icon_start)
            // making edit text editable
           editTextMinute!!.isEnabled = true
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED
            stopCountDownTimer()
        }
    }

    /**
     * method to initialize the values for count down timer
     */
    private fun setTimerValues() {
        var time = 0
        if (editTextMinute!!.text.toString().isNotEmpty()) {
            // fetching value from edit text and type cast to integer
            time = editTextMinute!!.text.toString().trim { it <= ' ' }.toInt()
        } else {
            // toast message to fill edit text
            Toast.makeText(
                applicationContext,
                getString(R.string.message_minutes),
                Toast.LENGTH_LONG
            ).show()
        }
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = (time * 60 * 1000).toLong()
    }

    /**
     * method to start count down timer
     */
    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(timeCountInMilliSeconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textViewTime!!.text = hmsTimeFormatter(millisUntilFinished)
                progressBarCircle!!.progress = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                textViewTime!!.text = hmsTimeFormatter(timeCountInMilliSeconds)
                // call to initialize the progress bar values
                setProgressBarValues()
                // hiding the reset icon
                imageViewReset!!.visibility = View.GONE
                // changing stop icon to start icon
                imageViewStartStop!!.setImageResource(R.drawable.icon_start)
                // making edit text editable
                editTextMinute!!.isEnabled = true
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED
            }
        }.start()
        countDownTimer?.start()
    }

    /**
     * method to stop count down timer
     */
    private fun stopCountDownTimer() {
        countDownTimer!!.cancel()
    }

    /**
     * method to set circular progress bar values
     */
    private fun setProgressBarValues() {
        progressBarCircle!!.max = timeCountInMilliSeconds.toInt() / 1000
        progressBarCircle!!.progress = timeCountInMilliSeconds.toInt() / 1000
    }

    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private fun hmsTimeFormatter(milliSeconds: Long): String {
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(milliSeconds),
            TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    milliSeconds
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    milliSeconds
                )
            )
        )
    }


}