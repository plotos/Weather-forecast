package com.example.weatherforecast.Activity;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.db.Weather;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    //常量声明
    private static String SP_NAME="com.example.weatherforecast_preferences";
    private static final String TAG = "HistoryFragment";

    //控件声明
    private ListView lvHistory;
    private TextView tvCityHistory;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SharedPreferences sharedPreferences=getActivity().getSharedPreferences(SP_NAME,MODE_PRIVATE);
        String city1=sharedPreferences.getString("defCity","温州市");
        int historyDayNumber=sharedPreferences.getInt("historyDayNumber",5);
        Log.d(TAG, "onViewCreated: "+city1);
        List<Weather> weatherList= LitePal.where("city = ?",city1).order("id desc").limit(historyDayNumber).find(Weather.class);

        tvCityHistory=getActivity().findViewById(R.id.tvCityHistory);
        tvCityHistory.setText(city1);

        //列表数据
        String[] elementList={"title","content"};
        List<Map<String,Object>> mapList=new ArrayList<>();
        for (int i=0;i<weatherList.size();i++){
            Map<String,Object> map=new HashMap<>();
            map.put(elementList[0],weatherList.get(i).getDate());
            map.put(elementList[1],weatherList.get(i).getHigh()+weatherList.get(i).getLow());
            mapList.add(map);
        }
        //列表数据填充
        SimpleAdapter simpleAdapter=new SimpleAdapter(getContext(),mapList,R.layout.weather_list,elementList,new int[]{R.id.tvTitle,R.id.tvContent});
        lvHistory=getActivity().findViewById(R.id.lvHistory);
        lvHistory.setAdapter(simpleAdapter);

    }

}
