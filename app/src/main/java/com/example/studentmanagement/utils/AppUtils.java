package com.example.studentmanagement.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.FragmentActivity;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.DialogDeleteBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class AppUtils {
    static boolean flag = false;
    static Map<Integer, String> imageString = new HashMap<>();

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
                flag = true;
                dialog.dismiss();
            }
        });
        dialog.show();
        return flag;
    }

    public static void showNotiDialog(
            Context context,
            String content,
            Callable<Void> actionAccept
    ) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification);
        TextView txtContentNoti = dialog.findViewById(R.id.txt_content_noti);
        Button btnAcceptNoti = dialog.findViewById(R.id.btn_accept_noti);
        Button btnCancelNoti = dialog.findViewById(R.id.btn_cancel_noti);


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
                try {
                    actionAccept.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    public static void showSuccessDialog(
            Context context,
            String content
    ) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        TextView txtContentSuccess = dialog.findViewById(R.id.txt_content_success);
        Button btnOk = dialog.findViewById(R.id.btn_ok_success);


        txtContentSuccess.setText(content);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });

        dialog.show();
    }

    public static void showErrorDialog(
            Context context,
            String title,
            String content
    ) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);
        TextView txtTitleError = dialog.findViewById(R.id.txt_title_error);
        TextView txtContentError = dialog.findViewById(R.id.txt_content_error);
        Button btnTryAgainError = dialog.findViewById(R.id.btn_try_again_error);


        txtTitleError.setText(title);
        txtContentError.setText(content);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        btnTryAgainError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //TODO 1: Add FormatPersonName
    public static String formatPersonName(String name) {
        if (name.length() == 0) return "";
        name = name.trim();
        name = name.replaceAll("\\s+", " ");
        String[] s = name.split(" ");
        name = "";
        for (String s1 : s) {
            name += s1.toUpperCase().charAt(0);
            if (s1.length() > 1) name += s1.toLowerCase().substring(1);
            name += " ";
        }
        return name.trim();
    }

    //TODO 2: Add FormatGradeName
    public static String formatGradeName(String name) {
        return name.trim().toUpperCase();
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatTimeStampToDate(Long timeStamp) {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(timeStamp));
    }

    public static void updateAuthentication(Activity activity, boolean value){
        SharedPreferences sharedPref = activity.
                getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Authenticated",value);
        editor.apply();
    }

    public static int getTeacherIdFromDropDown(String value){
        return Integer.parseInt(value.substring(0,value.indexOf(" ")));
    }

    // chosse image

    public static void chooseImage(Context context, ImageView img, int requestCode) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImagePicker(context, img, requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "Permission deny\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private static void openImagePicker(Context context, ImageView img, int requestCode) {

        TedBottomPicker.with((FragmentActivity) context)
                .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected image uri
                        imageString.put(requestCode,uri.toString());
                        System.out.println("uri:" +imageString);

                        try {
                            img.setImageBitmap(
                                    MediaStore.Images.Media.getBitmap(context.getContentResolver()
                                            , Uri.parse(getImageString(requestCode))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public static String getImageString(int requestCode){
        if(imageString.size()!=0){
            if(imageString.get(requestCode)!=null)
            return imageString.get(requestCode);
        }
        return "";

    }

    public static void deleteCode(int requestCode){
        imageString.remove(requestCode);
    }
}
