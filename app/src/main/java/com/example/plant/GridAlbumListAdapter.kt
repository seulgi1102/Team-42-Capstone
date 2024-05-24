package com.example.plant
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class GridAlbumListAdapter(var items: ArrayList<PlantListItem>): BaseAdapter(){
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    fun updateData(newPlantList: ArrayList<PlantListItem>) {
        items.clear()
        items.addAll(newPlantList)
        notifyDataSetChanged()
    }
    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view =
            LayoutInflater.from(parent?.context).inflate(R.layout.grid_album_item, parent, false)
        val item = items[position]
        var itemName = view.findViewById<TextView>(R.id.itemName)
        var itemImage = view.findViewById<ImageView>(R.id.imageView3)
        var date = view.findViewById<TextView>(R.id.date)
        var place = view.findViewById<TextView>(R.id.place)
        var detail = view.findViewById<TextView>(R.id.detail)
        date.text = item.getPlantDate()
        place.text = item.getPlantLocation()
        detail.text =item.getPlantPoint()
        itemName.text = item.getPlantName()
        parent?.context?.let {
            Glide.with(it)
                .load(item.getImageUrl())
                .into(itemImage)
        }
        //itemImage.setImageResource(R.drawable.img_6)

        return view
    }


}