package com.rmohd8788.helloji.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rmohd8788.helloji.R
import com.rmohd8788.helloji.SwipeController
import com.rmohd8788.helloji.adapters.TagAdapter
import com.rmohd8788.helloji.model.Tags
import com.rmohd8788.helloji.viewmodels.TagsViewModel
import kotlinx.android.synthetic.main.fragment_hash_tag.*

class HashTagFragment : Fragment() {

    //if u need a singleton class that only has one instance, that's what companion object do
    companion object{
        lateinit var tagsViewModel: TagsViewModel
    }
    private lateinit var getTags: String
    private lateinit var tagAdapter: TagAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hash_tag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()

        //calling viewModelProvider to observe the date from the room database
        tagsViewModel = ViewModelProvider(this).get(TagsViewModel::class.java)
        tagsViewModel.allTags.observe(viewLifecycleOwner, {
            it?.let {
                tagAdapter.addTags(it)
            }
        })

        //deleting the tags
        swipeToDelete()

        //adding tags in room so that observer can observe the changes
        addingTags()

        //filtering the tags by adding text watcher listener
        filteringTags()

    }

    private fun filteringTags() {
        et_search_tag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tagAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                tagAdapter.filter.filter(s)
            }
        })
    }

    private fun addingTags() {
        btn_addTag.setOnClickListener {
            getTags = et_search_tag.text.toString()
            if (getTags.isEmpty()) {
                Toast.makeText(context, "You can't add a empty tag", Toast.LENGTH_SHORT).show()
            } else if (getTags.substring(0, 1) != "#") {
                getTags = "#$getTags"
                tagsViewModel.insert(Tags(tags = getTags))
            }
            et_search_tag.setText("")
        }
    }

    private fun swipeToDelete() {
        //adding swipe to delete functionality
        val swipeController = SwipeController {
            tagsViewModel.delete(tagAdapter.getTagAt(it))
        }

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(rv_tag)
    }

    private fun setupRecyclerView() {
        rv_tag.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        tagAdapter = TagAdapter()
        rv_tag.adapter = tagAdapter
    }

}