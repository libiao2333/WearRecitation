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
import com.libiao.WearRecitation.util.PoetDatabaseHelper
import java.io.Serializable
import org.json.JSONObject

class PoemDetailNew : Fragment() {
    
    companion object {
        private const val ARG_POEM = "poem"
        private const val ARG_POET = "poet"
        
        fun newInstance(poem: Poem, poet: Poet?): PoemDetailNew {
            val fragment = PoemDetailNew()
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
    private var poet: Poet? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poem_detail_new, container, false)
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
        poet = arguments?.getSerializable(ARG_POET) as? Poet
        
        // 如果没有传递诗人信息，则从数据库中查找
        if (poet == null && poem != null) {
            val dbHelper = PoetDatabaseHelper(requireContext())
            poet = dbHelper.findPoetByName(poem.writer)
        }
        
        // 显示诗歌详细信息和作者信息
        if (poem != null) {
            displayPoemDetails(poem, poet)
        }
        
        // 为作者名添加点击事件
        val authorView = view.findViewById<android.widget.TextView>(R.id.tv_poem_author)
        authorView?.setOnClickListener {
            poet?.let { 
                val aboutWriterFragment = AboutWriter.newInstance(it)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, aboutWriterFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
    
    private fun displayPoemDetails(poem: Poem, poet: Poet?) {
        // 获取所有视图组件
        val titleView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_title)
        val authorView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_author)
        val contentView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_content)
        val remarkView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_remark)
        val translationView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_translation)
        val shangxiView = view?.findViewById<android.widget.TextView>(R.id.tv_poem_shangxi)
        
        // 标签视图
        val remarkLabel = view?.findViewById<android.widget.TextView>(R.id.tv_poem_remark_label)
        val translationLabel = view?.findViewById<android.widget.TextView>(R.id.tv_poem_translation_label)
        val shangxiLabel = view?.findViewById<android.widget.TextView>(R.id.tv_poem_shangxi_label)
        
        // 设置标题和作者信息
        titleView?.text = poem.title
        authorView?.text = "${poem.dynasty}·${poem.writer}"
        contentView?.text = poem.content.replace("。", "。\n")
        
        // 设置注释部分
        if (poem.remark != null && poem.remark.isNotEmpty()) {
            remarkView?.text = poem.remark
            remarkView?.visibility = android.view.View.VISIBLE
            remarkLabel?.visibility = android.view.View.VISIBLE
        } else {
            remarkView?.visibility = android.view.View.GONE
            remarkLabel?.visibility = android.view.View.GONE
        }
        
        // 设置译文部分
        if (poem.translation != null && poem.translation.isNotEmpty()) {
            translationView?.text = poem.translation
            translationView?.visibility = android.view.View.VISIBLE
            translationLabel?.visibility = android.view.View.VISIBLE
        } else {
            translationView?.visibility = android.view.View.GONE
            translationLabel?.visibility = android.view.View.GONE
        }
        
        // 设置赏析部分
        if (poem.shangxi != null && poem.shangxi.isNotEmpty()) {
            shangxiView?.text = poem.shangxi
            shangxiView?.visibility = android.view.View.VISIBLE
            shangxiLabel?.visibility = android.view.View.VISIBLE
        } else {
            shangxiView?.visibility = android.view.View.GONE
            shangxiLabel?.visibility = android.view.View.GONE
        }
    }
}