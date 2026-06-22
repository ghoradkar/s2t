package com.myhindlab.abkat.activities.doortodoor.d2d_camp_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.myhindlab.abkat.activities.doortodoor.d2d_camp_activity.fragments.D2DCampClosingFragment
import com.myhindlab.abkat.activities.doortodoor.d2d_camp_activity.fragments.LabLocationMappingFragment
import com.myhindlab.abkat.activities.doortodoor.d2d_camp_activity.fragments.ReadinessFragment
import com.myhindlab.abkat.adapters.doortodoor.d2d_camp_activity.D2DCampActivityViewPagerAdapter
import com.myhindlab.abkat.databinding.ActivityD2DcampActivityBinding
import com.myhindlab.abkat.models.UserAttendanceModel
import com.myhindlab.abkat.models.UserAttendanceModel.Output
import com.myhindlab.abkat.models.doortodoor.D2DCampDetails

class D2DCampActivity_Activity : AppCompatActivity() {
    lateinit var adapter: D2DCampActivityViewPagerAdapter
    lateinit var binding: ActivityD2DcampActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityD2DcampActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments = ArrayList<Fragment>()
        val campDetails: D2DCampDetails.Output? =
            intent.getSerializableExtra("campDetails") as D2DCampDetails.Output
        val userAttendanceModel: UserAttendanceModel =
            intent.getSerializableExtra("userAttendanceModel") as UserAttendanceModel

        fragments.add(ReadinessFragment.newInstance(userAttendanceModel, campDetails))
        fragments.add(LabLocationMappingFragment.newInstance(userAttendanceModel, campDetails))
        fragments.add(D2DCampClosingFragment.newInstance(userAttendanceModel, campDetails))
        adapter = D2DCampActivityViewPagerAdapter(fragments, supportFragmentManager, lifecycle)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.viewPager.adapter = adapter
        binding.viewPager.setPageTransformer(MarginPageTransformer(1500))
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position) {
                0 ->
                    tab.text = "Readiness"
                1 ->
                    tab.text = "Location"
                2 ->
                    tab.text = "Closing"
            }

        }.attach()
    }
}