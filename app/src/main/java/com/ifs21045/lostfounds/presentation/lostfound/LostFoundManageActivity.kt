package com.ifs21045.lostfounds.presentation.lostfound

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ifs21045.lostfounds.R
import com.ifs21045.lostfounds.data.model.LostFound
import com.ifs21045.lostfounds.data.remote.MyResult
import com.ifs21045.lostfounds.databinding.ActivityLostfoundManageBinding
import com.ifs21045.lostfounds.helper.Utils.Companion.observeOnce
import com.ifs21045.lostfounds.presentation.ViewModelFactory

class LostFoundManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLostfoundManageBinding
    private val viewModel by viewModels<LostFoundViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostfoundManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAtion()
    }


    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri = result.data?.data
            // Lakukan sesuatu dengan URI gambar yang dipilih
            // Misalnya, tampilkan gambar tersebut di ImageView

        }
    }

    private fun setupAtion() {
        val isAddLostFound = intent.getBooleanExtra(KEY_IS_ADD, true)
        if (isAddLostFound) {
            manageAddLostFound()
        } else {

            val delcomTodo = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    intent.getParcelableExtra(KEY_TODO, LostFound::class.java)
                }

                else -> {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<LostFound>(KEY_TODO)
                }
            }

            if (delcomTodo == null) {
                finishAfterTransition()
                return
            }

            manageEditTodo(delcomTodo)
        }

        binding.appbarLostFoundManage.setNavigationOnClickListener {
            finishAfterTransition()
        }
    }

    private fun manageAddLostFound() {

        binding.apply {
            appbarLostFoundManage.title = "Tambah Lost And Found"

            btnLostFoundManageSave.setOnClickListener {
                val title = etLostFoundManageTitle.text.toString()
                val description = etLostFoundManageDesc.text.toString()
                val status = etLostFoundManageStatus.selectedItem.toString()

                if (title.isEmpty() || description.isEmpty()) {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage("Tidak boleh ada data yang kosong!")
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    return@setOnClickListener
                }

                observePostLostFound(title, description, status)
            }
        }
    }

    private fun observePostLostFound(title: String, description: String, status: String) {
        viewModel.postLostFound(title, description, status).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }

                is MyResult.Success -> {
                    showLoading(false)

                    val resultIntent = Intent()
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }

                is MyResult.Error -> {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage(result.error)
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    showLoading(false)
                }
            }
        }
    }

    private fun manageEditTodo(todo: LostFound) {
        binding.apply {
            appbarLostFoundManage.title = "Ubah LostAnd Found"

            etLostFoundManageTitle.setText(todo.title)
            etLostFoundManageDesc.setText(todo.description)
            // Mengatur item yang dipilih di Spinner
            val statusArray = resources.getStringArray(R.array.status)
            val statusIndex = statusArray.indexOf(todo.status)
            etLostFoundManageStatus.setSelection(statusIndex)


            btnLostFoundManageSave.setOnClickListener {
                val title = etLostFoundManageTitle.text.toString()
                val description = etLostFoundManageDesc.text.toString()
                val status = etLostFoundManageStatus.selectedItem.toString()

                if (title.isEmpty() || description.isEmpty()) {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage("Tidak boleh ada data yang kosong!")
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    return@setOnClickListener
                }

                observePutLostFound(todo.id, title, description, status, todo.iscompleted)
            }
        }
    }

    private fun observePutLostFound(
        todoId: Int,
        title: String,
        description: String,
        status: String,
        isCompleted: Boolean,
    ) {
        viewModel.putLostFound(
            todoId,
            title,
            description,
            status,
            isCompleted
        ).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }

                is MyResult.Success -> {
                    showLoading(false)
                    val resultIntent = Intent()
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }

                is MyResult.Error -> {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage(result.error)
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {


        binding.btnLostFoundManageSave.isActivated = !isLoading

        binding.btnLostFoundManageSave.text =
            if (isLoading) "" else "Simpan"
    }

    companion object {
        const val KEY_IS_ADD = "is_add"
        const val KEY_TODO = "todo"
        const val RESULT_CODE = 1002
    }
}