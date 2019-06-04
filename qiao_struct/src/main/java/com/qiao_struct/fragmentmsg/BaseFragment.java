package com.qiao_struct.fragmentmsg;

import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment{

    protected FrmFunctionManager frmFunctionManager;

    private DemoActivity mDemoActivity;

    public void setFrmFunctionManager (FrmFunctionManager frmFunctionManager) {
        this.frmFunctionManager = frmFunctionManager;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DemoActivity) {
            mDemoActivity = (DemoActivity)context;
            mDemoActivity.setFunctionForFm(getTag());
        }
    }
}
