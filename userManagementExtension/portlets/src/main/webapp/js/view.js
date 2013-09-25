var $userNameInput;
listKey= new Array();
var saveProfile = function ()
{
    var jData = new Array();
    var keyEntity;
    for (i = 0; i < listKey.length; i++) 
    {
        keyEntity = new Object();
        keyEntity.connectId = listKey[i][0];
        keyEntity.key = listKey[i][1];
        jData.push(keyEntity);
    }
    $.ajax
    ({
        cache: true,
        type: "POST",
        async: false,
        data: JSON.stringify(jData),
        contentType: "application/json",
        dataType: "json",
        url: "/rest/private/keyEntitiesManagement/saveKeyEntities/" + $userNameInput[0].value,
    })
    .fail
    (
        function () 
        {
        }
    )
    .done
    (
        function () 
        {
        }
    );
}

function createTable(source) 
{
    var keyEntities_ = source;
    listKey = new Array();
    if (keyEntities_ != "") 
    {
        var splittedKeyEntities = keyEntities_.split("@");
        for (i = 0; i < splittedKeyEntities.length; i++) 
        {
            entity = new Array();
            entity[0] = splittedKeyEntities[i].split("/")[0];
            entity[1] = splittedKeyEntities[i].split("/")[1];
            entity[2] = "";
            listKey.push(entity);
        }
    }
    oTable = $("#listKeyEntities").dataTable
    ({
        "bJQueryUI": true,
        "bProcessing": true,
        "sPaginationType": "full_numbers",
        "aaData": listKey,
        "oLanguage": 
        {
            "sUrl": "/userManagementPortlets/dataTable_locale/dataTable_" + eXo.env.portal.language + ".txt"
        },
        "aoColumns": 
        [
	        {
	            "sTitle": "Connect"
	        }, 
	        {
	           "sTitle": "Key"
	        }, 
	        {
	            "sTitle": "Delete",
	            "mRender": function (data, type, row) 
	            {
	                return "<input type='checkbox'>";
	            }
	        }
        ]
    });
}

function setKeys(listKey_) 
{
    var jData = new Array();
    var sData;
    var keyEntity;
    if (listKey_ != null) 
    {
        for (i = 0; i < listKey_.length; i++) 
        {
            keyEntity = new Object();
            keyEntity.connectId = listKey_[i][0];
            keyEntity.key = listKey_[i][1];
            jData.push(keyEntity);
        }
        sData = JSON.stringify(jData);
    } 
    else 
    {
        sData = null;
    }
    $.ajax
    ({
        cache: true,
        type: "POST",
        async: false,
        data: sData,
        contentType: "application/json",
        dataType: "json",
        url: "/rest/private/keyEntitiesManagement/setKeyEntities",
    })
    .fail
    (
        function () 
        {
        }
    )
    .done
    (
        function () 
        {
        }
    );
}

