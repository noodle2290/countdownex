package app.maeda.rurina.newcountdowntimer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import app.maeda.rurina.newcountdowntimer.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater).apply { setContentView(this.root) }
        val sound1 = MediaPlayer.create(this, R.raw.sound1)
        val sound2 = MediaPlayer.create(this, R.raw.sound2)
        //バイブレーションのインスタンス生成
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        //プルダウンの選択肢を作成
        val spinnerItems = arrayOf(
            "さあいくぞ",
            "やるじゃないか"
        )
        //サウンドかバイブか判別する変数
        var select = 0
        var item: String = "さあいくぞ"
        //プルダウンのadapter設定
        val adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            spinnerItems
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // spinner に adapter をセット
        binding.spinner.adapter = adapter
        // リスナーを登録
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //　アイテムが選択された時
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                val spinnerParent = parent as Spinner
                item = spinnerParent.selectedItem as String
            }

            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.soundButton.setOnClickListener {
            select = 1
            //ボタンの色を変更
            binding.soundButton.setBackgroundColor(Color.RED)
            binding.vibrationButton.setBackgroundColor(Color.BLUE)
            //プルダウンの選択によってなる音を変更
            when (item) {
                "さあいくぞ" -> {
                    sound1.seekTo(0)
                    sound1.start()
                }
                "やるじゃないか" -> {
                    sound2.seekTo(0)
                    sound2.start()
                }
            }

            binding.vibrationButton.setOnClickListener {
                select = 2
                //ボタンの色を変更
                binding.vibrationButton.setBackgroundColor(Color.RED)
                binding.soundButton.setBackgroundColor(Color.BLUE)
                //バージョンによってバイブレーションの鳴らし方を変える
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val vibrationEffect = VibrationEffect.createOneShot(300, DEFAULT_AMPLITUDE)
                    vibrator.vibrate(vibrationEffect)
                } else {
                    vibrator.vibrate(300)
                }
            }

            //HomeActivityからMainActivityに移動
            binding.startButton1.setOnClickListener {
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                //hourEditTextの内容によって値渡しの数値を変更
                if (binding.hourEditText.text.toString() == "") {
                    mainActivityIntent.putExtra("hour", 0)
                } else {
                    mainActivityIntent.putExtra(
                        "hour",
                        binding.hourEditText.text.toString().toLong()
                    )
                }
                //minuteEditTextの内容によって値渡しの数値を変更
                if (binding.minuteEditText.text.toString() == "") {
                    mainActivityIntent.putExtra("minute", 0)
                } else {
                    mainActivityIntent.putExtra(
                        "minute",
                        binding.minuteEditText.text.toString().toLong()
                    )
                }
                //secondEditTextの内容によって値渡しの数値を変更
                if (binding.secondEditText.text.toString() == "") {
                    mainActivityIntent.putExtra("second", 0)
                } else {
                    mainActivityIntent.putExtra(
                        "second",
                        binding.secondEditText.text.toString().toLong()
                    )
                }
                //サウンドかバイブかを判断する変数を値渡し
                mainActivityIntent.putExtra("select", select)
                startActivity(mainActivityIntent)
            }
        }
    }
}

