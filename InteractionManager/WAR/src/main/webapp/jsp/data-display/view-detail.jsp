<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="interaction-manager interaction-manager-view">
    <div id="overlay" class="ui-corner-all view-overlay"></div>
    <form action="<portlet:actionURL/>" method="POST">
        <div class="fg-toolbar ui-corner-tl ui-corner-tr ui-helper-clearfix ui-toolbar ui-widget-header">
            <div class="im-view-title"></div>
        </div>
        <div class="detail-content ui-widget-content" id="detailContent">
            <c:if test="${not empty fieldsMetadata}">
                <c:set value="${0}" var="startIndex"/>
                <c:forEach begin="${1}" end="${columns}" var="columnIndex">
                    <div class="detail-column" id="column${columnIndex}" style="width: <c:out value='${100 / columns}'/>%;">
                        <c:set value="${(fn:length(fieldsMetadata) + columns - columnIndex) / columns}" var="columnSize"/>
                        <c:forEach begin="${startIndex}" end="${startIndex + columnSize - 1}" items="${fieldsMetadata}" var="fieldMetadata">
                            <div class="im-detail-row ui-corner-all ui-widget"
                                  data-metadata-basetype="<c:out value="${fieldMetadata['BaseType']}"/>"
                                  data-metadata-controller="<c:out value="${fieldMetadata['Controller']}"/>"
                                  data-metadata-entity="<c:out value="${fieldMetadata['LookupFor']}"/>"
                                  data-metadata-name="<c:out value="${fieldMetadata['Name']}"/>"
                                  data-metadata-types="<c:out value="${fieldMetadata['Types']}"/>"
                                  <c:forEach var="availableDisplay" items="${availableDisplays}">
                                      <c:if test="${availableDisplay.visible && not empty fieldMetadata[availableDisplay.name]}">
                                          data-setting-<c:out value="${fn:toLowerCase(availableDisplay.name)}"/>="<c:out value='${fieldMetadata[availableDisplay.name]}'/>"
                                      </c:if>
                                  </c:forEach>>
                                <label class="im-detail-row-label"
                                       for="<c:out value="${fieldMetadata['Name']}"/>">
                                       <c:out value="${fieldMetadata['Name']}"/>
                                </label>
                            </div>
                        </c:forEach>
                        <c:set value="${startIndex + columnSize}" var="startIndex"/>
                    </div>
                </c:forEach>
            </c:if>
        </div>
        <div class="fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix"></div>
    </form>
</div>

<script type="text/javascript">   
    $(function()
    {
        defineCustomerDataDisplayViewDetail("customerDataDisplayViewDetail");
        customerDataDisplayViewDetail.initialize(
        {
            Controller: "${controller}",
            Entity: "${entity}",
            ErrorDataJson: '${errorDataJson}',
            ErrorMessageJson: '${errorMessageJson}',
            ExoLocale: "${exoLocale}",
            PortalUri: "${portalUri}",
            RequestContextPath: "${requestContextPath}",
            WindowId: "${windowId}",
        });
    });
</script>