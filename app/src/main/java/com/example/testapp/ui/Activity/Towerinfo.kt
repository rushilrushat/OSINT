package com.example.testapp.ui.Activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.CellLocation
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.testapp.R
import kotlinx.android.synthetic.main.activity_towerinfo.*

class Towerinfo : AppCompatActivity() {
    var telephonyManager: TelephonyManager? = null
    var lac = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_towerinfo)
        val ty = findViewById<TextView>(R.id.tv)
        val mnc = findViewById<TextView>(R.id.mnc)
        val mcc = findViewById<TextView>(R.id.mcc)
        val lac = findViewById<TextView>(R.id.loc)
        val cid = findViewById<TextView>(R.id.cellid)
        telephonyManager =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
        }
        val gsmCellLocation =
            telephonyManager!!.cellLocation as GsmCellLocation
        val gsmCellLocation2 =
            gsmCellLocation(gsmCellLocation)
        var i = -1
        this.lac = gsmCellLocation2?.lac ?: -1
        if (gsmCellLocation2 != null) {
            i = gsmCellLocation2.cid
        }
        cid.text = "Cell-ID: $i"
        val networkOperator = telephonyManager!!.networkOperator
        mnc.text = "MNC: " + networkOperator.substring(3)
        mcc.text = "MCC: " + networkOperator.substring(0, 3)
        ty.text = telephonyManager!!.networkOperatorName + "," + getNetworkType(telephonyManager)
        lac.text = "Location: " + this.lac
        main_cellid.text = "Main Cell-Id: ${networkOperator.substring(0, 3)}-${networkOperator.substring(3)}-${this.lac}-$i"
    }
    fun getNetworkType(telephonyManager2: TelephonyManager?): String {
        return when (telephonyManager2!!.networkType) {
            1, 2, 4, 7, 11 -> "2G"
            3, 5, 6, 8, 9, 10, 12, 14, 15 -> "3G"
            13 -> "4G"
            else -> "Unknown"
        }
    }

    companion object {
        private fun gsmCellLocation(cellLocation: CellLocation): GsmCellLocation? {
            return try {
                cellLocation as GsmCellLocation
            } catch (unused: ClassCastException) {
                null
            }
        }
    }
}
