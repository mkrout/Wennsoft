
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Login</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" type="image/x-icon"  href="<c:out value='${contextPath}'/>/favicon.ico" />
    <link href="<c:out value='${contextPath}'/>/css/Style_forget.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/platform-extension/javascript/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="<c:out value='${contextPath}'/>/javascript/forget.js"></script>

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
                <c:if test="${wrongOldPassword == 'true'}">
                    <div class="signinFail"><i class="uiIconError"></i><c:out value='${wrongOldPassword}'/></div>
                </c:if>
                <c:if test="${notValidNewPassword == 'true'}">
                    <div class="signinFail"><i class="uiIconError"></i><c:out value='${notValidNewPassword}'/></div>
                </c:if>
            </div>
            <div class="centerLoginContent">
                <form name="forgetForm" action="<c:out value='${contextPath}'/>/wennsoft-change-password-action" method="post" style="margin: 0px;">
                    <input  id="oldPassword" name="oldPassword" type="text" placeholder="<c:out value='${oldPassword}'/>" onblur="this.placeholder = <c:out value='${oldPassword}'/>" onfocus="this.placeholder = ''"/>
                    <input  id="newPassword" name="newPassword" type="text" placeholder="<c:out value='${newPassword}'/>" onblur="this.placeholder = <c:out value='${newPassword}'/>" onfocus="this.placeholder = ''"/>
                    <input  id="reNewPassword" name="reNewPassword" type="text" placeholder="<c:out value='${reNewPassword}'/>" onblur="this.placeholder = <c:out value='${reNewPassword}'/>" onfocus="this.placeholder = ''"/>
                    <div id="UIPortalLoginFormAction" class="loginButton" onclick="forget();">
                        <button class="button" href="#"><c:out value='${send}'/></button>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
