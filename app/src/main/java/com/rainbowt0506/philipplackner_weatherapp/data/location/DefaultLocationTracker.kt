package com.rainbowt0506.philipplackner_weatherapp.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.rainbowt0506.philipplackner_weatherapp.domain.location.LocationTracker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * 此類別提供預設的實作方式來取得使用者當前位置。
 * 使用 Android 系統的 FusedLocationProviderClient 來取得位置，
 * 並檢查是否具備位置相關的權限和 GPS 是否已啟用。
 *
 * @property locationClient Android 提供的融合位置提供器，用來取得當前位置。
 * @property application 應用程式的 Application context，用於檢查權限及系統服務。
 */
@ExperimentalCoroutinesApi
class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
): LocationTracker {

    /**
     * 檢查是否擁有精確與粗略位置權限，以及 GPS 是否啟用，若皆符合條件，
     * 則透過 FusedLocationProviderClient 取得最後已知的位置。
     * 此方法使用 suspendCancellableCoroutine 將 callback 轉換成 suspend function。
     *
     * @return 如果成功取得位置則回傳 [Location]，否則回傳 null。
     */
    override suspend fun getCurrentLocation(): Location? {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // 若沒有權限或 GPS 未啟用則直接返回 null。
        if(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            return null
        }

        // 使用 suspendCancellableCoroutine 將 callback API 轉換為 coroutine。
        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if(isComplete) {
                    if(isSuccessful) {
                        cont.resume(result)
                    } else {
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }

                // 設定成功取得位置時的回調。
                addOnSuccessListener {
                    cont.resume(it)
                }

                // 設定取得位置失敗時的回調。
                addOnFailureListener {
                    cont.resume(null)
                }

                // 設定取得位置操作被取消時的回調。
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}