package com.moyao.generator.runtime;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class GenericDaoInterceptor implements Interceptor {

    private final static Class genericClz = GenericDao.class;

    private final static Class invokeClass = GenericDaoInvoke.class;

    private final static Map<String, GenericMethodInfo> methodCache = new ConcurrentHashMap<>();

    private MapperRegistry mapperRegistry;

    private boolean isInit = false;

    public GenericDaoInterceptor(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];

        GenericMethodInfo methodInfo = methodCache.get(ms.getId());
        if (methodInfo != null) {
            methodInfo.invoke(parameter);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (!isInit) {
            methodCacheInit();
        }
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private synchronized void methodCacheInit() {
        if (isInit)  return;

        List<Method> methods = Stream.of(invokeClass.getDeclaredMethods()).filter(m-> Modifier.isPublic(m.getModifiers())).collect(Collectors.toList());
        List<Class> clzList = mapperRegistry.getMappers().stream().filter(genericClz::isAssignableFrom).collect(Collectors.toList());

        for (Class clz : clzList) {
            GenericDaoInvoke invoke = new GenericDaoInvoke(clz);
            for (Method m : methods) {
                String id = clz.getName() + "." + m.getName();
                methodCache.put(id, new GenericMethodInfo(invoke, m));
            }
        }
        isInit = true;
    }
}
