<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Forget password</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="<c:out value="${contextPath}"/>/css/forgetPassword.css" rel="stylesheet" type="text/css"/>
    </head>
    <body class="forget-password">
        <div class="bg-light"><span></span></div>
        <div class="ui-forget-password">
            <div class="forget-password-container">
                <div class="forget-password-header intro-box">
                    <div class="forget-password-icon"><c:out value="${forgetPassword}"/></div>
                </div>
                <div class="forget-password-content">
                    <div class="forget-password-title">
                        <c:if test="${notValidEmail == 'true'}">
                            <div class="email-error"><i class="forget-password-icon-error"></i><c:out value="${emailError}"/></div>
                         </c:if>
                    </div>
                    <div class="center-forget-password-content">
                        <form id="forgetPasswordForm" name="forgetPasswordForm" action="<c:out value="${contextPath}"/>/forgetPassword" method="post">
                            <input  id="emailAccount" name="emailAccount" type="text" placeholder="<c:out value="${emailAccount}"/>" onblur="this.placeholder = <c:out value="${emailAccount}"/>" onfocus="this.placeholder = ''"/>
                            <div id="forgetPasswordFormAction" class="forget-password-button" onclick="submit();">
                                <button class="button" href="#"><c:out value="${send}"/></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
       </div>
    </body>
</html>
