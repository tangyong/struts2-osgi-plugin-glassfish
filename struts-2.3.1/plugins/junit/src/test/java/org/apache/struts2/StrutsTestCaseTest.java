/*
 * $Id: StrutsTestCaseTest.java 1157925 2011-08-15 17:48:07Z mcucchiara $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.struts2;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import javax.servlet.ServletException;
import java.io.UnsupportedEncodingException;

public class StrutsTestCaseTest extends StrutsSpringTestCase {
    public void testGetActionMapping() {
        ActionMapping mapping = getActionMapping("/test/testAction.action");
        assertNotNull(mapping);
        assertEquals("/test", mapping.getNamespace());
        assertEquals("testAction", mapping.getName());
    }

    public void testGetActionProxy() throws Exception {
        //set parameters before calling getActionProxy
        request.setParameter("name", "FD");
        
        ActionProxy proxy = getActionProxy("/test/testAction.action");
        assertNotNull(proxy);

        JUnitTestAction action = (JUnitTestAction) proxy.getAction();
        assertNotNull(action);

        String result = proxy.execute();
        assertEquals(Action.SUCCESS, result);
        assertEquals("FD", action.getName());
    }

    public void testExecuteAction() throws ServletException, UnsupportedEncodingException {
        String output = executeAction("/test/testAction.action");
        assertEquals("Hello", output);
    }

    public void testGetValueFromStack() throws ServletException, UnsupportedEncodingException {
        request.setParameter("name", "FD");
        executeAction("/test/testAction.action");
        String name = (String) findValueAfterExecute("name");
        assertEquals("FD", name);
    }
}
