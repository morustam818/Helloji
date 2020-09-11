package com.rmohd8788.helloji.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rmohd8788.helloji.R
import com.rmohd8788.helloji.RecyclerItemDecoration
import com.rmohd8788.helloji.adapters.TrendingAdapter
import com.rmohd8788.helloji.model.Trending
import com.rmohd8788.helloji.viewmodels.TrendingViewModel
import kotlinx.android.synthetic.main.app_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_trending.*

class TrendingFragment : Fragment() {

    private lateinit var trendingViewModel: TrendingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_trending.layoutManager = LinearLayoutManager(context)
        rv_trending.setHasFixedSize(true)
        rv_trending.addItemDecoration(RecyclerItemDecoration(requireContext(),50,0))

        trendingViewModel = ViewModelProvider(this).get(TrendingViewModel::class.java)
        trendingViewModel.getAllTrendingData().observe(viewLifecycleOwner,  {
            rv_trending.adapter = TrendingAdapter(it)
        })
    }

}