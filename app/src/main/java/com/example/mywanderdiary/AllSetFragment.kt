package com.example.mywanderdiary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class AllSetFragment: Fragment(R.layout.activity_start_allset) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.activity_start_allset_btn_start).setOnClickListener {
            val sp = requireContext().getSharedPreferences("WanderDiaryPrefs", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("KEY_ONBOARDING_STATUS", "COMPLETE")
            editor.apply()

            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}