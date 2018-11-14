package com.butul0ve.urbanslang.mvp.trends

import android.content.Context
import com.butul0ve.urbanslang.R

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.butul0ve.urbanslang.adapter.LetterAdapter
import com.butul0ve.urbanslang.adapter.WordAdapter
import com.butul0ve.urbanslang.utils.readDictionaryFromAssets
import java.io.IOException

private const val LETTER = "letter_extra_key"

class TrendsFragment : Fragment(), TrendsMvpView {

    private lateinit var lettersRV: RecyclerView
    private lateinit var wordsRV: RecyclerView
    private lateinit var map: Map<String, List<String>>
    private lateinit var presenter: TrendsMvpPresenter<TrendsMvpView>
    private lateinit var callback: Callback
    private var letter: String = "a"

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            map = readDictionaryFromAssets(context!!)
            callback = context as Callback
        } catch (ex: ClassCastException) {
            throw ClassCastException("${activity?.localClassName} must implement TrendsFragment.Callback")
        } catch (e: IOException) {
            Log.d("trends fragment", "error reading files from assets ${e.localizedMessage}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = TrendsPresenter(map)
    }

    override fun onPause() {
        super.onPause()

        if (arguments == null) {
            arguments = Bundle()
        }

        onSaveInstanceState(arguments!!)
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

        if (savedInstanceState == null) {
            if (arguments != null && arguments?.containsKey(LETTER)!!) {
                letter = arguments?.getString(LETTER)!!
            }
        } else {
            if (savedInstanceState.containsKey(LETTER)) {
                letter = savedInstanceState.getString(LETTER)
            }
        }

        if (::presenter.isInitialized) {
            presenter.onAttach(this)
            presenter.showWordsByLetter(letter)
        }
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