package com.sun.jersey.server.impl.model.method.dispatch;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.server.impl.inject.InjectableValuesProvider;
import com.sun.jersey.spi.inject.Errors;

import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractJavaMethodParamProvider implements JavaMethodParamProvider {

    protected final Method method;
    protected final List<Parameter> params;
    protected final InjectableValuesProvider ivp;

    protected AbstractJavaMethodParamProvider(AbstractResourceMethod abstractResourceMethod) {
        this.method = abstractResourceMethod.getMethod();
        this.params = abstractResourceMethod.getParameters();
        this.ivp = getInjectableValuesProvider();
    }

    protected abstract InjectableValuesProvider getInjectableValuesProvider();

    @Override
    public boolean isEmpty() {
        return (ivp == null);
    }

    @Override
    public boolean hasMissing() {
        if (ivp.getInjectables().contains(null)) {
            // Missing dependency
            for (int i = 0; i < ivp.getInjectables().size(); i++) {
                if (ivp.getInjectables().get(i) == null) {
                    Errors.missingDependency(method, i);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Object[] getParams(HttpContext context) {
        return ivp.getInjectableValues(context);
    }

}
