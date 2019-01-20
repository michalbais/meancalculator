package buddy.biased.thinkingcalculator

/**
 * Created by Michal on 5/28/2018.
 */
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.history_recycler_item.view.*


class HistoryFragment : Fragment() {

    lateinit var dbHelper: SQLiteDatabaseHelper

    companion object {

        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_history, container,
                false)
        val activity = activity
        dbHelper = SQLiteDatabaseHelper(activity)
        val recyclerView = view.findViewById<RecyclerView>(R.id.history_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = HistoryAdapter(dbHelper.getAllEquations(), activity)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

      /*  val activity = activity
        dbHelper = SQLiteDatabaseHelper(activity)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.history_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = HistoryAdapter(dbHelper.getAllEquations(), activity)*/
    }

    internal inner class HistoryAdapter(val items : List<Equation>, context: Context) : RecyclerView.Adapter<ViewHolder>() {

        override fun getItemCount(): Int {
            return items.size
        }

        private val layoutInflater: LayoutInflater

        init {
            layoutInflater = LayoutInflater.from(context)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.history_recycler_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.equationTextView?.text = items.get(position).equation + items.get(position).result
        }
    }

    internal inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val equationTextView = view.history_item_text_view
    }

}
