package com.Jyw.todolist

import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Jyw.todolist.databinding.ActivityMainBinding
import com.Jyw.todolist.databinding.ItemTodoBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val data = arrayListOf<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerview.adapter = TodoAdapter(data,
                onClickDeleteIcon = {
                    deleteTodo(it)
                }, onClickItem =  {
                    toggleTodo(it)
                }
            )

            addBtn.isEnabled = false
            editTextInput.addTextChangedListener {
                addBtn.isEnabled = editTextInput.text.isNotEmpty()
                addBtn.setBackgroundResource(R.drawable.round_button)
            }

            addBtn.setOnClickListener {
                addTodo()
                editTextInput.setText(null)
            }
        }
    }

    private fun toggleTodo(todo: Todo) {
        todo.isDone = !todo.isDone
        binding.recyclerview.adapter?.notifyDataSetChanged()
    }

    private fun addTodo() {
        val todo = Todo(binding.editTextInput.text.toString())
        data.add(todo)
        binding.recyclerview.adapter?.notifyDataSetChanged()
    }

    private fun deleteTodo(todo: Todo) {
        data.remove(todo)
        binding.recyclerview.adapter?.notifyDataSetChanged()
    }
}

data class Todo(val text: String, var isDone: Boolean = false)

class TodoAdapter(
    val listData: ArrayList<Todo>,
    val onClickDeleteIcon: (todo: Todo) -> Unit,
    val onClickItem: (todo: Todo) -> Unit
) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(ItemTodoBinding.bind(view))
    }

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = listData.get(position)

        holder.binding.textTodo.text = todo.text
        if (todo.isDone) {
            holder.binding.textTodo.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }
        } else {
            holder.binding.textTodo.apply {
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }
        }

        holder.binding.deleteImageView.setOnClickListener {
            onClickDeleteIcon.invoke(todo)
        }

        holder.binding.root.setOnClickListener {
            onClickItem.invoke(todo)
        }
    }
}