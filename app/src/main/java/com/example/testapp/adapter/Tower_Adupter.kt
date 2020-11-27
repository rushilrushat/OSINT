import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import kotlinx.android.synthetic.main.raw_tower.view.*
import kotlinx.android.synthetic.main.row_layout.view.*

class Tower_Adupter(val  cellid:ArrayList<String>,val datetime: ArrayList<String>, val context: Context) : RecyclerView.Adapter<Tower_Adupter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.raw_tower,parent,false))
    }

    override fun getItemCount(): Int {
        return datetime.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.no.text= (position+1).toString()
        holder.cellids.text=cellid.get(position)
        holder.datetime.text=datetime.get(position)

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to

        val no = view.tower_no
        val cellids = view.tower_cellid
        val datetime = view.tower_datetime
    }



}
