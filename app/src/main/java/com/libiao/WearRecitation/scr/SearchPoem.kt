package com.libiao.WearRecitation.scr

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.heytap.wearable.support.widget.HeyBackTitleBar
import com.heytap.wearable.support.widget.HeyShapeButton
import com.heytap.wearable.support.widget.HeyMultipleDefaultItem
import com.libiao.WearRecitation.R

class SearchPoem : Fragment() {
    
    private var searchType = 0 // 0: 诗句内容, 1: 诗人, 2: 诗名
    private lateinit var gestureDetector: GestureDetector
    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var searchEditText: EditText
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_poem, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 初始化手势检测器
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                // 右滑返回（从左向右滑动）
                if (e1 != null && e2.x - e1.x > 100 && Math.abs(velocityX) > Math.abs(velocityY)) {
                    // 右滑直接返回首页
                    parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    return true
                }
                return false
            }
        })
        
        // 为整个视图设置触摸监听器
        view.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
        
        val titleBar = view.findViewById<HeyBackTitleBar>(R.id.search_poem_titlebar)
        titleBar.setTitle("搜索古诗")
        titleBar.setBackListener({ 
            parentFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }, activity)
        
        searchEditText = view.findViewById<EditText>(R.id.poem_search_edit_text)
        searchEditText.hint = "输入关键字"
        
        val searchButton = view.findViewById<HeyShapeButton>(R.id.poem_search_button)
        searchHistoryContainer = view.findViewById(R.id.search_history_container)
        
        // 显示搜索历史
        showSearchHistory()
        
        // 设置搜索按钮点击事件
        searchButton.setOnClickListener {
            val keyword = searchEditText.text.toString().trim()
            if (keyword.isNotEmpty()) {
                // 保存搜索历史
                saveSearchHistory(keyword)
                
                // 直接跳转到搜索结果页面
                val searchResultFragment = SearchResult.newInstance(0, keyword) // 默认按诗句内容搜索
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, searchResultFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
    
    private fun saveSearchHistory(keyword: String) {
        val sharedPrefs = requireActivity().getSharedPreferences("search_history", Context.MODE_PRIVATE)
        val historySet = sharedPrefs.getStringSet("history", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        
        // 移除已存在的相同关键词，确保最新搜索在最前面
        historySet.remove(keyword)
        // 添加到集合开头
        historySet.add(keyword)
        
        // 限制历史记录数量为10条
        if (historySet.size > 10) {
            val iterator = historySet.iterator()
            while (historySet.size > 10 && iterator.hasNext()) {
                iterator.next()
                iterator.remove()
            }
        }
        
        sharedPrefs.edit().putStringSet("history", historySet).apply()
    }
    
    private fun showSearchHistory() {
        searchHistoryContainer.removeAllViews()
        
        val sharedPrefs = requireActivity().getSharedPreferences("search_history", Context.MODE_PRIVATE)
        val historySet = sharedPrefs.getStringSet("history", mutableSetOf())?.toList() ?: emptyList()
        
        if (historySet.isNotEmpty()) {
            // 添加历史记录标题
            val title = TextView(requireContext())
            title.text = "搜索历史"
            title.textSize = 14f
            title.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 16, 0, 8)
            title.layoutParams = layoutParams
            searchHistoryContainer.addView(title)
            
            // 添加历史记录项
            for (history in historySet) {
                val item = HeyMultipleDefaultItem(requireContext(), null)
                item.setTitle(history)
                item.setOnClickListener {
                    searchEditText.setText(history)
                    // 触发搜索
                    val searchResultFragment = SearchResult.newInstance(0, history)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, searchResultFragment)
                        .addToBackStack(null)
                        .commit()
                }
                searchHistoryContainer.addView(item)
            }
        }
    }
}