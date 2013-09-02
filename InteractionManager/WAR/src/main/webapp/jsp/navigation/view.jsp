<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="interaction-manager interaction-manager-view">
    <nav class="im-navigation ui-corner-all ui-helper-clearfix ui-widget-header" id="navigation" style="display: none;">
        <c:if test="${not empty pages && not empty pages['Children']}">
            <ul>
                <c:forEach items="${pages['Children']}" var="page">
                    <li class="im-navigation-top">
                        <a <c:if test="${not empty page['Uri']}">
                               href="<c:out value="${portalUri}${page['Uri']}"/>"
                           </c:if>>
                            <c:out value="${page['Label']}"/>
                        </a>
                        <c:if test="${not empty page['Children']}">
                            <ul>
                                <c:forEach items="${page['Children']}" var="childPage">
                                    <li>
                                        <a href="<c:out value="${portalUri}${childPage['Uri']}"/>">
                                            <c:out value="${childPage['Label']}"/>
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
        <ul class="im-navigation-right">
            <li class="im-navigation-top">
                <a class="im-navigation-user" id="remoteUser"><c:out value="${remoteUser}"/></a>
                <ul>
                    <li>
                        <a class="im-navigation-user" href="javascript:if(document.getElementById('UIMaskWorkspace')) ajaxGet(eXo.env.server.createPortalURL('UIPortal', 'AccountSettings', true));">
                            AccountSettings
                        </a>
                    </li>
                    <li>
                        <a class="im-navigation-user" href="javascript:if(document.getElementById('UIMaskWorkspace')) ajaxGet(eXo.env.server.createPortalURL('UIPortal', 'ChangeLanguage', true));">
                            Language
                        </a>
                    </li>
                    <li>
                        <a class="im-navigation-user" href="javascript:if(document.getElementById('UIMaskWorkspace')) eXo.portal.logout();">
                            SignOut
                        </a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>
</div>

<script type="text/javascript">
    $(function()
    {
        defineNavigationView("navigationView");
        navigationView.initialize(
        {
            Administrator: "${administrator}",
            FormatContainers: "${formatContainers}",
            FormatNavigation: "${formatNavigation}",
            PortalUri: "${portalUri}",
            UserPlatformToolbar: "${userPlatformToolbar}",
            WindowId: "${windowId}",
        });
    });
</script>