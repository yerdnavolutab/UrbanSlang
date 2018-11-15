package com.butul0ve.urbanslang

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

const val PRIVACY_POLICY_ACCEPTED = "privacy_policy_accepted_key"
const val IS_USER_CHOICE = "is_user_choice_key"

class PrivacyPolicyFragmentDialog: DialogFragment() {

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
            val sharedPreferences = it.getSharedPreferences(it.packageName, Context.MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putBoolean(PRIVACY_POLICY_ACCEPTED, value)
                putBoolean(IS_USER_CHOICE, true)
                apply()
            }
        }
    }

    interface PrivacyPolicyOnClickListener {

        fun initStatistics()

        fun disableStatistics()
    }
}