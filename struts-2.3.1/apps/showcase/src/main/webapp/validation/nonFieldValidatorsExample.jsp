<%-- 
    nonFieldValidatorsExample.jsp
    
    @author tm_jee
    @version $Date: 2007-07-11 04:48:47 +0900 (水, 11 7 2007) $ $Id: nonFieldValidatorsExample.jsp 555058 2007-07-10 19:48:47Z musachy $
--%>


<%@taglib prefix="s" uri="/struts-tags" %>

<html>
    <head>
        <title>Showcase - Validation - Non Field Validator Example</title>
        <s:url var="siteCss" value="/validation/validationExamplesStyles.css" includeContext="true" />
        <s:head />
        <!-- link rel="stylesheet" type="text/css" href='<s:property value="%{siteCss}" />'-->
    </head>
    <body>
    
       
       <!-- START SNIPPET: nonFieldValidatorsExample -->
        <s:actionerror />
    
        <s:form method="POST" action="submitNonFieldValidatorsExamples" namespace="/validation">
            <s:textfield name="someText" label="Some Text" />
            <s:textfield name="someTextRetype" label="Retype Some Text" />  
            <s:textfield name="someTextRetypeAgain" label="Retype Some Text Again" />
            <s:submit label="Submit" />
        </s:form>
        
        
        <!--  END SNIPPET: nonFieldValidatorsExample -->
        
        
        <s:include value="footer.jsp" />
    </body>
</html>

