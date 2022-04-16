package com.example.studentmanagement.feature.loginScreen;

import static com.example.studentmanagement.utils.AppUtils.isValidEmail;
import static com.example.studentmanagement.utils.AppUtils.showNotificationDialog;
import static com.example.studentmanagement.utils.AppUtils.updateTeacherId;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentmanagement.databinding.FragmentLoginBinding;
import com.example.studentmanagement.utils.AppUtils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import papaya.in.sendmail.SendMail;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppUtils.getTeacherId(requireActivity()) != -1){
            navigateToHomePage();
        }
    }

    private void navigateToHomePage(){
        NavDirections action = LoginFragmentDirections.actionLoginFragmentToHomeFragment();
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginViewModel loginViewModel = 
                new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        if(loginViewModel.getEmail()!=null) binding.editTextEmail.setText(loginViewModel.getEmail());
        if(loginViewModel.getPassword()!=null) binding.editTextPassword.setText(loginViewModel.getPassword());
        binding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidEmail(s)) binding.textInputLayoutEmail.setError("Email không hợp lệ.");
                else binding.textInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<6) binding.textInputLayoutPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                else binding.textInputLayoutPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnLogin.setOnClickListener(v -> {
            String email = String.valueOf(binding.editTextEmail.getText());
            String password = String.valueOf(binding.editTextPassword.getText());
            if(!email.isEmpty() && !password.isEmpty()
                && !binding.textInputLayoutEmail.isErrorEnabled()
                && !binding.textInputLayoutPassword.isErrorEnabled()
            ){
                if(loginViewModel.login(email,password)){
                    Toast.makeText(getContext(), "Login Thành công", Toast.LENGTH_SHORT).show();
                    int teacherId = loginViewModel.getTeacherIdByEmail(email);
                    updateTeacherId(requireActivity(),teacherId);
                    sendEmail();
                    navigateToHomePage();
                }else{
                    showNotificationDialog(requireContext(),"Đăng nhập thất bại",
                            "Tài khoản hoặc mật khẩu không chính xác. Vui lòng kiểm tra lại.",null);
                }
            }else{
                if(email.isEmpty()) binding.textInputLayoutEmail.setError("Email không được trống.");
                if(password.isEmpty()) binding.textInputLayoutPassword.setError("Mật khẩu không được trống.");
            }
        });
        binding.txtRegister.setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
            Navigation.findNavController(v).navigate(action);
        });
    }
    private void sendEmail(){
//        Properties properties = new Properties();
//        properties.put("mail.smtp.auth","true");
//        properties.put("mail.smtp.starttls.enable","true");
//        properties.put("mail.smtp.host","smtp.gmail.com");
//        properties.put("mail.smtp.port","587");
//        String email = "ungluan01@gmail.com ";
//        String password = "testingapp";
//        String content = "Tài khoản của bạn vừa mới đăng nhập trên thiết bị "+getDeviceName();
//        String receiver = "truongluan8102000@gmail.com";
//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(email,password);
//            }
//        });
//        try{
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(email));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
//            message.setSubject("Cảnh báo đăng nhập ứng dụng StudentManagerment.");
//            message.setText(content);
//            Observable.empty().subscribeOn(Schedulers.computation()).doOnComplete(() -> Transport.send(message)).subscribe();
//            Log.d("LoginFragment","Send Email Successful");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        SendMail mail = new SendMail("ungluan01@gmail.com", "testingapp",
                "truongluan8102000@gmail.com",
                "Cảnh báo đăng nhập.",
                "Tài khoản của bạn vừa mới đăng nhập trên thiết bị "+getDeviceName());
        mail.execute();
    }
    public String getLocation(){

        return "";
    }
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}