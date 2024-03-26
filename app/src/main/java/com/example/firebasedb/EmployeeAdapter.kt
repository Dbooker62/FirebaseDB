package com.example.firebasedb


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase

class EmployeeAdapter(private var employees: MutableList<Employee>, val context: Context) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView = view.findViewById(R.id.textViewName)
        val textViewPosition: TextView = view.findViewById(R.id.textViewPosition)
        val textViewEmail: TextView = view.findViewById(R.id.textViewEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employees[position]
        holder.textViewName.text = employee.name
        holder.textViewPosition.text = employee.position
        holder.textViewEmail.text = employee.email
    }

    override fun getItemCount(): Int = employees.size

    fun showDeleteConfirmation(position: Int) {
        Log.d("EmployeeAdapter", "Before deletion - employees size: ${employees.size}")
        AlertDialog.Builder(context)
            .setTitle("Delete Employee")
            .setMessage("Are you sure you want to delete this employee?")
            .setPositiveButton("Yes") { _, _ ->
                if (position >= 0 && position < employees.size) {
                    val employeeId = employees[position].id
                    FirebaseDatabase.getInstance().getReference("Employees").child(employeeId).removeValue()
                        .addOnSuccessListener {
                            employees.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, employees.size - position)
                            Log.d("EmployeeAdapter", "After deletion - employees size: ${employees.size}")
                            Toast.makeText(context, "Employee deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to delete employee", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Invalid position: $position", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                notifyItemChanged(position)
            }
            .create()
            .show()
    }
}

