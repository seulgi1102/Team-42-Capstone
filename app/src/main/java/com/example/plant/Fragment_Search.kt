package com.example.plant

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView

class Fragment_Search : Fragment() {
    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__search, container, false)
        // Inflate the layout for this fragment
        listView = view.findViewById(R.id.searchList)
        searchView = view.findViewById(R.id.searchView)
        listView.adapter = SearchAdapter(getSearchList())
        initSearchView()
        return view
    }
    private fun getSearchList(): ArrayList<SearchListItem> {
        val searchList = ArrayList<SearchListItem>()

        val searchItem1 = SearchListItem()
        searchItem1.setTitle("행운목")
        searchItem1.setContent("행운목의 특징")
        searchList.add(searchItem1)
        val searchItem2 = SearchListItem()
        searchItem2.setTitle("푸밀라 고무나무")
        searchItem2.setContent("푸밀라 고무나무의 특징")
        searchList.add(searchItem2)
        val searchItem3 = SearchListItem()
        searchItem3.setTitle("필로덴드론")
        searchItem3.setContent("필로덴드론의 특징")
        searchList.add(searchItem3)
        val searchItem4 = SearchListItem()
        searchItem4.setTitle("스킨답서스")
        searchItem4.setContent("스킨답서스의 특징")
        searchList.add(searchItem4)
        val searchItem5 = SearchListItem()
        searchItem5.setTitle("시페루스")
        searchItem5.setContent("시페루스의 특징")
        searchList.add(searchItem5)
        return searchList
    }
    private fun initSearchView() {
        // init SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (listView.adapter as SearchAdapter).filter.filter(newText)
                return true
            }
        })
    }
}