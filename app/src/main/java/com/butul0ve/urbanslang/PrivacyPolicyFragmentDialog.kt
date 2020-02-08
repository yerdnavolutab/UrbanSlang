package com.butul0ve.urbanslang

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.butul0ve.urbanslang.utils.SharedPreferencesManager

const val PRIVACY_POLICY_ACCEPTED = "privacy_policy_accepted_key"
const val IS_USER_CHOICE = "is_user_choice_key"

class PrivacyPolicyFragmentDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var listener: PrivacyPolicyOnClickListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = context as PrivacyPolicyOnClickListener
        } catch (ex: ClassCastException) {
            throw ClassCastException("$context must implement PrivacyPolicyOnClickListener")
        }
    }

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
        listener.initStatistics()
        dialog.dismiss()
    }

    private fun clickNo() {
        writeToSharedPreferences(false)
        listener.disableStatistics()
        dialog.dismiss()
    }

    private fun writeToSharedPreferences(value: Boolean) {
        activity?.let {
            SharedPreferencesManager.putBoolean(it, PRIVACY_POLICY_ACCEPTED, value)
            SharedPreferencesManager.putBoolean(it, IS_USER_CHOICE, true)
        }
    }

    interface PrivacyPolicyOnClickListener {

        fun initStatistics()

        fun disableStatistics()
    }
}