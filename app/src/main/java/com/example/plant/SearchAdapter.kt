package com.example.plant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable

import android.widget.TextView

class SearchAdapter (private var items: ArrayList<SearchListItem>): BaseAdapter(), Filterable {

    private var filteredItemList: ArrayList<SearchListItem> = items

    override fun getCount(): Int {
        return filteredItemList.size
    }

    override fun getItem(position: Int): Any {
        return filteredItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.search_item, parent, false)
        }
        val item = getItem(position) as SearchListItem
        var title = view!!.findViewById<TextView>(R.id.searchTitle)
        var content = view.findViewById<TextView>(R.id.searchContent)
        title.text = item.getTitle()
        content.text = item.getContent()

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList = ArrayList<SearchListItem>()

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(items)
                } else {
                    val query = constraint.toString().toLowerCase().trim()
                    for (item in items) {
                        if (item.getTitle().toLowerCase().contains(query) ||
                            item.getContent().toLowerCase().contains(query)) {
                            filteredList.add(item)
                        }
                    }
                }

                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    filteredItemList = results.values as ArrayList<SearchListItem>
                    notifyDataSetChanged()
                }
            }
        }
    }
}