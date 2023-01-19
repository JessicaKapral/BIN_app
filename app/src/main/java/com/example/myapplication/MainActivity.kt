package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.ActivityMainBinding
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    data class BINResult(val success: Boolean, val result: String)

    public fun GetBinInfo_API(bin:CharSequence) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var resCode:Int = 0
        var dResponce:String = ""
        var res:BINResult = BINResult(false, "")
        executor.execute{
            var url = URL("https://lookup.binlist.net/$bin")


            with(url.openConnection() as HttpURLConnection){
                requestMethod = "GET"

                println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")


                if(responseCode == 200){
                    inputStream.bufferedReader().use {
                        val jsonResp = it.readLine()
                        println(jsonResp)
                        dResponce = jsonResp
                    }
                }

                resCode = responseCode
            }

            handler.post{
                if(resCode == 200){
                    Toast.makeText(this, "Поиск выполнен", Toast.LENGTH_SHORT).show()

                    try {
                        val path = this.filesDir
                        val letDirectoty = File(path, "LET")

                        if(!letDirectoty.exists()){
                            letDirectoty.mkdirs()
                        }

                        if(letDirectoty.exists()){
                            val file = File(letDirectoty, "records.data")
                            file.appendBytes(dResponce.toByteArray())
                            println("Write success")
                        }
                    }catch (ex:Exception){
                        if(this != null) Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
                    }

                    println("Navigate to third menu")

                    /*supportFragmentManager.beginTransaction().add(R.id.frameLayout, ThirdFragment.newInstance(dResponce)).commit()*/
                    /*ThirdFragment.newInstance(dResponce)*/
                    var bundle = Bundle()
                    bundle.putString("test", dResponce)
                    findNavController(R.id.nav_host_fragment_content_main)
                        .navigate(R.id.action_FirstFragment_to_thirdFragment, bundle)
                } else {
                    Toast.makeText(this, "Информация не найдена", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}