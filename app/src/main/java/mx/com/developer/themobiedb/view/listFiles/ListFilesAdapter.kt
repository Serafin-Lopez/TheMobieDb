package mx.com.developer.themobiedb.view.listFiles

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.com.developer.themobiedb.R
import mx.com.developer.themobiedb.helpers.loadText
import mx.com.developer.themobiedb.helpers.loadUrlImage
import mx.com.developer.themobiedb.view.uploadFiles.File


class ListFilesAdapter : RecyclerView.Adapter<ListFilesAdapter.FilesListViewHolder>() {
    private var data: List<File> = ArrayList()
    var listCallback: FilesListCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesListViewHolder {
        return FilesListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movies_list, parent, false),
            listCallback
        )
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FilesListViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.bind(data[position])

    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<File>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FilesListViewHolder(itemView: View, var filesListCallBack: FilesListCallback?) : RecyclerView.ViewHolder(itemView) {

        var name:TextView = itemView.findViewById(R.id.textViewMovie)
        var imageView: ImageView = itemView.findViewById(R.id.imageViewMovie)

        var file: File?= null

        fun bind(item: File) = with(itemView) {

            name.loadText(item.title)

            imageView.loadUrlImage(item.imageUrl)

            file = item

            setOnClickListener {
                selectItem()
            }

        }

        /**
         * this function was created to select the item in recyclerView
         */

        private fun selectItem() {
            file?.let {
                filesListCallBack?.onFileSelected(it)
            }
        }

    }

}

interface FilesListCallback {
    fun onFileSelected(file: File)
}