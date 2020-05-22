package com.example.groceryrun

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    private val TAG: String? = "NoteActivity"
    // hashmap storing item, quantity, removeButton name, findButtonName
    private var map : HashMap<String, Int> = HashMap<String, Int> ()
    private var count = 2000    // start at 2000 just in case some previous ones were already initialized

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
    }

    fun saveItem(view: View) {     // for when a new item is added
        if (enterItem.getText().toString()=="" || enterQuantity.getText().toString()==""){
            // errortrap
        }
        else {     // save item to hashmap and create new spot for entering data, if that slot is filled out properly
            map.put(
                enterItem.getText().toString(),
                Integer.parseInt(enterQuantity.getText().toString())
            )
            var msg = enterItem.getText().toString() + ", " + enterQuantity.getText().toString()
            Log.i("New item saved: ", msg)

            // add new linear layout for edit text
            val parent = findViewById(R.id.mainList) as LinearLayout
            val ll = LinearLayout(this)
            ll.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            ll.orientation = LinearLayout.HORIZONTAL

            val tv = TextView(this)
            tv.setText(enterItem.getText().toString() + " x " + enterQuantity.getText().toString())

            val removeButton = Button(this)
            removeButton.text = "Remove Item"
            removeButton.id = count
            removeButton.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    removeClicked(view)
                }
            })

            val findButton = Button(this)
            findButton.text = "Find Item"
            findButton.id = count + 1

            // increment count by 2
            // even ids are removeButtons, odd are findButtons
            count+=2

            ll.addView(tv)
            ll.addView(removeButton)
            ll.addView(findButton)
            parent.addView(ll)

            // erase edittext text
            enterItem.getText().clear()
            enterQuantity.getText().clear()
        }
    }

    fun removeClicked (view: View){
        (view.parent.parent as ViewGroup).removeView(view.parent as ViewGroup)
    }

    companion object {
        private val TAG = "NoteActivity"
    }
}