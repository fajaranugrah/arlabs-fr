package com.example.baseproject.util.telpo

import android.content.Context
import com.common.thermalimage.TemperatureBitmapData
import com.common.thermalimage.HotImageCallback
import com.common.thermalimage.TemperatureData
import org.json.JSONObject

class FRThermal(context: Context) {

    companion object {
        var context: Context? = null
        private var instance: FRThermal? = null

        fun getSingleton(): FRThermal {
            if (instance == null) {
                instance = context?.let { FRThermal(it) }
                instance?.initThermal()
            }
            return instance as FRThermal
        }
    }

    private var temperatureUtil: com.common.thermalimage.ThermalImageUtil? = null

    private fun initThermal() {
        temperatureUtil = com.common.thermalimage.ThermalImageUtil(context)
        this.getData(null)
    }

    fun getData(thermalListener: ThermalListener?) {
        if (this.temperatureUtil?.usingModule == null || this.temperatureUtil?.usingModule?.size == 0) {
            thermalListener?.onDone("Start Get Data Error", null, null)
            return
        }
        val distance = 50.0f
        Thread {
            var text = ""
            var temperatureBitmapData: TemperatureBitmapData? = null
            var temperatureData: TemperatureData? = null
            try {
                temperatureData = temperatureUtil?.getDataAndBitmap(distance,
                    true,
                    object : HotImageCallback.Stub() {
                        override fun onTemperatureFail(e: String) {
                            try {
                                val jsonErrorInfo = JSONObject(e)
                                text += "[" + jsonErrorInfo.get("errCode") + "] " +
                                        jsonErrorInfo.get("err")
                            } catch (e: Exception) {
                                text = "ERROR"
                            }
                        }

                        override fun getTemperatureBimapData(data: TemperatureBitmapData) {
                            temperatureBitmapData = data
                        }
                    }
                )
            } catch (e: Exception) {
                text = "ERROR"
            }
            thermalListener?.onDone(text, temperatureBitmapData, temperatureData)
        }.start()
    }

    fun destroy() {
        temperatureUtil?.release()
    }

    interface ThermalListener {

        fun onDone(
            error: String?,
            temperatureBitmapData: TemperatureBitmapData?,
            temperatureData: TemperatureData?
        )
    }
}