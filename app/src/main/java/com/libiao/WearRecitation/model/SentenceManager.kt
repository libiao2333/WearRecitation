package com.libiao.WearRecitation.model

import android.content.Context
import org.json.JSONObject
import java.util.Random
import java.io.BufferedReader
import java.io.InputStreamReader

class SentenceManager(private val context: Context) {
    private val sentences = mutableListOf<Sentence>()

    init {
        loadSentences()
    }

    private fun loadSentences() {
        try {
            // 从assets目录读取句子文件
            val inputStream = context.assets.open("Ancientpoetry/sentence/sentence1-10000.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            
            // 逐行读取并解析JSON
            while (reader.readLine().also { line = it } != null) {
                if (!line.isNullOrBlank()) {
                    try {
                        val jsonObject = JSONObject(line)
                        val name = jsonObject.getString("name")
                        val from = jsonObject.getString("from")
                        sentences.add(Sentence(name, from))
                    } catch (e: Exception) {
                        // 忽略解析错误的行
                    }
                }
            }
            
            reader.close()
            inputStream.close()
        } catch (e: Exception) {
            // 如果加载失败，添加默认句子
            sentences.add(Sentence("山有木兮木有枝，心悦君兮君不知。", "佚名《越人歌》"))
        }
    }

    fun getRandomSentence(): Sentence {
        return if (sentences.isNotEmpty()) {
            sentences[Random().nextInt(sentences.size)]
        } else {
            Sentence("山有木兮木有枝，心悦君兮君不知。", "佚名《越人歌》")
        }
    }
}