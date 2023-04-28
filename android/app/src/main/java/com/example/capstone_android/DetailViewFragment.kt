package com.example.capstone_android

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*

import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.Util.SingleTonData
import com.example.capstone_android.Util.getImageResult
import com.example.capstone_android.button.*
import com.example.capstone_android.data.SignUpData
import com.example.capstone_android.data.getclubuid
import com.example.capstone_android.data.ClubData
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*

import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_meeting_room_info.*
import kotlinx.android.synthetic.main.item_main.view.*
import kotlinx.android.synthetic.main.item_main2.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class DetailViewFragment: Fragment() {
    lateinit var db : FirebaseFirestore
    var clubdata:ArrayList<ClubData> = arrayListOf()
    var hobbydata:ArrayList<String> = arrayListOf()
    var address:String?=null
    var clubroomuid:ArrayList<String> =arrayListOf()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_main,container,false)
        db= Firebase.firestore
        val uid= Firebase.auth.currentUser?.uid
        val fab:FloatingActionButton=view.CreateClub
        val changefab:FloatingActionButton=view.Settingbtn
        val recyclerView1:RecyclerView=view.room_recyclerview
        val recyclerView2:RecyclerView=view.hobby_recyclerview
        val nestedscrollview:NestedScrollView=view.nestedScrollView

        nestedscrollview.setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if(scrollY>oldScrollY){
                    fab.hide()
                }
                if(scrollY<oldScrollY){
                    fab.show()
                }
            }

        })
        changefab.setOnClickListener{
            val intent=Intent(activity,SelectHobbyActivity::class.java)
            intent.putExtra("key","change")
            startActivityForResult(intent,1)
        }
        fab.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(activity, CreateActivity::class.java)
                intent.putExtra("create","hello")
                startActivityForResult(intent,9)
            } else {
                Toast.makeText(activity, "스토리지 읽기 권한이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

        lifecycleScope.launch(Dispatchers.Main){
            try{
               RoadData()
                val userinfo=db.collection("user").document(Firebase.auth.currentUser?.uid.toString())
                val hobby=userinfo.get().await().toObject(SignUpData::class.java)
                hobbydata=hobby?.interest_array!!
                address=hobby.address
                recyclerView2.adapter=HobbyImageIconAdapter()
                recyclerView2.layoutManager=LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
                for(data in hobbydata){
                    val roominfo=db.collection("meeting_room").whereEqualTo("category",data).whereEqualTo("address",address).get().await()
                    for(data2 in roominfo){
                        clubdata.add(data2.toObject(ClubData::class.java))
                        SingleTonData.clubdata.add(data2.toObject(ClubData::class.java))
                        SingleTonData.clubdata.sortByDescending { it.positionx }
                    }
                }
                recyclerView1.adapter=DetailViewRecyclerViewAdapter()
                recyclerView1.layoutManager=LinearLayoutManager(activity)
                ClearData()
            }catch(e:Exception){
                Log.e(TAG,"Firebase Error : ${e.message}")
            }
        }



        view.refresh_layout.setOnRefreshListener {
            //clubdata.shuffle()
            update()
           recyclerView1.adapter?.notifyDataSetChanged()
            refresh_layout.isRefreshing = false
        }




        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if(requestCode==9){
                    update()
            }
        else if(requestCode==1){
                update_interest()
                update()

            }
    }
    fun RoadData(){
        view?.progressBar?.visibility=View.VISIBLE
        activity?.getWindow()?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    fun ClearData(){
        view?.progressBar?.visibility=View.GONE
        activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    fun update_interest(){
        hobbydata.clear()
        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener{ document->
            val item=document.toObject(SignUpData::class.java)
            for(data in item?.interest_array!!){
                hobbydata.add(data)
                view?.hobby_recyclerview?.adapter?.notifyDataSetChanged()
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    fun update() {
        RoadData()
        SingleTonData.clubdata.clear()
        clubdata.clear()
        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener{   document->
            val item=document.toObject(SignUpData::class.java)
            address=item?.address
            for(data in item?.interest_array!!){
                db.collection("meeting_room").whereEqualTo("category",data).whereEqualTo("address",address).get().addOnSuccessListener {
                        snapshot->
                    for(doc in snapshot){
                        clubdata.add(doc.toObject(ClubData::class.java))
                        SingleTonData.clubdata.add(doc.toObject(ClubData::class.java))
                        SingleTonData.clubdata.sortByDescending { it.positionx }
                        view?.room_recyclerview?.adapter?.notifyDataSetChanged()
                    }
                }
            }
            ClearData()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor", "SetTextI18n")
    inner class DetailViewRecyclerViewAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        //var clubdata:ArrayList<ClubData> = arrayListOf()

        /*
        init{
            SingleTonData.clubdata.clear()
            clubdata.clear()
            db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener{   document->
                val item=document.toObject(SignUpData::class.java)
                view?.username?.text=item?.nickname+"님을"
                address=item?.address
                for(data in item?.interest_array!!){
                // interest_text(data)
                 db.collection("meeting_room").whereEqualTo("category",data).whereEqualTo("address",address).get().addOnSuccessListener {
                     snapshot->
                     for(doc in snapshot){
                         clubdata.add(doc.toObject(ClubData::class.java))
                         SingleTonData.clubdata.add(doc.toObject(ClubData::class.java))
                         SingleTonData.clubdata.sortByDescending { it.positionx }
                         notifyDataSetChanged()
                     }
                 }
                }
            }
        }
         */

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.item_main,parent,false)
            return CustomViewHolder(view)

        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }
        @SuppressLint("CheckResult", "SuspiciousIndentation")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewholder=(holder as CustomViewHolder).itemView
            Glide.with(holder.itemView.context).load(clubdata[position].imageUrl).apply(RequestOptions().circleCrop()).into(viewholder.detailviewitem_imageview_content)
            viewholder.ClubName.text= clubdata[position].title
            viewholder.NumberCount.text=  clubdata[position].max.toString()
            viewholder.ClubExplain.text= clubdata[position].info_text
            viewholder.CardView.setOnClickListener{
                var intent= Intent(context, MeetingRoomActivity::class.java)
                intent.putExtra("meeting_room_id", clubdata[position].Uid)
                startActivity(intent)
                println(clubdata[position].Uid)
            }
        }


        override fun getItemCount(): Int {
            return  clubdata.size
        }

    }
    inner class HobbyImageIconAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.item_main2,parent,false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewholder=(holder as HobbyImageIconAdapter.CustomViewHolder).itemView
            viewholder.hobbyiconimage.setImageResource(getImageResult(hobbydata[position]))
            viewholder.iconimagetext.text=hobbydata[position]
            viewholder.hobbyimage.setOnClickListener(ButtonListener(hobbydata[position]))
        }
        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }
        override fun getItemCount(): Int {
            return hobbydata.size
        }

    }

    inner class ButtonListener(val hobby: String):View.OnClickListener{
        override fun onClick(v: View?) {
            val intent= Intent(activity, ClickiconActivity::class.java)
            intent.putExtra("hobby", hobby)
            intent.putExtra("address",address)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        print("디테일뷰 ㅍ파괴")
        super.onDestroy()

    }
}