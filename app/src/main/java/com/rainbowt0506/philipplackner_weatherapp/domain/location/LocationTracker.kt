package com.rainbowt0506.philipplackner_weatherapp.domain.location

import android.location.Location

/**
 * 提供位置追蹤相關的抽象介面。
 * 實現此介面的類別應該負責以異步的方式取得使用者當前的位置。
 */
interface LocationTracker {
    /**
     * 以非同步方式取得使用者當前的位置。
     *
     * @return 取得當前位置的 [Location] 物件，如果無法取得位置則回傳 null。
     */
    suspend fun getCurrentLocation(): Location?
}