package com.wlm.milkernote

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
                rich_edit_text.text = content
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
        if (rich_edit_text.text.isBlank()) {
            return
        }
        val noteText = rich_edit_text.text
        var titleText = et_title.text.toString()
        if (titleText.isBlank()) {
            titleText = if (noteText.length > 10) noteText.substring(0, 10) else noteText
        }
        if (note == null) {
            dao.insert(
                Note(
                    0,
                    titleText,
                    noteText,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                )
            )
        } else {
            note?.run {
                title = titleText
                content = noteText
                updateTime = System.currentTimeMillis()
                dao.update(this)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.finish -> {
                save()
                finish()
            }
            R.id.delete -> {
                note?.run {
                    dao.delete(id)
//                    dao.delete(this)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
