<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="interaction-manager">
    <div id="overlay" class="edit-overlay ui-corner-all"></div>
    <form action="<portlet:actionURL/>" method="POST">
        <div id="accordion">
            <h3 id="displayPreferencesHeader">DisplayPreferences</h3>
            <div id="displayPreferences" style="width: 100%;">
                <div class="im-detail-row">
                    <label class="im-detail-row-label ui-widget" for="entityCombobox">Entity</label>
                    <select id="entity" name="entity">
                        <c:forEach var="entityMetadata" items="${entitiesMetadata}">
                            <option data-metadata-controller="<c:out value='${entityMetadata["Controller"]}'/>"
                                    value="<c:out value='${entityMetadata["Entity"]["Name"]}'/>">
                                    <c:out value="${entityMetadata['Entity']['Name']}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="im-detail-row im-detail-preference">
                    <label class="im-detail-row-label ui-widget" for="columns">Columns</label>
                    <input id="columns" name="columns" value="<c:out value='${columns}'/>"/>
                </div>
            </div>
            <h3 id="fieldPreferencesHeader">FieldPreferences</h3>
            <div id="fieldPreferences">
                <table class="display" id="availableFields">
                    <thead>
                        <tr>
                            <th>Field</th>
                            <th>Type</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
                <div class="dual-table-control" id="dualTableControl" style="float: left;">
                    <button class="dual-table-control-button" id="dualTableControlButton" type="button"></button>
                </div>
                <table class="display" id="visibleFields">
                    <thead>
                        <tr>
                            <th></th>
                            <th></th>
                            <th>Field</th>
                            <th>Type</th>
							<th>Input</th>
                            <th>Default</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
        <input id="fields" name="fields" type="hidden">
        <input id="save" name="save" type="hidden" value="Edit">
    </form>
</div>

<script type="text/javascript">
    $(function()
    {
        defineRequestEdit("RequestEdit");
        RequestEdit.initialize(
        {
            Columns: "${columns}",
            Create: "${create}",
            Display: "${display}",
            Entity: "${entity}",
            ExoLocale: "${exoLocale}",
            ExportControl: "${exportControl}",
            Fields: "${fields}",
            Filter: "${filter}",
            PageSize: "${pageSize}",
            WebServiceUri: "${connectSettings['WebServiceUri']}",
            WindowId: "${windowId}",
            RequestContextPath: "${requestContextPath}",
        });
    });
</script>