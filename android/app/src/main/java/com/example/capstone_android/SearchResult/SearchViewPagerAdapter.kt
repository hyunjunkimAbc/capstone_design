package com.example.capstone_android.SearchResult

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,var searchdata:String) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
       when(position){
           0->return SearchMeetingFragment(searchdata)
           1->return SearchLightFragment(searchdata)
           2->return SearchPlaceFragment(searchdata)
           3->return SearchCompetitionFragment(searchdata)
       }
        return SearchMeetingFragment(searchdata)
    }
}