package com.example.myapplication5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication5.adapter.recycleradapter
import com.example.myapplication5.databinding.ActivityFeedBinding
import com.example.myapplication5.model.post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FeedActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db :FirebaseFirestore
    private lateinit var  postArrayList:ArrayList<post>
    private lateinit var adapter: recycleradapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth=Firebase.auth
        db=Firebase.firestore
        postArrayList=ArrayList<post>()
        getdata()
        binding.recyclerview.layoutManager=LinearLayoutManager(this)
        adapter= recycleradapter(postArrayList)
        binding.recyclerview.adapter=adapter
    }

private fun getdata()
{
    db.collection("posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
        if(error != null)
        { Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
        }else
        { if(value !=null)
            { if(!value.isEmpty)
                {
                    val documents =value.documents
                    postArrayList.clear()
                    for(document in documents)
                    {
                        val comment  =document.get("comment") as String
                        val userEmail =document.get("userEmail") as String
                        val dowloandUrl=document.get("dowloandUrl") as String
                        val post = post(userEmail,comment,dowloandUrl)
                        postArrayList.add(post) }
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}


    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.insmenu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    { if(item.itemId==R.id.add_post)
        {
            val intent=Intent(this,UploadActivity::class.java)
            startActivity(intent)
        }else if(item.itemId==R.id.signout){
            auth.signOut()
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}