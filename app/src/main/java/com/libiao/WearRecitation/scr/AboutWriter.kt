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
import com.libiao.WearRecitation.model.Poet

class AboutWriter : Fragment() {
    
    companion object {
        private const val ARG_POET = "poet"
        
        fun newInstance(poet: Poet): AboutWriter {
            val fragment = AboutWriter()
            val args = Bundle()
            args.putSerializable(ARG_POET, poet)
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
        return inflater.inflate(R.layout.fragment_about_writer, container, false)
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
        
        val titleBar = view.findViewById<HeyBackTitleBar>(R.id.writer_detail_titlebar)
        titleBar.setTitle("作者详情")
        titleBar.setBackListener(null, activity)
        
        // 获取传递的参数
        val poet = arguments?.getSerializable(ARG_POET) as? Poet
        
        // 显示作者详细信息
        if (poet != null) {
            displayWriterDetails(poet)
        }
    }
    
    private fun displayWriterDetails(poet: Poet) {
        // 获取所有视图组件
        val nameView = view?.findViewById<android.widget.TextView>(R.id.tv_writer_name)
        val introView = view?.findViewById<android.widget.TextView>(R.id.tv_writer_intro)
        val detailView = view?.findViewById<android.widget.TextView>(R.id.tv_writer_detail)
        
        // 设置作者姓名
        nameView?.text = poet.name
        
        // 设置作者简介
        if (poet.simpleIntro != null && poet.simpleIntro.isNotEmpty()) {
            introView?.text = poet.simpleIntro
            introView?.visibility = android.view.View.VISIBLE
        } else {
            introView?.visibility = android.view.View.GONE
        }
        
        // 设置作者详介
        if (poet.detailIntro != null && poet.detailIntro.isNotEmpty()) {
            // 解析JSON格式的detailIntro
            try {
                val detailJson = org.json.JSONObject(poet.detailIntro)
                val detailText = StringBuilder()
                
                // 遍历所有字段
                val keys = detailJson.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = detailJson.getString(key)
                    detailText.append(key).append("\n").append(value).append("\n\n")
                }
                
                detailView?.text = detailText.toString()
                detailView?.visibility = android.view.View.VISIBLE
            } catch (e: Exception) {
                // JSON解析失败，直接显示原始内容
                detailView?.text = poet.detailIntro
                detailView?.visibility = android.view.View.VISIBLE
            }
        } else {
            detailView?.visibility = android.view.View.GONE
        }
    }
}