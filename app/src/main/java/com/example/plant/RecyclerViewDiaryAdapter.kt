import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plant.DiaryListItem
import com.example.plant.PlantListItem
import com.example.plant.R

class RecyclerViewDiaryAdapter(private val mList: ArrayList<DiaryListItem>) :
    RecyclerView.Adapter<RecyclerViewDiaryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.diaryTitle)
        var content: TextView = itemView.findViewById(R.id.diaryContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.diary_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: DiaryListItem = mList[position]
        // holder.image.setImageResource(item.getImgSrc())
        holder.title.text = item.getDiaryTitle()
        holder.content.text = item.getDiaryContent()
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}