package com.libiao.WearRecitation.scr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.heytap.wearable.support.widget.HeyMainTitleBar
import com.heytap.wearable.support.widget.HeyShapeButton
import com.heytap.wearable.support.widget.HeyMultipleDefaultItem
import com.libiao.WearRecitation.R
import com.libiao.WearRecitation.model.SentenceManager
import com.libiao.WearRecitation.scr.SearchPoem
// Removed EnglishLevelSelect import

class MainScreen : Fragment() {
    
    private lateinit var listItem: HeyMultipleDefaultItem
    private lateinit var sentenceManager: SentenceManager
    private lateinit var gestureDetector: GestureDetector
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 初始化手势检测器
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                // 右滑返回（从左向右滑动）
                if (e1 != null && e2.x - e1.x > 100 && Math.abs(velocityX) > Math.abs(velocityY)) {
                    // 在首页右滑退出应用
                    activity?.finish()
                    return true
                }
                return false
            }
        })
        
        // 为整个视图设置触摸监听器
        view.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
        
        val titleBar = view.findViewById<HeyMainTitleBar>(R.id.default_titlebar_notime)
        titleBar.setTitle("腕上研诗")
        
        val chineseButton = view.findViewById<HeyShapeButton>(R.id.chinese_button)
        // 初始化句子管理器
        sentenceManager = SentenceManager(requireContext())
        
        // Load a random sentence for the list item
        listItem = view.findViewById<HeyMultipleDefaultItem>(R.id.daily_sentence)
        updateRandomSentence()
        
        // 点击卡片刷新一条新的名言
        listItem.setOnClickListener {
            updateRandomSentence()
        }
        
        // 为按钮添加点击事件
        chineseButton.setOnClickListener {
            // 跳转到SearchPoem页面
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchPoem())
                .addToBackStack(null)
                .commit()
        }
        
        // Removed English button functionality
    }
    
    private fun updateRandomSentence() {
        val sentence = sentenceManager.getRandomSentence()
        listItem.setTitle(sentence.name)
        listItem.setSummary(sentence.from)
    }
}