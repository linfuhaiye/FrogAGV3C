package com.furongsoft.frogagvscanner.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.furongsoft.frogagvscanner.adapter.MaterialAdapter
import com.furongsoft.frogagvscanner.adapter.ProductAdapter
import com.furongsoft.frogagvscanner.databinding.ActivityChooseProductBinding
import com.furongsoft.frogagvscanner.entity.Product
import com.furongsoft.frogagvscanner.miscs.ChannelUtils
import com.furongsoft.frogagvscanner.miscs.NetworkUtils.createService
import com.furongsoft.frogagvscanner.service.MaterialService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 选择产品界面
 *
 * @author alex
 */
class ChooseProductActivity : AppCompatActivity(), Product.OnProductClickListener {
    private lateinit var binding: ActivityChooseProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseProductBinding.inflate(layoutInflater)
        binding.model = Model()
        setContentView(binding.root)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        binding.rvActivityChooseProduct.layoutManager = LinearLayoutManager(this)
        binding.rvActivityChooseProduct.adapter = MaterialAdapter(ArrayList())
        getProducts()
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

    override fun onClick(product: Product) {
        setResult(Activity.RESULT_OK, Intent().putExtra("product", product))
        finish()
    }

    override fun onShowDetail(product: Product) {
        val intent = Intent(this, ChooseMaterialActivity::class.java)
        intent.putParcelableArrayListExtra("materials", ArrayList(product.materials))
        startActivity(intent)
    }

    /**
     * 获取产品列表
     */
    private fun getProducts() {
        binding.slActivityChooseProduct.isRefreshing = true
        val service = createService(ChooseModuleActivityMainActivity.Address, MaterialService::class.java)
        val channel = ChooseModuleActivityMainActivity.AreaType
        var str="";

        if(channel.equals("3B消毒间"))
            str = "3B_DISINFECTION";
        else if(channel.equals("3B仓库"))
            str = "3B_WAREHOUSE";
        else if(channel.equals("3C消毒间"))
            str = "3C_DISINFECTION ";
        else if(channel.equals("3C仓库"))
            str = "3C_WAREHOUSE";
        else
            return

        service.getProducts(str).enqueue(object : Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Handler().post {
                    Toast.makeText(this@ChooseProductActivity, "获取产品列表失败!", Toast.LENGTH_SHORT)
                        .show()
                    binding.slActivityChooseProduct.isRefreshing = false
                }
            }

            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.code() == 200) {
                    val list = response.body()
                    list?.forEach { m -> m.listener = this@ChooseProductActivity }
                    Handler().post {
                        binding.rvActivityChooseProduct.adapter = ProductAdapter(list)
                        binding.slActivityChooseProduct.isRefreshing = false
                    }
                }
            }
        })
    }

    inner class Model : ViewModel() {
        fun onRefresh() {
            getProducts()
        }
    }
}
