package com.car.sharing.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.car.sharing.R
import kotlinx.android.synthetic.main.home_activity.*

class Home : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        logout_button.setOnClickListener {

        }
    }
}
