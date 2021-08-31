package com.bootcamp.businesscard.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bootcamp.businesscard.App
import com.bootcamp.businesscard.databinding.ActivityMainBinding
import com.bootcamp.businesscard.ui.adapter.BusinessCardAdapter
import com.bootcamp.businesscard.util.Image
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }
    private val adapter by lazy { BusinessCardAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mainCardsRv.adapter = adapter
        getAllBusinessCards()
        initActions()
    }

    private fun initActions() {
        binding.mainFab.setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                AddBusinessCardActivity::class.java
            )
            startActivity(intent)
        }
        adapter.deleteClickListener = {
            MaterialAlertDialogBuilder(this)
                .setTitle("Remover cartão")
                .setMessage("Deseja remover o cartão de visitas?")
                .setPositiveButton("SIM") { dialog, _ ->
                    mainViewModel.delete(it)
                    dialog.dismiss()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Cartão removido", Toast.LENGTH_SHORT).show()
                }
                .setNeutralButton("NÃO"){ dialog, _ ->
                    dialog.dismiss()
                }.show()
        }

        adapter.shareClickListener = { card ->
            Image.share(this@MainActivity, card)
        }
    }

    private fun getAllBusinessCards() {
        mainViewModel.getAll().observe(this, {
            adapter.submitList(it)
        })
    }
}