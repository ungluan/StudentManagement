package com.example.studentmanagement.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;



import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.DialogDeleteBinding;
import com.example.studentmanagement.databinding.DialogErrorBinding;
import com.example.studentmanagement.databinding.DialogErrorBindingImpl;
import com.example.studentmanagement.databinding.DialogNotificationBinding;
import com.example.studentmanagement.databinding.DialogSuccessBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtils {
    static boolean flag=false;
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

    public static boolean showDialogDelete(
            Context context,
            String title,
            String content
    ) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);
        DialogDeleteBinding binding = DialogDeleteBinding.inflate(
                LayoutInflater.from(context)
        );
        TextView txtTitle = binding.txtTitleDialogDelele;
        TextView txtContent = binding.txtContentDialogDelete;
        Button btnConfirm = binding.btnDelDialogDelete;
        Button btnCancel = binding.btnCancelDialogDelete;



        txtTitle.setText(title);
        txtContent.setText(content);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = false;
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                dialog.dismiss();
            }
        });
        dialog.show();
        return flag;
    }

    public  static void showNotiDialog(
            Context context,
            String content
    ) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification);
        DialogNotificationBinding binding = DialogNotificationBinding.inflate(
                LayoutInflater.from(context)
        );
        TextView txtContentNoti = binding.txtContentNoti;
        Button btnAcceptNoti = binding.btnAcceptNoti;
        Button btnCancelNoti = binding.btnCancelNoti;



        txtContentNoti.setText(content);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        btnCancelNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAcceptNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public  static boolean showSuccessDialog(
            Context context,
            String content
    ) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        DialogSuccessBinding binding = DialogSuccessBinding.inflate(
                LayoutInflater.from(context)
        );
        TextView txtContentSuccess = binding.txtContentSuccess;
        Button btnOk = binding.btnOk;



        txtContentSuccess.setText(content);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                flag = true;
            }
        });

        dialog.show();
        return  flag;
    }

    public  static boolean showErrorDialog(
            Context context,
            String title,
            String content
    ) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);
        DialogErrorBinding binding = DialogErrorBinding.inflate(
                LayoutInflater.from(context)
        );
        TextView txtTitleError = binding.txtTitleError;
        TextView txtContentError = binding.txtContentError;
        Button btnTryAgainError = binding.btnTryAgainError;



        txtTitleError.setText(title);
        txtTitleError.setText(content);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        btnTryAgainError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                flag = true;
            }
        });
        dialog.show();
        return flag;
    }
    //TODO 1: Add FormatPersonName
    public static String formatPersonName(String name){
        if(name.length()==0) return "";
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

    @SuppressLint("SimpleDateFormat")
    public static String formatTimeStampToDate(Long timeStamp){
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(timeStamp));
    }
}
