package com.example.twicephotocards

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.json.JSONObject

class Wszystkie : AppCompatActivity() {

    private val GESTURE_THRESHOLD_DP = 16.0f
    private var mGestureThreshold: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wszystkie)
        val scale: Float = resources.displayMetrics.density
        mGestureThreshold = (GESTURE_THRESHOLD_DP * scale + 0.5f).toInt()
        getPersons()
    }

    fun getPersons() {
        GlobalScope.launch {
            val nc = NetworkConnection()
            var result: String = ""
            CoroutineScope(Dispatchers.IO).launch {
                result = nc.getResult(prefs.baseUrl + "api/categories")
                val cats = JSONObject(result.toString()).getJSONArray("data")
                for (i in 0 until cats.length()) {
                    val cat = cats.getJSONObject((i))
                    val cat_id = cat.getInt("id")
                    val cat_name = cat.getString("name")
                    withContext(Dispatchers.Main) {
                        addButtonToStage(cat_id, cat_name)
                    }
                }
            }
        }
    }

    fun addButtonToStage(id: Int, name: String)
    {
        val layout: LinearLayout = findViewById(R.id.catContainer)
        val button = Button(ContextThemeWrapper(this, R.style.Widget_AppCompat_Button_Colored), null, 0)
        button.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            5*mGestureThreshold)
        button.text = name
        button.tag = id
        button.setOnClickListener(View.OnClickListener { v ->
            val b: Button = v as Button
            val catId:String = v.getTag().toString()
            val catName:String = v.getText().toString()
            val intent = Intent(this, showCards::class.java)
            intent.putExtra("selectedCategory", catId as String)
            intent.putExtra("categoryName", catName)
            startActivity(intent)
        })
        layout.addView(button)
    }



}
