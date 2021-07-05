package com.example.tailorsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private PieChart pieChart;
    private TextView userName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_home,container,false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
        String Name = sharedPreferences.getString("UserName","");
        userName=root.findViewById(R.id.userNameTV);
        userName.setText(Name);

        pieChart=root.findViewById(R.id.PieChart);
        Description description=new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setUsePercentValues(true);
        List<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(40f,"Completed"));
        values.add(new PieEntry(60f,"Pending"));
        PieDataSet pieDataSet=new PieDataSet(values,"Orders");
        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChart.animateXY(1400,1400);

        return root;


    }
}
