package com.example.plant

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2

class Fragment_Garden : Fragment() {
    private lateinit var viewPagerTip:ViewPager2
    private lateinit var gridView:GridView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment__garden, container, false)
        viewPagerTip = view.findViewById(R.id.viewPagerTip)
        gridView = view.findViewById(R.id.gridView)
        gridView.adapter = GridPlantListAdapter(getPlantList())
        viewPagerTip.adapter = ViewPagerAdapter(getTipList())
        val intent = Intent(requireContext(), CalenderActivity::class.java)
        gridView.setOnItemClickListener { parent, view, position, id ->
            val item: PlantListItem = (parent.adapter as GridPlantListAdapter).getItem(position) as PlantListItem
            startActivity(intent)
            Toast.makeText(context, item.getNum().toString(), Toast.LENGTH_SHORT).show()
        }
        return view
    }
    private fun getTipList(): ArrayList<String> {
        return arrayListOf("채광이 잘드는 장소를 찾은 다음에 무엇을 키울지 결정하는것이 좋습니다.", "지나치게 물을 많이 주지말고 흙이 건조할때 물을 주는것이 좋습니다.",
            "너무 강한 직사광선은 식물에게 해롭습니다.", "식물의 잘 자라는 온도는 18~23도로 사람과 같습니다. 열대식물과 같이 습한 환경에서 잘자라는 식물은 분무기로 2~3일에 한번씩 물을 뿌려줍시다.")
    }
    private fun getPlantList(): ArrayList<PlantListItem> {
        val plantList = ArrayList<PlantListItem>()

        val plant1 = PlantListItem()
        plant1.setName("스킨답서스")
        plant1.setImgSrc(R.drawable.img_7) // Assuming you have a rose_image in your resources
        plant1.setNum(1111111)
        plantList.add(plant1)

        val plant2 = PlantListItem()
        plant2.setName("테이블야자")
        plant2.setImgSrc(R.drawable.img_8) // Assuming you have a sunflower_image in your resources
        plant2.setNum(2222222)
        plantList.add(plant2)

        val plant3 = PlantListItem()
        plant3.setName("허브")
        plant3.setImgSrc(R.drawable.img_6) // Assuming you have a rose_image in your resources
        plant3.setNum(3333333)
        plantList.add(plant3)

        val plant4 = PlantListItem()
        plant4.setName("아이비")
        plant4.setImgSrc(R.drawable.img_5) // Assuming you have a rose_image in your resources
        plant4.setNum(4444444)
        plantList.add(plant4)

        return plantList
    }



}