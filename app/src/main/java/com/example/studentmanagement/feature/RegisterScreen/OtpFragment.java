package com.example.studentmanagement.feature.RegisterScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;


public class OtpFragment extends Fragment {
    private FragmentOtpBinding binding;
    private FirebaseAuth mAuth;
    private String TAG = "OtpFragment";
    private String verificationCode="";
    private RegisterViewModel registerViewModel ;
    private LoginViewModel loginViewModel;
    private ChangePasswordViewModel changePasswordViewModel;
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
        mAuth = FirebaseAuth.getInstance();
        registerViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        String phoneNumber = "+84" + registerViewModel.getPhone().substring(1);
        binding.txtPhoneNumber.setText(phoneNumber);
        mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(10L, TimeUnit.SECONDS) // Timeout and unit
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
                                Log.d("OtpFragment","onCodeSent");
                                verificationCode = s;
                                binding.btnConfirm.setEnabled(true);
                                binding.txtResendOtp.setEnabled(false);
                            }
                            @Override
                            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                                super.onCodeAutoRetrievalTimeOut(s);
                                binding.btnConfirm.setEnabled(false);
                                binding.txtResendOtp.setEnabled(true);
                            }
                        }).build();          // OnVerificationStateChangedCallbacks
        PhoneAuthProvider.verifyPhoneNumber(options);

        binding.btnConfirm.setOnClickListener(v -> {
            if(verificationCode.isEmpty() || !binding.textInputLayoutOTP.isErrorEnabled() ) return;
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, binding.editTextOTP.
                    getText().toString());
            try {
                signInWithPhoneAuthCredential(credential);
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
                if(s.toString().length()!=6) binding.editTextOTP.setError("Mã OTP không hợp lệ.");
                else binding.editTextOTP.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.txtResendOtp.setOnClickListener(v -> {
            PhoneAuthProvider.verifyPhoneNumber(options);
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
}