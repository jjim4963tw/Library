package com.jjim4963tw.library.layout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.jjim4963tw.library.R
import com.jjim4963tw.library.databinding.ActivityRecyclerViewBinding
import com.jjim4963tw.library.databinding.AdapterRecyclerViewBinding


data class MyData(
        val id: Int,
        var text: String,
)

class RecyclerViewActivity : AppCompatActivity(R.layout.activity_recycler_view) {
    private lateinit var binding: ActivityRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rvList1.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            //確定每個Item高度，固定可用此增加效能
            this.setHasFixedSize(true)

            val adapter = MyDiffUtilViewAdapter().apply {
                this.setList(listOf(MyData(1, "Hello World")))
            }

            val listAdapter = MyListAdapter().apply {
                this.setList(listOf(MyData(1, "Hello World")))
            }

            val sortedAdapter = MySortedAdapter().apply {
                addData(MyData(1, "Hello World"))
            }

            val concatAdapter = ConcatAdapter(adapter, listAdapter, sortedAdapter)
            concatAdapter.removeAdapter(sortedAdapter)
            concatAdapter.addAdapter(sortedAdapter)
            this.adapter = concatAdapter

            /**
             * setIsolateViewTypes：設定是否要將ViewType 個別儲存，預設為true。如組合的adapter為相同，或有辦法確定所有ViewType一定不一樣，即可設為false。
             * setStableIdMode：可使用這個方法避免重繪整個View
             *      1. 可指定沒有stable id
             *      2. 有stable id 但各個adapter分開
             *      3. 有stable id 且各個adapter都是獨一無二
             */
            val concatAdapter1 = ConcatAdapter(ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .setStableIdMode(ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS)
                    .build()
            )
        }
    }
}

//region DiffUtil
class MyDiffUtilViewAdapter : RecyclerView.Adapter<MyDiffUtilViewAdapter.MyViewHolder>() {
    private var list: List<MyData> = listOf()

    fun setList(list: List<MyData>) {
        // 建立一個DiffUtil.callback，透過呼叫calculateDiff計算差異
        val result = MyDiffUtil(this.list, list).run {
            DiffUtil.calculateDiff(this)
        }
        this.list = list

        // 將差異結果dispatch給adapter
        result.dispatchUpdatesTo(this)

        // AdapterListUpdateCallback 實作所有更新資料的方式
        result.dispatchUpdatesTo(AdapterListUpdateCallback(this))

        // 自定義 refresh data
        result.dispatchUpdatesTo(object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position + 1, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position + 1, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition + 1, toPosition + 1)
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                notifyItemRangeChanged(position + 1, count, payload)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(AdapterRecyclerViewBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(this.list[position])
    }

    override fun getItemCount(): Int {
        return this.list.size
    }

    class MyViewHolder(private val binding: AdapterRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MyData) {
            binding.tvTest1.text = data.text
        }
    }
}

class MyDiffUtil(
        private val oldList: List<MyData>,
        private val newList: List<MyData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
//endregion

//region ListAdapter
class MyListAdapter : ListAdapter<MyData, MyListAdapter.MyViewHolder>(MyDiffUtil2()) {

    fun setList(list: List<MyData>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(AdapterRecyclerViewBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: AdapterRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MyData) {
            binding.tvTest1.text = data.text
        }
    }
}

class MyDiffUtil2 : DiffUtil.ItemCallback<MyData>() {
    override fun areItemsTheSame(oldItem: MyData, newItem: MyData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MyData, newItem: MyData): Boolean {
        return oldItem == newItem
    }
}
//endregion

//region SortedList
/**
 * 在DiffUtil計算差異的基礎上，再加上Java的compare來排序資料
 */
class MySortedListCallback(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) : SortedListAdapterCallback<MyData>(adapter) {
    override fun compare(o1: MyData?, o2: MyData?): Int {
        return (o1?.id ?: 0) - (o2?.id ?: 0)
    }

    override fun areContentsTheSame(oldItem: MyData?, newItem: MyData?): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(item1: MyData?, item2: MyData?): Boolean {
        return item1?.id == item2?.id
    }
}

class MySortedAdapter : RecyclerView.Adapter<MySortedAdapter.MyViewHolder>() {
    private val list = SortedList(MyData::class.java, MySortedListCallback(this))

    fun addData(data: MyData) {
        with(list) {
            beginBatchedUpdates()
            add(data)
            endBatchedUpdates()
        }
    }

    fun removeData(data: MyData) {
        with(list) {
            beginBatchedUpdates()
            remove(data)
            endBatchedUpdates()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(AdapterRecyclerViewBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }
    override fun getItemCount(): Int {
        return list.size()
    }

    class MyViewHolder(private val binding: AdapterRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MyData) {
            binding.tvTest1.text = data.text
        }
    }
}
//endregion

