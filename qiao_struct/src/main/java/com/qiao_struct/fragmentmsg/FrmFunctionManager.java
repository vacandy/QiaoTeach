package com.qiao_struct.fragmentmsg;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class FrmFunctionManager {

    private static FrmFunctionManager instance;
    private Map<String,FrmFunctonResult> mFrmFunctonResultMap;
    private Map<String,FrmFunctionResult2> mFrmFunctionResultMap2;
    private Map<String,FrmFunctionResult3> mFrmFunctionResultMap3;
    private Map<String,FrmFunctionResult4> mFrmFunctionResultMap4;


    public static FrmFunctionManager getInstance () {
        if (null == instance) {
            instance = new FrmFunctionManager ();
        }
        return instance;
    }

    private FrmFunctionManager () {
        mFrmFunctonResultMap = new HashMap<>();
        mFrmFunctionResultMap2 = new HashMap<>();
        mFrmFunctionResultMap3 = new HashMap<>();
        mFrmFunctionResultMap4 = new HashMap<>();
    }


    public FrmFunctionManager addFunction (FrmFunctonResult frmFunctonResult) {
        mFrmFunctonResultMap.put(frmFunctonResult.mFunctionName,frmFunctonResult);
        return this;
    }

    public void invoke (String functionName) {
        if (TextUtils.isEmpty(functionName)) {
            return;
        }
        if (null != mFrmFunctonResultMap) {
            FrmFunctonResult frmFunctonResult = mFrmFunctonResultMap.get(functionName);
            if (null != frmFunctonResult) {
                frmFunctonResult.function();
            }
        }




    }


}
