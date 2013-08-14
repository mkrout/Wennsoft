<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Forget password</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="<c:out value='${contextPath}'/>/css/Style_forget.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="loginBGLight"><span></span></div>
        <div class="uiLogin">
            <div class="loginContainer">
                <div class="loginHeader introBox">
                    <div class="userLoginIcon"><c:out value='${forgetPassword}'/></div>
                </div>
                <div class="loginContent">
                    <div class="titleLogin">
                        <c:if test="${notValidEmail == 'true'}">
                            <div class="signinFail"><i class="uiIconError"></i><c:out value='${emailError}'/></div>
                         </c:if>
                    </div>
                    <div class="centerLoginContent">
                        <form name="forgetPasswordForm" action="<c:out value='${contextPath}'/>/forgetPassword" method="post" style="margin: 0px;">
                            <input  id="emailAccount" name="emailAccount" type="text" placeholder="<c:out value='${emailAccount}'/>" onblur="this.placeholder = <c:out value='${emailAccount}'/>" onfocus="this.placeholder = ''"/>
                            <div id="UIPortalLoginFormAction" class="loginButton" onclick="submit();">
                                <button class="button" href="#"><c:out value='${send}'/></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
       </div>
    </body>
</html>
