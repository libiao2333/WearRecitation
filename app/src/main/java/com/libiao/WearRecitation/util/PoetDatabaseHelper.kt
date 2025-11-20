package com.libiao.WearRecitation.util

import android.content.Context
import com.libiao.WearRecitation.model.Poet
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

class PoetDatabaseHelper(private val context: Context) {
    
    fun findPoetByName(name: String): Poet? {
        // 遍历所有writer文件
        for (i in 0..3) {
            val fileName = when (i) {
                0 -> "writer0-1000.json"
                1 -> "writer1001-2000.json"
                2 -> "writer2001-3000.json"
                3 -> "writer3001-4000.json"
                else -> continue
            }
            
            val poet = searchInFile(fileName, name)
            if (poet != null) {
                return poet
            }
        }
        
        return null
    }
    
    private fun searchInFile(fileName: String, name: String): Poet? {
        return try {
            val inputStream: InputStream = context.assets.open("Ancientpoetry/writer/$fileName")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            
            val jsonString = String(buffer, Charsets.UTF_8)
            val jsonArray = JSONArray(jsonString)
            
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val poetName = jsonObject.getString("name")
                
                if (poetName == name) {
                    val id = jsonObject.getJSONObject("_id").getString("\$oid")
                    val headImageUrl = if (jsonObject.has("headImageUrl")) jsonObject.getString("headImageUrl") else null
                    val simpleIntro = if (jsonObject.has("simpleIntro")) jsonObject.getString("simpleIntro") else null
                    val detailIntro = if (jsonObject.has("detailIntro")) jsonObject.getString("detailIntro") else null
                    
                    return Poet(id, poetName, headImageUrl, simpleIntro, detailIntro)
                }
            }
            
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}