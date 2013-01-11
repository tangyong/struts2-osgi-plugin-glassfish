struts2-osgi-plugin-glassfish
=============================

Using Struts2 OSGi plugin with GlassFish

[Background]

Please seeing http://stackoverflow.com/questions/14200300/using-struts2-osgi-plugin-with-glassfish

[Enviroment]

1 struts 2.3.1 tag
Note: I will firstly extend 2.3.1 as an experiment, then rebase such extending into struts trunk.
As a convenient, I will upload structs 2.3.1(struts2 OSGi plugin's source is included in struts) into this repo

Noting: I changes xwork-core's com.opensymphony.xwork2.util.DomHelper class because my working env is behind a http proxy,
so, once deploying demo and starting tomcat, tomcat will report “java.net.UnknownHostException: struts.apache.org”. So,
I must disable xerces dtd validation[1].

[1]: http://isocra.com/2006/05/making-xerces-ignore-a-dtd/

2 glassfish v4 trunk

1) trunk url: https://svn.java.net/svn/glassfish~svn/trunk/main

2) building steps: https://wikis.oracle.com/display/GlassFish/FullBuildInstructions

3 Struts2OSGi demo

The demo is from http://code.google.com/p/class-reloading-test/

About detailed info about the demo, please seeing http://classreloadingwebapp.blogspot.gr/2012/04/class-reloading-4-using-struts2-osgi.html
As a convenient, I will also upload the demo into this repo

4 jdk7

Because glassfish v4 trunk's building requires jdk7, I must use jdk7

5 maven 3.0.4

6 (optional)tomcat 7

You can use tomcat 7 to see whether Struts2OSGi demo works.

[Design]

ToDo
