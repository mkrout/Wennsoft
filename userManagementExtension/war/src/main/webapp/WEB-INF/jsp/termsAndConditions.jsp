<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<c:out value="${lang}"/>" lang="<c:out value="${lang}"/>">
    <head>
        <title>Wennsoft Terms And Conditions</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link href="<c:out value="${contextPath}"/>/css/termsAndConditions.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="<c:out value="${contextPath}"/>/javascript/termsAndConditions.js"></script>
    </head>
    <body class="terms-and-conditions">
        <div class="back-light"></div>
        <div class="ui-welcome-box">
            <div class="header">Terms and Conditions Agreement</div>
            <div class="content">
                <c:set var="content" value="${fn:replace(tcNodeContent, '&lt;', '<')}" />
                <c:set var="content" value="${fn:replace(content, '&gt;', '>')}" />
                <p>${content}</p>
            </div>
            <div class="bottom clearfix">
                <form name="termsAndConditionsForm" action="<c:out value="${contextPath}"/>/termsAndConditions" method="post">
                    <div class="pull-right">
                        <button class="btn inactive" disabled="disabled" id="continueButton" onclick="validate();">Continue</button>
                    </div>
                    <div class="pull-left">
                        <label class="ui-check-box"><input type="checkbox" id="agreement" name="checktc" value="false" onclick="toggleState();" class="checkbox"/>
                            <span>I agree with this terms and conditions agreement.</span>
                        </label>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>