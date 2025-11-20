package com.libiao.WearRecitation.model

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class PoemManager(private val context: Context) {
    
    fun searchPoemsByContent(keyword: String): List<Poem> {
        val poems = mutableListOf<Poem>()
        
        try {
            // 遍历所有古文文件
            val guwenFiles = arrayOf(
                "Ancientpoetry/guwen/guwen0-1000.json",
                "Ancientpoetry/guwen/guwen1001-2000.json",
                "Ancientpoetry/guwen/guwen2001-3000.json",
                "Ancientpoetry/guwen/guwen3001-4000.json",
                "Ancientpoetry/guwen/guwen4001-5000.json",
                "Ancientpoetry/guwen/guwen5001-6000.json",
                "Ancientpoetry/guwen/guwen6001-7000.json",
                "Ancientpoetry/guwen/guwen7001-8000.json",
                "Ancientpoetry/guwen/guwen8001-9000.json",
                "Ancientpoetry/guwen/guwen9001-10000.json"
            )
            
            for (fileName in guwenFiles) {
                val poemList = loadPoemsFromFile(fileName)
                poems.addAll(poemList.filter { 
                    it.content.contains(keyword) || 
                    it.title.contains(keyword) || 
                    it.writer.contains(keyword)
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return poems
    }
    
    fun searchPoemsByPoet(keyword: String): List<Poem> {
        val poems = mutableListOf<Poem>()
        
        try {
            // 遍历所有古文文件
            val guwenFiles = arrayOf(
                "Ancientpoetry/guwen/guwen0-1000.json",
                "Ancientpoetry/guwen/guwen1001-2000.json",
                "Ancientpoetry/guwen/guwen2001-3000.json",
                "Ancientpoetry/guwen/guwen3001-4000.json",
                "Ancientpoetry/guwen/guwen4001-5000.json",
                "Ancientpoetry/guwen/guwen5001-6000.json",
                "Ancientpoetry/guwen/guwen6001-7000.json",
                "Ancientpoetry/guwen/guwen7001-8000.json",
                "Ancientpoetry/guwen/guwen8001-9000.json",
                "Ancientpoetry/guwen/guwen9001-10000.json"
            )
            
            for (fileName in guwenFiles) {
                val poemList = loadPoemsFromFile(fileName)
                poems.addAll(poemList.filter { 
                    it.writer.contains(keyword)
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return poems
    }
    
    fun searchPoemsByTitle(keyword: String): List<Poem> {
        val poems = mutableListOf<Poem>()
        
        try {
            // 遍历所有古文文件
            val guwenFiles = arrayOf(
                "Ancientpoetry/guwen/guwen0-1000.json",
                "Ancientpoetry/guwen/guwen1001-2000.json",
                "Ancientpoetry/guwen/guwen2001-3000.json",
                "Ancientpoetry/guwen/guwen3001-4000.json",
                "Ancientpoetry/guwen/guwen4001-5000.json",
                "Ancientpoetry/guwen/guwen5001-6000.json",
                "Ancientpoetry/guwen/guwen6001-7000.json",
                "Ancientpoetry/guwen/guwen7001-8000.json",
                "Ancientpoetry/guwen/guwen8001-9000.json",
                "Ancientpoetry/guwen/guwen9001-10000.json"
            )
            
            for (fileName in guwenFiles) {
                val poemList = loadPoemsFromFile(fileName)
                poems.addAll(poemList.filter { 
                    it.title.contains(keyword)
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return poems
    }
    
    fun searchPoets(keyword: String): List<Poet> {
        val poets = mutableListOf<Poet>()
        
        try {
            // 遍历所有诗人文件
            val writerFiles = arrayOf(
                "Ancientpoetry/writer/writer0-1000.json",
                "Ancientpoetry/writer/writer1001-2000.json",
                "Ancientpoetry/writer/writer2001-3000.json",
                "Ancientpoetry/writer/writer3001-4000.json"
            )
            
            for (fileName in writerFiles) {
                val poetList = loadPoetsFromFile(fileName)
                poets.addAll(poetList.filter { 
                    it.name.contains(keyword)
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return poets
    }
    
    fun getPoemsByWriter(writerName: String): List<Poem> {
        val poems = mutableListOf<Poem>()
        
        try {
            // 遍历所有古文文件
            val guwenFiles = arrayOf(
                "Ancientpoetry/guwen/guwen0-1000.json",
                "Ancientpoetry/guwen/guwen1001-2000.json",
                "Ancientpoetry/guwen/guwen2001-3000.json",
                "Ancientpoetry/guwen/guwen3001-4000.json",
                "Ancientpoetry/guwen/guwen4001-5000.json",
                "Ancientpoetry/guwen/guwen5001-6000.json",
                "Ancientpoetry/guwen/guwen6001-7000.json",
                "Ancientpoetry/guwen/guwen7001-8000.json",
                "Ancientpoetry/guwen/guwen8001-9000.json",
                "Ancientpoetry/guwen/guwen9001-10000.json"
            )
            
            for (fileName in guwenFiles) {
                val poemList = loadPoemsFromFile(fileName)
                poems.addAll(poemList.filter { 
                    it.writer == writerName
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return poems
    }
    
    fun loadPoemsFromFile(fileName: String): List<Poem> {
        val poems = mutableListOf<Poem>()
        
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            
            while (reader.readLine().also { line = it } != null) {
                if (!line.isNullOrBlank()) {
                    try {
                        val jsonObject = JSONObject(line)
                        val id = jsonObject.getJSONObject("_id").getString("\$oid")
                        val title = jsonObject.optString("title", "")
                        val dynasty = jsonObject.optString("dynasty", "")
                        val writer = jsonObject.optString("writer", "")
                        val content = jsonObject.optString("content", "")
                        val remark = jsonObject.optString("remark", null)
                        val translation = jsonObject.optString("translation", null)
                        val shangxi = jsonObject.optString("shangxi", null)
                        val audioUrl = jsonObject.optString("audioUrl", null)
                        
                        poems.add(Poem(id, title, dynasty, writer, content, remark, translation, shangxi, audioUrl))
                    } catch (e: Exception) {
                        // 忽略解析错误的行
                    }
                }
            }
            
            reader.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return poems
    }
    
    fun loadPoetsFromFile(fileName: String): List<Poet> {
        val poets = mutableListOf<Poet>()
        
        try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            
            while (reader.readLine().also { line = it } != null) {
                if (!line.isNullOrBlank()) {
                    try {
                        val jsonObject = JSONObject(line)
                        val id = jsonObject.getJSONObject("_id").getString("\$oid")
                        val name = jsonObject.optString("name", "")
                        val headImageUrl = jsonObject.optString("headImageUrl", null)
                        val simpleIntro = jsonObject.optString("simpleIntro", null)
                        val detailIntro = jsonObject.optString("detailIntro", null)
                        
                        poets.add(Poet(id, name, headImageUrl, simpleIntro, detailIntro))
                    } catch (e: Exception) {
                        // 忽略解析错误的行
                    }
                }
            }
            
            reader.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return poets
    }
}