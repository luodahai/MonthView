package com.ishow.ischool.business.tabdata;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.FragmentAdapter;
import com.ishow.ischool.application.Constants;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.ChangeRoleEvent;
import com.ishow.ischool.util.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wqf on 16/9/6.
 */
public class TabDataFragment extends BaseFragment4Crm<TabDataPresenter, TabDataModel> implements TabDataContract.View,
        DataTeachFragment.OnFragmentInteractionListener, DataMarketFragment.OnFragmentInteractionListener {

    //    @BindView(R.id.tabs)
//    TabLayout mTabs;
    @BindView(R.id.title_radio)
    RadioGroup mTitleRadioGroup;
    @BindView(R.id.title_radio_1)
    RadioButton titleRadioButton1;
    @BindView(R.id.title_radio_2)
    RadioButton titleRadioButton2;
    @BindView(R.id.viewpager)
    ViewPager mViewPaper;
    @BindView(R.id.toolbar_title)
    TextView mTitleTv;
    private FragmentAdapter mFragmentAdapter;
    private DataMarketFragment dataMarketFragment;
    private DataTeachFragment dataTeachFragment;
    private User mUser;
    private int type_time = 7;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_data;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RxBus.getDefault().register(ChangeRoleEvent.class, new Action1<ChangeRoleEvent>() {
            @Override
            public void call(ChangeRoleEvent o) {
                init();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unregister(ChangeRoleEvent.class);
    }

    @Override
    public void init() {
        mUser = UserManager.getInstance().get();
        mPresenter.getCampusList();     //进入app获取所有校区信息
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>();

        if (AppUtil.hasSalesPermision()) {
            titleList.add(getString(R.string.data_market));
            dataMarketFragment = DataMarketFragment.newInstance();
            fragments.add(dataMarketFragment);
        }
        if (AppUtil.hasTeachPermision()) {
            titleList.add(getString(R.string.data_teach));
            dataTeachFragment = DataTeachFragment.newInstance();
            fragments.add(dataTeachFragment);
        }
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager(), fragments, titleList);
        mViewPaper.setAdapter(mFragmentAdapter);
        mViewPaper.setCurrentItem(0);
        mFragmentAdapter.notifyDataSetChanged();

        if (titleList.size() > 1) {
            mTitleRadioGroup.setVisibility(View.VISIBLE);
            mTitleTv.setVisibility(View.GONE);
        } else if (AppUtil.hasSalesPermision()) {
            mTitleRadioGroup.setVisibility(View.GONE);
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(titleList.get(0));
        } else if (AppUtil.hasTeachPermision()) {
            mTitleRadioGroup.setVisibility(View.GONE);
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(titleList.get(0));
        }

        mTitleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        mViewPaper.setCurrentItem(i);
                        return;
                    }
                }
            }
        });

        mViewPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleRadioGroup.check(mTitleRadioGroup.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        titleRadioButton1.setChecked(true);

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void getListSuccess(ArrayList<CampusInfo> campusInfos) {
        CampusManager.getInstance().init(getActivity().getApplicationContext());
        CampusManager.getInstance().save(campusInfos);
    }

    @Override
    public void getListFail(String msg) {

    }

}
