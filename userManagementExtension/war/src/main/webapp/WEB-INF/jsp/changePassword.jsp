<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Change password</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="<c:out value='${contextPath}'/>/css/Style_forget.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="loginBGLight"><span></span></div>
        <div class="uiLogin">
            <div class="loginContainer">
                <div class="loginHeader introBox">
                    <div class="userLoginIcon"><c:out value='${changePassword}'/></div>
                </div>
                <div class="loginContent">
                    <div class="titleLogin">
                        <c:if test="${notValidNewPassword == 'true'}">
                            <div class="signinFail"><i class="uiIconError"></i><c:out value='${newPasswordError}'/></div>
                        </c:if>
                    </div>
                    <div class="centerLoginContent">
                        <form name="changePasswordForm" action="<c:out value='${contextPath}'/>/changePassword" method="post" style="margin: 0px;">
                            <input  id="newPassword" name="newPassword" type="password" placeholder="<c:out value='${newPassword}'/>" onblur="this.placeholder = <c:out value='${newPassword}'/>" onfocus="this.placeholder = ''"/>
                            <input  id="reNewPassword" name="reNewPassword" type="password" placeholder="<c:out value='${reNewPassword}'/>" onblur="this.placeholder = <c:out value='${reNewPassword}'/>" onfocus="this.placeholder = ''"/>
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
