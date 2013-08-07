<%--

    Copyright (C) 2009 eXo Platform SAS.
    
    This is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 2.1 of
    the License, or (at your option) any later version.
    
    This software is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
    Lesser General Public License for more details.
    
    You should have received a copy of the GNU Lesser General Public
    License along with this software; if not, write to the Free
    Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
    02110-1301 USA, or see the FSF site: http://www.fsf.org.

--%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="org.exoplatform.container.PortalContainer" %>
<%@ page import="org.exoplatform.services.resources.ResourceBundleService" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.gatein.common.text.EntityEncoder" %>
<%@ page language="java" %>
<%
    String contextPath = request.getContextPath();
    String lang = request.getLocale().getLanguage();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<%=lang%>" lang="<%=lang%>">
	<head>
		<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
		<title>Wennsoft Subscription Agreement</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

		<link href="<%=request.getContextPath()%>/css/Style.css" rel="stylesheet" type="text/css"/>
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
				<form name="tcForm" action="<%= contextPath + "/wennsoft-terms-and-conditions-action"%>" method="post">
					<div class="pull-right">
						<button class="btn inactive" disabled="disabled" id="continueButton" onclick="validate();">Continue</button>
					</div>
					<div class="pull-left">
						<label class="uiCheckbox"><input type="checkbox" id="agreement" name="checktc" value="false" onclick="toggleState();" class="checkbox"/>
							<span>I agree with this terms and conditions agreement.</span>
						</label>
					</div>
					<script type="text/javascript" src="<%=contextPath%>/javascript/welcomescreens.js"></script>
				</form>
			</div>
		</div>
	</body>
</html>