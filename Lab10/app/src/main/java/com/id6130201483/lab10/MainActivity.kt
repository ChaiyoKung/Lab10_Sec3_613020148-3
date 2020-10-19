package com.id6130201483.lab10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.insert_layout.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var dbHandle: DatabaseHelper
    private var studentList = arrayListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandle = DatabaseHelper.getInstance(this)
        dbHandle.writableDatabase
        callStudentData()
        recycler_view.adapter = StudentAdapter(studentList, applicationContext)
        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun onResume() {
        super.onResume()
        callStudentData()
    }

    fun callStudentData() {
        studentList.clear()
        studentList.addAll(dbHandle.getAllStudent())
        recycler_view.adapter?.notifyDataSetChanged()
    }

    fun addStudentDialog(v: View) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.insert_layout, null)
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.btnAdd.setOnClickListener {
            val id = mDialogView.edt_id.text.toString()
            val name = mDialogView.edt_name.text.toString()
            val age = mDialogView.edt_age.text.toString().toInt()
            var result = dbHandle.insertStudent(Student(id, name, age))

            if (result > -1) {
                Toast.makeText(
                    applicationContext,
                    "The student is inserted successfully",
                    Toast.LENGTH_LONG
                ).show()
                mAlertDialog.dismiss()
                callStudentData()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Insert Failure",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        mDialogView.btnReset.setOnClickListener {
            mDialogView.edt_id.setText("")
            mDialogView.edt_name.setText("")
            mDialogView.edt_age.setText("")
        }
    }
}