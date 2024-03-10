package com.example.firebasegroupapp9

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val txtTotal: TextView = findViewById(R.id.txtTotal)

        val txtFirstname: EditText = findViewById(R.id.txtFirstname)
        val txtLastName: EditText = findViewById(R.id.txtLastName)
        val txtEmail: EditText = findViewById(R.id.txtEmail)
        val txtPhoneNumber: EditText = findViewById(R.id.txtPhoneNumber)
        val txtAddress: EditText = findViewById(R.id.txtAddress)
        val txtPostalCode: EditText = findViewById(R.id.txtPostalCode)
        val txtCity: EditText = findViewById(R.id.txtCity)
        val txtProvince: EditText = findViewById(R.id.txtProvince)
        val txtCountry: EditText = findViewById(R.id.txtCountry)
        val txtNameOnCard: EditText = findViewById(R.id.txtNameOnCard)
        val txtCardNumber: EditText = findViewById(R.id.txtCardNumber)
        val txtValidity: EditText = findViewById(R.id.txtValidity)
        val txtCvv: EditText = findViewById(R.id.txtCvv)
        val btnCheckout: Button = findViewById(R.id.btnCheckout)

        btnCheckout.setOnClickListener {

            val firstname = txtFirstname.text.toString()
            val lastName = txtLastName.text.toString()
            val email = txtEmail.text.toString()
            val phoneNumber = txtPhoneNumber.text.toString()
            val address = txtAddress.text.toString()
            val postalCode = txtPostalCode.text.toString()
            val city = txtCity.text.toString()
            val province = txtProvince.text.toString()
            val country = txtCountry.text.toString()
            val nameOnCard = txtNameOnCard.text.toString()
            val cardNumber = txtCardNumber.text.toString()
            val validity = txtValidity.text.toString()
            val cvv = txtCvv.text.toString()

            if (validateName(firstname) && validateName(lastName) && validateEmail(email) && validatePhoneNumber(
                    phoneNumber
                ) && validateStreetAddress(address) && validateCanadianPostalCode(postalCode) && validateCanadianAddress(
                    city
                ) && validateCanadianAddress(province) && validateCountry(country) && validateName(
                    nameOnCard
                ) && validateCardNumber(cardNumber) && validateCVV(cvv)
            ) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid

                if (userId != null) {
                    val userRef =
                        FirebaseDatabase.getInstance().reference.child("orders").child(userId)
                    val userData = HashMap<String, Any>()
                    userData["firstName"] = firstname
                    userData["lastName"] = lastName
                    userData["email"] = email
                    userData["phoneNumber"] = phoneNumber
                    userData["address"] = address
                    userData["postalCode"] = postalCode
                    userData["city"] = city
                    userData["province"] = province
                    userData["country"] = country
                    userData["nameOnCard"] = nameOnCard
                    userData["cardNumber"] = cardNumber
                    userData["validity"] = validity
                    userData["cvv"] = cvv

                    userRef.setValue(userData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Your Order is successfully placed",
                                Toast.LENGTH_SHORT
                            ).show()
                            // You can navigate to a success screen or perform other actions here
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "Failed to place order: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateName(name: String): Boolean {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!trimmedName.matches(Regex("^[a-zA-Z]+(?: [a-zA-Z]+)*\$"))) {
            Toast.makeText(this, "$name is INVALID!! Please enter a valid name", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    private fun validateEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        if (matcher.matches()) {
            return matcher.matches()
        } else {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        val phonePattern = "^\\+1\\d{10}\$"
        val pattern = Pattern.compile(phonePattern)
        val matcher = pattern.matcher(phoneNumber)
        if (matcher.matches()) {
            return matcher.matches()
        } else {
            Toast.makeText(
                this,
                "Please enter a valid Canadian phone number in the format +1 999 999 9999",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }

    private fun validateStreetAddress(streetAddress: String): Boolean {
        val trimmedStreetAddress = streetAddress.trim()
        if (trimmedStreetAddress.isEmpty()) {
            Toast.makeText(this, "Please enter a valid Address", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!trimmedStreetAddress.matches(Regex("^[a-zA-Z0-9]+(?: [a-zA-Z0-9]+)*\$"))) {
            Toast.makeText(
                this,
                "$streetAddress is INVALID!! Please enter a valid address",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun validateCanadianAddress(address: String): Boolean {
        val trimmedAddress = address.trim()
        if (trimmedAddress.isEmpty()) {
            Toast.makeText(this, "Please enter a valid City and Province", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (!trimmedAddress.matches(Regex("^[a-zA-Z]+(?: [a-zA-Z]+)*\$"))) {
            Toast.makeText(
                this,
                "$address is INVALID!! Please enter a valid City and Province",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun validateCanadianPostalCode(postalCode: String): Boolean {
        val canadianPostalCodePattern = "^[A-Za-z]\\d[A-Za-z]\\s?\\d[A-Za-z]\\d$"
        val pattern = Pattern.compile(canadianPostalCodePattern)
        val matcher = pattern.matcher(postalCode.trim())
        if (matcher.matches()) {
            return matcher.matches()
        } else {
            Toast.makeText(this, "Please enter a valid Canadian postal code", Toast.LENGTH_SHORT)
                .show()
            return false
        }
    }

    private fun validateCountry(country: String): Boolean {
        val trimmedCountry = country.trim()
        if (trimmedCountry.equals("canada", ignoreCase = true)) {
            return true
        } else {
            Toast.makeText(
                this,
                "Country INVALID!! Services are limited to Canada at the moment",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }

    private fun validateCardNumber(cardNumber: String): Boolean {
        val cardNumberRegex = "^(\\d{4}[- ]){3}\\d{4}|\\d{16}$"
        if (cardNumber.trim().matches(Regex(cardNumberRegex))) {
            return true
        } else {
            Toast.makeText(this, "INVALID card number", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun validateCVV(cvv: String): Boolean {
        val cvvRegex = "^\\d{3,4}$"
        if (cvv.trim().matches(Regex(cvvRegex))) {
            return true
        } else {
            Toast.makeText(this, "INVALID cvv detected", Toast.LENGTH_SHORT).show()
            return false
        }
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