package view;

import ognl.OgnlRuntime;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;


/**
 * To allow works Struts 2 with Google App Engine
 */
public class InitListener implements ServletContextListener {

    public InitListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        OgnlRuntime.setSecurityManager(null);
        System.setProperty("java.version", "1.6.0");
        System.out.println("Context initialized"+System.getProperty("java.version"));
    }

    public void contextDestroyed(ServletContextEvent sce) {
    	System.out.println("Context destroyed..");
    }

}
