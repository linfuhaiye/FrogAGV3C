package com.furongsoft.frogagvscanner.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.furongsoft.frogagvscanner.adapter.MaterialAdapter
import com.furongsoft.frogagvscanner.databinding.ActivityChooseMaterialBinding
import com.furongsoft.frogagvscanner.entity.Material

/**
 * 选择原料界面
 *
 * @author alex
 */
class ChooseMaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseMaterialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseMaterialBinding.inflate(layoutInflater)
        binding.model = Model()
        setContentView(binding.root)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val list = intent?.getParcelableArrayListExtra<Material>("materials")
        binding.rvActivityChooseMaterial.layoutManager = LinearLayoutManager(this)
        binding.rvActivityChooseMaterial.adapter = MaterialAdapter(list ?: ArrayList())
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

    inner class Model : ViewModel() {
        fun onRefresh() {}
    }
}
