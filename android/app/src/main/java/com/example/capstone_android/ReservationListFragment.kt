package com.example.capstone_android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_android.databinding.FragmentReservationlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReservationListFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentReservationlistBinding? = null
    private val binding get() = _binding!!

    lateinit var auth: FirebaseAuth
    private val viewModel: ReservationViewModel by viewModels<ReservationViewModel>()

    val db = Firebase.firestore
    var stRef = Firebase.storage.reference
    var bmp: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        binding.textView.text = "예약신청내역"
    }

    private fun initializeViews() {
        auth = FirebaseAuth.getInstance()
        db.collection("user").document(auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener {
                if (it["place_id_list"] == null) {
                    Toast.makeText(this.activity, "등록한 장소가 없습니다.", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                } else {
                    val adapter = ReservationListAdapter(viewModel)
                    binding.reserlist.adapter = adapter
                    binding.reserlist.layoutManager = LinearLayoutManager(activity)
                    binding.reserlist.setHasFixedSize(true)
                    viewModel.itemsListData.observe(viewLifecycleOwner) {
                        adapter.notifyDataSetChanged()
                    }
                    viewModel.itemClickEvent.observe(viewLifecycleOwner) {
                        val i = viewModel.itemClickEvent.value
                    }
                    registerForContextMenu(binding.reserlist)

                    //데이터 얻어와서 ui에 반영
                    initDataAndUI()
                }
            }
    }

    private fun initDataAndUI() {
        db.collection("user").document(auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener {
                var place_id_list = it["place_id_list"] as List<String>
                viewModel.items.clear()
                for (places in place_id_list) {
                    db.collection("place_rental_room").document(places).get()
                        .addOnSuccessListener {
                            if (it["reservation_uid_list"] == null) {
                                return@addOnSuccessListener
                            }
                            var reservation_uid_list =
                                it["reservation_uid_list"] as List<String>
                            var placeImageRes =
                                stRef.child("place_rental_room/${it["document_id"]}.jpg")
                            placeImageRes.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    bmp = BitmapFactory.decodeByteArray(
                                        it.result,
                                        0,
                                        it.result.size
                                    )
                                    for (reservationHistory in reservation_uid_list) {
                                        db.collection("reservation")
                                            .document(reservationHistory)
                                            .get()
                                            .addOnSuccessListener {
                                                var reserDocument = it.id
                                                var placeImage = bmp!!
                                                var placeName = it["placeName"].toString()
                                                var reservatorName =
                                                    it["reservatorName"].toString()
                                                var requestDate = it["requestDate"].toString()
                                                var startReservedSchedule =
                                                    it["startReservedSchedule"].toString()
                                                var endReservedSchedule =
                                                    it["endReservedSchedule"].toString()
                                                var numOfPeople =
                                                    it["numOfPeople"].toString().toInt()
                                                var okCheck =
                                                    it["okCheck"].toString().toBoolean()
                                                var requestToday = it["requestToday"].toString()
                                                var requestTime =
                                                    it["requestTime"].toString().toLong()

                                                viewModel.addItem(
                                                    ReservationData(
                                                        reserDocument,
                                                        placeImage,
                                                        placeName,
                                                        reservatorName,
                                                        requestDate,
                                                        startReservedSchedule,
                                                        endReservedSchedule,
                                                        numOfPeople,
                                                        okCheck,
                                                        requestToday,
                                                        requestTime
                                                    )
                                                )
                                            }.addOnFailureListener {
                                                println("recycler view 요소 한개 얻어오는 것 실패 변수 조정")
                                            }
                                    }
                                }
                            }
                        }
                }
            }
    }
}