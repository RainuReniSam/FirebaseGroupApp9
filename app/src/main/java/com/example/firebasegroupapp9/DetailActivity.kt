package com.example.firebasegroupapp9

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val product = intent.getSerializableExtra("productDetails") as Product

        val txtName: TextView = findViewById(R.id.txtName)
        txtName.text = product.name

        val txtDescription: TextView = findViewById(R.id.txtDescription)
        txtDescription.text = product.description

        val txtPrice: TextView = findViewById(R.id.txtPrice)
        txtPrice.text = "Price: $ " + product.price

        val txtManufacturer: TextView = findViewById(R.id.txtManufacturer)
        txtManufacturer.text = "Sold By: " + product.manufacturer

        val txtSize: TextView = findViewById(R.id.txtSize)
        txtSize.text = "Size: " + product.size

        val txtFullDescription: TextView = findViewById(R.id.txtFullDescription)
        txtFullDescription.text = product.fullDescription

        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this@DetailActivity, ProductActivity::class.java))
        }
        val storageRef: StorageReference =
            FirebaseStorage.getInstance().getReferenceFromUrl(product.url)
        Glide.with(this).load(storageRef).into(findViewById(R.id.imgProduct))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_logout) {
            Toast.makeText(this, "User Logged Out", Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().signOut()
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
            finish()
        } else if (item.itemId == R.id.nav_home) {
            val mainIntent = Intent(this, ProductActivity::class.java)
            startActivity(mainIntent)
        } else if (item.itemId == R.id.nav_cart) {
            val cartIntent = Intent(this, CartActivity::class.java)
            startActivity(cartIntent)
        }
        return true
    }
}