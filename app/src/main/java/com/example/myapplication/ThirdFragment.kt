package com.example.myapplication

import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentThirdBinding
import org.json.JSONObject
import org.w3c.dom.Text

class ThirdFragment : Fragment() {
    private val MY_STR = "mystr"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*return super.onCreateView(inflater, container, savedInstanceState)*/
        println("BOB1")
        return  inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg = arguments?.getString("test")

        println("Text received is '$arg'")

        var jsonObject = JSONObject(arg)
        println("Text received is '$arg'")
        /*jsonObject.get()*/
        view.findViewById<TextView>(R.id.textView_scheme).text = jsonObject.getString("scheme")
        view.findViewById<TextView>(R.id.textView_brand).text = jsonObject.getString("brand")
        view.findViewById<TextView>(R.id.textView_cardNum).text =
            String.format("Length: %s    LUHN:%s",
                jsonObject.getJSONObject("number").getInt("length"),
                jsonObject.getJSONObject("number").getBoolean("luhn"))
        view.findViewById<TextView>(R.id.textView_type).text = jsonObject.getString("type")
        view.findViewById<TextView>(R.id.textView_prepaid).text = jsonObject.getBoolean("prepaid").toString()
        view.findViewById<TextView>(R.id.textView_country).text =
            String.format("(%s) %s",
                jsonObject.getJSONObject("country").getString("emoji"),
                jsonObject.getJSONObject("country").getString("name"))
        /*jsonObject.getString("")*/
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}