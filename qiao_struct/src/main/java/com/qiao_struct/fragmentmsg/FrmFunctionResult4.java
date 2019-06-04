package com.qiao_struct.fragmentmsg;

public abstract class FrmFunctionResult4<Param,Result> extends FrmFunction{

    public FrmFunctionResult4(String functionName) {
        super(functionName);
    }

    public abstract Result function (Param param);
}
