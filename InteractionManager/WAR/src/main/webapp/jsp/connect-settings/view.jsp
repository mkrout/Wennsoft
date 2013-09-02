<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>

<div class="interaction-manager interaction-manager-view">
    <div id="overlay" class="ui-corner-all view-overlay"></div>
    <form action="<portlet:actionURL/>" method="POST">
        <div class="ui-corner-all ui-widget-content" id="connectSettings">
            <div class="fg-toolbar ui-corner-tl ui-corner-tr ui-helper-clearfix ui-toolbar ui-widget-header">
                <div class="im-view-title"></div>
            </div>
            <div id="tabs">
                <ul>
                    <li><a href="#generalTab">General</a></li>
                    <li id="pageAccessTabActivator"><a href="#pageAccessTab">PageAccess</a></li>
                    <li><a href="#visualTab">Visual</a></li>
                    <li><a href="#cacheTab">GlobalCache</a></li>
                </ul>
                <div id="generalTab">
                    <div id="fieldSettings" class="detail-content">
                        <div class="detail-column" style="width: 50%">
                            <div class="im-detail-row">
                                <label class="im-detail-row-label" for="webServiceUri">WebServiceUri</label>
                                <input class="ui-corner-all ui-widget-content"
                                       id="webServiceUri"
                                       name="webServiceUri"
                                       value="<c:out value='${connectSettings["WebServiceUri"]}'/>"/>
                            </div>
                            <div class="im-detail-row">
                                <label class="im-detail-row-label" for="webServiceLoggingLevelCombobox">WebServiceLoggingLevel</label>
                                <select id="webServiceLoggingLevel" name="webServiceLoggingLevel">
                                    <option value="OFF">OFF</option>
                                    <option value="FATAL">FATAL</option>
                                    <option value="ERROR">ERROR</option>
                                    <option value="WARN">WARN</option>
                                    <option value="INFO">INFO</option>
                                    <option value="DEBUG">DEBUG</option>
                                    <option value="ALL">ALL</option>
                                </select>
                            </div>
                        </div>
                        <div class="detail-column" style="width: 50%">
                            <div class="im-detail-row">
                                <label class="im-detail-row-label" for="historyLimit">HistoryLimit</label>
                                <input class="ui-corner-all ui-widget-content"
                                       id="historyLimit"
                                       name="historyLimit"
                                       value="<c:out value='${connectSettings["HistoryLimit"]}'/>"/>
                            </div>
                            <div class="im-detail-row">
                                <label class="im-detail-row-label" for="termsAndConditions">TermsAndConditions</label>
                                <span class="center">
                                    <input id="termsAndConditions"
                                           name="termsAndConditions"
                                           type="checkbox"
                                           <c:if test="${connectSettings['TermsAndConditions']}">checked="checked"</c:if>>
                                </span>
                            </div>
                        </div>
                    </div>
                    <table class="display" id="registration">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Registered</th>
                                <th>Activated</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>TODO</td>
                                <td>TODO</td>
                                <td>TODO</td>
                            </tr>
                            <tr>
                                <td>TODO</td>
                                <td>TODO</td>
                                <td>TODO</td>
                            </tr>
                            <tr>
                                <td>TODO</td>
                                <td>TODO</td>
                                <td>TODO</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div id="pageAccessTab">
                    <table class="display" id="pageAccessTable">
                        <thead>
                            <tr>
                                <th>Entity</th>
                                <th>Page</th>
                                <th>Enabled</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
                <div id="visualTab">
                    <div class="detail-content">
                        <div class="detail-column" style="width: 50%">
                            <div class="im-detail-row">
                                <label class="im-detail-row-label" for="theme">Theme</label>
                                <select id="theme" name="theme">
                                    <c:forEach var="availableTheme" items="${availableThemes}">
                                        <c:if test="${availableTheme['Type'] == 'Directory'}">
                                            <option value="<c:out value='${availableTheme["Name"]}'/>"
                                                    <c:if test="${availableTheme['Name'] == connectSettings['Theme']}">
                                                        selected
                                                    </c:if>>
                                                <c:out value="${availableTheme['Name']}"/>
                                            </option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                         </div>
                    </div>
                </div>
                <div id="cacheTab">
                    <table class="display" id="cacheTable">
                        <thead>
                            <tr>
                                <th>Cache</th>
                                <th>Elements</th>
                                <th>Size</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
            <div class="fg-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix ui-toolbar ui-widget-header"></div>
        </div>
        <input id="pageAccess" name="pageAccess" type="hidden">
    </form>
</div>

<script type="text/javascript">
    $(function()
    {
        defineConnectSettingsView("connectSettingsView");
        connectSettingsView.initialize(
        {
            AvailableThemes: '${availableThemes}',
            ExoLocale: "${exoLocale}",
            PageAccessJson: '${pageAccessJson}',
            RequestContextPath: "${requestContextPath}",
            WindowId: "${windowId}",
        });
    });
</script>