struts2-osgi-plugin-glassfish
=============================

Using Struts2 OSGi plugin with GlassFish

Background
------------

Please seeing http://stackoverflow.com/questions/14200300/using-struts2-osgi-plugin-with-glassfish

Enviroment
------------

* **Struts 2.3.1 Tag**

Note: I will firstly extend 2.3.1 as an experiment, then rebase such extending into struts trunk.
As a convenient, I will upload structs 2.3.1(struts2 OSGi plugin's source is included in struts) into this repo

Noting: I changes xwork-core's com.opensymphony.xwork2.util.DomHelper class because my working env is behind a http proxy,
so, once deploying demo and starting tomcat, tomcat will report “java.net.UnknownHostException: struts.apache.org”. So,
I must disable xerces dtd validation[1].

[1]: http://isocra.com/2006/05/making-xerces-ignore-a-dtd/

* **Glassfish v4 Trunk**

1) trunk url: https://svn.java.net/svn/glassfish~svn/trunk/main

2) building steps: https://wikis.oracle.com/display/GlassFish/FullBuildInstructions

* **Struts2OSGi Demo**

The demo is from http://code.google.com/p/class-reloading-test/

About detailed info about the demo, please seeing http://classreloadingwebapp.blogspot.gr/2012/04/class-reloading-4-using-struts2-osgi.html
As a convenient, I will also upload the demo into this repo

* **JDK 7**

Because glassfish v4 trunk's building requires jdk 7, I must use jdk7

* **Maven 3.0.4**


* **(Optional)Tomcat 7**

You can use tomcat 7 to see whether Struts2OSGi demo works.

Using Way
---------------
* **1 Building struts2-osgi-plugin**

cd struts-2.3.1\plugins\osgi

mvn -DskipTests=true clean install


* **2 Glassfish deployment parameter setting**

Please setting the following context-param in demo's web.xml,

param-name : StrutsOSGi_Platform

param-value : Glassfish

Noting: if deploying tomcat, the above setting is not required.

* **3 Demo building with glassfish**

cd truts2-osgi-plugin-glassfish\Struts2OSGi-demo

mvn -P glassfish clean install

* **4 Deploy the demo under glassfish **

asadmin start-domain domain1

asadmin deploy osgi-2.3.1.war


* **5 Access the demo **

http://localhost:8080/osgi-2.3.1/osgi/admin/

