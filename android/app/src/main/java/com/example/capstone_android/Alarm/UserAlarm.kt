package com.example.capstone_android.Alarm

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone_android.Alarma.UserAlarmAdapter
import com.example.capstone_android.MeetingList.ListperiodicAdapter
import com.example.capstone_android.databinding.ActivityAlarmBinding
import kotlinx.android.synthetic.main.fragment_main.view.*

class UserAlarm:AppCompatActivity() {
    private lateinit var viewModel: AlarmViewModel
    private lateinit var AlarmAdapter : UserAlarmAdapter
    private lateinit var binding: ActivityAlarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView: RecyclerView =binding.alarmrecycler
        val toolbar=binding.alarmtoolbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title="내 알림"
        viewModel= ViewModelProvider(this)[AlarmViewModel::class.java]
        recyclerView.layoutManager = LinearLayoutManager(this)
        AlarmAdapter=UserAlarmAdapter(emptyList())
        recyclerView.adapter = AlarmAdapter
        viewModel.AlarmItem.observe(this) { data ->
            AlarmAdapter.subalarm(data)
        }
        viewModel.loadalarm()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                setResult(RESULT_OK)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}