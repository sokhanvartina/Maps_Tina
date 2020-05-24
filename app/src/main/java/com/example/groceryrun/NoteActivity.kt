package com.example.groceryrun

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_note.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


class NoteActivity : AppCompatActivity() {
    private val TAG: String? = "NoteActivity"
    // hashmap storing item, quantity, removeButton name, findButtonName
    private var map : HashMap<String, Int> = HashMap<String, Int> ()
    private var count = 2000    // start at 2000 just in case some previous ones were already initialized
    private var names: ArrayList<String> = ArrayList()   // item/ category names

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        // get JSON Data
        try {
            // get JSONObject from JSON file
            var itemArray = JSONArray(loadJSONFromAsset())
            for (i in 0 until itemArray.length()) {
                // create a JSONObject for fetching single user data
                var itemDetail = itemArray.getJSONObject(i)
                var name = itemDetail.getString("name")
                names.add(name)
                Log.i("Item loaded from json: ", name)
            }
        }
        catch (e: JSONException) {
            Log.i("Couldn't load json data", "Couldn't get json data but did load the file")
            e.printStackTrace()
        }

        val autocomplete = findViewById<View>(R.id.enterItem) as AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        autocomplete.setAdapter(adapter)
    }

    // load data from JSON file
    private fun loadJSONFromAsset(): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = assets.open("item_data.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            val charset: Charset = Charsets.UTF_8
            String(buffer, charset)
        }
        catch (ex: IOException) {
            Log.i("Couldn't load json", "Couldn't load json file")
            ex.printStackTrace()
            return null
        }
        Log.i("json file loaded", "json file loaded properly")
        return json
    }

    fun saveItem(view: View) {     // for when a new item is added
        if (enterItem.getText().toString()==""){
            // errortrap
        }
        else {     // save item to hashmap and create new spot for entering data, if that slot is filled out properly
            map.put(
                enterItem.getText().toString(),
                1
            )
            var msg = enterItem.getText().toString()
            Log.i("New item saved: ", msg)

            // add new linear layout for edit text
            val parent = findViewById(R.id.mainList) as LinearLayout
            val ll = LinearLayout(this)
            ll.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            ll.orientation = LinearLayout.HORIZONTAL

            val tv = TextView(this)
            tv.setText(enterItem.getText().toString())

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
        }
    }

    fun removeClicked (view: View){
        (view.parent.parent as ViewGroup).removeView(view.parent as ViewGroup)
    }

    companion object {
        private val TAG = "NoteActivity"
    }
}