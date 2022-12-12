package com.android.sepiapets.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.sepiapets.databinding.PetListItemBinding
import com.android.sepiapets.models.petsData.Pet
import com.android.sepiapets.utils.setImage

class PetListAdapter(val onClickListener: OnClickListener) :
    RecyclerView.Adapter<PetListAdapter.PetListViewHolder>() {

    private val petList = mutableListOf<Pet>()

    fun setPetList(list: List<Pet>) {
        val positionStart = petList.size
        petList.addAll(list)
        notifyItemRangeChanged(positionStart, list.size)
    }

    inner class PetListViewHolder(private val binding: PetListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pet) {
            binding.apply {
                petImage.setImage(item.imageUrl)
                petName.apply {
                    text = item.title
                }

                binding.root.setOnClickListener {
                    onClickListener.onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PetListViewHolder(PetListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PetListViewHolder, position: Int) {
        holder.bind(petList[position])
    }

    override fun getItemCount(): Int {
        return petList.size
    }


    interface OnClickListener {
        fun onItemClick(pet: Pet)
    }
}
