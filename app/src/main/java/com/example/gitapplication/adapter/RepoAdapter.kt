package com.example.gitapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapplication.R
import com.example.gitapplication.pojomodel.Repository


/**
 * Adapter в RecyclerView отвечает за преобразование объекта данных в элемент интерфейса,
 * который затем появится на экране.
 *
 * Адаптер управляет данными и связывает их с ViewHolders.
 */
class RepoAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    /**
     * Этот код создает свойство data для хранения списка Репозиториев.
     * Когда data изменяется, этот код автоматически обновляет RecyclerView.
     * Это очень удобно, потому что вам не нужно вручную управлять обновлением
     * RecyclerView при изменении данных.
     *
     * ViewHolder - обеспечивает хранение ссылок на views каждого элемента списка.
     */
    var data: List<Repository> = emptyList() // Начальное значение — пустой список репозиториев
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    /**
     * Метод для создания нового объекта ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.repositories_layout, parent, false)
        return RepoViewHolder(itemView)
    }

    /**
     * Метод, который связывает данные с ViewHolder
     */
    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repository = data[position] // Получаем репозиторий из списка
        holder.repositoryName.text = repository.name
        holder.repositoryLang.text = repository.language
        holder.repositoryDescr.text = repository.description

        holder.itemView.setOnClickListener {
            listener.onItemClick(repository) // Вызываем метод интерфейса с переданным объектом
        }
    }

    /**
     * Метод, который возвращает общее количество элементов в данных
     */
    override fun getItemCount(): Int = data.size

    /**
     * ViewHolder обеспечивает хранение ссылок на views каждого элемента списка
     */
    class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val repositoryName: TextView = itemView.findViewById(R.id.repositoryName)
        val repositoryLang: TextView = itemView.findViewById(R.id.repositoryLang)
        val repositoryDescr: TextView = itemView.findViewById(R.id.repositoryDescr)
    }
}
