package com.example.capstone_android

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone_android.data.ClubData
import com.example.capstone_android.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_create.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateViewFragment: Fragment() {
    lateinit var storage: FirebaseStorage
    lateinit var db : FirebaseFirestore
    var photoUri: Uri?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= LayoutInflater.from(activity).inflate(R.layout.fragment_create,container,false)
        storage = Firebase.storage
        db= Firebase.firestore
        var mActivity = activity as CreateActivity
        view.changehobby.setOnClickListener{
            mActivity.changeFragment(1,"")
        }

        if(arguments?.getString("hobby")=="운동"){
            view.changehobby.setImageResource(R.drawable.icon_sports)
        }
        if(arguments?.getString("hobby")=="여행"){
            view.changehobby.setImageResource(R.drawable.icon_trip)
        }
        if(arguments?.getString("hobby")=="음악"){
            view.changehobby.setImageResource(R.drawable.icon_music)
        }
        if(arguments?.getString("hobby")=="사교"){
            view.changehobby.setImageResource(R.drawable.icon_job)
        }
        if(arguments?.getString("hobby")=="독서"){
            view.changehobby.setImageResource(R.drawable.icon_read)
        }
        if(arguments?.getString("hobby")=="요리"){
            view.changehobby.setImageResource(R.drawable.icon_cook)
        }
        if(arguments?.getString("hobby")=="사진"){
            view.changehobby.setImageResource(R.drawable.icon_photo)
        }
        if(arguments?.getString("hobby")=="게임"){
            view.changehobby.setImageResource(R.drawable.icon_game)
        }
        if(arguments?.getString("hobby")=="댄스"){
            view.changehobby.setImageResource(R.drawable.icon_dance)
        }
        if(arguments?.getString("hobby")=="자동차"){
            view.changehobby.setImageResource(R.drawable.icon_car)
        }
        if(arguments?.getString("hobby")=="애완동물"){
            view.changehobby.setImageResource(R.drawable.icon_pet)
        }
        if(arguments?.getString("hobby")=="공예"){
            view.changehobby.setImageResource(R.drawable.icon_art)
        }
        if(arguments?.getString("hobby")=="봉사활동"){
            view.changehobby.setImageResource(R.drawable.icon_volunteer)
        }
        if(arguments?.getString("hobby")=="스터디그룹"){
            view.changehobby.setImageResource(R.drawable.icon_study)
        }

        val filterActivityLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if(it.resultCode == AppCompatActivity.RESULT_OK && it.data !=null) {
                    photoUri = it.data?.data
                    try {
                        photoUri?.let {
                            if(Build.VERSION.SDK_INT < 28) {
                                val bitmap = MediaStore.Images.Media.getBitmap(
                                    mActivity.contentResolver,
                                    photoUri
                                )
                                view.clubprofile.setImageBitmap(bitmap)
                            } else {
                                val source = ImageDecoder.createSource(mActivity.contentResolver, photoUri!!)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                view.clubprofile.setImageBitmap(bitmap)
                            }
                        }


                    }catch(e:Exception) {
                        e.printStackTrace()
                    }
                } else if(it.resultCode == AppCompatActivity.RESULT_CANCELED){
                    Toast.makeText(mActivity, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }else{
                    Log.d("ActivityResult","something wrong")
                }
            }

        view.clubprofile.setOnClickListener{
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            filterActivityLauncher.launch(intent)
        }
        view.CreateBtn.setOnClickListener{
            uploadContent(arguments?.getString("hobby").toString(),view)
            println(arguments?.getString("hobby").toString())
        }
        return view
    }
    @SuppressLint("SimpleDateFormat")
    fun uploadContent(hobby: String, view: View){
        val s1:String= Firebase.auth.currentUser?.uid.toString()
        val s2:String=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val makeuid=s1.plus(s2)
        db.collection("category").document(hobby).update("RoomId",FieldValue.arrayUnion(makeuid)).addOnFailureListener{
            val data= hashMapOf("RoomId" to ArrayList<String>())
            println(data)
            println("테스트")
            db.collection("category").document(hobby).set(data)
            db.collection("category").document(hobby).update("RoomId",FieldValue.arrayUnion(makeuid))
        }

        val clubprofileimagename=makeuid
        val storageRef=storage.reference.child("meeting_info").child(clubprofileimagename)

        storageRef.putFile(photoUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri->
                val clubdata = ClubData()
                clubdata.category=hobby
                clubdata.imageUrl=uri.toString()
                clubdata.title = view.club.text.toString()
                clubdata.info_text = view.explainclub2.text.toString()
                clubdata.max = Integer.parseInt(view.maxnumber.text.toString())
                clubdata.upload_time = System.currentTimeMillis()
                clubdata.writer_uid = Firebase.auth.currentUser?.uid.toString()
                clubdata.Uid=makeuid
                db.collection("meeting_room").document(makeuid).set(clubdata)
                db.collection("meeting_room").document(makeuid).update("member_list",FieldValue.arrayUnion(Firebase.auth.currentUser?.uid.toString()))
                db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).update("meeting_room_id_list",FieldValue.arrayUnion(makeuid))
            }
        }
        activity?.finish()
    }
}