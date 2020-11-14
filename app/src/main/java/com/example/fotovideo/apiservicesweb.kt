package com.example.fotovideo

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_apiservicesweb.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class apiservicesweb : AppCompatActivity() {


    lateinit var service: ApiServices;
    val TAG_LOGS = "TEXTO";
    lateinit var listView:ListView;
    lateinit var adapter: ArrayAdapter<*>
    var lista1: MutableList<String> = ArrayList() ;


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apiservicesweb)
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

            service = retrofit.create<ApiServices>(ApiServices::class.java);

            listView = findViewById(R.id.list_item)
            getAllPosts()




            search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    listView = findViewById(R.id.list_item)

                    var lista2: MutableList<String> = ArrayList() ;
                    if (lista1.size != 0) {
                        if(query.length !=0){
                            for(item2 in lista1){
                                if(item2.contains(query)){
                                    lista2.add(item2)
                                }
                            }

                            listView.setAdapter(ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, lista2));

                        }
                        else{
                            listView.setAdapter(ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, lista1));

                        }


                    }else{
                        listView.setAdapter(ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, lista1));

                    }
//
//

                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    Log.i("sss",newText)
                    return false
                }
            })


        }



    fun getAllPosts(){
        //Recibimos todos los posts
        service.getAllPosts().enqueue(object: Callback<List<PlaceholderPost>> {
            override fun onResponse(call: Call<List<PlaceholderPost>>?, response: Response<List<PlaceholderPost>>?) {
                val posts = response?.body()
                val list = Gson().toJson(posts);
                listView = findViewById(R.id.list_item)


                if (posts != null) {
                    for(item in posts){
                        Log.i("Poner", item.title)
                        lista1.add(item.title)
                    }
                }
               listView.setAdapter(ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, lista1));
//
// 4

            }
            override fun onFailure(call: Call<List<PlaceholderPost>>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }



}


