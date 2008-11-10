/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.jersey.multipart;

import java.util.List;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import junit.framework.TestCase;

/**
 * <p>Test case for {@link BodyPart}.</p>
 */
public class BodyPartTest extends TestCase {
    
    public BodyPartTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}

    public void testCreate() {
        BodyPart bodyPart = new BodyPart();
        assertEquals("text/plain", bodyPart.getMediaType().toString());
        bodyPart.setMediaType(new MediaType("application", "json"));
        assertEquals("application/json", bodyPart.getMediaType().toString());
    }

    public void testEntity() {
        BodyPart bodyPart = new BodyPart();
        bodyPart.setEntity("foo bar baz");
        assertEquals("foo bar baz", bodyPart.getEntity());
    }

    public void testHeaders() {
        BodyPart bodyPart = new BodyPart();
        MultivaluedMap<String,String> headers = bodyPart.getHeaders();
        assertNotNull(headers);
        assertNull(headers.get(HttpHeaders.ACCEPT));
        headers.add(HttpHeaders.ACCEPT, "application/xml");
        assertEquals("application/xml", headers.getFirst(HttpHeaders.ACCEPT));
        headers.add(HttpHeaders.ACCEPT, "application/json");
        assertEquals("application/xml", headers.getFirst(HttpHeaders.ACCEPT));
        List values = headers.get(HttpHeaders.ACCEPT);
        assertTrue(values.contains("application/xml"));
        assertTrue(values.contains("application/json"));
        assertNotNull(headers.get("accept"));
        assertNotNull(headers.get("ACCEPT"));
    }

}
