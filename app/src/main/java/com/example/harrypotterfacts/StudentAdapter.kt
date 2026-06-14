package com.example.harrypotterfacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.harrypotterfacts.api.Character
import com.squareup.picasso.Picasso

class StudentAdapter(private var students: List<Character>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhoto: ImageView = itemView.findViewById(R.id.iv_student_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_student_name)
        val tvHouse: TextView = itemView.findViewById(R.id.tv_student_house)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StudentViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int,
    ) {
        val student = students[position]

        holder.tvName.text = student.name
        holder.tvHouse.text = student.house.ifEmpty { "Unknown House" }

        if (student.image.isNotEmpty()) {
            Picasso.get()
                .load(student.image)
                .into(holder.ivPhoto)
        } else {
            holder.ivPhoto.setImageDrawable(null)
        }
    }

    override fun getItemCount(): Int = students.size

    // Função para atualizar a lista quando a casa mudar
    fun updateData(newStudents: List<Character>) {
        students = newStudents
        notifyDataSetChanged()
    }
}
