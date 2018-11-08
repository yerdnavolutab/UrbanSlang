package com.butul0ve.urbanslang.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.butul0ve.urbanslang.R

class WordAdapter(
    private val words: List<String>,
    private val listener: WordClickListener
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_item, null)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.bind(word)
    }

    fun updateWords(words: List<String>) {
        this.words as ArrayList<String>
        this.words.clear()
        this.words.addAll(words)
        notifyDataSetChanged()
    }

    inner class WordViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val wordButton: Button = view.findViewById(R.id.word_button)

        init {
            wordButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onWordClick(wordButton.text.toString())
        }

        fun bind(text: String) {
            wordButton.text = text
        }
    }
}