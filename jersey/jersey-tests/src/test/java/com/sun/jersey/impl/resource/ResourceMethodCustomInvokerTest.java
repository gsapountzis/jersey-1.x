/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.jersey.impl.resource;

import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.impl.AbstractResourceTester;
import com.sun.jersey.spi.container.JavaMethodInvoker;
import com.sun.jersey.spi.container.ResourceMethodCustomInvokerDispatchProvider;
import com.sun.jersey.spi.container.ResourceMethodDispatchAdapter;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
import com.sun.jersey.spi.dispatch.RequestDispatcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;


/**
 *
 * @author Jakub.Podlesak@Oracle.Com
 */
public class ResourceMethodCustomInvokerTest extends AbstractResourceTester {
    
    public ResourceMethodCustomInvokerTest(String testName) {
        super(testName);
    }

    public static interface PostConstructListener {
        void postConstruct();

    }

    @Path("/iocfullymanagedresource")
    public static class IoCFullyManagedResource {

        @GET
        public String get() throws Exception {
            throw new Exception("this should never get invoked");
        }
    }

    @Path("/ordinaryresource")
    public static class OrdinaryResource {

        @GET
        public String get() throws Exception {
            return "ordinary";
        }
    }

    public static class MyDispatchProvider implements
            ResourceMethodDispatchProvider {

        @Context ResourceMethodCustomInvokerDispatchProvider dispatchProvider;

        static class MyJavaMethodInvoker implements JavaMethodInvoker {

            @Override
            public Object invoke(Method m, Object o, Object... parameters) throws InvocationTargetException, IllegalAccessException {
                return "Dispatch";
            }
        }

        @Override
        public RequestDispatcher create(AbstractResourceMethod abstractResourceMethod) {
            if (isTheMethodOurs(abstractResourceMethod)) {
                return dispatchProvider.create(abstractResourceMethod, new MyJavaMethodInvoker());
            }
            return null;
        }

        private boolean isTheMethodOurs(AbstractResourceMethod arm) {
            try {
                if (arm.getMethod().equals(IoCFullyManagedResource.class.getDeclaredMethod("get"))) {
                    return true;
                }
            } catch (Exception ex) {
            }
            return false;
        }
    }

     
    public void testDispatch() throws Exception {
        initiateWebApplication(MyDispatchProvider.class,
                IoCFullyManagedResource.class, OrdinaryResource.class);

        String s = resource("/ordinaryresource").get(String.class);
        assertEquals(s, "ordinary");
        //System.out.println(String.format("result: %s", s));

        s = resource("/iocfullymanagedresource").get(String.class);
        assertTrue(s.contains("Dispatch"));

        s = resource("/ordinaryresource").get(String.class);
        assertEquals(s, "ordinary");

        s = resource("/iocfullymanagedresource").get(String.class);
        assertTrue(s.contains("Dispatch"));
    }


    private static class PrivateDispatchProvider implements ResourceMethodDispatchProvider {

        private final ResourceMethodCustomInvokerDispatchProvider ciProvider;

        private final ResourceMethodDispatchProvider provider;

        public PrivateDispatchProvider(
                ResourceMethodDispatchProvider provider,
                ResourceMethodCustomInvokerDispatchProvider ciProvider) {
            this.provider = provider;
            this.ciProvider = ciProvider;
        }

        static class MyJavaMethodInvoker implements JavaMethodInvoker {

            @Override
            public Object invoke(Method m, Object o, Object... parameters) throws InvocationTargetException, IllegalAccessException {
                return "Adapt";
            }
        }

        @Override
        public RequestDispatcher create(AbstractResourceMethod abstractResourceMethod) {

            if (isTheMethodOurs(abstractResourceMethod)) {
                return ciProvider.create(abstractResourceMethod, new MyJavaMethodInvoker());
            }

            return provider.create(abstractResourceMethod);
        }

        private boolean isTheMethodOurs(AbstractResourceMethod arm) {
            try {
                if (arm.getMethod().equals(IoCFullyManagedResource.class.getDeclaredMethod("get"))) {
                    return true;
                }
            } catch (Exception ex) {
            }
            return false;
        }
    }

    public static class MyDispatchAdapter implements ResourceMethodDispatchAdapter {

        @Context ResourceMethodCustomInvokerDispatchProvider ciProvider;

        @Override
        public ResourceMethodDispatchProvider adapt(ResourceMethodDispatchProvider provider) {
            return new PrivateDispatchProvider(provider, ciProvider);
        }

    }

    public void testAdapt() throws Exception {
        initiateWebApplication(MyDispatchAdapter.class,
                IoCFullyManagedResource.class, OrdinaryResource.class);

        String s = resource("/ordinaryresource").get(String.class);
        assertEquals(s, "ordinary");
        //System.out.println(String.format("result: %s", s));

        s = resource("/iocfullymanagedresource").get(String.class);
        assertTrue(s.contains("Adapt"));

        s = resource("/ordinaryresource").get(String.class);
        assertEquals(s, "ordinary");

        s = resource("/iocfullymanagedresource").get(String.class);
        assertTrue(s.contains("Adapt"));
    }

}