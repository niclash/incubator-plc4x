/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
package org.apache.plc4x.java;

import org.apache.plc4x.java.api.authentication.PlcUsernamePasswordAuthentication;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.api.exceptions.PlcException;
import org.apache.plc4x.java.mock.MockConnection;
import org.apache.plc4x.test.FastTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class PlcDriverManagerTest {

    /**
     * Tries to get the mock plc driver which is part of this testsuite.
     */
    @Test
    @Category(FastTests.class)
    public void getExistingDriverTest() throws PlcException {
        MockConnection mockConnection = (MockConnection) new PlcDriverManager().getConnection("mock://some-cool-url");
        assertThat(mockConnection.getAuthentication(), nullValue());
        assertThat(mockConnection.isConnected(), is(true));
        assertThat(mockConnection.isClosed(), is(false));
    }

    /**
     * Tries to get the mock plc driver with authentication which is part of this testsuite.
     */
    @Test
    @Category(FastTests.class)
    public void getExistingDriverWithAuthenticationTest() throws PlcException {
        PlcUsernamePasswordAuthentication authentication =
            new PlcUsernamePasswordAuthentication("user", "pass");
        MockConnection mockConnection = (MockConnection) new PlcDriverManager().getConnection("mock://some-cool-url", authentication);

        assertThat(mockConnection.getAuthentication(), notNullValue());
        assertThat(mockConnection.getAuthentication(), instanceOf(PlcUsernamePasswordAuthentication.class));
        assertThat(mockConnection.isConnected(), is(true));
        assertThat(mockConnection.isClosed(), is(false));
    }

    /**
     * In this test case a driver is requested which is not registered with the {@link PlcDriverManager}.
     */
    @Test(expected = PlcConnectionException.class)
    @Category(FastTests.class)
    public void getNotExistingDriverTest() throws PlcConnectionException {
        new PlcDriverManager().getConnection("non-existing-protocol://some-cool-url");
    }

    /**
     * In this test case a driver is requested which is not registered with the {@link PlcDriverManager}.
     */
    @Test(expected = PlcConnectionException.class)
    @Category(FastTests.class)
    public void getInvalidUriTest() throws PlcConnectionException {
        new PlcDriverManager().getConnection("The quick brown fox jumps over the lazy dog");
    }

    /**
     * In this test the {@link PlcDriverManager} will be configured with a service list that
     * contains multiple implementation instances of the same protocol. This should result in
     * an error.
     */
    @Test(expected = IllegalStateException.class)
    @Category(FastTests.class)
    public void getDuplicateDriver() throws MalformedURLException, PlcConnectionException {
        // Save and replace the context classloader as we need to force the ServiceLoader to
        // use a different service file.
        ClassLoader originalClassloader = Thread.currentThread().getContextClassLoader();
        URL[] urls = new URL[1];
        urls[0] = new File("src/test/resources/test").toURI().toURL();
        ClassLoader fakeClassLoader = new URLClassLoader(urls, originalClassloader);

        // expect exception
        new PlcDriverManager(fakeClassLoader).getConnection("mock://some-cool-url");
    }

}
