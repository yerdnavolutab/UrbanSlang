package com.butul0ve.urbanslang.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.butul0ve.urbanslang.R

const val PRIVACY_POLICY_ACCEPTED = "privacy_policy_accepted_key"
const val IS_USER_CHOICE = "is_user_choice_key"

class PrivacyPolicyFragment: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.policy_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.yes_btn).setOnClickListener { clickYes() }
        view.findViewById<Button>(R.id.no_btn).setOnClickListener { clickNo() }
    }

    private fun clickYes() {
        writeToSharedPreferences(true)
        dialog.dismiss()
    }

    private fun clickNo() {
        writeToSharedPreferences(false)
        dialog.dismiss()
    }

    private fun writeToSharedPreferences(value: Boolean) {
        activity?.let {
            val sharedPreferences = it.getSharedPreferences(it.packageName, Context.MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putBoolean(PRIVACY_POLICY_ACCEPTED, value)
                putBoolean(IS_USER_CHOICE, true)
                apply()
            }
        }
    }
}