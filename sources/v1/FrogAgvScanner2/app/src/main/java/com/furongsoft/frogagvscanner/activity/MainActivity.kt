package com.furongsoft.frogagvscanner.activity

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import com.furongsoft.frogagvscanner.R
import com.furongsoft.frogagvscanner.databinding.ActivityMainBinding
import com.furongsoft.frogagvscanner.entity.Product
import com.furongsoft.frogagvscanner.miscs.NetworkUtils
import com.furongsoft.frogagvscanner.service.MaterialService
import com.symbol.emdk.EMDKManager
import com.symbol.emdk.barcode.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

/**
 * 主界面
 *
 * @author alex
 */

class MainActivity : AppCompatActivity(), EMDKManager.EMDKListener,
    Scanner.StatusListener, Scanner.DataListener, CompoundButton.OnCheckedChangeListener{

    private var emdkManager: EMDKManager? = null
    private var barcodeManager: BarcodeManager? = null
    private var scanner: Scanner? = null
    private val scanButton: Button? = null
    private var deviceList: List<ScannerInfo>? = null
    private val scannerIndex = 0

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.model = Model()

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val results = EMDKManager.getEMDKManager(
            applicationContext, this
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0x1000 -> {
                if ((resultCode == Activity.RESULT_OK) && (data != null)) {
                    val product = data.getParcelableExtra<Product>("product")
                    binding.model?.materialName = product?.name
                    binding.model?.materialCode = product?.code
                    binding.model?.notifyChange()
                }

                return
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
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

    /**
     * 提交备货信息
     */
    private fun stockUp(materialName: String?, materialCarName: String?, landMaskName: String?) {
        val button1 = findViewById<Button>(R.id.button);
        var button2 =findViewById<Button>(R.id.button2);
        button1.isEnabled = false;
        button2.isEnabled = false;
        val service =
            NetworkUtils.createService(ChooseModuleActivityMainActivity.Address, MaterialService::class.java)
        service.stockUp(materialName, materialCarName, landMaskName)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Handler().post {

                        Toast.makeText(this@MainActivity, "提交失败!：" + t.message, Toast.LENGTH_SHORT)
                            .show()
                        button1.isEnabled = true;
                        button2.isEnabled = true;
                    }
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val result = response.body()?.string() ?: ""
                    Handler().post {
                        if (result == "success") {
                            binding.model?.materialName = ""
                            binding.model?.materialCode = ""
                            binding.model?.materialCarName = ""
                            binding.model?.landMaskName = ""
                            binding.model?.notifyChange()
                            Toast.makeText(this@MainActivity, "提交成功!", Toast.LENGTH_SHORT)
                                .show()
                            button1.isEnabled = true;
                            button2.isEnabled = true;
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "提交失败! 原因: $result",
                                Toast.LENGTH_LONG
                            )
                                .show()

                            button1.isEnabled = true;
                            button2.isEnabled = true;
                        }
                    }
                }
            })
    }

    /**
     * 清除
     */
    private fun clear(materialName: String?, materialCarName: String?, landMaskName: String?) {
        val button1 = findViewById<Button>(R.id.button);
        var button2 =findViewById<Button>(R.id.button2);
        button1.isEnabled = false;
        button2.isEnabled = false;
        val service =
            NetworkUtils.createService(ChooseModuleActivityMainActivity.Address, MaterialService::class.java)
        service.clear(landMaskName)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Handler().post {

                        Toast.makeText(this@MainActivity, "清除失败!：" + t.message, Toast.LENGTH_SHORT)
                            .show()
                        button1.isEnabled = true;
                        button2.isEnabled = true;
                    }
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val result = response.body()?.string() ?: ""
                    Handler().post {
                        if (result == "success") {
                            binding.model?.materialName = ""
                            binding.model?.materialCode = ""
                            binding.model?.materialCarName = ""
                            binding.model?.landMaskName = ""
                            binding.model?.notifyChange()
                            Toast.makeText(this@MainActivity, "清除成功!", Toast.LENGTH_SHORT)
                                .show()
                            button1.isEnabled = true;
                            button2.isEnabled = true;
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "清除失败! 原因: $result",
                                Toast.LENGTH_LONG
                            )
                                .show()

                            button1.isEnabled = true;
                            button2.isEnabled = true;
                        }
                    }
                }
            })
    }

    /**
     *  开启扫码
     */
    private fun OpenScanner(): Boolean {
        try {
            val a = scanner!!.isEnabled;
            scanner!!.triggerType = Scanner.TriggerType.SOFT_ONCE
            if (scanner!!.isReadPending) scanner!!.cancelRead()
            scanner!!.read()
        } catch (e: ScannerException) {
            e.printStackTrace()
        }

        if (scanner?.isReadPending()!!)
            scanner!!.cancelRead();
        return false;
    }

    override fun onOpened(emdkManager: EMDKManager) {
        this.emdkManager = emdkManager
        barcodeManager = this.emdkManager!!
            .getInstance(EMDKManager.FEATURE_TYPE.BARCODE) as BarcodeManager
        try {
            enumerateScannerDevices()
        } catch (e: ScannerException) {
            e.printStackTrace()
        }
    }

    @Throws(ScannerException::class)
    private fun enumerateScannerDevices() {
        if (barcodeManager != null) {
            val friendlyNameList: MutableList<String> = ArrayList()
            var spinnerIndex = 0
            var defaultIndex = 0
            deviceList = barcodeManager!!.supportedDevicesInfo

            if ((deviceList as MutableList<ScannerInfo>?).isNullOrEmpty()) {
                val it = (deviceList as MutableList<ScannerInfo>).iterator()
                while (it.hasNext()) {
                    val scnInfo = it.next()
                    friendlyNameList.add(scnInfo.friendlyName)
                    if (scnInfo.isDefaultScanner) {
                        defaultIndex = spinnerIndex
                    }
                    ++spinnerIndex
                }
            }
            deInitScanner()
            initializeScanner()
            setProfile()
        }
    }
    private fun deInitScanner() {
        if (scanner != null) {
            try {
                scanner!!.cancelRead()
                scanner!!.removeDataListener(this)
                scanner!!.removeStatusListener(this)
                scanner!!.disable()
            } catch (e: ScannerException) {
            }
            scanner = null
        }
    }

    @Throws(ScannerException::class)
    private fun initializeScanner() {
        if (deviceList!!.size != 0) {
            scanner = barcodeManager!!.getDevice(deviceList!![scannerIndex])
        }
        if (scanner != null) {
            scanner!!.addDataListener(this)
            scanner!!.addStatusListener(this)
            try {
                scanner!!.enable()
            }
            catch (e: ScannerException) {
            }
        }
    }

    fun setProfile() {
        try {
            if (scanner!!.isReadPending) scanner!!.cancelRead()
            val config = scanner!!.config
            config.scanParams.audioStreamType =
                ScannerConfig.AudioStreamType.RINGER
            config.scanParams.decodeAudioFeedbackUri = "system/media/audio/notifications/decode.wav"
            scanner!!.config = config
            scanner!!.read()
        }
        catch (e: ScannerException) {
        }
    }

    override fun onClosed() {
        if (emdkManager != null) {
            emdkManager!!.release()
            emdkManager = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (barcodeManager != null) barcodeManager = null
        if (emdkManager != null) {
            emdkManager!!.release()
            emdkManager = null
        }
    }

    override fun onStop() {
        super.onStop()
        deInitScanner()
    }

    override fun onStatus(statusData: StatusData) {
    }

    override fun onData(scanDataCollection: ScanDataCollection) {
        AsyncDataUpdate().execute(scanDataCollection);
    }

    private inner class AsyncDataUpdate :
        AsyncTask<ScanDataCollection?, Void?, String>() {
        protected override fun doInBackground(vararg params: ScanDataCollection?): String? {
            val scanDataCollection = params[0]
            var statusStr = ""
            if ((scanDataCollection != null) || (scanDataCollection?.result == ScannerResults.SUCCESS)) {
                val scanData = scanDataCollection.scanData
                for (data in scanData) {
                    val barcodeDate = data.data
                    statusStr = barcodeDate
                }
            }

            return statusStr
        }

        override fun onPostExecute(result: String) {
            if (result.length <4) {
                return;
            }
            if (result.substring(0,4) == "0000") {
                binding.model?.materialCarName = result
            }
            else {
                binding.model?.landMaskName = result
            }
            binding.model?.notifyChange()
        }
        override fun onPreExecute() {}
        protected override fun onProgressUpdate(vararg values: Void?) {}
    }



    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
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

        fun onChooseMaterialButtonClick() {
            val intent = Intent(this@MainActivity, ChooseProductActivity::class.java)
            startActivityForResult(intent, 0x1000)
        }

        fun onScanMaterialCarButtonClick() {
            OpenScanner();
        }

        fun onScanLandMaskButtonClick() {
            OpenScanner();
        }

        fun onCommitButtonClick() {
            stockUp(materialCode, materialCarName, landMaskName)
        }

        fun onClearButtonClick(){
            clear(materialCode, materialCarName, landMaskName);
        }
    }
}
