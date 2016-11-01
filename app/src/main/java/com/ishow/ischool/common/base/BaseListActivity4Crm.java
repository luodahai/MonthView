package com.ishow.ischool.common.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.commonlib.core.BaseListActivity;
import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.manager.UserManager;

import butterknife.ButterKnife;

/**
 * Created by wqf on 16/8/13.
 * 此处T类型是列表item的基础数据类型
 */
public abstract class BaseListActivity4Crm<P extends BasePresenter, M extends BaseModel, T> extends BaseListActivity<P, M, T> {

    protected Snackbar snackbar = null;
    protected ProgressDialog dialog = null;
    protected User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //为了Translucent system bar
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
//            View parentView = contentFrameLayout.getChildAt(0);
//            parentView.setFitsSystemWindows(true);
//        }
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mUser = UserManager.getInstance().get();
    }

    @Override
    protected void bindView() {
        super.bindView();
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
        } else Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int stringId) {
        showToast(getString(stringId));
    }

    public void handProgressbar(boolean show) {
        if (show) {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setMessage("Loading...");
            }
            dialog.show();
        } else if (!show && dialog != null) dialog.dismiss();
    }
}
