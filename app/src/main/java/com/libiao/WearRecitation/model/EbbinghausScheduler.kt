package com.libiao.WearRecitation.model

/**
 * 艾宾浩斯遗忘曲线复习计划
 * 根据艾宾浩斯遗忘曲线理论，建议安排以下时间间隔进行复习
 */
class EbbinghausScheduler {
    
    companion object {
        /**
         * 艾宾浩斯遗忘曲线的复习时间点（分钟）
         * 1. 第1次学习：初次学习内容
         * 2. 第2次复习：20分钟后
         * 3. 第3次复习：1小时后 (60分钟)
         * 4. 第4次复习：8-12小时后 (720分钟)
         * 5. 第5次复习：1天后 (1440分钟)
         * 6. 第6次复习：2天后 (2880分钟)
         * 7. 第7次复习：4天后 (5760分钟)
         * 8. 第8次复习：7天后 (10080分钟)
         * 9. 第9次复习：15天后 (21600分钟)
         * 10. 第10次复习：30天后 (43200分钟)
         */
        val REVIEW_INTERVALS = longArrayOf(0, 20, 60, 720, 1440, 2880, 5760, 10080, 21600, 43200)
        
        /**
         * 获取指定复习次数的时间间隔（分钟）
         * @param reviewCount 复习次数，从0开始计数
         * @return 时间间隔（分钟）
         */
        fun getIntervalForReview(reviewCount: Int): Long {
            if (reviewCount < 0) return 0
            if (reviewCount >= REVIEW_INTERVALS.size) return REVIEW_INTERVALS.last()
            return REVIEW_INTERVALS[reviewCount]
        }
        
        /**
         * 计算下次复习时间
         * @param startTime 学习开始时间（毫秒）
         * @param reviewCount 复习次数，从0开始计数
         * @return 下次复习时间（毫秒）
         */
        fun calculateNextReviewTime(startTime: Long, reviewCount: Int): Long {
            val intervalMinutes = getIntervalForReview(reviewCount)
            return startTime + intervalMinutes * 60 * 1000
        }
    }
}