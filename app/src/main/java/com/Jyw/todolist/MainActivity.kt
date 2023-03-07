package com.Jyw.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
                }
            )

            addBtn.isEnabled = false
            editTextInput.addTextChangedListener {
                addBtn.isEnabled = editTextInput.text.isNotEmpty()
            }
            addBtn.setOnClickListener {
                addTodo()
                editTextInput.setText(null)
            }
        }
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
    val onClickDeleteIcon: (todo: Todo) -> Unit
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
        holder.binding.deleteImageView.setOnClickListener {
            onClickDeleteIcon.invoke(todo)
        }
    }
}