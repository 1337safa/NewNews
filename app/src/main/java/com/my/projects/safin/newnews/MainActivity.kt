package com.my.projects.safin.newnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.my.projects.safin.newnews.databinding.ActivityMainBinding
import com.my.projects.safin.newnews.fragments.MainContent

class MainActivity(): AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        openFragment(R.id.main_frame_layout, MainContent.getInstance())
    }

    private fun openFragment(placeHolder: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(placeHolder, fragment).commit()
    }
}
