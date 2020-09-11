package com.rmohd8788.helloji.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.rmohd8788.helloji.R
import com.rmohd8788.helloji.SwipeController
import com.rmohd8788.helloji.adapters.ExploreAdapter
import com.rmohd8788.helloji.model.Explore
import com.rmohd8788.helloji.viewmodels.ExploreViewModel
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment() {

    private var checkedItem = 0

    //single instance
    companion object {
        lateinit var exploreViewModel: ExploreViewModel
    }
    private lateinit var expAdapter: ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()

        //observing posts from viewModel
        exploreViewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)
        exploreViewModel.allTweets.observe(viewLifecycleOwner, {
            it?.let {
                expAdapter.addPost(it)
            }
        })

        img_btn_filter.setOnClickListener {
            showDialog()
        }
        filteringThePost()
//        insertingData()
        swipeToDelete()

    }

    private fun setupRecyclerView() {
        rv_explore.layoutManager = LinearLayoutManager(context)
        expAdapter = ExploreAdapter(false)
        rv_explore.adapter = expAdapter
    }

    private fun filteringThePost() {
        et_exp_filter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                expAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                expAdapter.filter.filter(s)
            }

        })
    }

    private fun insertingData(){
        //inserting data into feedList
        exploreViewModel.insert(
            Explore(imageId = R.drawable.pic_1, username = "Hs Alex",
                jobTitle = "Jk Pvt Ltd,East",
                description = "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores.",
                tags = "#foodie #pose #mac #coding" )
        )
    }

    private fun swipeToDelete(){
        val swipeController = SwipeController {
            exploreViewModel.delete(expAdapter.getItemAt(it))
        }
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(rv_explore)

    }

    private fun showDialog() {
        val filterItems =
            arrayOf("Search by tags", "Search by names", "Search by jobs", "Search by posts")
        val filterDialog = AlertDialog.Builder(context)
            .setTitle("Choose Filter")
            .setCancelable(false)
            .setSingleChoiceItems(filterItems, checkedItem) { dialogInterface, i ->
                //passing the arrays current position to adapter, so that adapter can perform the filtering
                //according to the position
                expAdapter.putItemAt(i)
                et_exp_filter.hint = filterItems[i]
                checkedItem = i
                dialogInterface.dismiss()
            }
            .setNeutralButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create()
        filterDialog.show()

    }
}