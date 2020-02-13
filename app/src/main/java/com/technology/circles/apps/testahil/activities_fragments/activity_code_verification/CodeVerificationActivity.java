package com.technology.circles.apps.testahil.activities_fragments.activity_code_verification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivityCodeVerificationBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.HomeActivity;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.remote.Api;
import com.technology.circles.apps.testahil.share.Common;
import com.technology.circles.apps.testahil.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeVerificationActivity extends AppCompatActivity {
    private ActivityCodeVerificationBinding binding;

    private boolean canResend = true;
    private CountDownTimer countDownTimer;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_code_verification);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("data"))
        {
            userModel = (UserModel) intent.getSerializableExtra("data");
        }
    }

    private void initView() {

        preferences = Preferences.newInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.btnConfirm.setOnClickListener(v -> checkData());

        binding.btnResend.setOnClickListener(v -> {

            if (canResend)
            {
                reSendSMSCode();
            }
        });
        startCounter();
    }

    private void checkData() {
        String code = binding.edtCode.getText().toString().trim();
        if (!TextUtils.isEmpty(code))
        {
            Common.CloseKeyBoard(this,binding.edtCode);
            ValidateCode(code);
        }else
        {
            binding.edtCode.setError(getString(R.string.field_req));
        }
    }

    private void ValidateCode(String code)
    {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        try {

            Api.getService(Tags.base_url)
                    .checkCode(userModel.getId(),code)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                userModel.setIs_confirmed("1");
                                preferences.create_update_userData(CodeVerificationActivity.this,userModel);
                                preferences.createSession(CodeVerificationActivity.this, Tags.session_login);

                                Intent intent = new Intent(CodeVerificationActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }else
                            {

                                if (response.code() == 422) {
                                    Toast.makeText(CodeVerificationActivity.this,"Error Validation", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(CodeVerificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(CodeVerificationActivity.this, R.string.inc_code, Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(CodeVerificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CodeVerificationActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CodeVerificationActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e)
                            {
                                Log.e("rrr",e.getMessage()+"_");

                            }
                        }
                    });
        }catch (Exception e)
        {
            dialog.dismiss();
            Log.e("dddd",e.getMessage()+"_");
        }
    }

    private void startCounter()
    {
        countDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                canResend = false;

                int AllSeconds = (int) (millisUntilFinished / 1000);
                int seconds= AllSeconds%60;
                binding.btnResend.setText("00:"+seconds);
            }

            @Override
            public void onFinish() {
                canResend = true;
                binding.btnResend.setText(getString(R.string.resend));
            }
        }.start();
    }

    private void reSendSMSCode() {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(Tags.base_url)
                    .reSendCode(userModel.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            dialog.dismiss();

                            if (response.isSuccessful())
                            {
                                startCounter();

                            }else
                            {
                                try {
                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code()==422)
                                {
                                    Toast.makeText(CodeVerificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }else if (response.code()==500)
                                {
                                    Toast.makeText(CodeVerificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    Toast.makeText(CodeVerificationActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(CodeVerificationActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(CodeVerificationActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e)
                            {
                                Log.e("Exe",e.getMessage()+"__");
                                dialog.dismiss();
                            }
                        }
                    });
        }catch (Exception e)
        {
            dialog.dismiss();
            Log.e("ddd",e.getMessage()+"__");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null)
        {
            countDownTimer.cancel();
        }
    }


}
