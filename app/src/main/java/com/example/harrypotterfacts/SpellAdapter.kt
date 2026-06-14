package com.example.harrypotterfacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.harrypotterfacts.api.Spell

class SpellAdapter(
    private var spells: List<Spell>,
    private val onItemClicked: (Spell) -> Unit,
) : RecyclerView.Adapter<SpellAdapter.SpellViewHolder>() {
    class SpellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_spell_name)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SpellViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_spell, parent, false)
        return SpellViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SpellViewHolder,
        position: Int,
    ) {
        val spell = spells[position]
        holder.tvName.text = spell.name

        // Configura o evento de clique na linha da lista
        holder.itemView.setOnClickListener {
            onItemClicked(spell)
        }
    }

    override fun getItemCount(): Int = spells.size

    fun updateData(newSpells: List<Spell>) {
        spells = newSpells
        notifyDataSetChanged()
    }
}