$(function () 
{
    var test=true;
    window.require(["SHARED/bootstrap", "SHARED/portalControl"], function (createTab) 
    {
        var originalAjaxGet = window.ajaxGet;
        window.ajaxGet = function (f, e) 
        {
            originalAjaxGet(f, e);
            addTab();
        };
        var originalChangeTabForUIFormTabpane = $.proxy(eXo.webui.UIHorizontalTabs.changeTabForUIFormTabpane, eXo.webui.UIHorizontalTabs);
        eXo.webui.UIHorizontalTabs.changeTabForUIFormTabpane = function (a, c, e) 
        {
            originalChangeTabForUIFormTabpane(a, c, e);
            addTab();
        };
        var addTab = function () 
        {
            setTimeout(function () 
            {
	            entityKeyList = new Array();
	            var selected = "";
	            var deleted = "";
	            var $uiOrganizationPortletDiv = $("#UIOrganizationPortlet");
	            $userNameInput = $("#userName");
	            if (($userNameInput.length) && (typeof ($("#addKeys-tab")[0]) == "undefined")) 
	            {
	                var keyEntities = "";
	                $uiOrganizationPortletDiv.find("#UIUserManagement ul").append("<li><a data-toggle='tab' data-target='#addKeys-tab'>Manage Keys</a></li>");
	                $uiOrganizationPortletDiv.find(".tab-content").append("<div id='addKeys-tab' class='tab-pane fade'></div>");
	                $("#addKeys-tab").append("<div id='addKeys'></div>");
	                $("#addKeys").append("<table id='listKeyEntities' class='display'></table>");
	                $("#listKeyEntities").append("<thead></thead>").append("<tr><th></th><th></th><th></th></tr>");
	                $("#listKeyEntities").append("<tbody></tbody>").append("<tr><td></td><td></td><td></td></tr>");
	                $.ajax
	                ({
	                    cache: true,
	                    url: "/rest/private/keyEntitiesManagement/getKeyEntities/" + $userNameInput[0].value,
	                })
	                .fail
				    (
				        function () 
				        {
				        }
				    )
				    .done
				    (
				        function (result) 
				        {
				             if (typeof result != "undefined") 
				             {
	                             keyEntities = result;
	                         }
	                         createTable(keyEntities);
				        }
				    );
	                $("#addKeys").append("<button id='my-button' type='button'>ADD</button>");
	                $("#addKeys").append("<div id='add_pop_up' style='display:none' ></div>");
	                $("#addKeys").append("<button id='delete' type='button'>delete</button>");
	                $("#add_pop_up").append("<table id='addKeyEntities' class='display'></table>");
	                $("#addKeyEntities").append("<thead></thead>").append("<tr><th></th><th></th><th></th></tr>");
	                $("#addKeyEntities").append("<tbody></tbody>").append("<tr><td></td><td></td><td></td></tr>");
	                $("#add_pop_up").append("<button id='button-save-add' type='button' class='b-close'>Add</button>");
	                $("#add_pop_up").append("<button id='button-cancel-add' type='button' class='b-close'>Cancel</button>");
	                $("#addKeyEntities").on("click", "tbody tr td input", function () 
	                {
		                rowIndex = addTable.fnGetPosition($(this).closest("tr")[0]);
		                rowdata = addTable.fnGetData(rowIndex);
		                list = "CustomerNumber:" + rowdata[0] + "|CustomerName:" + rowdata[1];
		                if (selected.indexOf(list) == -1) 
		                {
		                    selected = selected + "@Customer/" + list;
		                } 
		                else 
		                {
		                    selected = selected.split("@Customer/" + list)[0] + selected.split("Customer/" + list)[1];
		                }
	                });
	                $("#listKeyEntities").on("click", "tbody tr td input", function () 
	                {
	                    rowIndex = oTable.fnGetPosition($(this).closest("tr")[0]);
	                    rowdata = oTable.fnGetData(rowIndex);
	                    list = rowdata[0] + "/" + rowdata[1];
	                    if (deleted.indexOf(list) == -1) 
	                    {
	                        deleted = deleted + "@" + list;
	                    } 
	                    else 
	                    {
	                        deleted = deleted.split("@" + list)[0] + deleted.split("@" + list)[1];
	                    }
	                });
	                $('#button-cancel-add').bind('click', function (e) 
	                {
	                });
	                $('#button-save-add').bind('click', function (e) 
	                {
	                    var splittedKeyEntities = selected.split("@");
	                    for (i = 0; i < splittedKeyEntities.length; i++) 
	                    {
	                         if (splittedKeyEntities[i] != "") 
	                         {
	                             entity = new Array();
	                             entity[0] = splittedKeyEntities[i].split("/")[0];
	                             entity[1] = splittedKeyEntities[i].split("/")[1];
	                             entity[2] = "";
	                             listKey.push(entity);
	                         }
	                    }
	                    oTable.fnClearTable();
	                    oTable.fnAddData(listKey);
	                    oTable.fnDraw();
	                    selected = "";
	                    setKeys(listKey);
					    submitButton = $('.btn')[0].getAttribute("onclick");
					    link = "javascript:saveProfile();" + submitButton.split("javascript:")[1];
					    $('.btn')[0].setAttribute("onclick",link);
	                });
	                $('#delete').bind('click', function (e) 
	                {
	                    var splittedKeyEntities = deleted.split("@");
	                    for (i = 0; i < splittedKeyEntities.length; i++) 
	                    {
	                        if (splittedKeyEntities[i] != "") 
	                        {
	                            entity = new String();
	                            entity = splittedKeyEntities[i].split("/")[1];
	                            for (j = 0; j < listKey.length; j++) 
	                            {
	                                if (listKey[j][1].indexOf(entity) != -1) 
	                                {
	                                    listKey.splice(j, 1);
	                                }
	                            }
	                        }
	                    }
	                    setKeys(listKey);
	                    deleted = "";
	                    submitButton = $('.btn')[0].getAttribute("onclick");
					    link = "javascript:saveProfile();" + submitButton.split("javascript:")[1];
					    $('.btn')[0].setAttribute("onclick",link);
	                    oTable.fnClearTable();
	                    oTable.fnAddData(listKey);
	                });
	                $('.btn').on('click', function (e) 
	                {
	                    if (e.currentTarget.getAttribute("onclick").indexOf("Save") != -1) 
	                    {
	                    } 
	                    else 
	                    {
	                        setKeys(null);
	                    }
	
	                });
	                $('#my-button').bind('click', function (e) 
	                {
	                    e.preventDefault();
	                    if (typeof addTable != "undefined") 
	                    {
	                        addTable.fnClearTable();
	                        addTable.fnDestroy();
	                        $("#addKeyEntities tbody").remove();
	                    }
	                    entityList = new Array();
	                    if (listKey.length != 0) 
	                    {
	                        entityKeyList = new Array();
	                        for (i = 0; i < listKey.length; i++) 
	                        {
	                            entityKeyList.push(listKey[i][1].split("|"));
	                        }
	                        for (i = 0; i < entityKeyList.length; i++) 
	                        {
	                            entitySel = new Array();
	                            entitySel[0] = "CustomerNumber";
	                            entitySel[1] = "!=";
	                            entitySel[2] = (entityKeyList[i][0]).split(":")[1];
	                            entityList.push(entitySel);
	                            entitySel = new Array();
	                            entitySel[0] = "CustomerName";
	                            entitySel[1] = "!=";
	                            entitySel[2] = (entityKeyList[i][1]).split(":")[1];
	                            entityList.push(entitySel);
	                        }
	                    }
	                    addTable = $("#addKeyEntities").dataTable
	                    ({
	                        "bJQueryUI": true,
	                        "bProcessing": true,
	                        "bServerSide": true,
	                        bLengthChange: false,
	                        bAutoWidth: false,
	                        sPaginationType: "full_numbers",
	                        "oLanguage": 
	                        {
	                            "sUrl": "/userManagementPortlets/dataTable_locale/dataTable_" + eXo.env.portal.language + ".txt"
	                        },
	                        "sAjaxSource": "/interaction-manager/service/bridge/customer-connect/Customers?fields=CustomerNumber,CustomerName",
	                        "aoColumns": 
	                        [
	                            {
	                                "sTitle": "CustomerNumber"
	                            }, 
	                            {
	                                "sTitle": "CustomerName"
	                            }, 
	                            {
	                                "sTitle": "Include",
	                                "mRender": function (data, type, row) 
	                                {
	                                    list = "CustomerNumber:" + row[0] + "|CustomerName:" + row[1];
	                                    if (selected.indexOf(list) == -1) 
	                                    {
	                                         return "<input type='checkbox'>";
	                                    } 
	                                    else 
	                                    {
	                                        return "<input type='checkbox' checked='checked'>";
	                                    }
	                                }
	                            }
	                        ],
	                        fnServerParams: function (data) 
	                        {
	                            var filters = 
	                            {
	                                op: "&&",
	                                conds: entityList,
	                            };
	                            data.push
	                            (
	                                {
	                                    name: "Filters",
	                                    value: JSON.stringify(filters),
	                                    
	                                }
	                            );
	                        }
	                    });
	                    $('#add_pop_up').bPopup
	                    ({
	                        follow: (true, true),
	                        position: ['auto', 100],
	                        modalClose: false
	                    });
	                });
	            }
            }, 500);
        };
    });
});