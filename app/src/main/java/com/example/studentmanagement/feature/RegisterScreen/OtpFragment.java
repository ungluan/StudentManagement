package com.example.studentmanagement.feature.RegisterScreen;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;
import com.example.studentmanagement.databinding.FragmentOtpBinding;
import com.example.studentmanagement.feature.ProfileScreen.ChangePasswordFragmentDirections;
import com.example.studentmanagement.feature.ProfileScreen.ChangePasswordViewModel;
import com.example.studentmanagement.feature.loginScreen.LoginViewModel;
import com.example.studentmanagement.utils.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import in.xiandan.countdowntimer.CountDownTimerSupport;
import in.xiandan.countdowntimer.OnCountDownTimerListener;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class OtpFragment extends Fragment {
    private FragmentOtpBinding binding;
    private FirebaseAuth mAuth;
    private String TAG = "OtpFragment";
    private String verificationCode="";
    private RegisterViewModel registerViewModel ;
    private LoginViewModel loginViewModel;
    private ChangePasswordViewModel changePasswordViewModel;
    private CountDownTimer countDownTimer;
    private CountDownTimerSupport mTimer;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        String phoneNumber = "+84" + registerViewModel.getPhone().substring(1);
        setUpCountDownTimer2();
        sendOtp(phoneNumber,view);

        binding.txtPhoneNumber.setText(phoneNumber);
        binding.btnConfirm.setEnabled(true);
        binding.btnConfirm.setOnClickListener(v -> {
            Log.d("OtpFragment","onClicked");
            if(verificationCode.isEmpty() || !binding.textInputLayoutOTP.isErrorEnabled() ) return;
            try {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, binding.editTextOTP.
                        getText().toString());
                signInWithPhoneAuthCredential(credential);
            Log.d("OtpFragment","onClicked");
            }catch (Exception e){
                Log.d(TAG,e.getMessage());
            }

        });
        binding.editTextOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()!=6) binding.textInputLayoutOTP.setError("Mã OTP không hợp lệ.");
                else binding.textInputLayoutOTP.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.txtResendOtp.setOnClickListener(v -> {
            sendOtp(phoneNumber,view);
        });
        binding.btnBack.setOnClickListener(v -> {
            if(registerViewModel.isRegisterPage()) navigateToRegisterPage(view);
            else navigateToForgotPasswordPage(view);
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(DataBaseHelper.databaseExecutor, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Observable.empty().subscribeOn(AndroidSchedulers.mainThread())
                                    .doOnComplete(() -> {
                                        if(registerViewModel.isRegisterPage()) handleCreateAccount(getView());
                                        else handleForgetPassword(getView());
                                    })
                                    .subscribe();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void handleCreateAccount(View view){
        registerViewModel.insertAccount();
        loginViewModel.saveInformation(registerViewModel.getEmail(), loginViewModel.getPassword());
        String content ="Bạn đã đăng ký thành công. vui lòng đăng nhập ứng dụng để tiếp tục.";
        AppUtils.showNotificationDialog(requireContext(),
                "Thông báo",content,
                () -> {
                    navigateToLoginPage(view);
                    return null;
                });
    }
    private void handleForgetPassword(View view){
        changePasswordViewModel = new ViewModelProvider(requireActivity()).get(ChangePasswordViewModel.class);
        changePasswordViewModel.setIsForgotPassword(true);
        String content = "Xác thực thành công. Vui lòng đổi mật khẩu mới.";
        AppUtils.showNotificationDialog(requireContext(),
                "Thông báo",content, null);
        navigateToChangePasswordPage(view);
    }
    private void navigateToLoginPage(View view){
        NavDirections action = OtpFragmentDirections.actionOtpFragmentToLoginFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToChangePasswordPage(View view){
        NavDirections action = OtpFragmentDirections.actionOtpFragmentToChangePasswordFragment2();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToRegisterPage(View view){
        NavDirections action = OtpFragmentDirections.actionOtpFragmentToRegisterFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToForgotPasswordPage(View view){
        NavDirections action = OtpFragmentDirections.actionOtpFragmentToForgotPasswordFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void setUpCountDownTimer(){
        long duration = TimeUnit.MINUTES.toMillis(1);
        countDownTimer = new CountDownTimer(duration,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                String sDuration = String.format(Locale.ENGLISH,"%02d : %02d",TimeUnit.MILLISECONDS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMillis(millisUntilFinished)));
                binding.txtTime.setText(sDuration);
            }

            @Override
            public void onFinish() {
                binding.txtResendOtp.setTextColor(getResources().getColor(R.color.purple_500));
                binding.txtTime.setEnabled(true);
            }
        };
    }
    private void setUpCountDownTimer2(){
        mTimer = new CountDownTimerSupport(59000, 1000);
        mTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String number = millisUntilFinished/1000L < 10 ? "0"+millisUntilFinished/1000L : String.valueOf(millisUntilFinished/1000L);
                binding.txtTime.setText("00:"+ number);
            }

            @Override
            public void onFinish() {
                binding.txtTime.setText("00:00");
            }

            @Override
            public void onCancel() {

            }
        });
    }
    private void sendOtp(String phoneNumber, View view){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Log.d("OtpFragment","onVerificationCompleted");
                                if(registerViewModel.isRegisterPage()) handleCreateAccount(view);
                                else handleForgetPassword(view);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.d("OtpFragment","onVerificationFailed");
                                binding.textInputLayoutOTP.setError("Mã OTP không trùng khớp.");

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                mTimer.start();
                                Log.d("OtpFragment","onCodeSent");
                                Snackbar.make(view,"Mã Otp đã được gửi về điện thoại của bạn.",
                                        Snackbar.LENGTH_SHORT).show();
                                verificationCode = s;
                                binding.btnConfirm.setEnabled(true);
                                binding.txtResendOtp.setEnabled(false);
                                binding.txtResendOtp.setTextColor(0xff808080);

                            }
                            @Override
                            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                                super.onCodeAutoRetrievalTimeOut(s);
                                binding.btnConfirm.setEnabled(false);
                                binding.txtResendOtp.setEnabled(true);
                                binding.txtResendOtp.setTextColor(getResources().getColor(R.color.purple_500));
                            }
                        }).build();          // OnVerificationStateChangedCallbacks
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
}