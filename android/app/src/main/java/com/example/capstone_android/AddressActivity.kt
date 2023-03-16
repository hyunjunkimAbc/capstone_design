package com.example.capstone_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone_android.data.AddressData
import com.example.capstone_android.databinding.ActivityAddressBinding
import jxl.Workbook
import jxl.read.biff.BiffException
import java.io.IOException


class AddressActivity:AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            val `is` = baseContext.resources.assets.open("address.xls")
            val wb = Workbook.getWorkbook(`is`)
            if (wb != null) {
                val sheet = wb.getSheet(0) // 시트 불러오기
                if (sheet != null) {
                    val colTotal = sheet.columns // 전체 컬럼
                    val rowIndexStart = 1 // row 인덱스 시작
                    val rowTotal = sheet.getColumn(colTotal - 1).size
                    var sb: StringBuilder
                    for (row in rowIndexStart until rowTotal) {
                        sb = StringBuilder()
                        val item= AddressData()
                        val contentss = sheet.getCell(row, row).contents
                        for (col in 0 until colTotal) {
                            val contents = sheet.getCell(col, row).contents
                            sb.append("col$col : $contents , ")
                        }
                        Log.i("test", sb.toString())
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            println("?wkfaht잘못됫어")
        } catch (e: BiffException) {
            e.printStackTrace()
            println("?wkfaht")
        }
    }
    data class SearchData(var first:String?=null,var second:String?=null,var third:String?=null)

}