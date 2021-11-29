package com.example.tyap2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

	private var edit1: EditText? = null
	private var edit2: EditText? = null
	private var titleText1: TextInputLayout? = null
	private var titleText2: TextInputLayout? = null
	private var text3: TextView? = null
	private var recreateButton: Button? = null
	private var stackTextView: TextView? = null
	private var testTextView: TextView? = null

	private var n: Int = 0
	private var k: Int = 0
	private var iteratorState: Int = -1
	private var endList: String = ""
	private var uiState: Int = 0

	private var translationTable: Array<Array<Int>> = arrayOf()
	private var alphabet: Array<String> = arrayOf()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val but1: Button = findViewById(R.id.but1)
		recreateButton = findViewById(R.id.but2)
		recreateButton?.isVisible = false
		edit1 = findViewById(R.id.edit1)
		edit2 = findViewById(R.id.edit2)
		stackTextView = findViewById(R.id.stack_text_view)
		testTextView = findViewById(R.id.test_text_view)
		titleText1 = findViewById(R.id.title_text1)
		titleText2 = findViewById(R.id.title_text2)
		text3 = findViewById(R.id.text3)

		but1.setOnClickListener {
			when (uiState) {
				0 -> initialTable()
				1 -> initialAlphabet()
				2 -> initialEndState()
				3 -> textExampleState()
			}
			edit1?.setText("")
		}
		recreateButton?.setOnClickListener {
			titleText1?.hint = "Количество состояний:"
			titleText2?.isVisible = true
			edit2?.isVisible = true
			edit2?.setText("")
			text3?.text = ""
			stackTextView?.text = ""
			testTextView?.text = ""
			recreateButton?.isVisible = false
			uiState = 0
			iteratorState = -1
		}
	}

	private fun initialTable() {
		n = edit1?.text.toString().toInt()
		k = edit2?.text.toString().toInt()
		translationTable = Array(n) { Array(k) { 0 } }
		alphabet = Array(k) { "" }
		titleText1?.hint = "Введите алфавит"
		titleText2?.isVisible = false
		edit2?.isVisible = false
		testTextView?.isVisible = false
		stackTextView?.isVisible = false
		uiState = 1
	}

	private fun initialAlphabet() {
		val inputString = edit1?.text.toString()
		var iteratorAlphabet = 0
		for (item in inputString) {
			if (iteratorState == -1) {
				alphabet[iteratorAlphabet] = item.toString()
			} else {
				if (item == '-') translationTable[iteratorState][iteratorAlphabet] = -1
				else translationTable[iteratorState][iteratorAlphabet] = item.code - 48
			}
			iteratorAlphabet++
		}
		val titleText = "Введите переходы " + (iteratorState + 1).toString() + " состояния"
		titleText1?.hint = titleText
		iteratorState += 1
		if (iteratorState == n) {
			uiState = 2
			titleText1?.hint = "Введите индекс(ы) конечного состояния"
		}
	}

	private fun initialEndState() {
		endList = edit1?.text.toString()
		titleText1?.hint = "Введите строку на проверку"
		recreateButton?.isVisible = true
		uiState = 3
		testTextView?.isVisible = true
		stackTextView?.isVisible = true
	}

	private fun textExampleState() {
		val testString = edit1?.text.toString()
		stackTextView?.text = edit1?.text.toString()
		var symbol = 0
		var chain = "q0 ->"
		var correctAlphabet = true
		text3?.text = ""
		for (char in testString) {
			correctAlphabet = false
			for (itemAlphabet in alphabet) {
				if (itemAlphabet == char.toString()) {
					correctAlphabet = true
				}
			}
			if (!correctAlphabet) break
		}
		if (correctAlphabet) {
			for (g in testString) {
				for ((p, h) in alphabet.withIndex()) {
					if (h == g.toString()) {
						symbol = translationTable[symbol][p]
						chain += "q$symbol ->"
					}
				}
				if (symbol == -1) {
					break
				}
			}
			text3?.setTextColor(getColor(R.color.red))
			when {
				symbol == -1                         -> {
					text3?.text = "Такого перехода не существует"
					chain = chain.dropLast(6) + "?"
				}

				!endList.contains(symbol.toString()) -> {
					val error = "Cостояние q$symbol не является конечным"
					text3?.text = error
					chain = chain.dropLast(2)
				}

				else                                 -> {
					text3?.text = ""
					chain = chain.dropLast(2)
				}
			}
		} else text3?.text = "Нет такого символа в алфавите"
		testTextView?.text = chain
	}
}