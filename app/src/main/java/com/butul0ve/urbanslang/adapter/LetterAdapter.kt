package com.butul0ve.urbanslang.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.butul0ve.urbanslang.R

class LetterAdapter(
    private val letters: List<String>,
    private val listener: LetterClickListener
) : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.letter_item, null)
        return LetterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return letters.size
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val letter = letters[position]
        holder.bind(letter)
    }

    inner class LetterViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val letterButton: Button = view.findViewById(R.id.letter_button)

        init {
            letterButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onLetterClick(letterButton.text.toString())
        }

        fun bind(text: String) {
            letterButton.text = text
        }
    }
}