package com.moyao.generator.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GenericMethodInfo {

    private GenericDaoInvoke invoke;

    private Method method;

    public GenericMethodInfo(GenericDaoInvoke invoke, Method method) {
        this.invoke = invoke;
        this.method = method;
    }

    public void invoke(Object parameter) throws InvocationTargetException, IllegalAccessException {
        Class pType = parameter instanceof BaseDo ? BaseDo.class : parameter.getClass();
        Class<?> parameterType = method.getParameterTypes()[0];
        if (parameterType != pType) {
            return;
        }
        method.invoke(invoke, parameter);
    }
}
