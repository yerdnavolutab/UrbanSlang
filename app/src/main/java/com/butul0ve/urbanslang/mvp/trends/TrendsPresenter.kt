package com.butul0ve.urbanslang.mvp.trends

import com.butul0ve.urbanslang.adapter.LetterAdapter
import com.butul0ve.urbanslang.adapter.LetterClickListener
import com.butul0ve.urbanslang.adapter.WordAdapter
import com.butul0ve.urbanslang.adapter.WordClickListener
import com.butul0ve.urbanslang.mvp.BasePresenter
import java.util.*

class TrendsPresenter<V : TrendsMvpView>(private val dictionary: Map<String, List<String>>) : BasePresenter<V>(),
    TrendsMvpPresenter<V>, LetterClickListener, WordClickListener {

    private lateinit var letterAdapter: LetterAdapter
    private lateinit var wordAdapter: WordAdapter

    private lateinit var letters: List<String>
    private lateinit var words: List<String>

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        letters = dictionary.keys.toList()
        Collections.sort(letters)
        letterAdapter = LetterAdapter(letters, this)
        mvpView.setLetterAdapter(letterAdapter)
    }

    override fun showWordsByLetter(letter: String) {
        words = dictionary[letter]!!
        Collections.sort(words)
        wordAdapter = WordAdapter(words, this)
        mvpView?.setWordAdapter(wordAdapter)
    }

    override fun onLetterClick(letter: String) {
        words = dictionary[letter]!!
        Collections.sort(words)
        wordAdapter.updateWords(words)
        mvpView?.saveLetter(letter)
    }

    override fun onWordClick(word: String) {
        if (isViewAttached()) {
            mvpView?.searchWord(word)
        }
    }
}