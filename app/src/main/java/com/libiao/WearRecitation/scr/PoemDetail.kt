package com.libiao.WearRecitation.scr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.heytap.wearable.support.widget.HeyBackTitleBar
import com.libiao.WearRecitation.R
import com.libiao.WearRecitation.model.Poem
import com.libiao.WearRecitation.model.Poet
import java.io.Serializable

class PoemDetail : Fragment() {
    
    companion object {
        private const val ARG_POEM = "poem"
        private const val ARG_POET = "poet"
        
        fun newInstance(poem: Poem, poet: Poet?): PoemDetail {
            val fragment = PoemDetail()
            val args = Bundle()
            args.putSerializable(ARG_POEM, poem as Serializable)
            if (poet != null) {
                args.putSerializable(ARG_POET, poet as Serializable)
            }
            fragment.arguments = args
            return fragment
        }
    }
    
    private lateinit var gestureDetector: GestureDetector
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poem_detail, container, false)
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
        
        val titleBar = view.findViewById<HeyBackTitleBar>(R.id.poem_detail_titlebar)
        titleBar.setTitle("诗歌详情")
        titleBar.setBackListener(null, activity)
        
        // 获取传递的参数
        val poem = arguments?.getSerializable(ARG_POEM) as? Poem
        val poet = arguments?.getSerializable(ARG_POET) as? Poet
        
        // 显示诗歌详细信息和作者信息
        if (poem != null) {
            displayPoemDetails(poem, poet)
        }
    }
    
    private fun displayPoemDetails(poem: Poem, poet: Poet?) {
        val titleView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_title)
        val authorView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_author)
        val contentView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_content)
        val remarkView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_remark)
        val translationView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_translation)
        val shangxiView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_shangxi)
        
        titleView?.text = poem.title
        authorView?.text = "${poem.dynasty}·${poem.writer}"
        contentView?.text = poem.content.replace("。", "。\n")
        
        if (poem.remark != null) {
            remarkView?.text = poem.remark
        } else {
            remarkView?.visibility = android.view.View.GONE
        }
        
        if (poem.translation != null) {
            translationView?.text = poem.translation
        } else {
            translationView?.visibility = android.view.View.GONE
        }
        
        if (poem.shangxi != null) {
            shangxiView?.text = poem.shangxi
        } else {
            shangxiView?.visibility = android.view.View.GONE
        }
    }
}