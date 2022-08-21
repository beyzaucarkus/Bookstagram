package com.example.myapplication5.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication5.databinding.RecyclerrowBinding
import com.example.myapplication5.model.post
import com.squareup.picasso.Picasso

data class recycleradapter(private val postlist:ArrayList<post>):RecyclerView.Adapter<recycleradapter.postholder>()
{
    class postholder(val binding:RecyclerrowBinding):RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postholder
    {
     val binding =RecyclerrowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return postholder(binding) }

    override fun onBindViewHolder(holder: postholder, position: Int)
    { //holder.binding.recycleremail.text=postlist.get(position).email
       holder.binding.recyclercomment.text=postlist.get(position).comment
       Picasso.get().load(postlist.get(position).dowloandUrl).into(holder.binding.recyrlerimageview)
    }

    override fun getItemCount(): Int { return postlist.size }
}