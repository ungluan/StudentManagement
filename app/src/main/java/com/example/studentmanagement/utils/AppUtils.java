package com.example.studentmanagement.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.util.Consumer;
import androidx.databinding.DataBindingUtil;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.DialogAddGradeBinding;

import java.util.function.BiFunction;
import java.util.function.Function;

public class AppUtils {
    public static void showNotificationDialog(
            Context context,
            String title,
            String content
    ) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog_notice);

        TextView txtTitle = dialog.findViewById(R.id.txt_title);
        TextView txtContent = dialog.findViewById(R.id.txt_content);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);

        txtTitle.setText(title);
        txtContent.setText(content);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        dialog.show();

        btnConfirm.setOnClickListener(v -> dialog.dismiss());
    }
    //TODO 1: Add FormatPersonName
    public static String formatPersonName(String name){
        name = name.trim();
        name = name.replaceAll("\\s+"," ");
        String[] s = name.split(" ");
        name = "";
        for (String s1 : s) {
            name += s1.toUpperCase().charAt(0);
            if(s1.length()>1) name += s1.toLowerCase().substring(1);
            name += " ";
        }
        return name.trim();
    }
    //TODO 2: Add FormatGradeName
    public static String formatGradeName(String name){
        return name.trim().toUpperCase();
    }
}
