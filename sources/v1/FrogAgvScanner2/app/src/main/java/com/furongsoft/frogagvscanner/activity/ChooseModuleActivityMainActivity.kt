package com.furongsoft.frogagvscanner.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.furongsoft.frogagvscanner.R
import com.furongsoft.frogagvscanner.databinding.ChooseModuleActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


class ChooseModuleActivityMainActivity : AppCompatActivity() {
    companion object StaticParams {
        var Address: String? = null
        var AreaType:String? =null
        var Sn:String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_module_activity_main)
        Address = GetUrl();
        val configureButton =
            findViewById<Button>(R.id.configure_button)

        configureButton.setOnClickListener {
            val intent = Intent(
                this@ChooseModuleActivityMainActivity,
                ConfigModuleActivityMainActivity::class.java
            )
            startActivity(intent)
        }
        val batteryButton =
            findViewById<Button>(R.id.consignment_button)
        batteryButton.setOnClickListener {
            val intent =
                Intent(this@ChooseModuleActivityMainActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun GetUrl():String{
        val properties = Properties()
        try {
            val file = File(cacheDir,"config");
            if (!file.exists()) {
                initConfig()
            }
            val fis = FileInputStream(file)
            properties.load(fis)
            val  ip = properties["ip"].toString()
            val port = properties["port"].toString()
            AreaType = properties["areaType"].toString()
            Sn = properties["sn"].toString()
            fis.close()
            return  "http://" + ip + ":" + port+ "/"

        } catch (e: Exception) {
            e.printStackTrace()
            return "http:// 192.168.2.90:9090/";
        }
    }

    fun initConfig(){
        val properties = Properties()
        try {
            val file = File(cacheDir, "config");
            val fos =
                FileOutputStream(file)
            properties.setProperty("ip", "192.168.2.90")
            properties.setProperty("port", "9090")
            properties.setProperty("areaType", "")
            properties.setProperty("sn", "")
            properties.store(fos, null)
            Address = "http:// 192.168.2.90:9090/";
            AreaType = ""
            Sn = ""
            fos.flush()
            val close: Any = fos.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }


    inner class Model : BaseObservable() {
        @Bindable
        var materialName: String? = null

        @Bindable
        var materialCode: String? = null

        @Bindable
        var materialCarName: String? = null

        @Bindable
        var landMaskName: String? = null

    }
}