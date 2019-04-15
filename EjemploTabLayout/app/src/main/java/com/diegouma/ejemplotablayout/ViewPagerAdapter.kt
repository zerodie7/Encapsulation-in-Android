package com.diegouma.ejemplotablayout

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter (fragmentManager) {

    var fragmentList:ArrayList<Fragment>? = null
    var fragmentTitleList:ArrayList<String>? = null

    init {
        fragmentList= ArrayList()
        fragmentTitleList= ArrayList()
    }

    override fun getItem(p0: Int): Fragment {
        return fragmentList?.get(p0)!!
    }

    override fun getCount(): Int {
        return fragmentList?.size!!
    }

    fun addFragment(fragment: Fragment, title:String){
        fragmentList?.add(fragment)
        fragmentTitleList?.add(title)!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList?.get(position)!!
    }

}