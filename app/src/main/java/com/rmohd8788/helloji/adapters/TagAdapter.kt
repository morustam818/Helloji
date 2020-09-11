package com.rmohd8788.helloji.adapters


import android.app.Activity
import android.app.AlertDialog
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.view.LayoutInflaterCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rmohd8788.helloji.R
import com.rmohd8788.helloji.databinding.TagListBinding
import com.rmohd8788.helloji.databinding.TagListBinding.inflate
import com.rmohd8788.helloji.fragments.HashTagFragment
import com.rmohd8788.helloji.model.Tags
import java.util.*
import kotlin.collections.ArrayList


class TagAdapter : RecyclerView.Adapter<TagAdapter.TagViewHolder>(), Filterable {

    private var listTags: List<Tags> = emptyList()
    private var filteredList: List<Tags> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {

        DataBindingUtil.inflate<TagListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.tag_list, parent, false
        ).apply {
            return TagViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        //passing the current tag position
        holder.onBind(filteredList[position])
    }

    override fun getItemCount() = filteredList.size

    //getting list position
    fun getTagAt(position: Int): Tags = filteredList[position]

    //adding items to list
    fun addTags(list: List<Tags>) {
        this.listTags = list
        this.filteredList = list
        notifyDataSetChanged()
    }


    class TagViewHolder(private val binding: TagListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //binding data through data binding
        fun onBind(tags: Tags) {
            binding.tagBinding = tags

            //Deleting the tag by close btn
            binding.root.setOnClickListener {

                val dialogLayout = LayoutInflater.from(itemView.context).inflate(R.layout.update_tag_dialog, null)
                val editTag: EditText? = dialogLayout.findViewById(R.id.edt_tags)
                editTag?.setText(tags.tags)

                val updateDialog = AlertDialog.Builder(binding.root.context as Activity)
                    .setTitle("Edit Tag")
                    .setView(dialogLayout)
                    .setCancelable(false)
                    .setPositiveButton("Update"){ dialogInterface, _ ->
                        val getTag = editTag?.text.toString()
                        if (getTag == tags.tags){
                            Toast.makeText(binding.root.context,"Tag can't be update" , Toast.LENGTH_SHORT).show()
                        }else{
                            HashTagFragment.tagsViewModel.update(Tags(tags = getTag,tagsKey = tags.tagsKey))
                            Toast.makeText(binding.root.context,"Tag update" , Toast.LENGTH_SHORT).show()
                        }
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton("Cancel"){ dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }.create()

                updateDialog.show()
            }

            //deleting the tags
            binding.deleteTag.setOnClickListener {
                val deleteDialog = AlertDialog.Builder(binding.root.context as Activity)
                    .setTitle("Delete?")
                    .setMessage("Are you sure want to delete this tag?")
                    .setCancelable(false)
                    .setPositiveButton("Delete") { dialogInterface, _ ->
                        HashTagFragment.tagsViewModel.delete(tags)
                        Toast.makeText(binding.root.context, "Tag Deleted!", Toast.LENGTH_SHORT).show()
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        Toast.makeText(binding.root.context, "Cancelled!", Toast.LENGTH_SHORT).show()
                        dialogInterface.dismiss()
                    }.create()

                // Show Dialog
                deleteDialog.show()
            }
        }
    }


    //filtering the data
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(char: CharSequence?): FilterResults {
                val charSearch = char.toString()
                filteredList = if (charSearch.isEmpty()) {
                    listTags
                } else {
                    val resultList = ArrayList<Tags>()
                    for (list in listTags) {
                        if (list.tags.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )
                        )
                            resultList.add(list)
                    }

                    resultList
                }

                val filterResult = FilterResults()
                filterResult.values = filteredList
                return filterResult
            }

            override fun publishResults(
                chatsequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                filteredList = filterResults!!.values as List<Tags>
                notifyDataSetChanged()
            }

        }
    }

}