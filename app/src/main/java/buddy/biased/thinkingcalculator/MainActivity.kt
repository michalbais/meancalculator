package buddy.biased.thinkingcalculator


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var historyShown = false
    var timepassed = 1000
    var dbHelper = SQLiteDatabaseHelper(this)

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        historyShown = savedInstanceState?.getBoolean("historyboolean")!!
        if (historyShown) {
            menu_icon_text_view.setBackgroundResource(R.drawable.ic_baseline_restore_page_24px)
        }
        else{
            menu_icon_text_view.setBackgroundResource(R.drawable.ic_baseline_description_24px)
        }
   //     history_text_view.setText(savedInstanceState?.getString("hisorytext"))
   //     robot_text_view.setText(savedInstanceState?.getString("robottext"))
    //    result_text_view.setText(savedInstanceState?.getCharSequence("resulttext"))

        robot_text_view.text = getChatMessage("onrestore")
        timepassed=0
        when((1..3).shuffled().first()) {
            1 -> robot_imageView.setImageResource(R.drawable.robotonchange)
            2 -> robot_imageView.setImageResource(R.drawable.robotonchange2)
            3 -> robot_imageView.setImageResource(R.drawable.robotonchange3)
        }

    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putBoolean("historyboolean",historyShown)
      //      putString("hisorytext",history_text_view.text.toString())
       //     putString("robottext",robot_text_view.text.toString())
       //     putCharSequence("resulttext",result_text_view.text)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        robot_text_view.text = getChatMessage("greetings")
        robot_imageView.setImageResource(R.drawable.robotgreetings)

        val handler = Handler()

        val runnableCode = object : Runnable {
            override fun run() {

                timepassed+=2000
                if (timepassed>14000){
                    robot_text_view.text = getChatMessage("random")
                    when((1..4).shuffled().first()) {
                        1 -> robot_imageView.setImageResource(R.drawable.robotrandom)
                        2 -> robot_imageView.setImageResource(R.drawable.robotrandom2)
                        3 -> robot_imageView.setImageResource(R.drawable.robotrandom3)
                        4 -> robot_imageView.setImageResource(R.drawable.robotrandom4)
                    }
                timepassed=0
                }

                handler.postDelayed(this, 2000)
            }
        }

        handler.post(runnableCode)

        if (savedInstanceState == null) {
            addFragment(CalculatorFragment.newInstance(),"Calculator")
        }

        menu_icon_text_view.setOnClickListener {
            if (!historyShown) {
                replaceFragment(HistoryFragment.newInstance(),"History")
                historyShown = true
                menu_icon_text_view.setBackgroundResource(R.drawable.ic_baseline_restore_page_24px)
            }
            else{
                replaceFragment(CalculatorFragment.newInstance(),"Calculator")
                menu_icon_text_view.setBackgroundResource(R.drawable.ic_baseline_description_24px)
                historyShown = false
            }
        }
        robot_imageView.setOnClickListener{
            robot_text_view.text = getChatMessage("tap")
            when((1..4).shuffled().first()) {
                1 -> robot_imageView.setImageResource(R.drawable.robottap)
                2 -> robot_imageView.setImageResource(R.drawable.robottap2)
                3 -> robot_imageView.setImageResource(R.drawable.robottap3)
                4 -> robot_imageView.setImageResource(R.drawable.robottap4)
            }
            timepassed=0
        }

    }

    fun getChatMessage(category: String): String {
        return dbHelper.getRandomSpeechByCategory(category)
    }



    fun AppCompatActivity.addFragment(fragment: Fragment, tag: String){
        supportFragmentManager
                .beginTransaction()
                .add(R.id.root_frame_layout, fragment, tag)
                .commit()
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_frame_layout, fragment, tag)
                .commit()
    }


}
