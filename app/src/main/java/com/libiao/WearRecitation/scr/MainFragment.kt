package com.libiao.WearRecitation.scr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.heytap.wearable.support.widget.HeyMainTitleBar
import com.heytap.wearable.support.widget.HeyMultipleDefaultItem
import com.libiao.WearRecitation.MainActivity
import com.libiao.WearRecitation.R

class MainFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val titleBar = view.findViewById<HeyMainTitleBar>(R.id.main_title_bar)
        titleBar.setTitle("腕上研诗")
        
        val searchPoemItem = view.findViewById<HeyMultipleDefaultItem>(R.id.search_poem_item)
        val searchEnglishItem = view.findViewById<HeyMultipleDefaultItem>(R.id.search_english_item)
        val recitePoemItem = view.findViewById<HeyMultipleDefaultItem>(R.id.recite_poem_item)
        val englishLearningItem = view.findViewById<HeyMultipleDefaultItem>(R.id.english_learning_item)
        
        searchPoemItem.setTitle("搜索古诗")
        searchPoemItem.setSummary("点击搜索古诗")
        searchPoemItem.setOnClickListener {
            (activity as? MainActivity)?.navigateToSearchPoem()
        }
        
        // 移除了英语相关功能
        searchEnglishItem.visibility = View.GONE
        
        recitePoemItem.setTitle("背诵古诗")
        recitePoemItem.setSummary("点击进入背诵")
        recitePoemItem.setOnClickListener {
            // TODO: 实现背诵古诗功能
        }
        
        // 移除了英语相关功能
        englishLearningItem.visibility = View.GONE
    }
}