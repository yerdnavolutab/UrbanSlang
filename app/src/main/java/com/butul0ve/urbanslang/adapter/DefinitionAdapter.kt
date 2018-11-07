package com.butul0ve.urbanslang.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.butul0ve.urbanslang.R
import com.butul0ve.urbanslang.bean.Definition

class DefinitionAdapter(
    val definitions: List<Definition>,
    private val clickListener: DefinitionClickListener
) :
    RecyclerView.Adapter<DefinitionAdapter.DefinitionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.definition_item, null)
        return DefinitionHolder(view)
    }

    override fun onBindViewHolder(holder: DefinitionHolder, position: Int) {
        val definition = definitions[position]
        holder.bind(definition)
    }

    override fun getItemCount(): Int {
        return definitions.size
    }

    inner class DefinitionHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private val wordTV: TextView = view.findViewById(R.id.word_TV)
        private val definitionTV: TextView = view.findViewById(R.id.definition_TV)
        private val exampleTV: TextView = view.findViewById(R.id.example_TV)
        private val authorTV: TextView = view.findViewById(R.id.author_TV)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onItemClick(layoutPosition)
        }

        fun bind(definition: Definition) {
            wordTV.text = definition.word
            definitionTV.text = definition.definition
            exampleTV.text = definition.example
            authorTV.text = definition.author
        }
    }
}