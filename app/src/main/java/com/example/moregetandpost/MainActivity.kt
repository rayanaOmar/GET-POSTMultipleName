package com.example.moregetandpost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {
    lateinit var nameEd: EditText
    lateinit var saveBtn: Button
    lateinit var showBtn: Button
    lateinit var recyclerView: RecyclerView

    lateinit var names: ArrayList<People>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEd = findViewById(R.id.editText1)
        saveBtn = findViewById(R.id.button)
        showBtn = findViewById(R.id.getNameBtn)
        recyclerView = findViewById(R.id.rv)

        names = ArrayList<People>()
        recyclerView.adapter = RVadapter(this, names)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if(apiInterface != null){
            apiInterface.getName()?.enqueue(object: Callback<ArrayList<People>>{
                override fun onResponse(
                    call: Call<ArrayList<People>>,
                    response: Response<ArrayList<People>>
                ) {
                    for(i in response.body()!!){
                        names.add(i)
                        nameEd.text.clear()
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<People>>, t: Throwable) {
                    Toast.makeText(applicationContext, ""+t.message, Toast.LENGTH_SHORT).show();

                }
            })
        }

        saveBtn.setOnClickListener {
            val f = People(nameEd.text.toString())

            addSingleUser(f, onResult = {
                nameEd.text.clear()

                Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show()
            })
        }

        showBtn.setOnClickListener {
            recyclerView.adapter?.notifyDataSetChanged()
            recyclerView.isVisible = true
        }
    }

    private fun addSingleUser(f: People, onResult: () -> Unit){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        apiInterface?.addUser(f)?.enqueue(object : Callback<People>{
            override fun onResponse(call: Call<People>, response: Response<People>) {
                onResult()
            }

            override fun onFailure(call: Call<People>, t: Throwable) {
                onResult()
                Toast.makeText(applicationContext, "ERROR!", Toast.LENGTH_SHORT).show();
            }
        })
    }
}