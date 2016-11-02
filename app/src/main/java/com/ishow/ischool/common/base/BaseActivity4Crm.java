package com.ishow.ischool.common.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.commonlib.core.BaseActivity;
import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.manager.UserManager;

import butterknife.ButterKnife;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by wqf on 16/8/13.
 */
public abstract class BaseActivity4Crm<P extends BasePresenter, M extends BaseModel> extends BaseActivity<P, M> {

    protected Snackbar snackbar = null;
    protected ProgressDialog dialog = null;
    protected User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //为了Translucent system bar
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && SDK_INT < 23) {
//            ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
//            View parentView = contentFrameLayout.getChildAt(0);
//            parentView.setFitsSystemWindows(true);
//        }

//        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//
//        } else if (SDK_INT > Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //  设置根布局的参数
//            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
//            ViewCompat.setFitsSystemWindows(rootView, false);
//            rootView.setClipToPadding(true);
//        }


    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mUser = UserManager.getInstance().get();
    }

    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }

    public void showToast(String s) {

        if (mToolbar != null) {
            snackbar = Snackbar.make(mToolbar, s, Snackbar.LENGTH_LONG);
            snackbar.setAction("朕知道了", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        } else {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(int stringId) {
        showToast(getString(stringId));
    }

    public void handProgressbar(boolean show) {
        if (show) {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setMessage("Loading ...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            }
            dialog.show();
        } else if (!show && dialog != null) {
            dialog.dismiss();
        }
    }

    InputMethodManager manager;

    public void hideSoftPanel(View view) {
        if (manager == null) {
            manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showSoftPanel(View view) {
        if (manager == null) {
            manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        manager.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

}
