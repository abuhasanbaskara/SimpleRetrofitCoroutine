package com.baskara.retrofitcoroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.baskara.retrofitcoroutinedemo.databinding.ActivityMainBinding
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var retrofitInstance: AlbumService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        retrofitInstance = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        binding.btnGetAll.setOnClickListener {
            getAll()
        }
        binding.btnGetPath.setOnClickListener {
            getResponsePathParameter()
        }
        binding.btnGetSorted.setOnClickListener {
            getResponseQuery()
        }
        binding.btnPost.setOnClickListener {
            uploadAlbum()
        }
    }
    
    private fun getResponsePathParameter(){
        binding.textView.text = ""
        val pathResponse : LiveData<Response<AlbumItem>> = liveData {
            val response = retrofitInstance.getPathedAlbum(5)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            binding.textView.text = title
        })
    }

    private fun getAll(){
        binding.textView.text = ""
        val responseLiveData: LiveData<Response<Album>> = liveData {
            val response = retrofitInstance.getAlbums()
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumList = it.body()?.listIterator()
            if (albumList!=null){
                while (albumList.hasNext()){
                    val albumItem = albumList.next()
                    val result = " "+"Album title : ${albumItem.title}"+"\n"+
                            " "+"Album id : ${albumItem.id}"+"\n"+
                            " "+"User id : ${albumItem.userId}"+"\n\n\n"
                    binding.textView.append(result)
                }
            }
        })
    }

    private fun getResponseQuery(){
        binding.textView.text = ""
        val responseLiveData: LiveData<Response<Album>> = liveData {
            val response = retrofitInstance.getSortedAlbums(1) //with query
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumList = it.body()?.listIterator()
            if (albumList!=null){
                while (albumList.hasNext()){
                    val albumItem = albumList.next()
                    val result = " "+"Album title : ${albumItem.title}"+"\n"+
                            " "+"Album id : ${albumItem.id}"+"\n"+
                            " "+"User id : ${albumItem.userId}"+"\n\n\n"
                    binding.textView.append(result)
                }
            }
        })
    }

    private fun uploadAlbum(){
        binding.textView.text = ""
        val album = AlbumItem(5, "Save new title", 5)
        val postResponse : LiveData<Response<AlbumItem>> = liveData {
            val response = retrofitInstance.uploadAlbum(album)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedAlbumItem = it.body()
            val result = " "+"Album title : ${receivedAlbumItem?.title}"+"\n"+
                    " "+"Album id : ${receivedAlbumItem?.id}"+"\n"+
                    " "+"User id : ${receivedAlbumItem?.userId}"+"\n\n\n"
            binding.textView.append(result)
        })
    }
}