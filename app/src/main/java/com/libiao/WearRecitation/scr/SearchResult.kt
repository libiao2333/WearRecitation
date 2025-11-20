package com.libiao.WearRecitation.scr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.heytap.wearable.support.widget.HeyBackTitleBar
import com.heytap.wearable.support.widget.HeyMultipleDefaultItem
import com.libiao.WearRecitation.R
import com.libiao.WearRecitation.model.Poem
import com.libiao.WearRecitation.model.PoemManager
import com.libiao.WearRecitation.model.Poet
import java.io.Serializable

class SearchResult : Fragment() {
    
    private lateinit var poemManager: PoemManager
    private lateinit var resultContainer: LinearLayout
    private var poems: List<Poem> = emptyList()
    private var poets: List<Poet> = emptyList()
    private lateinit var gestureDetector: GestureDetector
    
    companion object {
        private const val ARG_SEARCH_TYPE = "search_type"
        private const val ARG_SEARCH_KEYWORD = "search_keyword"
        private const val ARG_POEMS = "poems"
        private const val ARG_POETS = "poets"
        
        fun newInstance(searchType: Int, keyword: String): SearchResult {
            val fragment = SearchResult()
            val args = Bundle()
            args.putInt(ARG_SEARCH_TYPE, searchType)
            args.putString(ARG_SEARCH_KEYWORD, keyword)
            fragment.arguments = args
            return fragment
        }
        
        fun newInstanceWithResults(poems: List<Poem>, poets: List<Poet>): SearchResult {
            val fragment = SearchResult()
            val args = Bundle()
            args.putSerializable(ARG_POEMS, poems as Serializable)
            args.putSerializable(ARG_POETS, poets as Serializable)
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
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
        
        val titleBar = view.findViewById<HeyBackTitleBar>(R.id.search_result_titlebar)
        titleBar.setTitle("搜索结果")
        titleBar.setBackListener(null, activity)
        
        resultContainer = view.findViewById(R.id.search_result_container)
        poemManager = PoemManager(requireContext())
        
        // 获取传递的参数
        val searchType = arguments?.getInt(ARG_SEARCH_TYPE) ?: 0
        val keyword = arguments?.getString(ARG_SEARCH_KEYWORD) ?: ""
        
        // 检查是否直接传递了搜索结果
        val poemsObj = arguments?.getSerializable(ARG_POEMS)
        val poetsObj = arguments?.getSerializable(ARG_POETS)
        
        if (poemsObj is List<*> && poetsObj is List<*>) {
            poems = poemsObj as List<Poem>
            poets = poetsObj as List<Poet>
        }
        
        // 如果已经有搜索结果，直接显示
        if (poems.isNotEmpty() || poets.isNotEmpty()) {
            displayResults()
        } else {
            // 根据搜索类型执行不同的搜索逻辑
            when (searchType) {
                0 -> searchByContent(keyword)  // 诗句内容
                1 -> searchByPoet(keyword)     // 诗人
                2 -> searchByTitle(keyword)    // 诗名
            }
        }
    }
    
    private fun searchByContent(keyword: String) {
        val poems = poemManager.searchPoemsByContent(keyword)
        this.poems = poems
        displayResults()
    }
    
    private fun searchByPoet(keyword: String) {
        // 先搜索诗人
        val poets = poemManager.searchPoets(keyword)
        this.poets = poets
        
        if (poets.isNotEmpty()) {
            // 如果找到诗人，则显示该诗人的所有诗歌
            val poet = poets[0] // 取第一个匹配的诗人
            val poems = poemManager.getPoemsByWriter(poet.name)
            this.poems = poems
        } else {
            // 如果没有找到诗人，则在诗歌中搜索
            val poems = poemManager.searchPoemsByPoet(keyword)
            this.poems = poems
        }
        
        displayResults()
    }
    
    private fun searchByTitle(keyword: String) {
        val poems = poemManager.searchPoemsByTitle(keyword)
        this.poems = poems
        displayResults()
    }
    
    private fun displayResults() {
        resultContainer.removeAllViews()
        
        if (poems.isEmpty() && poets.isEmpty()) {
            val noResultText = TextView(requireContext())
            noResultText.text = "未找到相关结果"
            noResultText.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            resultContainer.addView(noResultText)
            return
        }
        
        // 添加提示文本
        val tipText = TextView(requireContext())
        tipText.text = "点击条目查看详情"
        tipText.textSize = 12f
        tipText.gravity = android.view.Gravity.CENTER
        tipText.setTextColor(android.graphics.Color.parseColor("#888888"))
        
        // 使用正确的布局参数
        val tipLayoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tipLayoutParams.setMargins(0, 0, 0, 8)
        tipText.layoutParams = tipLayoutParams
        resultContainer.addView(tipText)
        
        // 显示诗人结果
        for (poet in poets) {
            val item = HeyMultipleDefaultItem(requireContext(), null)
            item.setTitle(poet.name)
            item.setSummary("诗人")
            // 通过XML属性控制图标显示，代码中不设置
            
            // 设置点击事件，跳转到该诗人的诗歌列表
            item.setOnClickListener {
                val poetPoems = poemManager.getPoemsByWriter(poet.name)
                this.poems = poetPoems
                poets = emptyList() // 清除诗人列表，只显示诗歌
                displayResults()
            }
            
            resultContainer.addView(item)
        }
        
        // 显示诗歌结果
        for (poem in poems) {
            val item = HeyMultipleDefaultItem(requireContext(), null)
            // 使用标题作为主文本
            item.setTitle(poem.title)
            // 使用作者和朝代作为摘要文本
            item.setSummary("${poem.dynasty} · ${poem.writer}")
            // 通过XML属性控制图标显示，代码中不设置
            
            // 设置点击事件，跳转到诗歌详情页面
            item.setOnClickListener {
                // 跳转到新的诗歌详情页面
                val poet = poemManager.searchPoets(poem.writer).firstOrNull()
                val poemDetailFragment = PoemDetailNew.newInstance(poem, poet)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, poemDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }
            
            resultContainer.addView(item)
        }
    }
}