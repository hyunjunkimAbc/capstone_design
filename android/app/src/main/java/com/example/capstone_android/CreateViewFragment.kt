package com.example.capstone_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
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
import com.example.capstone_android.SearchAddress.SearchMap
import com.example.capstone_android.Util.App
import com.example.capstone_android.Util.getImageResult
import com.example.capstone_android.data.ClubData
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

class CreateViewFragment: Fragment() {
    lateinit var storage: FirebaseStorage
    lateinit var db : FirebaseFirestore
    var photoUri: Uri?=null
    lateinit var hobby:String
    var disx:Double?=null
    var disy:Double?=null
    var bigaddress:String?=null
    var hobbycheck :Boolean = false
    var addresscheck:Boolean=false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= LayoutInflater.from(activity).inflate(R.layout.fragment_create,container,false)
        storage = Firebase.storage
        db= Firebase.firestore
        var mActivity = activity as CreateActivity

        view.address.setOnClickListener{
            Log.d(ContentValues.TAG, App.instance.toString()+"애플리케이션 생성")
            val intent = Intent(activity, SearchMap::class.java)
            intent.putExtra("create","hello")
            startActivityForResult(intent,9)
        }
        view.changehobby.setOnClickListener{
            val intent=Intent(activity, SelectHobbyActivity::class.java)
            intent.putExtra("key","select")
            startActivityForResult(intent,5)
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
            if(photoUri==null){
                Toast.makeText(context, "사진을 선택해주세요", Toast.LENGTH_LONG).show();
            }else if(view.maxnumber.text.toString()==""){
                Toast.makeText(context, "모임 제한 인원 수를 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(view.explainclub2.text.toString()==""){
                Toast.makeText(context, "모임 제한 인원 수를 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(!hobbycheck){
                Toast.makeText(context, "모임의 관심사를 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(!addresscheck){
                Toast.makeText(context, "모임 지역을 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else if(view.club.text.toString()==""){
                Toast.makeText(context, "모임의 제목을 설정해주세요", Toast.LENGTH_LONG).show();
            }
            else {
                uploadContent(hobby, view)
            }
        }
        return view
    }
    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==5){
            hobby= data?.extras?.getString("hobby").toString()
            view?.changehobby?.setImageResource(getImageResult(hobby))
            hobbycheck=true
        }
        else if(requestCode==9) {
            val name = data?.extras?.getString("name")
            bigaddress = data?.extras?.getString("address")
            println(bigaddress)
            disx = data?.extras?.getDouble("disx")
            disy = data?.extras?.getDouble("disy")
            view?.useraddress?.text = name
            view?.useraddress?.setTextColor(R.color.black)
            addresscheck=true
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun uploadContent(hobby: String, view: View){
        val s1:String= Firebase.auth.currentUser?.uid.toString()
        val s2:String=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val makeuid=s1.plus(s2)

        val clubprofileimagename=makeuid
        val storageRef=storage.reference.child("meeting_info").child(clubprofileimagename)

        storageRef.putFile(photoUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri->
                val clubdata = ClubData()
                clubdata.category=hobby
                clubdata.imageUrl=uri.toString()
                clubdata.title = view.club.text.toString()
                clubdata.info_text = view.explainclub2.text.toString()
                clubdata.max = (view.maxnumber.text.toString())
                clubdata.upload_time = System.currentTimeMillis()
                clubdata.writer_uid = Firebase.auth.currentUser?.uid.toString()
                clubdata.Uid=makeuid
                clubdata.positionx=disx
                clubdata.positiony=disy
                clubdata.address=bigaddress

                db.collection("meeting_room").document(makeuid).set(clubdata).addOnSuccessListener{
                    db.collection("meeting_room").document(makeuid).update("member_list",FieldValue.arrayUnion(Firebase.auth.currentUser?.uid.toString())).addOnSuccessListener{
                        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).update("meeting_room_id_list",FieldValue.arrayUnion(makeuid)).addOnSuccessListener{
                            activity?.setResult(Activity.RESULT_OK)
                            activity?.finish()
                            println("모임만들기 성공")
                        }
                    }
                }
            }
        }

    }
}