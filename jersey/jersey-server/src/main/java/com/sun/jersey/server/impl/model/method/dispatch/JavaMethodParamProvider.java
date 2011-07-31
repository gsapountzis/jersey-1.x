package com.sun.jersey.server.impl.model.method.dispatch;

import com.sun.jersey.api.core.HttpContext;

public interface JavaMethodParamProvider {

    boolean isEmpty();

    boolean hasMissing();

    Object[] getParams(HttpContext context);
}
