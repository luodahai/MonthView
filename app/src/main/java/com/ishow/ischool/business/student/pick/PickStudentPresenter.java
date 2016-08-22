package com.ishow.ischool.business.student.pick;

import com.ishow.ischool.bean.student.StudentList;
import com.ishow.ischool.common.api.ApiObserver;

import java.util.HashMap;

/**
 * Created by abel on 16/8/17.
 */
public class PickStudentPresenter extends PickStudentContract.Presenter {
    @Override
    public void getStudentStatisticsList(int campusId, HashMap<String, String> params, int page) {
        mModel.getStudentStatisticsList(campusId, params, page).subscribe(new ApiObserver<StudentList>() {
            @Override
            public void onSuccess(StudentList studentStatisticsList) {
                mView.getListSuccess(studentStatisticsList);
            }

            @Override
            public void onError(String msg) {
                mView.getListFail(msg);
            }
        });
    }
}
