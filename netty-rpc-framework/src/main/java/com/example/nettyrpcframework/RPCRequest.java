package com.example.nettyrpcframework;

public class RPCRequest {

    String unique;
    String interfaceStr;
    String methodStr;
    Object[] param;
    Class[] paramType;

    RPCResponse response;

    public RPCResponse getResponse() {
        return response;
    }

    public void setResponse(RPCResponse response) {
        this.response = response;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public Class[] getParamType() {
        return paramType;
    }

    public void setParamType(Class[] paramType) {
        this.paramType = paramType;
    }

    public String getInterfaceStr() {
        return interfaceStr;
    }

    public void setInterfaceStr(String interfaceStr) {
        this.interfaceStr = interfaceStr;
    }

    public String getMethodStr() {
        return methodStr;
    }

    public void setMethodStr(String methodStr) {
        this.methodStr = methodStr;
    }

    public Object[] getParam() {
        return param;
    }

    public void setParam(Object[] param) {
        this.param = param;
    }
}
