package com.wlm.milkernote

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.wlm.milkernote.base.BaseVMActivity
import com.wlm.milkernote.db.AppDataBase
import com.wlm.milkernote.db.Note
import com.wlm.milkernote.utils.SoftKeyBoardUtils
import com.wlm.milkernote.viewmodel.CreateViewModel
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.layout_header.*

class CreateActivity : BaseVMActivity<CreateViewModel>() {
    override val providerVMClass: Class<CreateViewModel> = CreateViewModel::class.java
    override val layoutId: Int = R.layout.activity_create

    companion object {
        const val KEY_NOTE = "KEY_NOTE"
    }

    private val dao by lazy { AppDataBase.instance.getNoteDao() }
    private var note: Note? = null

    override fun init() {
        super.init()
        initView()
        initData()

        SoftKeyBoardUtils.init(this)
    }

    private fun initData() {
        val id = intent?.extras?.getInt(KEY_NOTE)
        id?.let {
            val note = dao.getNote(it)
            note.run {
                et_title.setText(title)
                rich_edit_text.setHtml(html, content)
            }
            this.note = note
        }
    }

    private fun initView() {
        headerToolbar.title = ""
        setSupportActionBar(headerToolbar)
        headerToolbar.setNavigationIcon(R.drawable.arrow_back)
        headerToolbar.setNavigationOnClickListener {
            finish()
        }
        et_title.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) rich_edit_text.hideTools()
        }
        et_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    headerToolbar.title = it.toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    private fun save() {
        val noteText = rich_edit_text.text
        if (noteText.isBlank()) {
            return
        }
        var titleText = et_title.text.toString()
        if (titleText.isBlank()) {
            titleText = if (noteText.length > 10) noteText.substring(0, 10) else noteText
            titleText = titleText.replace("[图片]", "")
            if (titleText.isBlank()) {
                titleText = "图片笔记"
            }
        }
        val html = rich_edit_text.getSavedHtml()
        if (note == null) {
            dao.insert(
                Note(
                    0,
                    titleText,
                    noteText,
                    html,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                )
            )
        } else {
            note?.run {
                title = titleText
                content = noteText
                this.html = html
                updateTime = System.currentTimeMillis()
                dao.update(this)
            }
        }
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create, menu)
        val id = intent?.extras?.getInt(KEY_NOTE)

        if (id != null) {
            menu?.findItem(R.id.delete)?.isVisible = true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.finish -> {
                save()
            }
            R.id.delete -> {
                note?.run {
                    dao.delete(id)
                    finish()
//                    dao.delete(this)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                102 -> {//选择图片
                    val uri = data?.data

                    uri?.let {
                        var bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                        bitmap = reSizeBitmap(bitmap, 400)
                        rich_edit_text.addImage(bitmap)
                    }
                }
            }
        }
    }

    private fun reSizeBitmap(bitmap: Bitmap, newWidth: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width < newWidth) {
            return bitmap
        }
        val scale = newWidth.toFloat() / width
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)

    }

}
