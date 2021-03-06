package com.example.testingsample.calculator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testingsample.R
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * reference from
 * https://github.com/manhduy/hilt-testing-sample
 */

@AndroidEntryPoint
class CalculatorActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: CalculatorViewModelFactory
    private lateinit var viewModel: CalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CalculatorViewModel::class.java)
        viewModel.sum.observe(this, Observer {
            tvSum.text = it.toString()
        })
        viewModel.msg.observe(this, Observer {
            it.getContentIfNotHandled()?.let { msg ->
                showMessage(msg)
            }
        })

    }

    private fun showMessage(@StringRes msgId: Int) {
        val msg = getString(msgId)
        //Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        Snackbar.make(rootView, msgId, Snackbar.LENGTH_LONG).show()

    }

    fun onSumClick(view: View) {
        hideKeyboard()
        viewModel.onSumClick(edtA.text.toString().trim(), edtB.text.toString().trim())
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}
