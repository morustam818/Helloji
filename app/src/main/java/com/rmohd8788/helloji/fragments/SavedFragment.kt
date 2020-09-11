package com.rmohd8788.helloji.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rmohd8788.helloji.R
import com.rmohd8788.helloji.adapters.ExploreAdapter
import com.rmohd8788.helloji.viewmodels.ExploreViewModel
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedFragment : Fragment() {

    //the purpose of making companion object is
    // it can be accessed by anywhere,
    // in our case we want to remove the post
    companion object {
        lateinit var savedListModel: ExploreViewModel
    }
    private lateinit var savedAdapter : ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_saved.layoutManager = LinearLayoutManager(context)
        savedAdapter = ExploreAdapter(true)
        rv_saved.adapter = savedAdapter

        savedListModel = ViewModelProvider(this).get(ExploreViewModel::class.java)
        savedListModel.allFavList.observe(viewLifecycleOwner,{
            it?.let {
                savedAdapter.addPost(it)
            }
        })
    }
}