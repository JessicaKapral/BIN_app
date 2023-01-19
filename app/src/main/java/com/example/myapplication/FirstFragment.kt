package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentFirstBinding
import kotlin.math.log


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/


        val enter_btn = view.findViewById<Button>(R.id.button_toast)
        val edit_txt = view.findViewById<EditText>(R.id.editTextBIN)

        enter_btn.setOnClickListener{
            edit_txt.clearFocus()
            getBINDataByAPI(view)
        }

        edit_txt.onFocusChangeListener = OnFocusChangeListener{ _, b ->
            if(b){
                /*edit_txt.setSelection(edit_txt.text.length)*/
                println("Focus active")
            } else{
                val imm:InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                println("lol")
            }
        }

        edit_txt.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                enter_btn.performClick()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getBINDataByAPI(view: View) {
        val binRawText = view.findViewById<EditText>(R.id.editTextBIN).text
        println("Введеный BIN: $binRawText")

        if(binRawText.trim().length < 6){
            Toast.makeText(context, "Формат БИН не соответсвует. Проверьте корректность ввода",
                Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context, "Идет поиск BIN", Toast.LENGTH_SHORT).show()

        (activity as MainActivity).GetBinInfo_API(binRawText.trim())   //calling an API to get bin info
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}