package com.libiao.WearRecitation.scr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.heytap.wearable.support.widget.HeyBackTitleBar
import com.heytap.wearable.support.widget.HeyShapeButton
import com.libiao.WearRecitation.R

class SearchPoem : Fragment() {
    
    private var searchType = 0 // 0: 诗句内容, 1: 诗人, 2: 诗名
    private lateinit var gestureDetector: GestureDetector
    
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
                    // 不在首页，右滑返回上一页
                    parentFragmentManager.popBackStack()
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
            parentFragmentManager.popBackStack()
        }, activity)
        
        val searchEditText = view.findViewById<EditText>(R.id.poem_search_edit_text)
        searchEditText.hint = "输入关键字"
        
        val searchButton = view.findViewById<HeyShapeButton>(R.id.poem_search_button)
        
        // 设置搜索按钮点击事件
        searchButton.setOnClickListener {
            val keyword = searchEditText.text.toString().trim()
            if (keyword.isNotEmpty()) {
                // 直接跳转到搜索结果页面
                val searchResultFragment = SearchResult.newInstance(0, keyword) // 默认按诗句内容搜索
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, searchResultFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}