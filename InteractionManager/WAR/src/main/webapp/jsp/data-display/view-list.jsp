<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="interaction-manager interaction-manager-view">
    <div id="overlay" class="ui-corner-all view-overlay"></div>
    <table id="<c:out value='${entity}'/>" class="display">
        <thead>
            <tr>
                <c:if test="${not empty fieldsMetadata}">
                    <c:forEach items="${fieldsMetadata}" var="fieldMetadata">
                        <th data-preference-sort="<c:out value="${fieldMetadata['Sort']}"/>"
                            data-metadata-basetype="<c:out value="${fieldMetadata['BaseType']}"/>"
                            data-metadata-entity="<c:out value="${fieldMetadata['LookupFor']}"/>"
                            data-metadata-name="<c:out value="${fieldMetadata['Name']}"/>"
                            data-metadata-types="<c:out value="${fieldMetadata['Types']}"/>"
                            <c:forEach var="availableDisplay" items="${availableDisplays}">
                                <c:if test="${availableDisplay.visible && not empty fieldMetadata[availableDisplay.name]}">
                                    data-setting-<c:out value='${fn:toLowerCase(availableDisplay.name)}'/>="<c:out value='${fieldMetadata[availableDisplay.name]}'/>"
                                </c:if>
                            </c:forEach>>
                            <c:out value="${fieldMetadata['Name']}"/>
                        </th>
                    </c:forEach>
                </c:if>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<script type="text/javascript">
    $(function()
    {
        defineCustomerDataDisplayViewList("customerDataDisplayViewList");
        customerDataDisplayViewList.initialize(
        {
            Controller: "${controller}",
            Entity: "${entity}",
            ExoLocale: "${exoLocale}",
            ExportControl: "${exportControl}",
            Filter: "${filter}",
            FilterEntity: "${filterEntity}",
            HistoryLimit: "${historyLimit}",
            PageSize: "${pageSize}",
            PortalUri: "${portalUri}",
            RequestContextPath: "${requestContextPath}",
            WindowId: "${windowId}",
        });
    });
</script>