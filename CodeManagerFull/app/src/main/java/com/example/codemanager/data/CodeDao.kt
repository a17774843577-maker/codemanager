package com.example.codemanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeDao {

    @Insert suspend fun insert(code: CodeEntity)

    @Query("SELECT * FROM codes WHERE category=:cat AND isUsed=0 ORDER BY createdAt ASC LIMIT 1")
    suspend fun getOldest(cat:String): CodeEntity?

    @Query("UPDATE codes SET isUsed=1 WHERE id=:id")
    suspend fun markUsed(id:Int)

    @Query("SELECT * FROM codes WHERE isUsed=1")
    fun getUsed(): Flow<List<CodeEntity>>
}