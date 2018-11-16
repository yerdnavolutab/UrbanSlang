package com.butul0ve.urbanslang.mvp.trends

import android.content.Context
import com.butul0ve.urbanslang.R

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.butul0ve.urbanslang.UrbanSlangApp
import com.butul0ve.urbanslang.adapter.LetterAdapter
import com.butul0ve.urbanslang.adapter.WordAdapter
import javax.inject.Inject

private const val LETTER = "letter_extra_key"

class TrendsFragment : Fragment(), TrendsMvpView {

    @Inject
    lateinit var presenter: TrendsMvpPresenter<TrendsMvpView>

    private lateinit var lettersRV: RecyclerView
    private lateinit var wordsRV: RecyclerView
    private lateinit var callback: Callback
    private var letter: String = "a"

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        UrbanSlangApp.netComponent.inject(this)
        try {
            callback = context as Callback
        } catch (ex: ClassCastException) {
            throw ClassCastException("${activity?.localClassName} must implement TrendsFragment.Callback")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(LETTER, letter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_trends, container, false)
        lettersRV = view.findViewById(R.id.letters_RV)
        wordsRV = view.findViewById(R.id.words_RV)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(LETTER)) {
            letter = savedInstanceState.getString(LETTER)!!

            if (arguments == null) {
                arguments = Bundle()
            }

            arguments!!.putString(LETTER, letter)
        }

        if (arguments != null && arguments!!.containsKey(LETTER)) {
            letter = arguments!!.getString(LETTER)!!
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
        presenter.showWordsByLetter(letter)
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetach()
    }

    override fun setLetterAdapter(letterAdapter: LetterAdapter) {
        lettersRV.adapter = letterAdapter
    }

    override fun setWordAdapter(wordAdapter: WordAdapter) {
        wordsRV.adapter = wordAdapter
    }

    override fun searchWord(word: String) {
        callback.onWordClick(word)
    }

    override fun saveLetter(letter: String) {
        this.letter = letter
    }

    interface Callback {

        fun onWordClick(word: String)
    }
}