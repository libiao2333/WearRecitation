package com.libiao.WearRecitation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.libiao.WearRecitation.scr.MainScreen
import com.libiao.WearRecitation.scr.SearchPoem

class MainActivity : FragmentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        if (savedInstanceState == null) {
            val mainScreen = MainScreen()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mainScreen)
                .commit()
        }
    }
    
    fun navigateToSearchPoem() {
        val searchPoemFragment = SearchPoem()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, searchPoemFragment)
            .addToBackStack(null)
            .commit()
    }
    
    // 移除了英语相关功能
    /*
    fun navigateToSearchEnglish() {
        val searchEnglishFragment = SearchEnglish()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, searchEnglishFragment)
            .addToBackStack(null)
            .commit()
    }
    
    fun navigateToEnglishLevelSelect() {
        val englishLevelSelectFragment = EnglishLevelSelect()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, englishLevelSelectFragment)
            .addToBackStack(null)
            .commit()
    }
    */
    
    // 处理返回键事件
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            // 首页按返回键退出应用
            super.onBackPressed()
        } else {
            // 其他页面按返回键返回上一页
            supportFragmentManager.popBackStack()
        }
    }
}