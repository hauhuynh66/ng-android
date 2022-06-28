package com.app.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.data.SearchCardData
import com.app.ngn.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ExpandableCardAdapter(val context: Activity, val data : SearchCardData) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var expandPos: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CardViewHolder((context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.com_search_card, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val isExpanded = position == expandPos
        (holder as CardViewHolder).apply {
            val card = this.v.findViewById<ConstraintLayout>(R.id.com_search_card_card)
            val title = this.v.findViewById<ConstraintLayout>(R.id.com_search_card_title)
            val name = this.v.findViewById<TextView>(R.id.com_search_card_name)
            val img = this.v.findViewById<ImageView>(R.id.com_search_card_img)
            val chipGroup = this.v.findViewById<ChipGroup>(R.id.com_search_card_extra)
            name.text = data.title
            card.visibility = if (isExpanded) View.VISIBLE else View.GONE
            title.isActivated = isExpanded
            val drawable = ContextCompat.getDrawable(context,
                if(isExpanded) R.drawable.ic_baseline_arrow_upward else R.drawable.ic_baseline_arrow_downward)
            img.setImageDrawable(drawable)
            if(data.args!=null && data.args.size > 0){
                for(s:String in data.args){
                    val chip = Chip(context)
                    chip.text = s
                    chipGroup.addView(chip)
                }
            }
            title.setOnClickListener {
                expandPos = if (isExpanded) -1 else position
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    class CardViewHolder(val v : View) : RecyclerView.ViewHolder(v)
}