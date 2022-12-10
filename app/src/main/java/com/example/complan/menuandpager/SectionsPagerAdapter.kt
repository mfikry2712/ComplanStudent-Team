package com.example.complan.menuandpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.complan.ui.history.FragmentRiwayatFasilitas
import com.example.complan.ui.history.FragmentRiwayatPerson

class SectionsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity){

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FragmentRiwayatPerson()
            1 -> fragment = FragmentRiwayatFasilitas()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }



}