import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import kotlinx.android.synthetic.main.row_layout.view.*

class adupter(val  key:ArrayList<String>,val value: ArrayList<String>, val context: Context) :
        RecyclerView.Adapter<adupter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return value.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvkey?.text=key.get(position)
        holder.tvvalue?.text=value.get(position)

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to

        val tvkey = view.key
        val tvvalue = view.value
    }
}
