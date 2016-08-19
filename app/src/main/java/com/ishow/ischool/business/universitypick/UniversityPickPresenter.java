package com.ishow.ischool.business.universitypick;

import com.ishow.ischool.bean.university.SearchUniversityResult;
import com.ishow.ischool.bean.university.UniversityInfoListResult;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by wqf on 16/8/16.
 */
public class UniversityPickPresenter extends UniversityPickContract.Presenter {

    @Override
    public void getListUniversity(String cityName) {
        mModel.getListUniversity(cityName)
                .subscribe(new ApiObserver<UniversityInfoListResult>() {
                    @Override
                    public void onSuccess(UniversityInfoListResult universityInfos) {
                        mView.getListSuccess(universityInfos.lists);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getListFail(msg);
                    }
                });
    }

    @Override
    public void searchUniversity(String universityName) {
        mModel.searchUniversity(universityName)
                .subscribe(new ApiObserver<SearchUniversityResult>() {
                    @Override
                    public void onSuccess(SearchUniversityResult searchUniversityResult) {
                        mView.searchSuccess(searchUniversityResult.list);
                    }

                    @Override
                    public void onError(String msg) {
                    }
                });
    }
}