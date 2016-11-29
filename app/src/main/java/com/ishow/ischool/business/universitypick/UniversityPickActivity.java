package com.ishow.ischool.business.universitypick;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.commonlib.util.PermissionUtil;
import com.commonlib.widget.pull.BaseItemDecor;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.university.UniversityInfo;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.zaaach.citypicker.CityPickerActivity;
import com.zaaach.citypicker.utils.LocManager;
import com.zaaach.citypicker.utils.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wqf on 16/8/16.
 */
public class UniversityPickActivity extends BaseListActivity4Crm<UniversityPickPresenter, UniversityPickModel, UniversityInfo> implements UniversityPickContract.View {

    public static final int REQUEST_CODE_PICK_UNIVERSITY = 2001;
    public static final String KEY_PICKED_UNIVERSITY = "picked_university";
    private String curCity;
    private AMapLocationClient mLocationClient;
    private ArrayList<UniversityInfo> originalDatas = new ArrayList<>();
    private SearchView mSearchView;
    private String mSearchKey;
    private boolean isSearchFlag = false;       // 搜索模式

    @Override
    protected void setUpView() {
        super.setUpView();
        curCity = LocManager.getInstance().getCurCityName();
        mToolbarTitle.setText(curCity);
        mToolbarTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_xiala, 0);
        mToolbarTitle.setCompoundDrawablePadding(20);
        mToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(UniversityPickActivity.this, CityPickerActivity.class), CityPickerActivity.REQUEST_CODE_PICK_CITY);
            }
        });
        setUpMenu(R.menu.menu_search);
        init();
    }

    private void init() {
        if (TextUtils.isEmpty(LocManager.getInstance().getCurCityName())) {
            PermissionUtil.getInstance().checkPermission(UniversityPickActivity.this, new PermissionUtil.PermissionChecker() {
                @Override
                public void onGrant(String grantPermission, int index) {
                    startLocation();
                }

                @Override
                public void onDenied(String deniedPermission, int index) {
                    // showToast();
                }
            }, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().notifyPermissionsChange(this, permissions, grantResults);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new BaseItemDecor(this, 16);
    }

    @Override
    protected boolean setPageEnable() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
            if (!TextUtils.isEmpty(city) && (!city.equals(curCity))) {
                curCity = city;
                mToolbarTitle.setText(curCity);
                recycler.setRefreshing();
            }
        }
    }

    private void startLocation() {
        mLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);       //高精度模式
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        curCity = StringUtils.extractLocation(city, district);
                        mToolbarTitle.setText(curCity);
                        recycler.setRefreshing();
                    } else {
                        //定位失败
                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    @Override
    public void getListSuccess(ArrayList<UniversityInfo> universityInfos) {
        loadSuccess(universityInfos);
        originalDatas.clear();
        originalDatas.addAll(universityInfos);
    }

    @Override
    public void getListFail(String msg) {
        loadFailed();
        showToast(msg);
    }

    @Override
    public void searchSuccess(ArrayList<UniversityInfo> universityInfos) {
        if (mDataList != null) {
            mDataList.clear();
        } else {
            mDataList = new ArrayList<>();
        }
        loadSuccess(universityInfos);
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }

        if (isSearchFlag) {
            mPresenter.searchUniversity(mSearchKey, mCurrentPage++);
        } else {
            mPresenter.getListUniversity(curCity);
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_university, parent, false);
        return new UniversityListViewHolder(view);
    }

    class UniversityListViewHolder extends BaseViewHolder {
        @BindView(R.id.university_name)
        TextView university_name;

        public UniversityListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            UniversityInfo data = mDataList.get(position);
            if (data != null) {
                university_name.setText(data.name);
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            UniversityInfo data = mDataList.get(position);
            Intent intent = new Intent();
            intent.putExtra(KEY_PICKED_UNIVERSITY, data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
