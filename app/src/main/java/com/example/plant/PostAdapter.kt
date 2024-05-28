package com.example.plant

//import ApiService
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import ApiService
data class Post(
    val post_num: Int,
    val board_type: Int,
    val post_title: String,
    val post_content: String,
    val post_writer: String,
    val post_date: String
)

class PostAdapter(private val postList: List<Post>, private val onItemClick: (Post) -> Unit) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(),
    Filterable {

    private var filteredList: List<Post> = postList


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = mutableListOf<Post>()

                if (constraint.isNullOrEmpty()) {
                    filteredResults.addAll(postList)
                } else {
                    val query = constraint.toString().toLowerCase(Locale.getDefault()).trim()

                    for (item in postList) {
                        if (item.post_title.toLowerCase(Locale.getDefault()).contains(query) || item.post_content.toLowerCase(Locale.getDefault()).contains(query)) {
                            filteredResults.add(item)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredResults
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<Post>
                notifyDataSetChanged()
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_list, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        //val post = postList[position]
        val post = filteredList[position]
        holder.bind(post)
        holder.itemView.setOnClickListener {
            onItemClick(post)
        }

    }

    override fun getItemCount(): Int {
        //return postList.size
        return filteredList.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.viewpost_title)
        private val writerTextView: TextView = itemView.findViewById(R.id.viewpost_date)
        private val dateTextView: TextView = itemView.findViewById(R.id.post_date)
        private val coverimageView: ImageView = itemView.findViewById(R.id.coverimage)

        fun bind(post: Post) {
            titleTextView.text = post.post_title
            writerTextView.text = post.post_writer
            dateTextView.text = post.post_date
            showImageById(post.post_num)
        }

        //리사이클러뷰 사진 등록(안되면 삭제)
        private fun showImageById(id: Int) {
            // Retrofit을 사용하여 서버에 요청
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.233.22:80/") // 서버의 기본 URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            // 서버에 이미지 보기 요청
            apiService.getImageById(id).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // 서버로부터 이미지 데이터를 받아와서 이미지뷰에 표시
                        val inputStream = response.body()?.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        //coverimageView.setImageBitmap(bitmap)
                        Glide.with(coverimageView.context)
                            .load(bitmap)
                            .transform(CenterCrop(), RoundedCorners(20)) // 둥근 모서리 설정
                            .into(coverimageView)
                    } else {
                        // 오류 처리
                        Log.e("ImageLoad", "Failed to load image")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 오류 처리
                    Log.e("ImageLoad", "Error loading image", t)
                }
            })
        }

    }

}