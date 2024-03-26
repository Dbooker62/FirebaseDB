package com.example.firebasedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var employeeRecyclerView: RecyclerView
    private lateinit var employees: MutableList<Employee>
    private lateinit var adapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        employeeRecyclerView = findViewById(R.id.employeeRecyclerView)
        employees = arrayListOf()

        // Initialize Firebase Database and point to 'Employees' node
        databaseReference = FirebaseDatabase.getInstance().getReference("Employees")

        // Set up RecyclerView
        employeeRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EmployeeAdapter(employees , this)
        employeeRecyclerView.adapter = adapter


        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(employeeRecyclerView)

        // Fetch data from Firebase
        fetchDataFromFirebase()

        findViewById<FloatingActionButton>(R.id.addEmployeeFab).setOnClickListener {
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fetchDataFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedEmployees = mutableListOf<Employee>()
                for (postSnapshot in snapshot.children) {
                    val employee = postSnapshot.getValue(Employee::class.java)
                    employee?.let { updatedEmployees.add(it) }
                }
                employees.clear()
                employees.addAll(updatedEmployees)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
            }
        })
    }
}


