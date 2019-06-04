package com.qiao_struct.fragmentmsg;

public abstract class FrmFunctionResult2<Param> extends FrmFunction{

    public FrmFunctionResult2(String functionName) {
        super(functionName);
    }

    public abstract void function (Param param);
}
