package id.nphew.binar.challenge5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nphew.binar.challenge5.databinding.ItemMovieBinding
import id.nphew.binar.challenge5.model.MoviePopular
import id.nphew.binar.challenge5.model.MoviePopularItem

class MovieAdapter(private val onClickListener: (id: Int, movie: MoviePopularItem) -> Unit) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<MoviePopularItem>() {
        override fun areItemsTheSame(oldItem: MoviePopularItem, newItem: MoviePopularItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MoviePopularItem, newItem: MoviePopularItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun updateData(movie: List<MoviePopularItem>) = differ.submitList(movie)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviePopularItem) {
            binding.apply {
                tvMovieName.text = item.title
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500${item.posterPath}")
                    .into(ivMovie)
                tvReleaseDate.text = item.releaseDate
                lyMovie.setOnClickListener {
                    onClickListener.invoke(item.id, item)
                }
            }
        }
    }


}