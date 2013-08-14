<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<c:out value='${lang}'/>" lang="<c:out value='${lang}'/>">
    <head>
        <title>Wennsoft Terms And Conditions</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	    <link href="<c:out value='${contextPath}'/>/css/Style.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="<c:out value='${contextPath}'/>/javascript/termsAndConditions.js"></script>
    </head>
    <body>
	    <div class="backLight"></div>
        <div class="uiWelcomeBox" id="AccountSetup">
		    <div class="header">Terms and Conditions Agreement</div>
		    <div class="content" id="AccountSetup">
			    <p class="c15 c17"><span class="c0 c4">Wennsoft</span><span class="c0"><br/>
				    1970 S Calhoun Rd.New Berlin, WI 53151</span></p>
			    <p class="c15 c17"><span class="c0 c4">Wennsoft master Subscription Agreement</span></p>
			    <p class="c9"><span class="c0">PLEASE READ THIS MASTER SUBSCRIPTION AGREEMENT BEFORE
				    PURCHASING OR USING THE PRODUCTS OR SERVICES. BY USING OR PURCHASING THE PRODUCTS OR
					SERVICES, CUSTOMER SIGNIFIES ITS ASSENT TO THIS AGREEMENT. IF YOU ARE ACTING ON BEHALF
				    OF AN ENTITY, THEN YOU REPRESENT THAT YOU HAVE THE AUTHORITY TO ENTER INTO THIS
					AGREEMENT ON BEHALF OF THAT ENTITY. IF CUSTOMER DOES NOT ACCEPT THE TERMS OF THIS
				    AGREEMENT, THEN IT MUST NOT PURCHASE OR USE THE PRODUCTS OR SERVICES.</span></p>
			</div>
		    <div class="bottom clearfix">
			    <form name="termsAndConditionscForm" action="<c:out value='${contextPath}'/>/termsAndConditions" method="post">
			        <div class="pull-right">
					    <button class="btn inactive" disabled="disabled" id="continueButton" onclick="validate();">Continue</button>
				    </div>
			        <div class="pull-left">
					    <label class="uiCheckbox"><input type="checkbox" id="agreement" name="checktc" value="false" onclick="toggleState();" class="checkbox"/>
						    <span>I agree with this terms and conditions agreement.</span>
					    </label>
					</div>
			    </form>
		    </div>
	    </div>
    </body>
</html>