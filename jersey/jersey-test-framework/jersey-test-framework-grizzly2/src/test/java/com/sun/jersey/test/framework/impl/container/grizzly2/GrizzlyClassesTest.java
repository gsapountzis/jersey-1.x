package com.sun.jersey.test.framework.impl.container.grizzly2;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly2.GrizzlyTestContainerFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author paulsandoz
 */
public class GrizzlyClassesTest extends JerseyTest {

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyTestContainerFactory();
    }

    @Path("root")
    public static class TestResource {
        @GET
        public String get() {
            return "GET";
        }

        @Path("sub")
        @GET
        public String getSub() {
            return "sub";
        }
    }

    public GrizzlyClassesTest() {
        super(new LowLevelAppDescriptor.Builder(TestResource.class).
                contextPath("context").
                build());        
    }

    @Test
    public void testGet() {
        WebResource r = resource().path("root");

        String s = r.get(String.class);
        Assert.assertEquals("GET", s);
    }

    @Test
    public void testGetSub() {
        WebResource r = resource().path("root/sub");

        String s = r.get(String.class);
        Assert.assertEquals("sub", s);
    }
}