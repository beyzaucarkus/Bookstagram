package com.example.myapplication5

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


import com.example.myapplication5.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.sql.Timestamp
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var storage:FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher:ActivityResultLauncher<String>

        /*get() = _permissionLauncher
        set(value) {
            _permissionLauncher = value
        }*/
    var selectedPicture: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerlauncher()
        auth=Firebase.auth
        firestore=Firebase.firestore
        storage=Firebase.storage
    }

    fun upload( view: View)
    {
        val uuid=UUID.randomUUID()
        val imagename="$uuid.jpg"
        val reference=storage.reference
        val imagereference=reference.child("images").child(imagename)

if(selectedPicture !=null)
{
imagereference.putFile(selectedPicture !!).addOnSuccessListener {
//dowloand
val uploadPictureReference=storage.reference.child("images").child(imagename)
uploadPictureReference.downloadUrl.addOnSuccessListener {
    val dowloandUrl=it.toString()
    val postMap= hashMapOf<String, Any>()
    if(auth.currentUser != null)
    {
        postMap.put("dowloandUrl",dowloandUrl)
        postMap.put("userEmail",auth.currentUser!!.email!!)
        postMap.put("comment",binding.commendtext.text.toString())
        postMap.put("date",com.google.firebase.Timestamp.now())
        firestore.collection("posts").add(postMap).addOnSuccessListener {
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
        } }
}
    }.addOnFailureListener(){
       Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
    } }
}


    fun selectimage(view:View)
    {
if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
    if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
        Snackbar.make(view,"permission neded for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }.show()
    }else{
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
}else{
    val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    activityResultLauncher.launch(intentToGallery) }
    }

    private fun registerlauncher()
    {
activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
    if(result.resultCode== RESULT_OK)
    {
        val intentFromResult=result.data
        if(intentFromResult != null)
        {
          selectedPicture=  intentFromResult.data
            selectedPicture?.let { binding.selectimage.setImageURI(it) }
        }
    }
}
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
if(result)
{ val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
}else{ Toast.makeText(this@UploadActivity,"Permission needed",Toast.LENGTH_LONG).show() }
        }
    }
}