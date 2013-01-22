/*
 * $Id$
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

package org.apache.struts2.osgi;

import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import org.osgi.framework.Constants;
import org.osgi.framework.launch.FrameworkFactory;

import javax.servlet.ServletContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Apache felix implementation of an OsgiHost
 * See http://felix.apache.org/site/apache-felix-framework-launching-and-embedding.html
 * <br/>
 * Servlet config params:
 * <p>struts.osgi.clearBundleCache: Defaults to "true" delete installed bundles when the comntainer starts</p>
 * <p>struts.osgi.logLevel: Defaults to "1". Felix log level. 1 = error, 2 = warning, 3 = information, and 4 = debug </p>
 * <p>struts.osgi.runLevel: Defaults to "3". Run level to start the container.</p>
 */
public class FelixOsgiHost extends BaseOsgiHost {
    private static final Logger LOG = LoggerFactory.getLogger(FelixOsgiHost.class);
    
    Properties configProps = null;
    
	/**
     * Location inside the WAR file where framework jar is located.
     */
    private static final String LIB_DIR = "/WEB-INF/lib/";

    protected void startFelix() {
        //load properties from felix embedded file
        configProps = getProperties("default.properties");

        // Copy framework properties from the system properties.
        //Main.copySystemProperties(configProps);
        replaceSystemPackages(configProps);

        //struts, xwork and felix exported packages
        Properties strutsConfigProps = getProperties("struts-osgi.properties");
        addExportedPackages(strutsConfigProps, configProps);

        //find bundles and adde em to autostart property
        addAutoStartBundles(configProps);

        // Bundle cache
        String storageDir = System.getProperty("java.io.tmpdir") + ".felix-cache";
        configProps.setProperty(Constants.FRAMEWORK_STORAGE, storageDir);
        if (LOG.isDebugEnabled())
            LOG.debug("Storing bundles at [#0]", storageDir);

        String cleanBundleCache = getServletContextParam("struts.osgi.clearBundleCache", "true");
        if ("true".equalsIgnoreCase(cleanBundleCache)) {
            if (LOG.isDebugEnabled())
                LOG.debug("Clearing bundle cache");
            configProps.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
        }

        //other properties
        configProps.put("felix.service.urlhandlers", "false");
        configProps.put("felix.log.level", getServletContextParam("struts.osgi.logLevel", "1"));
        configProps.put(Constants.BUNDLE_CLASSPATH, ".");
        configProps.put(Constants.FRAMEWORK_BEGINNING_STARTLEVEL, getServletContextParam("struts.osgi.runLevel", "3"));

        try {
        	/*  felix 1.x launching mode --->needing upgrading
            List<BundleActivator> list = new ArrayList<BundleActivator>();
            list.add(new AutoActivator(configProps));
            configProps.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, list);
            felix = new Felix(configProps);
            felix.start();
            */
        	
        	//Tang Yong Added -->change into felix 4.x
        	final ClassLoader cl = createFrameworkLoader();
        	FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class, cl).iterator().next();
        	felix = frameworkFactory.newFramework(getConfig());
            felix.init();
            felix.start();
        	
            if (LOG.isTraceEnabled())
                LOG.trace("Apache Felix is running");
        }
        catch (Exception ex) {
            throw new ConfigurationException("Couldn't start Apache Felix", ex);
        }

        addSpringOSGiSupport();

        //add the bundle context to the ServletContext
        servletContext.setAttribute(OSGI_BUNDLE_CONTEXT, felix.getBundleContext());
    }

    @Override
    public void init(ServletContext servletContext) {
        this.servletContext = servletContext;
        startFelix();
    }
    
    private Map<String, String> getConfig() {
        HashMap<String, String> map = new HashMap<String, String>();
        for (Object key : configProps.keySet()) {
            map.put(key.toString(), (String) configProps.get(key));
        }
        return map;
    }
    
    /**
     * @return a class loader capable of finding OSGi frameworks.
     */
    private ClassLoader createFrameworkLoader() {
        // We need to create a URLClassLoader for Felix to be able to attach framework extensions
        List<URL> urls = new ArrayList<URL>();
        Set<String> set = servletContext.getResourcePaths(LIB_DIR);
        for (String s : set) {
            if (s.endsWith(".jar")) {
                try {
                    urls.add(servletContext.getResource(s));
                } catch (MalformedURLException e) {
                	LOG.error("OSGiFramework.createFrameworkLoader got exception while trying to get URL for resource " + s, e);
                }
            }
        }
        
        LOG.debug("OSGiFramework.createFrameworkLoader " + urls);
        ClassLoader cl = new URLClassLoader(urls.toArray(new URL[urls.size()]), Thread.currentThread().getContextClassLoader());
        LOG.debug("Classloader to find & load framework is: " + cl + " whose parent is: " + cl.getParent());
        return cl;
    }
}
