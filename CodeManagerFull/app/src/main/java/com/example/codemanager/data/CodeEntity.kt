package com.example.codemanager.data

import androidx.room.*

@Entity(tableName = "codes")
data class CodeEntity(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    val content:String,
    val category:String,
    val createdAt:Long=System.currentTimeMillis(),
    val isUsed:Boolean=false
)