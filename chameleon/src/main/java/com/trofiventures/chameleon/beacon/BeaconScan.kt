package com.trofiventures.chameleon.beacon

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.util.Log
import com.trofiventures.chameleon.SkinNotifier
import com.trofiventures.chameleon.common.Util
import java.util.*


class BeaconScan(val context: Context,
                 val skinNotifier: SkinNotifier) {

    private var btManager: BluetoothManager? = null
    private var btAdapter: BluetoothAdapter? = null
    private val map = mutableMapOf<String, Region>()
    private val handler: Handler = Handler()

    init {
        btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btManager?.let {
            btAdapter = it.adapter
        }
    }

    fun onResume() {
        btAdapter?.let {
            if (it.isEnabled) {
                val mBuilder = ScanSettings.Builder()
                mBuilder.setReportDelay(0)
                mBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                btAdapter?.bluetoothLeScanner?.startScan(null, mBuilder.build(), scan)
            }
        }
    }

    fun onPause() {
        btAdapter?.bluetoothLeScanner?.stopScan(scan)
    }

    private var scan = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("chameleon", "onBatchScanResults")

        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d("chameleon", "onBatchScanResults")
            result?.let {
                validate(it)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d("chameleon", "onBatchScanResults" + results?.size)
        }
    }

    private fun validate(scanResult: ScanResult) {
        val scanRecord = scanResult.scanRecord.bytes
        var startByte = 2
        var patternFound = false
        while (startByte <= 5) {
            if (scanRecord[startByte + 2].toInt() and 0xff == 0x02 && //Identifies an iBeacon
                    scanRecord[startByte + 3].toInt() and 0xff == 0x15) { //Identifies correct data length
                patternFound = true
                break
            }
            startByte++
        }

        if (patternFound) {

            handler.removeCallbacksAndMessages(null)

            //Convert to hex String
            val uuidBytes = ByteArray(16)
            System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16)
            val hexString = Util.bytesToHex(uuidBytes)

            //UUID detection
            val uuid = hexString.substring(0, 8) + "-" +
                    hexString.substring(8, 12) + "-" +
                    hexString.substring(12, 16) + "-" +
                    hexString.substring(16, 20) + "-" +
                    hexString.substring(20, 32)

            map[uuid] = Region(uuid, scanResult.rssi.toDouble().toInt(), Date().time)

            val regions = map.toList().sortedBy {
                it.second.rssi * -1
            }.filter {
                (Date().time - it.second.lastSeen) < 2000
            }

            skinNotifier.beaconSkin(regions[0].second.uuid)

            handler.postDelayed({
                skinNotifier.defaultSkin()
            }, 10000L)
        }
    }
}