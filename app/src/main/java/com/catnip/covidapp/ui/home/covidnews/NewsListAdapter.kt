package com.catnip.covidapp.ui.home.covidnews;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.catnip.covidapp.R
import com.catnip.covidapp.data.network.entity.responses.kawalcorona.IndonesiaProvinceCaseResponse
import com.catnip.covidapp.data.network.entity.responses.news.News
import com.catnip.covidapp.databinding.ItemCasesListBinding
import com.catnip.covidapp.databinding.ItemNewsBinding

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NewsListAdapter(val itemClick: (News) -> Unit) :
    RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    var items: List<News> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    class NewsViewHolder(
        private val binding: ItemNewsBinding,
        val itemClick: (News) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: News) {
            with(item) {
                binding.apply{
                   Glide.with(itemView.context)
                       .load(fields?.thumbnail)
                       .centerCrop()
                       .placeholder(R.drawable.ic_placeholder_poster)
                       .error(R.drawable.ic_placeholder_poster)
                       .into(ivImgThumbnail)
                    tvTitleNews.text = webTitle
                    tvTrailtextNews.text = fields?.trailText.orEmpty()
                }
                itemView.setOnClickListener { itemClick(this) }
            }

        }
    }

}