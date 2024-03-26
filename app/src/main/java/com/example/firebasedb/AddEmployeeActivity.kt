package com.example.firebasedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddEmployeeActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextPosition: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonSave: Button
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        editTextName = findViewById(R.id.editTextName)
        editTextPosition = findViewById(R.id.editTextPosition)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonSave = findViewById(R.id.buttonSave)


        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Employees")

        // Set click listener for the save button
        buttonSave.setOnClickListener {
            saveEmployee()
        }


    }
    private fun saveEmployee() {
        val name = editTextName.text.toString().trim()
        val position = editTextPosition.text.toString().trim()
        val email = editTextEmail.text.toString().trim()

        // Check if any field is empty
        if (name.isEmpty() || position.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Name validation: Only alphabets are allowed
        if (!name.matches("^[a-zA-Z\\s]+\$".toRegex())) {
            Toast.makeText(this, "Name must contain only alphabets", Toast.LENGTH_SHORT).show()
            return
        }

        // Position (Phone) validation: Only numbers are allowed
        if (!position.matches("^[a-zA-Z\\s]+\$".toRegex())) {
            Toast.makeText(this, "Position  must contain only alphabets", Toast.LENGTH_SHORT).show()
            return
        }

        // Email validation: Proper email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate a unique ID for the new employee
        val employeeId = databaseReference.push().key ?: return

        val employee = Employee(employeeId, name, position, email)

        // Save the employee to Firebase
        databaseReference.child(employeeId).setValue(employee).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add employee", Toast.LENGTH_SHORT).show()
            }
        }
    }
}