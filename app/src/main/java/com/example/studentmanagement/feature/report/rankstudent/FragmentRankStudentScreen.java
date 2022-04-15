package com.example.studentmanagement.feature.report.rankstudent;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database_sqlite.Dao.MarkDao;
import com.example.studentmanagement.databinding.FragmentRankStudentScreenBinding;
import com.example.studentmanagement.feature.MarkScreen.MarkViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;

public class FragmentRankStudentScreen extends Fragment {

    private FragmentRankStudentScreenBinding binding;
    private PieChart piechart;
    private MarkDao markDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRankStudentScreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        markDao = new MarkDao(requireActivity().getApplication());

        piechart = binding.piechartRankStudent;
        createPieChar();
//        setUpPieChart();
//        loadPiechartData();
        setEvents();
    }

    private void setEvents() {
        changeFrament();
    }

    private void loadDataRankStudent(){

    }

    private void setUpPieChart() {
        piechart = binding.piechartRankStudent;
        piechart.setDrawHoleEnabled(true);
        piechart.setUsePercentValues(true);
        piechart.setEntryLabelTextSize(12);
        piechart.setEntryLabelColor(Color.BLACK);
        piechart.setCenterText("Spending by Category");
        piechart.setCenterTextSize(24);
        piechart.getDescription().setEnabled(false);
        Legend le = piechart.getLegend();
        le.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        le.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        le.setOrientation(Legend.LegendOrientation.VERTICAL);
        le.setDrawInside(false);
        le.setEnabled(true);
    }

    private void createPieChar(){

        //set up pie chart
        Legend le = piechart.getLegend();
        le.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        le.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        le.setOrientation(Legend.LegendOrientation.VERTICAL);
        le.setDrawInside(false);
        le.setEnabled(false);



        ArrayList<PieEntry> ranks =new ArrayList<>();
        Map<String, Integer> map = markDao.countRankStudent();
        for(String key: map.keySet()){
            ranks.add(new PieEntry((int)map.get(key), key));
            System.out.println("value:" + map.get(key));
        }

//        ArrayList<PieEntry> visitors = new ArrayList<>();
//        visitors.add(new PieEntry(508, "Excellent"));
//        visitors.add(new PieEntry(600, "Good"));
//        visitors.add(new PieEntry(750, "Average"));
        PieDataSet pieDataSet = new PieDataSet(ranks, "Rank student");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        PieData pieData = new PieData(pieDataSet);
        piechart.setData(pieData);
        piechart.getDescription().setEnabled(false);
        piechart.setCenterText("Rank Student");
        piechart.animateY(1400, Easing.EaseInOutQuad);
    }
    private void loadPiechartData() {


        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(1000, "Excellent"));
        entries.add(new PieEntry(537, "Good"));
        entries.add(new PieEntry(275, "Average"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }
        PieDataSet dataset = new PieDataSet(entries, "Rank Student");
        dataset.setColors(colors);
        PieData data = new PieData(dataset);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        piechart.setData(data);
        piechart.invalidate();

        piechart.animateY(1400, Easing.EaseInOutQuad);
    }

//    private void createBarChart(){
//        BarChart barChart = binding.barchartRankStudent;
//        ArrayList<BarEntry> visitors = new ArrayList<>();
//        visitors.add(new BarEntry(2014, 1000, "Excellent"));
//        visitors.add(new BarEntry(2015, 900, "Good"));
//        visitors.add(new BarEntry(2016, 800, "Average"));
//
//        BarDataSet barDataSet = new BarDataSet(visitors, "Rank Student");
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        barDataSet.setValueTextColor(Color.BLACK);
//        barDataSet.setValueTextSize(16f);
//
//        BarData barData = new BarData(barDataSet);
//
//        barChart.setFitBars(true);
//        barChart.setData(barData);
//        barChart.getDescription().setText("bar char rank student");
//        barChart.animateY(1000);
//
//
//
//    }

    private void changeFrament() {
        binding.btnBackSubjectScreen.setOnClickListener(
                v -> {
                    Navigation.findNavController(v).navigate(R.id.action_fragmentRankStudentScreen_to_fragmentReportScreen);
                }
        );
    }
}
