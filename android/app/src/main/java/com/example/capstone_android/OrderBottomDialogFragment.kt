package com.example.capstone_android

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.capstone_android.hobbyfragment.SportFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.order_bottom_dialog.view.*

class OrderBottomDialogFragment(val itemClick: (String) -> Unit) :  BottomSheetDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
       val view= inflater.inflate(R.layout.order_bottom_dialog, container, false)

        return view
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }
    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val bottomSheet: View = dialog!!.findViewById(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        val view = view
        view!!.post{
            val parent = view!!.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = view!!.measuredHeight
        }
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.sportsImageButton.setOnClickListener{
            val sportbtn=SportFragment()
            itemClick("운동")
            dialog?.dismiss()
        }
        view.tripImageButton.setOnClickListener{
            itemClick("여행")
            dialog?.dismiss()
        }
        view.musicImageButton.setOnClickListener{
            itemClick("음악")
            dialog?.dismiss()
        }
        view.jobImageButton.setOnClickListener{
            itemClick("사교")
            dialog?.dismiss()
        }
        view.readImageButton.setOnClickListener{
            itemClick("독서")
            dialog?.dismiss()
        }
        view.cookImageButton.setOnClickListener{
            itemClick("요리")
            dialog?.dismiss()
        }
        view.photoImageButton.setOnClickListener{
            itemClick("사진")
            dialog?.dismiss()
        }
        view.gameImageButton.setOnClickListener{
            itemClick("게임")
            dialog?.dismiss()

        }
        view.danceImageButton.setOnClickListener{
            itemClick("댄스")
            dialog?.dismiss()
        }
        view.carImageButton.setOnClickListener {
            itemClick("자동차")
            dialog?.dismiss()
        }
        view.petImageButton.setOnClickListener {
            itemClick("애완동물")
            dialog?.dismiss()
        }
        view.artImageButton.setOnClickListener {
            itemClick("공예")
            dialog?.dismiss()
        }
        view.volunteerImageButton.setOnClickListener {
            itemClick("봉사활동")
            dialog?.dismiss()
        }
        view.studyImageButton.setOnClickListener {
            itemClick("스터디그룹")
            dialog?.dismiss()
        }
    }
}