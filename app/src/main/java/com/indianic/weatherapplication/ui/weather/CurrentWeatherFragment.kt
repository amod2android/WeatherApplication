package com.indianic.weatherapplication.ui.weather

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
import com.indianic.weatherapplication.*
import com.indianic.weatherapplication.databinding.FragmentCurrentWeatherBinding
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


/**
 * A placeholder fragment containing a simple view.
 */
class CurrentWeatherFragment : Fragment() {
    lateinit var recyclerViewAdapter: RecyclerAdapter
    private lateinit var pageViewModel: PageViewModel
    lateinit var bingding: FragmentCurrentWeatherBinding

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
        bingding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_weather, container, false)
        application = requireActivity().application as MyApplication
        application.gerRetroComponent().inject(this)
        getCurrentWeather()
        getWeatherForCast()
        return bingding.rootView
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): CurrentWeatherFragment {
            return CurrentWeatherFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private fun getCurrentWeather() {
        bingding.progressBar.visibility=View.VISIBLE
        WeatherInfoScreen.lat?.let {
            WeatherInfoScreen.lng?.let { it1 ->
                retroServiceInterface.getCurrentWeather(it, it1,RetroModule.appKey)
                    .enqueue(object : Callback<String> {
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                            if (response.isSuccessful) {
                                bingding.progressBar.visibility=View.VISIBLE
                                val jsonObject=JSONObject(response.body().toString())
                                val mainObject=jsonObject.getJSONObject("main")
                                val weatherArray=jsonObject.getJSONArray("weather")
                                val weatherObject=weatherArray.get(0) as JSONObject
                                val weather=weatherObject.optString("main")
                                val temp=mainObject.optString("temp")
                                val humidity=mainObject.optString("humidity")

                                bingding.textViewTemperature.text=temp.getTempString()
                                bingding.textViewWeatherMain.text=weather
                                bingding.textViewHumidity.text=humidity.getHumidityString()
                                Log.d("4343", response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            bingding.progressBar.visibility=View.VISIBLE
                            Toast.makeText(requireContext(), "Something went wrong...", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
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
                                val jsonObject=JSONObject(response.body().toString())
                                val weatherArray : JSONArray =jsonObject.getJSONArray("list")
                                val list = mutableListOf<RecyclerData>()
                                for (i in 0..4) {
                                    val item = weatherArray.getJSONObject(i)
                                    val dt=item.optLong("dt")
                                    val time=item.optString("dt_txt")
                                    val day=getDateTime(dt)?.getDisplayName(TextStyle.FULL, Locale.getDefault())
                                    val main=item.optJSONObject("main")
                                    val temp=main.optString("temp")
                                    val minTemp=main.optString("temp_min")
                                    val maxTemp=main.optString("temp_max")
                                    val recyclerData= day?.let {
                                        RecyclerData(time,
                                            it,temp,minTemp,maxTemp)
                                    }
                                    if (recyclerData != null) {
                                        list.add(recyclerData)
                                    }


                                    bingding.recyclerForecast.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                                    recyclerViewAdapter = RecyclerAdapter(list)
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