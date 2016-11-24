package com.ishow.ischool.business.teachprocess;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.teachprocess.TeachProcess;
import com.ishow.ischool.common.api.StatisticsApi;

import java.util.TreeMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrS on 2016/10/9.
 */

public class TeachModel implements TeachProcessConact.Model {

    @Override
    public Observable<ApiResult<TeachProcess>> getTeachProcessData4Home(TreeMap<String, Integer> map) {

        return ApiFactory.getInstance().getApi(StatisticsApi.class).getTeachProcess4Home(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<TeachProcess>> getTeachProcessData4Self(TreeMap<String, Integer> map) {

        return ApiFactory.getInstance().getApi(StatisticsApi.class).getTeatProcess(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
