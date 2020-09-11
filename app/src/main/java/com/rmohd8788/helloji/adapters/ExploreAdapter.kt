package com.rmohd8788.helloji.adapters

import android.app.AlertDialog
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rmohd8788.helloji.R
import com.rmohd8788.helloji.databinding.ExploreFeedListBinding
import com.rmohd8788.helloji.fragments.ExploreFragment
import com.rmohd8788.helloji.fragments.SavedFragment
import com.rmohd8788.helloji.model.Explore
import kotlinx.android.synthetic.main.explore_feed_list.view.*
import kotlinx.android.synthetic.main.fragment_saved.view.*
import java.util.*
import kotlin.collections.ArrayList

class ExploreAdapter(private val isSavedLayout : Boolean) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>(), Filterable {

    private var filterBy = 0

    private var exploreList: List<Explore> = emptyList()
    private var filteredList: List<Explore> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        DataBindingUtil.inflate<ExploreFeedListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.explore_feed_list, parent, false
        ).apply {
            return ViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listPosition = filteredList[position]
        holder.onBind(listPosition)

        /*
        checking if the holder is from saved fragment or explore
        so that according to this we set the layout for our post
         */

        if (isSavedLayout){
            holder.itemView.tv_save.visibility = View.GONE
            holder.itemView.tv_invite.visibility = View.GONE
            holder.itemView.save_icon.visibility = View.VISIBLE
        }else{
            holder.itemView.tv_save.visibility = View.VISIBLE
            holder.itemView.save_icon.visibility = View.GONE
            holder.itemView.tv_invite.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = filteredList.size

    fun putItemAt(position: Int){
        this.filterBy = position
    }

    //getting the current position of adapter
    fun getItemAt(position: Int):Explore = filteredList[position]

    //adding the post
    fun addPost(list: List<Explore>) {
        this.exploreList = list
        this.filteredList = list
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ExploreFeedListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(explore: Explore) {
            //binding the data
            binding.exploreDataBinding = explore
            //updating the profile
            binding.ivUserProfile.setImageResource(explore.imageId)

            //check the current status so that user can't save it again
            binding.tvSave.setOnClickListener {
                if (explore.isSaved){
                    Toast.makeText(binding.root.context,"You saved it already",Toast.LENGTH_SHORT).show()
                }else{
                    explore.isSaved = true

                    //calling single instance
                    ExploreFragment.exploreViewModel.update(explore)
                    Toast.makeText(binding.root.context,"Post Saved",Toast.LENGTH_SHORT).show()
                }
            }

            //sharing the post and tags
            binding.tvInvite.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)

                    // Add data to the intent, the receiving app will decide
                    // what to do with it.
                    putExtra(Intent.EXTRA_SUBJECT,"Testing Post")
                    putExtra(Intent.EXTRA_TEXT,TextUtils.concat(explore.description,explore.tags)).toString()

                    binding.root.context!!.startActivity(Intent.createChooser(this,"Share it!"))
                }
            }

            //removing form saved post make sure user doesn't unsaved by mistake
            binding.saveIcon.setOnClickListener {
                val verifyDeleteDialog = AlertDialog.Builder(binding.root.context)
                    .setTitle("Are you sure?")
                    .setMessage("Do you really want to unsave the post?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, _ ->
                        explore.isSaved = false
                        SavedFragment.savedListModel.update(explore)
                        Toast.makeText(binding.root.context,"Post unsaved",Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNeutralButton("No"){ dialog, _->
                        dialog.dismiss()
                    }.create()

                verifyDeleteDialog.show()
            }

        }
    }

    //filtering the post
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(char: CharSequence?): FilterResults {
                val charSearch = char.toString()
                filteredList = if (charSearch.isEmpty()) { exploreList
                } else {
                    val resultList = ArrayList<Explore>()
                    for (list in exploreList) {
                        when(filterBy){
                            0 ->{
                                if (list.tags.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                                    resultList.add(list)
                                }
                            }
                            1 ->{
                                if (list.username.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                                    resultList.add(list)
                                }
                            }
                            2 ->{
                                if (list.jobTitle.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                                    resultList.add(list)
                                }
                            }
                            3 ->{
                                if (list.description.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                                    resultList.add(list)
                                }
                            }
                        }
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
                filteredList = filterResults!!.values as List<Explore>
                notifyDataSetChanged()
            }

        }
    }
}