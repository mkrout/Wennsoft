<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="interaction-manager">
    <div id="overlay" class="edit-overlay ui-corner-all"></div>
    <form action="<portlet:actionURL/>" method="POST">
        <div class="detail-content">
            <div class="detail-column" style="width: 100%">
                <div class="im-detail-row">
                    <label class="im-detail-row-label" for="connect">Connect</label>
                    <select id="connect" name="connect">
                        <option val="customer-connect">Customer</option>
                    </select>
                </div>
                <div class="im-detail-row">
                    <label class="im-detail-row-label" for="formatNavigation">FormatNavigation</label>
                    <span class="center">
                        <input id="formatNavigation"
                               name="formatNavigation"
                               type="checkbox"
                               <c:if test="${formatNavigation}">checked="checked"</c:if>>
                    </span>
                </div>
                <div class="im-detail-row">
                    <label class="im-detail-row-label" for="formatContainers">FormatContainers</label>
                    <span class="center">
                        <input id="formatContainers"
                               name="formatContainers"
                               type="checkbox"
                               <c:if test="${formatContainers}">checked="checked"</c:if>>
                    </span>
                </div>
                <div class="im-detail-row">
                    <label class="im-detail-row-label" for="userPlatformToolbar">UserPlatformToolbar</label>
                    <span class="center">
                        <input id="userPlatformToolbar"
                               name="userPlatformToolbar"
                               type="checkbox"
                               <c:if test="${userPlatformToolbar}">checked="checked"</c:if>>
                    </span>
                </div>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function()
    {
        defineNavigationEdit("navigationEdit");
        navigationEdit.initialize(
        {
            FormatContainers: "${formatContainers}",
            FormatNavigation: "${formatNavigation}",
            PortalUri: "${portalUri}",
            UserPlatformToolbar: "${userPlatformToolbar}",
            WindowId: "${windowId}",
        });
    });
</script>