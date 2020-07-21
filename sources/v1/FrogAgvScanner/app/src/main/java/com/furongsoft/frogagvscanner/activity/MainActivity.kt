package com.furongsoft.frogagvscanner.activity

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.furongsoft.frogagvscanner.service.ScannerUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 主界面
 *
 * @author alex
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /**
     * 扫描广播接收器
     */
    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.android.scancontext") {
                val result = intent.getStringExtra("Scan_context") ?: return
                if (result.indexOf("000000") >= 0) {
                    binding.model?.materialCarName = result
                } else {
                    binding.model?.landMaskName = result
                }
                binding.model?.notifyChange()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.model = Model()

        ScannerUtils.register(this, receiver)
        ScannerUtils.open(this)
    }

    override fun onPause() {
        ScannerUtils.unregister(this, receiver)
        super.onPause()
    }

    override fun onResume() {
        ScannerUtils.register(this, receiver)
        super.onResume()
    }

    override fun onDestroy() {
        ScannerUtils.unregister(this, receiver)
        ScannerUtils.close(this)
        super.onDestroy()
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

    /**
     * 提交备货信息
     */
    private fun stockUp(materialName: String?, materialCarName: String?, landMaskName: String?) {
        val service =
            NetworkUtils.createService(ChooseProductActivity.BaseUrl, MaterialService::class.java)
        service.stockUp(materialName, materialCarName, landMaskName)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Handler().post {
                        Toast.makeText(this@MainActivity, "提交失败!：" + t.message, Toast.LENGTH_SHORT)
                            .show()
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
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "提交失败! 原因: $result",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
            })
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
            ScannerUtils.scan(this@MainActivity)
        }

        fun onScanLandMaskButtonClick() {
            ScannerUtils.scan(this@MainActivity)
        }

        fun onCommitButtonClick() {
            stockUp(materialCode, materialCarName, landMaskName)
        }
    }
}
