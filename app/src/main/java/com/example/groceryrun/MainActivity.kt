package com.example.groceryrun

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


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
    fun sendToMaps(view: View) {
        startActivity(Intent(this@MainActivity, MapsActivity::class.java))
    }
}