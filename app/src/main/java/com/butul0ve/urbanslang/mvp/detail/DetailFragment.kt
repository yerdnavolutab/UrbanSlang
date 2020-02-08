package com.butul0ve.urbanslang.mvp.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.butul0ve.urbanslang.R
import com.butul0ve.urbanslang.UrbanSlangApp
import com.butul0ve.urbanslang.bean.Definition
import javax.inject.Inject

private const val DEFINITION = "definition_extra_key"

class DetailFragment: androidx.fragment.app.Fragment(), DetailMvpView {

    @Inject
    lateinit var presenter: DetailMvpPresenter<DetailMvpView>

    private lateinit var wordTV: TextView
    private lateinit var definitionTV: TextView
    private lateinit var authorTV: TextView
    private lateinit var permalinkTV: TextView
    private lateinit var favIV: ImageView

    private var definitionId = -1L

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        UrbanSlangApp.netComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        wordTV = view.findViewById(R.id.word_TV)
        definitionTV = view.findViewById(R.id.definition_TV)
        authorTV = view.findViewById(R.id.author_TV)
        permalinkTV = view.findViewById(R.id.permalink_TV)
        favIV = view.findViewById(R.id.fav_IV)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null && arguments?.containsKey(DEFINITION)!!) {
            val definition = arguments?.getParcelable<Definition>(DEFINITION)
            if (definition != null) {
                wordTV.text = definition.word
                definitionTV.text = definition.definition
                authorTV.text = definition.author
                permalinkTV.text = definition.permalink
                favIV.setOnClickListener { presenter.handleClick(definition) }
                definition.id?.let { definitionId = it }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
        presenter.loadDefinition(definitionId)
    }

    override fun onStop() {
        super.onStop()
        if (::presenter.isInitialized) {
            presenter.onDetach()
        }
    }

    override fun setFav(isFav: Boolean) {
        if (isFav) {
            favIV.setImageResource(R.drawable.favorite_fill)
            favIV.tag = R.drawable.favorite_fill
        } else {
            favIV.setImageResource(R.drawable.favorite_empty)
            favIV.tag = R.drawable.favorite_empty
        }
    }

    override fun changeFavImg() {
        when(favIV.tag) {
            R.drawable.favorite_fill -> {
                favIV.setImageResource(R.drawable.favorite_empty)
                favIV.tag = R.drawable.favorite_empty
            }
            else -> {
                favIV.setImageResource(R.drawable.favorite_fill)
                favIV.tag = R.drawable.favorite_fill
            }
        }
    }

    companion object {

        fun newInstance(definition: Definition): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putParcelable(DEFINITION, definition)
            fragment.arguments = args
            return fragment
        }
    }
}