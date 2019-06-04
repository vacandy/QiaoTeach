package com.qiao_struct.fragmentmsg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class FragemenOne extends BaseFragment{


    public static final String INSTANCE = FragemenOne.class.getName() + "NPNR";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frmFunctionManager.invoke(INSTANCE);
    }


    //    frmFunctionManager.
}
