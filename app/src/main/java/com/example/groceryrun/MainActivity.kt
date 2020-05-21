package com.example.groceryrun

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private val TAG: String? = "MainActivity"
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [START initialize_database_ref]
        //mDatabase = FirebaseDatabase.getInstance().getReference()
    }

    companion object {
        private val TAG = "MainActivity"
    }

    fun sendToNotes(view: View) {
        startActivity(Intent(this@MainActivity, NoteActivity::class.java))
    }
}