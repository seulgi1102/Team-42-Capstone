import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plant.PlantListItem
import com.example.plant.R
import com.example.plant.StepListItem

class RecyclerViewStepAdapter(private val mList: ArrayList<StepListItem>) :
    RecyclerView.Adapter<RecyclerViewStepAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageView)
        var title: TextView = itemView.findViewById(R.id.stepTitle)
        var content: TextView = itemView.findViewById(R.id.stepContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.step_item, parent, false)
        return ViewHolder(view)
    }
    fun updateData(newPlantList: ArrayList<StepListItem>) {
        mList.clear()
        mList.addAll(newPlantList)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: StepListItem = mList[position]
        // holder.image.setImageResource(item.getImgSrc())
        holder.title.text = item.getStepTitle()
        holder.content.text = item.getStepContent()
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}