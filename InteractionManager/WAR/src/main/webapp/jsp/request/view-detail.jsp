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
                            <span class="im-detail-row ui-corner-all ui-widget"
                                  data-preference-edit="<c:out value="${fieldMetadata['Edit']}"/>"
                                  data-metadata-basetype="<c:out value="${fieldMetadata['BaseType']}"/>"
                                  data-metadata-controller="<c:out value="${fieldMetadata['Controller']}"/>"
                                  <c:if test="${not empty fieldMetadata['LookupFor']}">
                                      data-metadata-entity="<c:out value="${fieldMetadata['LookupFor']}"/>"
                                  </c:if>
                                  data-metadata-name="<c:out value="${fieldMetadata['Name']}"/>"
                                  data-metadata-required="<c:out value="${fieldMetadata['IsRequired']}"/>"
                                  data-metadata-input-type="<c:out value="${fieldMetadata['input-type']}"/>"
                                  data-metadata-types="<c:out value="${fieldMetadata['Types']}"/>"
                                  data-metadata-default="<c:out value="${fieldMetadata['default']}"/>"
                                  data-metadata-lookup-for="<c:out value="${fieldMetadata['LookupFor']}"/>"
                                  <c:if test="${fieldMetadata['ForeignKeyTo'] != 'null'}">
                                      <c:forEach items="${fieldMetadata['ForeignKeyTo']}" var="foreignKeyToItem">
                                          data-metadata-<c:out value="${fn:toLowerCase(foreignKeyToItem['Entity'])}"/>="<c:out value='${foreignKeyToItem["FkIndex"]}'/>"
                                      </c:forEach>
                                  </c:if>
                                  <c:forEach var="availableDisplay" items="${availableDisplays}">
                                      <c:if test="${availableDisplay.visible && not empty fieldMetadata[availableDisplay.name]}">
                                          data-setting-<c:out value="${fn:toLowerCase(availableDisplay.name)}"/>="<c:out value='${fieldMetadata[availableDisplay.name]}'/>"
                                      </c:if>
                                  </c:forEach>>
                                <label class="im-detail-row-label ui-widget
                                       <c:if test="${fieldMetadata['IsRequired'] && fieldMetadata['Edit']}">
                                           required-field
                                       </c:if>"
                                       for="<c:out value="${fieldMetadata['Name']}"/>">
                                       <c:out value="${fieldMetadata['Name']}"/>
                                </label>
                            </span>
                        </c:forEach>
                        <c:set value="${startIndex + columnSize}" var="startIndex"/>
                    </div>
                </c:forEach>
                <div class="detail-column-hidden" id="hiddenFieldsMetadata" style="display: none;">
                    <c:if test="${not empty fieldsMetadata}">
                        <c:forEach items="${hiddenFieldsMetadata}" var="hiddenFieldMetadata">
                                <span class="detail-cell-hidden"
                                      data-metadata-name="<c:out value="${hiddenFieldMetadata['Name']}"/>"
                                      <c:if test="${hiddenFieldMetadata['ForeignKeyTo'] != 'null'}">
                                          <c:forEach items="${hiddenFieldMetadata['ForeignKeyTo']}" var="foreignKeyToItem">
                                              data-metadata-<c:out value="${fn:toLowerCase(foreignKeyToItem['Entity'])}"/>="<c:out value='${foreignKeyToItem["FkIndex"]}'/>"
                                          </c:forEach>
                                      </c:if>
                                      data-value=""/>
                        </c:forEach>
                    </c:if>
                </div>
            </c:if>
        </div>
        <input id="save" name="save" type="hidden" value="Detail">
        <input id="action" name="action" type="hidden">
        <input id="controller" name="controller" type="hidden" value="<c:out value='${controller}'/>">
        <div class="fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix">
            <div class="action-buttons" id="actionButtons">
                <button id="saveButton" type="submit" value="Detail">Save</button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">   
    $(function()
    {
        defineRequestViewDetail("requestViewDetail");
        requestViewDetail.initialize(
        {
            Controller: "${controller}",
            Entity: "${entity}",
            ErrorDataJson: '${errorDataJson}',
            ErrorMessageJson: '${errorMessageJson}',
            ExoLocale: "${exoLocale}",
            PortalUri: "${portalUri}",
            WebServiceUri: "${connectSettings['WebServiceUri']}",
            WindowId: "${windowId}",
            RequestContextPath: "${requestContextPath}",
        });
    });
</script>