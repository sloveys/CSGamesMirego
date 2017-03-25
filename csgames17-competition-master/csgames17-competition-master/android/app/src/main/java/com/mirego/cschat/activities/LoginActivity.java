package com.mirego.cschat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mirego.cschat.BuildConfig;
import com.mirego.cschat.CSChatApplication;
import com.mirego.cschat.Prefs;
import com.mirego.cschat.R;
import com.mirego.cschat.controller.LoginController;
import com.mirego.cschat.models.User;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_root)
    ViewGroup root;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password) //TODO remove this part
    EditText etPassword;

    @Inject
    LoginController loginController;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ((CSChatApplication) getApplication()).component().inject(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_loading));

        if (BuildConfig.DEBUG) {
            etUsername.setText("horace");
            etPassword.setText("draught146"); //TODO remove this part
        }

    }

    @OnClick(R.id.btn_login_submit)
        //TODO change how this is handled
    void onLoginClicked() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.show();
        loginController.login(etUsername.getText().toString(), etPassword.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(@NonNull User user) throws Exception {
                        progressDialog.dismiss();
                        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                        sharedPreferences.edit().putString(Prefs.KEY_USER_ID, user.getId()).apply();
                        startActivity(new Intent(LoginActivity.this, ConversationsActivity.class));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        progressDialog.dismiss();
                        Snackbar.make(root, R.string.login_error, LENGTH_SHORT).show();
                    }
                });
    }
}
