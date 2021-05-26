package com.furongsoft.frogagvscanner.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.furongsoft.frogagvscanner.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class ConfigModuleActivityMainActivity : AppCompatActivity() {
    private lateinit var ip: EditText
    private lateinit var port: EditText
    private lateinit var areaType: Spinner
    private lateinit var sn: EditText

    private lateinit var mIp: String
    private lateinit var mPort: String
    private lateinit var mAreaType: String
    private lateinit var mSn: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_module_activity_main)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        ip = findViewById(R.id.ipText)
        port = findViewById(R.id.portText)
        areaType = findViewById(R.id.spinner)
        sn = findViewById(R.id.Sn_Text)

        val properties = Properties()
        try {
            val file = File(cacheDir, "config")
            if(!file.exists()) {
                initConfig()
            }

            val fis = FileInputStream(file)
        properties.load(fis)

            mIp = properties["ip"].toString()
            mPort = properties["port"].toString()
            mAreaType = properties["areaType"].toString()
            mSn = properties["sn"].toString()
            fis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ip.setText(mIp);
        port.setText(mPort);
        port.setText(mPort)
        areaType.setSelection(getPosition(areaType,mAreaType))

        sn.setText(mSn)
         val updateConfig =
            findViewById<Button>(R.id.update_button)
         updateConfig.setOnClickListener(View.OnClickListener {
            val properties = Properties()
            try {
                mAreaType = areaType.getSelectedItem().toString();
                val fos =
                    FileOutputStream(File(cacheDir, "config"))
                properties.setProperty("ip", ip.getText().toString())
                properties.setProperty("port", port.getText().toString())
                properties.setProperty("areaType", mAreaType)
                properties.setProperty("sn", sn.getText().toString())
                properties.store(fos, null)
                fos.flush()
                fos.close()
                Toast.makeText(this@ConfigModuleActivityMainActivity, "配置成功", Toast.LENGTH_LONG)
                    .show()

                ChooseModuleActivityMainActivity.Address = "http://" + ip.getText().toString() + ":" + port.getText().toString() + "/"
                ChooseModuleActivityMainActivity.AreaType = mAreaType.toString()
                ChooseModuleActivityMainActivity.Sn = sn.getText().toString()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    private fun initConfig(){
        val properties = Properties()
        try {
            val file = File(cacheDir, "config");
            val fos = FileOutputStream(file)
            properties.setProperty("ip", "192.168.2.90")
            properties.setProperty("port", "9090")
            properties.setProperty("areaType", "")
            properties.setProperty("sn", "")
            properties.store(fos, null)
            fos.flush()
            fos.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getPosition(spinner: Spinner, strCity: String): Int {
        val count = spinner.adapter.count
        for (i in 0 until count) {
            if (spinner.getItemAtPosition(i).toString() + "" == strCity) return i
        }
        return 0
    }
}