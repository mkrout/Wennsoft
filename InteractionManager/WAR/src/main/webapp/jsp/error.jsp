<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<div class="interaction-manager">
    <div class="fg-toolbar ui-toolbar ui-corner-tl ui-corner-tr ui-helper-clearfix ui-state-error">
        <div class="im-view-title">
            <span class="im-error-icon ui-icon ui-icon-alert"/>
        </div>
    </div>
    <div class="im-error-text ui-widget-content">
        <p>We apologize for any inconvenience, but an unexpected error occurred.</p>
        <p>Detailed information regarding the error has been automatically recorded and the site administrator has been notified.</p>
        <p><c:out value="${error}"/></p>
    </div>
    <div class="fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix ui-state-error"/>
</div>