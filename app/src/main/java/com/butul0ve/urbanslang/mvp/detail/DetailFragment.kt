package com.butul0ve.urbanslang.mvp.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.butul0ve.urbanslang.R
import com.butul0ve.urbanslang.bean.Definition
import com.butul0ve.urbanslang.db.AppDbHelper
import com.butul0ve.urbanslang.db.UrbanDatabase

private const val DEFINITION = "definition_extra_key"

class DetailFragment: Fragment(), DetailMvpView {

    private lateinit var wordTV: TextView
    private lateinit var definitionTV: TextView
    private lateinit var authorTV: TextView
    private lateinit var permalinkTV: TextView
    private lateinit var favIV: ImageView

    private lateinit var presenter: DetailMvpPresenter<DetailMvpView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = AppDbHelper(UrbanDatabase.getInstance(context!!)!!)
        presenter = DetailPresenter(dbHelper)
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

                if (::presenter.isInitialized) {
                    presenter.onAttach(this)
                    definition.id?.let { presenter.onViewInitialized(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::presenter.isInitialized) {
            presenter.onDetach()
        }
    }

    override fun setFav(isFav: Boolean) {
        if (isFav) {
            favIV.setImageResource(R.drawable.favorite_black)
            favIV.tag = R.drawable.favorite_black
        } else {
            favIV.setImageResource(R.drawable.favorite_white)
            favIV.tag = R.drawable.favorite_white
        }
    }

    override fun changeFavImg() {
        when(favIV.tag) {
            R.drawable.favorite_black -> {
                favIV.setImageResource(R.drawable.favorite_white)
                favIV.tag = R.drawable.favorite_white
            }
            else -> {
                favIV.setImageResource(R.drawable.favorite_black)
                favIV.tag = R.drawable.favorite_black
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