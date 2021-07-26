package com.amod2android.weatherapplication.ui.weather

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amod2android.weatherapplication.*
import com.amod2android.weatherapplication.adapters.RecyclerAdapterAll
import com.amod2android.weatherapplication.databinding.FragmentForecastBinding
import com.amod2android.weatherapplication.network.RetroModule
import com.amod2android.weatherapplication.network.RetroServiceInterface
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject


class ForecastFragment : Fragment() {
    lateinit var recyclerViewAdapter: RecyclerAdapterAll
    private lateinit var pageViewModel: PageViewModel
    lateinit var bingding: FragmentForecastBinding

    @Inject
    lateinit var retroServiceInterface: RetroServiceInterface
    lateinit var application: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bingding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false)
        application = requireActivity().application as MyApplication
        application.gerRetroComponent().inject(this)
        getWeatherForCast()
        return bingding.root
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): ForecastFragment {
            return ForecastFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }


    private fun getWeatherForCast() {

        WeatherInfoScreen.lat?.let {
            WeatherInfoScreen.lng?.let { it1 ->
                retroServiceInterface.getWeatherForCast(it, it1, RetroModule.appKey)
                    .enqueue(object : Callback<String> {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                            if (response.isSuccessful) {
                                val jsonObject = JSONObject(response.body().toString())
                                val weatherArray: JSONArray = jsonObject.getJSONArray("list")
                                val list = mutableListOf<RecyclerData>()
                                for (i in 0 until weatherArray.length()) {
                                    val item = weatherArray.getJSONObject(i)
                                    val dt = item.optLong("dt")
                                    val time = item.optString("dt_txt")
                                    val day =
                                        getDateTime(dt)?.getDisplayName(TextStyle.FULL, Locale.getDefault())
                                    val main = item.optJSONObject("main")
                                    val temp = main.optString("temp")
                                    val minTemp = main.optString("temp_min")
                                    val maxTemp = main.optString("temp_max")
                                    val recyclerData = day?.let {
                                        RecyclerData(
                                            time,
                                            it, temp, minTemp, maxTemp
                                        )
                                    }
                                    if (recyclerData != null) {
                                        list.add(recyclerData)
                                    }


                                    bingding.recyclerForecast.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    recyclerViewAdapter = RecyclerAdapterAll(list)
                                    bingding.recyclerForecast.adapter = recyclerViewAdapter

                                    // Your code here
                                }
                                Log.d("4343", response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(requireContext(), "Something went wrong...", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateTime(s: Long): DayOfWeek? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(s * 1000)
            val formattedDate = sdf.format(netDate)

            LocalDate.of(
                formattedDate.substringAfterLast("/").toInt(),
                formattedDate.substringAfter("/").take(2).toInt(),
                formattedDate.substringBefore("/").toInt()
            )
                .dayOfWeek
        } catch (e: Exception) {
            e.printStackTrace()
            DayOfWeek.MONDAY
        }
    }
}