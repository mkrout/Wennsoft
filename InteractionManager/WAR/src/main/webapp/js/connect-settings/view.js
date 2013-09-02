var defineConnectSettingsView = function(context)
{
    window[context] = (function(context)
    {
        var requestContextPath;
        var pageAccess;

        var $portletWindowDiv;
        var $overlayDiv;
        var overlaySpinner;
        var $form;
        var $tabsDiv;
        var $pageAccessTabActivator;
        var $webServiceUriInput;
        var $historyLimitInput;
        var $webServiceLoggingLevelSelect;
        var $registeredProductsTable;
        var registeredProductsDataTable;
        var $pageAccessTable;
        var pageAccessDataTable;
        var suspendPageAccessDataTableRender;
        var $themeSelect;
        var $cacheTable;
        var cacheDataTable;
        var $hiddenPageAccessInput;
        var $hiddenActionInput;

        var $webServiceNotFoundMessageDiv;

        context.initialize = function(renderParameters)
        {
            try
            {
                Globalize.culture(renderParameters.ExoLocale);

                requestContextPath = renderParameters.RequestContextPath;
                pageAccess = JSON.parse(renderParameters.PageAccessJson);

                $portletWindowDiv = $("div#" + renderParameters.WindowId);
                $overlayDiv = $portletWindowDiv.find("div#overlay");
                overlaySpinner = new Spinner(
                {
                    color: "#000",
                });
                $form = $portletWindowDiv.find("form");
                $tabsDiv = $portletWindowDiv.find("div#tabs");
                $pageAccessTabActivator = $portletWindowDiv.find("li#pageAccessTabActivator");
                $webServiceUriInput = $portletWindowDiv.find("input#webServiceUri");
                $historyLimitInput = $portletWindowDiv.find("input#historyLimit");
                $webServiceLoggingLevelSelect = $portletWindowDiv.find("select#webServiceLoggingLevel");
                $registeredProductsTable = $portletWindowDiv.find("table#registration");
                $pageAccessTable = $portletWindowDiv.find("table#pageAccessTable");
                suspendPageAccessDataTableRender = true;
                $themeSelect = $portletWindowDiv.find("select#theme");
                $cacheTable = $portletWindowDiv.find("table#cacheTable");
                $hiddenPageAccessInput = $form.find("> input#pageAccess");
                $hiddenActionInput = $form.find("> input#action");

                $webServiceNotFoundMessageDiv = $("<div>").append($("<span>",
                {
                    class: "ui-icon ui-icon-alert",
                    style: "display: inline-block; margin-right: 3px; vertical-align: middle;",
                }))
                .append($("<span>",
                {
                    style: "vertical-align: middle;",
                    text: Globalize.localize("WebServiceNotFound"),
                }));

                $portletWindowDiv.find("div.im-view-title").text(Globalize.localize("ConnectSettings"));
                $portletWindowDiv.find("div#tabs > ul > li > a").each(function(tabAnchorIndex, tabAnchor)
                {
                    $(tabAnchor).localize();
                });
                $portletWindowDiv.find("label.im-detail-row-label").each(function(detailCellLabelIndex, detailCellLabel)
                {
                    $(detailCellLabel).localize();
                });
                $webServiceLoggingLevelSelect.find("> option").each(function(webServiceLoggingLevelIndex, webServiceLoggingLevelOption)
                {
                    $(webServiceLoggingLevelOption).localize();
                });
                $registeredProductsTable.find("> thead > tr > th").each(function(registeredProductsTableHeadIndex, registeredProductsTableHead)
                {
                    $(registeredProductsTableHead).localize();
                });
                $pageAccessTable.find("> thead > tr > th").each(function(pageAccessTableHeadIndex, pageAccessTableHead)
                {
                    $(pageAccessTableHead).localize();
                });
                $cacheTable.find("> thead > tr > th").each(function(cacheTableHeadIndex, cacheTableHead)
                {
                    $(cacheTableHead).localize();
                });

                $tabsDiv.tabs();

                $pageAccessTabActivator.tooltip(
                {
                    close: function()
                    {
                        $pageAccessTabActivator.tooltip("disable");
                    },
                    content: function()
                    {
                        return $webServiceNotFoundMessageDiv.html();
                    },
                    disabled: true,
                    items: "li",
                    tooltipClass: "ui-state-error",
                })
                .on("click", function()
                {
                    if ($pageAccessTabActivator.hasClass("ui-state-disabled"))
                    {
                        $pageAccessTabActivator.tooltip("enable");
                        $pageAccessTabActivator.tooltip("open");
                    }
                });

                $webServiceUriInput.on("change", saveSettings);
                $webServiceLoggingLevelSelect.combobox(
                {
                    create: function()
                    {
                        var $webServiceLoggingLevelComboboxDiv = $webServiceLoggingLevelSelect.nextAll(".ui-combobox").first();
                        $webServiceLoggingLevelComboboxDiv.tooltip(
                        {
                            close: function()
                            {
                                $webServiceLoggingLevelComboboxDiv.tooltip("disable");
                            },
                            content: function()
                            {
                                return $webServiceNotFoundMessageDiv.html();
                            },
                            disabled: true,
                            items: "span",
                            tooltipClass: "ui-state-error",
                        })
                        .on("click", function()
                        {
                            if ($webServiceLoggingLevelSelect.combobox("option", "disabled"))
                            {
                                $webServiceLoggingLevelComboboxDiv.tooltip("enable");
                                $webServiceLoggingLevelComboboxDiv.tooltip("open");
                            }
                        });
                    },
                    selected: saveSettings,
                });
                $historyLimitInput.spinnerCustom(
                {
                    default: 5,
                    min: 1,
                    max: 100,
                    stop: saveSettings,
                });

                registeredProductsDataTable = $registeredProductsTable.dataTable(
                {
                    bDeferRender: true,
                    bJQueryUI: true,
                    bPaginate: false,
                    bSort: false,
                    sDom: "<'H'<'im-view-title'>>tS",
                });
                $portletWindowDiv.find("#registration_wrapper div.im-view-title").html(Globalize.localize("Registration"));

                pageAccessDataTable = $pageAccessTable.dataTable(
                {
                    aoColumns:
                    [
                        null,
                        null,
                        { sClass: "center" }
                    ],
                    bDeferRender: true,
                    bJQueryUI: true,
                    bPaginate: false,
                    bSort: false,
                    fnPreDrawCallback: function()
                    {
                        return !suspendPageAccessDataTableRender;
                    },
                    sDom: "tS",
                });

                $themeSelect.combobox(
                {
                    selected: saveSettings,
                });

                cacheDataTable = $cacheTable.dataTable(
                {
                    aoColumns:
                    [
                        null,
                        null,
                        null,
                        { sClass: "center" }
                    ],
                    bDeferRender: true,
                    bJQueryUI: true,
                    bPaginate: false,
                    bProcessing: false,
                    bServerSide: true,
                    bSort: false,
                    fnServerData: function(source, data, callback, settings)
                    {
                        settings.jqXHR = $.ajax(
                        {
                            beforeSend: function()
                            {
                                $overlayDiv.show();
                                overlaySpinner.spin($overlayDiv[0]);
                                $overlayDiv.delay(1000);
                            },
                            cache: false,
                            data: data,
                            dataType: "json",
                            url: source,
                        })
                        .fail(function()
                        {

                        })
                        .done(function(cacheTableData)
                        {
                            if (cacheTableData.aaData !== null)
                            {
                                $.each(cacheTableData.aaData, function(cacheDataIndex, cacheData)
                                {
                                    var $cacheButton = $("<button>",
                                    {
                                        class: "im-clear-button",
                                        type: "button",
                                        text: Globalize.localize("Clear"),
                                        value: cacheData[0],
                                    });
                                    cacheData.push($cacheButton[0].outerHTML);
                                });
                            }

                            callback(cacheTableData);

                            // TODO CACHE BUTTONS EVENTS
                            $cacheTable.find("> tbody > tr > td > button.im-clear-button").button().on("click", function(event)
                            {
                                $.ajax(
                                {
                                    beforeSend: function()
                                    {
                                        $overlayDiv.show();
                                        overlaySpinner.spin($overlayDiv[0]);
                                        $overlayDiv.delay(1000);
                                    },
                                    type: "DELETE",
                                    url: requestContextPath + "/service/cache/" + $(event.currentTarget).attr("value"),
                                })
                                .fail(function(jqXHR, textStatus, errorThrown)
                                {
                                    // TODO ?
                                    console.log(textStatus);
                                    console.log(errorThrown);
                                })
                                .done(function()
                                {
                                    cacheDataTable.fnDraw();
                                })
                                .always(function()
                                {
                                    $overlayDiv.hide(0);
                                });
                            });
                        })
                        .always(function()
                        {
                            $overlayDiv.hide(0);
                        });
                    },
                    sAjaxSource: renderParameters.RequestContextPath + "/service/cache/",
                    sDom: "tS",
                });

                loadEntityMetadata();
            }
            catch (exception)
            {
                throw exception;
            }
        };

        var loadEntityMetadata = function()
        {
            var displayOverlay = !$overlayDiv.is(":visible");
            if (displayOverlay)
            {
                $overlayDiv.show();
                overlaySpinner.spin($overlayDiv[0]);
                $overlayDiv.delay(1000);
            }

            suspendPageAccessDataTableRender = true;
            pageAccessDataTable.fnClearTable();
            suspendPageAccessDataTableRender = false;

            $.ajax(
            {
                cache: true,
                dataType: "json",
                url: requestContextPath + "/service/bridge/customer-connect/Metadata",
            })
            .fail(function()
            {
                $pageAccessTabActivator.addClass("ui-state-disabled");
                $webServiceUriInput.addClass("ui-state-error");
                $webServiceLoggingLevelSelect.combobox("option", "disabled", true);
            })
            .done(function(controllersMetadata)
            {
                $.each(controllersMetadata.Interface, function(controllerMetadataIndex, controllerMetadata)
                {
                    var page = "";
                    var enabled = false;
                    $.each(pageAccess, function(pageAccessIndex, pageAccessItem)
                    {
                        var pageAccessItemProcessed = false;

                        if (controllerMetadata.Entity.Name === pageAccessItem.Entity)
                        {
                            page = pageAccessItem.Page;
                            enabled = pageAccessItem.Enabled;
                            pageAccessItemProcessed = true;
                        }

                        return !pageAccessItemProcessed;
                    });

                    var $pageInput = $("<input>",
                    {
                        class: "page-input ui-corner-all ui-widget-content",
                        value: page,
                    });
                    if (!enabled)
                    {
                        $pageInput.prop("disabled", true);
                        $pageInput.addClass("ui-state-disabled");
                    }

                    var $enabledInput = $("<input>",
                    {
                        class: "enabled-input",
                        type: "checkbox",
                    });
                    if (enabled)
                    {
                        $enabledInput.attr("checked", true);
                    }

                    pageAccessDataTable.fnAddData([ Globalize.localize(controllerMetadata.Entity.Name),
                                                    $pageInput[0].outerHTML,
                                                    $enabledInput[0].outerHTML ]);
                    var $pageAccessTableRow = $pageAccessTable.find("> tbody > tr:last");
                    $pageAccessTableRow.attr("data-metadata-entity", controllerMetadata.Entity.Name);
                    $pageAccessTableRow.attr("data-metadata-display", "Detail");
                });
                
                pageAccessDataTable.fnSort([ [0, "asc" ] ]);
                $pageAccessTabActivator.removeClass("ui-state-disabled");
                $webServiceUriInput.removeClass("ui-state-error");
                $webServiceLoggingLevelSelect.combobox("option", "disabled", false);

                $.ajax(
                {
                    dataType: "json",
                    url: requestContextPath + "/service/bridge/customer-connect/LoggingLevel",
                })
                .fail(function()
                {
                    // TODO ?
                })
                .done(function(loggingLevel)
                {
                    $webServiceLoggingLevelSelect.combobox("select", loggingLevel);
                });
            })
            .always(function()
            {
                if (displayOverlay)
                {
                    $overlayDiv.hide(0);
                }

                $pageAccessTable.find("input.page-input").on("change", saveSettings);
                $pageAccessTable.find("input.enabled-input").on("change", function()
                {
                    var $enabledInput = $(this);
                    var $pageInput = $enabledInput.parent().prev().find("input-page-input");

                    if ($enabledInput.is(":checked"))
                    {
                        $pageInput.prop("disabled", false);
                        $pageInput.removeClass("ui-state-disabled");
                    }
                    else
                    {
                        $pageInput.prop("disabled", true);
                        $pageInput.addClass("ui-state-disabled");
                    }

                    saveSettings();
                });
            });
        };

        var saveSettings = function()
        {
            $overlayDiv.show();
            overlaySpinner.spin($overlayDiv[0]);
            $overlayDiv.delay(1000);

            if ($webServiceUriInput.val().split("").pop() === "/")
            {
                $webServiceUriInput.val($webServiceUriInput.val().slice(0, -1));
            }

            pageAccess = new Array();
            $(pageAccessDataTable.fnGetNodes()).each(function()
            {
                var $pageAccessTableRow = $(this);
                pageAccess.push(
                {
                    Entity: $pageAccessTableRow.attr("data-metadata-entity"),
                    Display: $pageAccessTableRow.attr("data-metadata-display"),
                    Page: $pageAccessTableRow.find("> td:nth-child(2) > input").val(),
                    Enabled: $pageAccessTableRow.find("> td:nth-child(3) > input").is(":checked"),
                });
            });
            $hiddenPageAccessInput.val(JSON.stringify(pageAccess));

            $.ajax(
            {
                data: $form.serialize(),
                type: $form.prop("method"),
                url: $form.prop("action"),
            })
            .fail(function(jqXHR, textStatus, errorThrown)
            {
                // TODO ?
            })
            .done(function()
            {

            })
            .always(function()
            {
                loadEntityMetadata();
                $overlayDiv.hide(0);
            });
        };

        return context;
    })(window[context] || {});
};