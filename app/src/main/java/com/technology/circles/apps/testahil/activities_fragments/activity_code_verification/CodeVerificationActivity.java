package com.technology.circles.apps.testahil.activities_fragments.activity_code_verification;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.ActivityCodeVerificationBinding;
import com.technology.circles.apps.testahil.language.LanguageHelper;
import com.technology.circles.apps.testahil.models.UserModel;
import com.technology.circles.apps.testahil.preferences.Preferences;
import com.technology.circles.apps.testahil.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

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
        initView();
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
        /*ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        try {

            Api.getService(Tags.base_url)
                    .confirmCode(userModel.getId(),code)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                preferences.create_update_userData(activity,response.body());
                                preferences.createSession(activity, Tags.session_login);

                                if (!activity.isOut)
                                {
                                    Intent intent = new Intent(activity, HomeActivity.class);
                                    startActivity(intent);
                                }

                                activity.finish();

                            }else
                            {

                                if (response.code() == 422) {
                                    Toast.makeText(activity,"Error Validation", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401)
                                {
                                    Toast.makeText(activity, R.string.inc_code, Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
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
        }*/
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
        /*final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(Tags.base_url)
                    .resendCode(userModel.getId())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {

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
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }else if (response.code()==500)
                                {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
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
        }*/

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
