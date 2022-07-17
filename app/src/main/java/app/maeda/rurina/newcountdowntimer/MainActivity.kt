package app.maeda.rurina.newcountdowntimer

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.view.isVisible
import app.maeda.rurina.newcountdowntimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var timer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }
        val sound = MediaPlayer.create(this, R.raw.sound1)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        //値渡しでタイマー時間を受け取り
        val hour: Long = intent.getLongExtra("hour", 0);
        val minute: Long = intent.getLongExtra("minute", 0);
        val second: Long = intent.getLongExtra("second", 0);
        //サウンドかバイブかの判断
        val select: Int = intent.getIntExtra("select", 0);

        //ビューに設定時間を表示、padStartで0埋め
        binding.timeTextView.text = "${hour.toString().padStart(2, '0')}:${
            minute.toString().padStart(2, '0')
        }:${second.toString().padStart(2, '0')}"

        //設定時間を秒数に変換
        var secondAll: Long = hour * 3600 + minute * 60 + second


        binding.stopButton.isVisible = false

        binding.startButton.setOnClickListener {
            binding.startButton.isVisible = false
            binding.stopButton.isVisible = true
            timer = object : CountDownTimer(secondAll * 1000, 1000) {
                //タイマー終了時の処理
                override fun onFinish() {
                    binding.startButton.isVisible = true
                    secondAll = hour * 3600 + minute * 60 + second
                    binding.timeTextView.text = "${hour.toString().padStart(2, '0')}:${
                        minute.toString().padStart(2, '0')
                    }:${second.toString().padStart(2, '0')}"
                    when (select) {
                        1 ->{
                            sound.seekTo(0)
                            sound.start()
                        }
                        2 -> {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                val vibrationEffect = VibrationEffect.createOneShot(
                                    300,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                                vibrator.vibrate(vibrationEffect)
                            } else {
                                vibrator.vibrate(300)
                            }
                        }
                    }
                }

                //1秒おきの処理
                override fun onTick(millisUntilFinished: Long) {
                    secondAll--
                    binding.timeTextView.text = "${
                        (secondAll / 3600).toString().padStart(2, '0')
                    }:${
                        ((secondAll % 3600) / 60).toString().padStart(2, '0')
                    }:${((secondAll % 3600) % 60).toString().padStart(2, '0')}"
                }
            }
            timer.start()
        }

        //ストップボタン処理
        binding.stopButton.setOnClickListener {
            binding.startButton.isVisible = true
            binding.stopButton.isVisible = false
            timer.cancel()
        }

        //リセットボタン処理
        binding.resetButton.setOnClickListener {
            timer.cancel()
            binding.startButton.isVisible = true
            binding.stopButton.isVisible = false
            binding.timeTextView.text = "${hour.toString().padStart(2, '0')}:${
                minute.toString().padStart(2, '0')
            }:${second.toString().padStart(2, '0')}"
            secondAll = hour * 3600 + minute * 60 + second
        }


    }
}
