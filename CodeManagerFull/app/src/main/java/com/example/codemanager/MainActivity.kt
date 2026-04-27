package com.example.codemanager

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.codemanager.data.*
import kotlinx.coroutines.launch

class MainActivity:AppCompatActivity(){

    lateinit var dao:CodeDao

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)

        dao=AppDatabase.get(this).dao()

        val layout=LinearLayout(this)
        layout.orientation=LinearLayout.VERTICAL

        val codeInput=EditText(this)
        codeInput.hint="兑换码"

        val catInput=EditText(this)
        catInput.hint="分类"

        val addBtn=Button(this)
        addBtn.text="添加"

        val getBtn=Button(this)
        getBtn.text="取一个(FIFO)"

        val recycleBtn=Button(this)
        recycleBtn.text="查看回收站"

        val importBtn=Button(this)
        importBtn.text="导入TXT"

        val output=TextView(this)

        layout.addView(codeInput)
        layout.addView(catInput)
        layout.addView(addBtn)
        layout.addView(getBtn)
        layout.addView(recycleBtn)
        layout.addView(importBtn)
        layout.addView(output)

        setContentView(layout)

        addBtn.setOnClickListener{
            lifecycleScope.launch{
                dao.insert(CodeEntity(content=codeInput.text.toString(),category=catInput.text.toString()))
                runOnUiThread{toast("已添加")}
            }
        }

        getBtn.setOnClickListener{
            lifecycleScope.launch{
                val c=dao.getOldest(catInput.text.toString())
                if(c!=null){
                    dao.markUsed(c.id)
                    runOnUiThread{output.text=c.content}
                }
            }
        }

        recycleBtn.setOnClickListener{
            lifecycleScope.launch{
                dao.getUsed().collect{
                    val txt=it.joinToString("\n"){c->c.content}
                    runOnUiThread{output.text=txt}
                }
            }
        }

        importBtn.setOnClickListener{
            val et=EditText(this)
            AlertDialog.Builder(this)
                .setTitle("粘贴TXT内容")
                .setView(et)
                .setPositiveButton("导入"){_,_->
                    lifecycleScope.launch{
                        et.text.toString().lines().forEach{
                            if(it.isNotBlank())
                                dao.insert(CodeEntity(content=it,category=catInput.text.toString()))
                        }
                        runOnUiThread{toast("导入完成")}
                    }
                }.show()
        }
    }

    fun toast(s:String)=Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
}