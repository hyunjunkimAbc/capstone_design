package com.example.capstone_android

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.capstone_android.Util.SingleTonData
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



class DetailViewFragment: Fragment() {
    lateinit var db : FirebaseFirestore
    var scrapMainLayout:GridLayout?=null
    var clubdata:ArrayList<ClubData> = arrayListOf()
    var address:String?=null
    var clubroomuid:ArrayList<String> =arrayListOf()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= LayoutInflater.from(activity).inflate(R.layout.fragment_main,container,false)
        db= Firebase.firestore
        val uid= Firebase.auth.currentUser?.uid
        val fab:FloatingActionButton=view.CreateClub
        val changefab:FloatingActionButton=view.Settingbtn
        val recyclerView:RecyclerView=view.detailviewfragment_recyclerview
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

        scrapMainLayout=view.imagegrid
        //scrapMainLayout?.columnCount=4



        view.detailviewfragment_recyclerview.adapter=DetailViewRecyclerViewAdapter()
        view.detailviewfragment_recyclerview.layoutManager=LinearLayoutManager(activity)

        view.refresh_layout.setOnRefreshListener {
            //clubdata.shuffle()
            update()
            view.detailviewfragment_recyclerview.adapter?.notifyDataSetChanged()
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
    fun update_interest(){
        scrapMainLayout?.removeAllViews()
        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener{ document->
            val item=document.toObject(SignUpData::class.java)
            for(data in item?.interest_array!!){
                interest_text(data)
            }
        }
    }

    fun interest_text(data:String){
        when (data) {
            "축구" -> {
                val btn = SoccerButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "농구"->{
                val btn = BasketballButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "탁구"->{
                val btn = PingpongButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "테니스"->{
                val btn = TennisButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "배드민턴"->{
                val btn = BadmintonButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "야구"->{
                val btn = BaseballButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "볼링"->{
                val btn = BowlingButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "자전거"->{
                val btn = BicycleButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "골프"->{
                val btn = GolfButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "런닝"->{
                val btn = RunningButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "수영"->{
                val btn = SwimButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "배구"->{
                val btn = VolleyballButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "요가|필라테스"->{
                val btn = YogaButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "태권|유도"->{
                val btn = TaekwondoButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "복싱"->{
                val btn = BoxButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "무술"->{
                val btn = MusulButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "승마"->{
                val btn = HorseButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "헬스"->{
                val btn = HellsButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "롤러|보드"->{
                val btn = RollerboardButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "스키|보드"->{
                val btn = SkiboardButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "당구"->{
                val btn = DangguButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "등산"->{
                val btn = HikingButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
            "수상|레저"->{
                val btn = LeisureButton(requireContext())
                btn.setOnClickListener(ButtonListener(data))
                scrapMainLayout?.addView(btn)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    fun update() {
        SingleTonData.clubdata.clear()
        clubdata.clear()
        db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener{   document->
            val item=document.toObject(SignUpData::class.java)
            address=item?.address
            for(data in item?.interest_array!!){
                interest_text(data)
                db.collection("meeting_room").whereEqualTo("category",data).whereEqualTo("address",address).get().addOnSuccessListener {
                        snapshot->
                    for(doc in snapshot){
                        clubdata.add(doc.toObject(ClubData::class.java))
                        SingleTonData.clubdata.add(doc.toObject(ClubData::class.java))
                        SingleTonData.clubdata.sortByDescending { it.positionx }
                        view?.detailviewfragment_recyclerview?.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor", "SetTextI18n")
    inner class DetailViewRecyclerViewAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        //var clubdata:ArrayList<ClubData> = arrayListOf()

        init{
            SingleTonData.clubdata.clear()
            clubdata.clear()
            db.collection("user").document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener{   document->
                val item=document.toObject(SignUpData::class.java)
                view?.UserName?.text=item?.nickname+"님을"
                address=item?.address
                for(data in item?.interest_array!!){
                 interest_text(data)
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
    inner class ButtonListener(val hobby: String):View.OnClickListener{
        override fun onClick(v: View?) {
            val intent= Intent(activity, ClickiconActivity::class.java)
            intent.putExtra("hobby", hobby)
            intent.putExtra("address",address)
            startActivity(intent)
        }

    }
}