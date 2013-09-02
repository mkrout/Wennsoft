var defineCustomerDataDisplayEdit = function(context)
{
    window[context] = (function(context)
    {
        var initialConnectPreference;
        var initialEntityPreference;
        var initialDisplayPreference;
        var initialFilterPreference;
        var initialExportControlPreference;
        var initialPageSizePreference;
        var initialColumnsPreference;
        var $initialFieldsPreferences;
        var requestContextPath;

        var $uiMaskWorkspaceDiv;
        var $portletWindowDiv;
        var $overlayDiv;
        var overlaySpinner;
        var $form;
        var $accordion;
        var $displayPreferencesDiv;
        var $connectSelect;
        var $entitySelect;
        var $displaySelect;
        var $filterSelect;
        var $fieldPreferencesHeader;
        var $availableFieldsTable;
        var availableFieldsDataTable;
        var $availableFieldsTableWrapperDiv;
        var $dualTableControlDiv;
        var $dualTableControlButton;
        var $visibleFieldsTable;
        var visibleFieldsDataTable;
        var $displayControlTableHeader;
        var $visibleFieldsWrapperDiv;
        var $hiddenFieldsInput;

        var $rowButton;
        var $editInput;

        var saveTimer;
        var saveDelay;

        context.initialize = function(renderParameters)
        {
            try
            {
                Globalize.culture(renderParameters.ExoLocale);

                initialConnectPreference = renderParameters.Connect;
                initialEntityPreference = renderParameters.Entity;
                initialDisplayPreference = renderParameters.Display;
                initialFilterPreference = renderParameters.Filter;
                initialExportControlPreference = renderParameters.ExportControl;
                initialPageSizePreference = renderParameters.PageSize;
                initialColumnsPreference = renderParameters.Columns;
                $initialFieldsPreferences = null;
                if (renderParameters.Fields)
                {
                    try
                    {
                        $initialFieldsPreferences = $($.parseXML(renderParameters.Fields));
                    }
                    catch (exception) { }
                }
                requestContextPath = renderParameters.RequestContextPath;

                $uiMaskWorkspaceDiv = $("div#UIMaskWorkspace");
                $portletWindowDiv = $("div#" + renderParameters.WindowId);
                $overlayDiv = $portletWindowDiv.find("div#overlay");
                overlaySpinner = new Spinner(
                {
                    color: "#000",
                });
                $form = $portletWindowDiv.find("form");
                $accordion = $portletWindowDiv.find("div#accordion");
                $displayPreferencesDiv = $portletWindowDiv.find("div#displayPreferences");
                $connectSelect = $portletWindowDiv.find("select#connect");
                $entitySelect = $portletWindowDiv.find("select#entity");
                $displaySelect = $portletWindowDiv.find("select#display");
                $filterSelect = $portletWindowDiv.find("select#filter");
                $fieldPreferencesHeader = $accordion.find("h3#fieldPreferencesHeader");
                $availableFieldsTable = $portletWindowDiv.find("table#availableFields");
                $dualTableControlDiv = $portletWindowDiv.find("div#dualTableControl");
                $dualTableControlButton = $dualTableControlDiv.find("button#dualTableControlButton");
                $visibleFieldsTable = $portletWindowDiv.find("table#visibleFields");
                $displayControlTableHeader = $visibleFieldsTable.find("> thead > tr > th#displayControl");
                $hiddenFieldsInput = $form.find("> input#fields");

                $rowButton = $("<button>",
                {
                    class: "row-button",
                    type: "button",
                });
                $editInput = $("<input>",
                {
                    name: "field",
                    type: "checkbox",
                    class: "edit-control ui-widget-content",
                });

                saveTimer = null;
                saveDelay = 750;

                // Prevent errors when the page editor is refreshed with the portlet edit modal open.
                if (!$.fn.DataTable.fnIsDataTable($visibleFieldsTable[0]))
                {
                    // Portlet edit modal modifications
                    var $uiTabContentDiv = $("div.uiTabContent");
                    if ($uiTabContentDiv.length)
                    {
                        $uiTabContentDiv.css("overflow", "hidden");
                        $uiTabContentDiv.css("max-height", "400px");
                    }

                    $fieldPreferencesHeader.localize();
                    $accordion.find("h3#displayPreferencesHeader").localize();
                    $displayPreferencesDiv.find("label.im-detail-row-label").each(function(entityPreferenceLabelIndex, entityPreferenceLabel)
                    {
                        $(entityPreferenceLabel).localize();
                    });
                    $connectSelect.find("> option").each(function(connectOptionIndex, connectOption)
                    {
                        $(connectOption).localize();
                    });
                    $entitySelect.find("> option").each(function(entityOptionIndex, entityOption)
                    {
                        $(entityOption).localize();
                    });
                    $displaySelect.find("> option").each(function(displayOptionIndex, displayOption)
                    {
                        $(displayOption).localize();
                    });
                    $availableFieldsTable.find("> thead > tr > th").each(function(availableFieldsTableHeaderIndex, availableFieldsTableHeader)
                    {
                        $(availableFieldsTableHeader).localize();
                    });
                    $visibleFieldsTable.find("> thead > tr > th").each(function(visibleFieldsTableHeaderIndex, visibleFieldsTableHeader)
                    {
                        $(visibleFieldsTableHeader).localize();
                    });

                    //$connectSelect.val(initialConnectPreference);
                    $connectSelect.combobox(
                    {
                        create: function()
                        {

                        },
                        required: true,
                        selected: function()
                        {

                        },
                    })
                    .combobox("alphaSort");

                    $entitySelect.val(initialEntityPreference);
                    $entitySelect.combobox(
                    {
                        create: function()
                        {
                            if (!$entitySelect.find("option:selected").length)
                            {
                                $fieldPreferencesHeader.addClass("ui-state-disabled");
                            }
                        },
                        required: true,
                        selected: function()
                        {
                            initialFilterPreference = null;
                            $initialFieldsPreferences = null;

                            loadFields();
                            savePreferences();

                            if (!$entitySelect.find("option:selected").length &&
                                !$displaySelect.find("option:selected").length)
                            {
                                $fieldPreferencesHeader.addClass("ui-state-disabled");
                            }
                            else
                            {
                                $fieldPreferencesHeader.removeClass("ui-state-disabled");
                            }
                        },
                    })
                    .combobox("alphaSort");

                    $displaySelect.val(initialDisplayPreference);
                    $displaySelect.combobox(
                    {
                        create: function()
                        {
                            if (!$displaySelect.find("option:selected").length)
                            {
                                $fieldPreferencesHeader.addClass("ui-state-disabled");
                            }
                            else
                            {
                                showDisplayPreferenceControls();
                            }
                        },
                        required: true,
                        selected: function()
                        {
                            initialFilterPreference = null;
                            initialPageSizePreference = null;
                            initialExportControlPreference = null;
                            initialColumnsPreference = null;
                            $initialFieldsPreferences = null;

                            showDisplayPreferenceControls();

                            loadFields();
                            savePreferences();

                            if (!$entitySelect.find("option:selected").length &&
                                !$displaySelect.find("option:selected").length)
                            {
                                $fieldPreferencesHeader.addClass("ui-state-disabled");
                            }
                            else
                            {
                                $fieldPreferencesHeader.removeClass("ui-state-disabled");
                            }
                        },
                    })
                    .combobox("alphaSort");

                    $filterSelect.combobox(
                    {
                        selected: savePreferences,
                    });
                    $displayPreferencesDiv.find("input#pageSize").spinnerCustom(
                    {
                        min: 5,
                        max: 100,
                        spin: savePreferences,
                        change: savePreferences,
                    });
                    $displayPreferencesDiv.find("input#exportControl").on("change", savePreferences);
                    $displayPreferencesDiv.find("input#columns").spinnerCustom(
                    {
                        min: 1,
                        max: 5,
                        stop: savePreferences,
                    });

                    availableFieldsDataTable = $availableFieldsTable.dataTable(
                    {
                        aaSorting:
                        [
                            [ 0, "asc" ]
                        ],
                        aoColumns:
                        [
                            null,
                            null,
                            { sClass: "center", }
                        ],
                        bDeferRender: true,
                        bJQueryUI: true,
                        bPaginate: false,
                        bScrollAutoCss: false,
                        bSort: false,
                        fnDrawCallback: function()
                        {
                            $availableFieldsTable.find("> tbody > tr > td.dataTables_empty").localize("NoRecords");
                        },
                        sDom: "<'H'F<'im-edit-title'>>tS<'F'>",
                        sScrollY: "230px",
                    });
                    $availableFieldsTableWrapperDiv = $portletWindowDiv.find("div#availableFields_wrapper");
                    $availableFieldsTableWrapperDiv.find("div.im-edit-title").localize("Available");
                    $availableFieldsTableWrapperDiv.hide();

                    $dualTableControlButton.button(
                    {
                        icons: { primary: "ui-icon-triangle-1-e", },
                        label: "DisplayAvailableFields",
                        text: false,
                    })
                    .tooltip({ content: Globalize.localize("ShowAvailableFields") });
                    $dualTableControlButton.on("click", function()
                    {
                        $dualTableControlDiv.css("float", "");
                        $dualTableControlButton.tooltip("close");
                        if ($availableFieldsTableWrapperDiv.is(":visible"))
                        {
                            $dualTableControlButton.tooltip({ content: Globalize.localize("ShowAvailableFields") });
                            $.when($availableFieldsTableWrapperDiv.animate({ width: "hide", }, "slow").promise(),
                                    $visibleFieldsWrapperDiv.animate({ width: "show", }, "slow").promise()).done(function()
                            {
                                visibleFieldsDataTable.fnAdjustColumnSizing();
                                $dualTableControlDiv.css("float", "left");
                                $dualTableControlButton.button("option",
                                {
                                    icons: { primary: "ui-icon-triangle-1-e", },
                                });
                                $dualTableControlButton.tooltip("close");
                            });
                        }
                        else
                        {
                            $dualTableControlButton.tooltip({ content: Globalize.localize("ShowVisibleFields") });
                            $.when($availableFieldsTableWrapperDiv.animate({ width: "show", }, "slow").promise(),
                                   $visibleFieldsWrapperDiv.animate({ width: "hide", }, "slow").promise()).done(function()
                            {
                                availableFieldsDataTable.fnSort([ [ 0, "asc" ] ]);
                                availableFieldsDataTable.fnAdjustColumnSizing();
                                $dualTableControlDiv.css("float", "right");
                                $dualTableControlButton.button("option",
                                {
                                    icons: { primary: "ui-icon-triangle-1-w", },
                                });
                                $dualTableControlButton.tooltip("close");
                            });
                        }
                    });

                    visibleFieldsDataTable = $visibleFieldsTable.dataTable(
                    {
                        aoColumns:
                        [
                            { bVisible: false },
                            { bSortable: false, sClass: "center", },
                            { bSortable: false, },
                            { bSortable: false, },
                            { bSortable: false, sClass: "center", }
                        ],
                        bDeferRender: true,
                        bJQueryUI: true,
                        bPaginate: false,
                        bScrollAutoCss: false,
                        bSort: true,
                        fnDrawCallback: function()
                        {
                            $visibleFieldsTable.find("> tbody > tr > td.dataTables_empty").localize("NoRecords");
                        },
                        fnRowCallback: function(row, data, rowIndex)
                        {
                            $(row).attr("id", rowIndex + 1);
                            return row;
                        },
                        sDom: "<'H'F<'im-edit-title'>>tS<'F'>",
                        sScrollY: "230px",
                    })
                    .rowReordering(
                    {
                        "callback": savePreferences,
                    });
                    $visibleFieldsWrapperDiv = $portletWindowDiv.find("div#visibleFields_wrapper");
                    $visibleFieldsWrapperDiv.find("div.im-edit-title").localize("Visible");

                    $accordion.accordion(
                    {
                        activate: function(event, ui)
                        {
                            if (ui.newHeader.is($fieldPreferencesHeader))
                            {
                                if ($availableFieldsTableWrapperDiv.is(":visible"))
                                {
                                    availableFieldsDataTable.fnAdjustColumnSizing();
                                }
                                if ($visibleFieldsWrapperDiv.is(":visible"))
                                {
                                    visibleFieldsDataTable.fnAdjustColumnSizing();
                                }
                            }
                        },
                        beforeActivate: function()
                        {
                            if ($uiMaskWorkspaceDiv.length)
                            {
                                $uiMaskWorkspaceDiv.animate({ top: ($(window).height() - 550) / 2, });
                            }
                        },
                        heightStyle: "content",
                    });
                    $fieldPreferencesHeader.tooltip(
                    {
                        close: function()
                        {
                            $fieldPreferencesHeader.tooltip("disable");
                        },
                        content: function()
                        {
                            var $fieldPreferencesMessageDiv = $("<div>").append($("<span>",
                            {
                                class: "error-message-icon ui-icon ui-icon-alert",
                                style: "display: inline-block; margin-right: 3px; vertical-align: middle;",
                            }))
                            .append($("<span>",
                            {
                                class: "error-message-text",
                                text: Globalize.localize("EntityAndDisplayRequired"),
                            }));

                            return $fieldPreferencesMessageDiv.html();
                        },
                        disabled: true,
                        items: "h3",
                        tooltipClass: "ui-state-error",
                    })
                    .on("click", function()
                    {
                        if ($fieldPreferencesHeader.hasClass("ui-state-disabled"))
                        {
                            $fieldPreferencesHeader.tooltip("enable");
                            $fieldPreferencesHeader.tooltip("open");
                        }
                    });

                    loadFields();
                }
            }
            catch (exception)
            {
                //alert("GenericError " + exception);
            }
        };

        // Display controls based on the display selection.
        var showDisplayPreferenceControls = function()
        {
            switch ($displaySelect.find("> option:selected").attr("value"))
            {
                case "Detail":
                {
                    $displayPreferencesDiv.find("div.im-list-preference").hide();
                    $displayPreferencesDiv.find("div.im-detail-preference").show();
                    $displayControlTableHeader.localize("Edit");

                    break;
                }
                case "List":
                {
                    $displayPreferencesDiv.find("div.im-list-preference").show();
                    $displayPreferencesDiv.find("div.im-detail-preference").hide();
                    $displayControlTableHeader.localize("Sort");

                    break;
                }
            }
        };

        // Populate the given span with controls for the list display's visible fields table.
        var generateSortControl = function($sortControlSpan)
        {
            $sortControlSpan.append($("<input>",
            {
                class: "sort-control-enabled",
                type: "checkbox",
            }))
            .append($("<select>")
                    .append($("<option>",
                    {
                        selected: "selected",
                        text: "▲",
                        val: "▲",
                    }))
                    .append($("<option>",
                    {
                        text: "▼",
                        val: "▼",
                    })));

            $sortControlSpan.find("> input.sort-control-enabled").on("change", function()
            {
                var $sortControlDirectionSelect = $sortControlSpan.find("> select");

                if (!this.checked)
                {
                    $sortControlDirectionSelect.combobox("select", "▲");
                }
                $sortControlDirectionSelect.combobox("option", "disabled", !this.checked);

                savePreferences();
            });

            $sortControlSpan.find("> select").combobox(
            {
                class: "sort-control-direction",
                disabled: true,
                selected: savePreferences,
            });
        }

        // Load the available and visible field data for the current entity and display.
        var loadFields = function()
        {
            var $selectedEntityOption = $entitySelect.find("> option:selected");
            var $selectedDisplayOption = $displaySelect.find("> option:selected");
            if ($selectedEntityOption.length && $selectedDisplayOption.length)
            {
                var displayOverlay = !$overlayDiv.is(":visible");
                if (displayOverlay)
                {
                    $overlayDiv.show();
                    overlaySpinner.spin($overlayDiv[0]);
                    $overlayDiv.delay(1000);
                }

                $.ajax(
                {
                    cache: true,
                    dataType: "json",
                    url: requestContextPath + "/service/bridge/customer-connect/" + $selectedEntityOption.attr("data-metadata-controller") + "/Metadata",
                })
                .done(function(fieldsMetadata)
                {
                    $filterSelect.empty();
                    availableFieldsDataTable.fnClearTable();
                    visibleFieldsDataTable.fnClearTable();

                    $.each(fieldsMetadata.Fields, function(fieldMetadataIndex, fieldMetadata)
                    {
                        var $fieldTypeSpan = $("<span>").append(Globalize.localize(fieldMetadata.BaseType));
                        if (fieldMetadata.Types !== null && fieldMetadata.Types.length)
                        {
                            var fieldMetadataTypeDisplay = new Array();
                            $.each(fieldMetadata.Types, function(fieldMetadataTypeIndex, fieldMetadataType)
                            {
                                fieldMetadataTypeDisplay.push(Globalize.localize(fieldMetadataType));
                            });

                            $fieldTypeSpan.append($("<span>",
                            {
                                class: "field-type",
                            })
                            .append(" (" + fieldMetadataTypeDisplay.join(",") + ")"));
                        }

                        var preferenced = false;
                        if ($selectedEntityOption.val() === initialEntityPreference && $initialFieldsPreferences !== null)
                        {
                            $initialFieldsPreferences.find("field").each(function(initialFieldPreferenceIndex, initialFieldPreference)
                            {
                                if ($(initialFieldPreference).find("name").text() === fieldMetadata.Name)
                                {
                                    preferenced = true;
                                }

                                return !preferenced;
                            });
                        }
                        if (!preferenced)
                        {
                            availableFieldsDataTable.fnAddData([ Globalize.localize(fieldMetadata.Name),
                                                                 $fieldTypeSpan[0].outerHTML,
                                                                 $rowButton.clone()[0].outerHTML ]);

                            var $availableFieldsTableRow = $availableFieldsTable.find("> tbody > tr:last");
                            $availableFieldsTableRow.attr("data-metadata-name", fieldMetadata.Name);
                            if(fieldMetadata.IsRequired)
                            {
                                $availableFieldsTableRow.find("> td:first").addClass("required-field");
                            }

                            var $availableFieldsTableRowButton = $availableFieldsTableRow.find("> td:last > button");
                            $availableFieldsTableRowButton.button(
                            {
                                icons: { primary: "ui-icon-plus", },
                                text: false,
                            });
                            $availableFieldsTableRowButton.on("click", moveAvailableRow);
                        }
                    });

                    if ($selectedEntityOption.val() === initialEntityPreference && $initialFieldsPreferences !== null)
                    {
                        $initialFieldsPreferences.find("field").each(function(initialFieldPreferenceIndex, initialFieldPreference)
                        {
                            var $initialFieldPreference = $(initialFieldPreference);

                            var $fieldTypeSpan = null;
                            var validPreference = false;
                            var immutableField = false;
                            $.each(fieldsMetadata.Fields, function(fieldMetadataIndex, fieldMetadata)
                            {
                                var fieldMetadataProcessed = false;

                                if ($initialFieldPreference.find("name").text() === fieldMetadata.Name)
                                {
                                    validPreference = true;
                                    $fieldTypeSpan = $("<span>").append(fieldMetadata.BaseType);
                                    if (fieldMetadata.Types !== null && fieldMetadata.Types.length)
                                    {
                                        var fieldMetadataTypeDisplay = new Array();
                                        $.each(fieldMetadata.Types, function(fieldMetadataTypeIndex, fieldMetadataType)
                                        {
                                            fieldMetadataTypeDisplay.push(Globalize.localize(fieldMetadataType));
                                        });
                                        $fieldTypeSpan.append($("<span>",
                                        {
                                            class: "field-type",
                                        })
                                        .append(" (" + fieldMetadataTypeDisplay.join(",") + ")"));

                                        immutableField = $.inArray("Immutable", fieldMetadata.Types) > -1;
                                    }

                                    fieldMetadataProcessed = true;
                                }

                                return !fieldMetadataProcessed;
                            });

                            if (validPreference)
                            {
                                var $visibleFieldDisplayControl = null;
                                switch ($selectedDisplayOption.attr("value"))
                                {
                                    case "Detail":
                                    {
                                        $visibleFieldDisplayControl = $editInput.clone();
                                        if ($initialFieldPreference.find("edit").text() === "true")
                                        {
                                            $visibleFieldDisplayControl.attr("checked", true);
                                        }
                                        if (immutableField)
                                        {
                                            $visibleFieldDisplayControl.prop("disabled", true);
                                            $visibleFieldDisplayControl.addClass("ui-state-disabled");
                                            $visibleFieldDisplayControl = $("<div>").append($visibleFieldDisplayControl);
                                        }

                                        break;
                                    }
                                    case "List":
                                    {
                                        $visibleFieldDisplayControl = $("<span>",
                                        {
                                            class: "sort-control",
                                        });

                                        break;
                                    }
                                }

                                visibleFieldsDataTable.fnAddData([ initialFieldPreferenceIndex + 1,
                                                                   $rowButton.clone()[0].outerHTML,
                                                                   Globalize.localize($initialFieldPreference.find("name").text()),
                                                                   $fieldTypeSpan[0].outerHTML,
                                                                   $visibleFieldDisplayControl[0].outerHTML ]);

                                var $visibleFieldsTableRow = $visibleFieldsTable.find("> tbody > tr:last");
                                $visibleFieldsTableRow.attr("data-metadata-name", $initialFieldPreference.find("name").text());

                                var $visibleFieldsTableRowButton = $visibleFieldsTableRow.find("> td > button");
                                $visibleFieldsTableRowButton.button(
                                {
                                    icons: { primary: "ui-icon-minus", },
                                    text: false,
                                });
                                $visibleFieldsTableRowButton.on("click", moveVisibleRow);

                                switch ($selectedDisplayOption.attr("value"))
                                {
                                    case "Detail":
                                    {
                                        var $editControl = $visibleFieldsTableRow.find("> td:nth-child(4) > input.edit-control");
                                        $editControl.on("change", savePreferences);
                                        $editControl.parent().tooltip(
                                        {
                                            content: Globalize.localize("FieldIsImmutable"),
                                            class: "ui-tooltip",
                                        });

                                        break;
                                    }
                                    case "List":
                                    {
                                        var $sortControl = $visibleFieldsTableRow.find("> td > span.sort-control");
                                        generateSortControl($sortControl);

                                        var initialFieldPreferenceSort = $initialFieldPreference.find("sort").length ?
                                                                         $initialFieldPreference.find("sort").text() : "";
                                        if (initialFieldPreferenceSort !== "")
                                        {
                                            var $sortControlEnabledInput = $sortControl.find("> input.sort-control-enabled");
                                            var $sortControlDirectionSelect = $sortControl.find("> select");

                                            $sortControlEnabledInput.attr("checked", true);
                                            $sortControlDirectionSelect.combobox("option", "disabled", false);
                                            $sortControlDirectionSelect.combobox("select", initialFieldPreferenceSort === "desc" ? "▼" : "▲");
                                        }

                                        break;
                                    }
                                }
                            }
                        });
                    }

                    if ($selectedDisplayOption.val() === "Detail")
                    {
                        visibleFieldsDataTable.fnSetColumnVis(4, false);
                    }
                    else if ($selectedDisplayOption.val() === "List")
                    {
                        var filterEntities = findFilterEntities($selectedEntityOption.val(), fieldsMetadata);
                        if (filterEntities.length)
                        {
                            $filterSelect.parents().eq(1).show();
                            $filterSelect.append($("<option>",
                            {
                                text: " ",
                                val: " ",
                            }));

                            $.each(filterEntities, function(filterEntityIndex, filterEntity)
                            {
                                var $filterOption = $("<option>",
                                {
                                    text: Globalize.localize(filterEntity.Field),
                                    val: filterEntity.Field,
                                });
                                $filterSelect.append($filterOption);

                                var $filterEntityOption = $entitySelect.find("option[value='" + filterEntity.Entity + "']");
                                if ($filterEntityOption.length)
                                {
                                    $.ajax(
                                    {
                                        cache: true,
                                        dataType: "json",
                                        url: requestContextPath + "/service/bridge/customer-connect/" + $filterEntityOption.attr("data-metadata-controller") + "/Metadata",
                                    })
                                    .done(function(filterFieldsMetadata)
                                    {
                                        $.each(findFilterEntities(filterEntity.Entity, filterFieldsMetadata), function(foreignFilterEntityIndex, foreignFilterEntity)
                                        {
                                            var $filterOption = $("<option>",
                                            {
                                                text: Globalize.localize(filterEntity.Field) + " " + Globalize.localize(foreignFilterEntity.Field),
                                                val: filterEntity.Field + "." + foreignFilterEntity.Field,
                                            });
                                            $filterSelect.append($filterOption);
                                        });
                                    })
                                    .fail(function()
                                    {
                                        // TODO ?
                                    });
                                }
                            });

                            $filterSelect.combobox("select", initialFilterPreference !== null ? initialFilterPreference : " ");
                        }
                        else
                        {
                            $filterSelect.parent().hide();
                        }
                    }
                    else
                    {
                        $filterSelect.parent().hide();
                    }
                })
                .always(function()
                {
                    if (displayOverlay)
                    {
                        $overlayDiv.hide(0);
                    }
                });
            }
        };

        var findFilterEntities = function(entity, fieldsMetadata)
        {
            var filterEntities = new Array();

            if (fieldsMetadata !== null && fieldsMetadata.Fields !== null && fieldsMetadata.Fields.length)
            {
                $.each(fieldsMetadata.Fields, function(fieldMetadataIndex, fieldMetadata)
                {
                    if (fieldMetadata.LookupFor !== null && fieldMetadata.LookupFor.length && fieldMetadata.LookupFor !== entity)
                    {
                        filterEntities.push(
                        {
                            Entity: fieldMetadata.LookupFor,
                            Field: fieldMetadata.Name,
                        });
                    }
                });
            }

            return filterEntities;
        };

        // Move the selected row from the available fields table to the visible fields table.
        var moveAvailableRow = function()
        {
            var $availableFieldsTableRow = $(this).parents().eq(1);
            var availableFieldRowPosition = $availableFieldsTableRow.position();

            var $rowOverlay = $("<div>",
            {
                class: "row-overlay",
                id: "rowOverlay",
                css:
                {
                    bottom: availableFieldRowPosition.bottom,
                    left: availableFieldRowPosition.left,
                    right: availableFieldRowPosition.right,
                    top: availableFieldRowPosition.top,
                },
            })
            .append($availableFieldsTableRow.clone());
            $rowOverlay.find("> tr > td").each(function(tableDataIndex, rowOverlayTableData)
            {
                var $rowOverlayTableData = $(rowOverlayTableData);
                var $availableFieldsTableData = $availableFieldsTableRow.find("> td:nth-child(" + (tableDataIndex + 1) + ")");
                $rowOverlayTableData.width($availableFieldsTableData.outerWidth());
            });
            $rowOverlay.find("> tr").height($availableFieldsTableRow.outerHeight());
            $availableFieldsTable.append($rowOverlay);

            $availableFieldsTableRow.css("visibility", "hidden");
            $rowOverlay.hide("slide", { direction: "right" }, "slow", function()
            {
                $rowOverlay.remove();

                availableFieldsDataTable.fnDeleteRow($availableFieldsTableRow[0]);

                var $visibleFieldDisplayControl = null;
                switch ($displaySelect.find("option:selected").attr("value"))
                {
                    case "Detail":
                    {
                        $visibleFieldDisplayControl = $editInput.clone();
                        if ($availableFieldsTableRow.find("> td:nth-child(2)").text().indexOf("Immutable") > -1)
                        {
                            $visibleFieldDisplayControl.prop("disabled", true);
                            $visibleFieldDisplayControl.addClass("ui-state-disabled");
                        }

                        break;
                    }
                    case "List":
                    {
                        $visibleFieldDisplayControl = $("<span>",
                        {
                            class: "sort-control",
                        });

                        break;
                    }
                }

                visibleFieldsDataTable.fnAddData([ visibleFieldsDataTable.fnSettings().fnRecordsTotal() + 1,
                                                   $rowButton.clone()[0].outerHTML,
                                                   $availableFieldsTableRow.find("> td:nth-child(1)").text(),
                                                   $availableFieldsTableRow.find("> td:nth-child(2)").text(),
                                                   $visibleFieldDisplayControl[0].outerHTML ]);

                var $visibleFieldsTableRow = $visibleFieldsTable.find("> tbody > tr:last");
                $visibleFieldsTableRow.attr("data-metadata-name", $availableFieldsTableRow.attr("data-metadata-name"));

                var $visibleFieldsTableRowButton = $visibleFieldsTableRow.find("> td > button");
                $visibleFieldsTableRowButton.button(
                {
                    icons: { primary: "ui-icon-minus", },
                    text: false,
                });
                $visibleFieldsTableRowButton.on("click", moveVisibleRow);

                switch ($displaySelect.find("option:selected").attr("value"))
                {
                    case "Detail":
                    {
                        $visibleFieldsTableRow.find("> td:nth-child(4) > input.edit-control").on("change", savePreferences);
                        break;
                    }
                    case "List":
                    {
                        generateSortControl($visibleFieldsTableRow.find("> td > span.sort-control"));
                        break;
                    }
                }

                savePreferences();
            });
        };

        // Move the selected row from the visible fields table to the available fields table.
        var moveVisibleRow = function()
        {
            var $visibleFieldsTableRow = $(this).parents().eq(1);
            var visibleFieldRowPosition = $visibleFieldsTableRow.position();

            var $rowOverlay = $("<div>",
            {
                class: "row-overlay",
                id: "rowOverlay",
                css:
                {
                    bottom: visibleFieldRowPosition.bottom,
                    left: visibleFieldRowPosition.left,
                    right: visibleFieldRowPosition.right,
                    top: visibleFieldRowPosition.top,
                },
            })
            .append($visibleFieldsTableRow.clone());
            $rowOverlay.find("> tr > td").each(function(tableDataIndex, rowOverlayTableData)
            {
                var $rowOverlayTableData = $(rowOverlayTableData);
                var $visibleFieldsTableData = $visibleFieldsTableRow.find("> td:nth-child(" + (tableDataIndex + 1) + ")");
                $rowOverlayTableData.width($visibleFieldsTableData.outerWidth());
            });
            $rowOverlay.find("> tr").height($visibleFieldsTableRow.outerHeight());
            $visibleFieldsTable.append($rowOverlay);

            $visibleFieldsTableRow.css("visibility", "hidden");
            $rowOverlay.hide("slide", { direction: "left" }, "slow", function()
            {
                $rowOverlay.remove();

                visibleFieldsDataTable.fnDeleteRow($visibleFieldsTableRow[0]);

                var localizedFieldName = $visibleFieldsTableRow.find("> td:nth-child(2)").text();
                availableFieldsDataTable.fnAddData([ localizedFieldName,
                                                     $visibleFieldsTableRow.find("> td:nth-child(3)").text(),
                                                     $rowButton.clone()[0].outerHTML ]);

                var $availableFieldsTableRow = null;
                $availableFieldsTable.find("> tbody > tr").each(function()
                {
                    var availableFieldProcessed = false;

                    $availableFieldsTableRow = $(this);
                    if (localizedFieldName === $availableFieldsTableRow.find("> td:nth-child(1)").text())
                    {
                        $availableFieldsTableRow.attr("data-metadata-name", $visibleFieldsTableRow.attr("data-metadata-name"));

                        var $availableFieldsTableRowButton = $availableFieldsTableRow.find("> td > button");
                        $availableFieldsTableRowButton.button(
                        {
                            icons: { primary: "ui-icon-plus", },
                            text: false,
                        });
                        $availableFieldsTableRowButton.on("click", moveAvailableRow);

                        availableFieldProcessed = true;
                    }

                    return !availableFieldProcessed;
                });
                availableFieldsDataTable.fnSort([ [0, "asc" ] ]);

                savePreferences();
            });
        };

        // Save the current preferences with asynchronous AJAX.
        var savePreferences = function()
        {
            clearTimeout(saveTimer);
            saveTimer = setTimeout(function()
            {
                $overlayDiv.show();
                overlaySpinner.spin($overlayDiv[0]);
                $overlayDiv.delay(1000);

                var $fields = $("<fields>");
                $visibleFieldsTable.find("> tbody > tr").each(function(visibleFieldsTableRowIndex, visibleFieldsTableRow)
                {
                    var $visibleFieldsTableRow = $(visibleFieldsTableRow);

                    var $field = $("<field>");
                    $field.append($("<name>").text($visibleFieldsTableRow.attr("data-metadata-name")));
                    switch ($displaySelect.find("option:selected").attr("value"))
                    {
                        case "Detail":
                        {
                            $field.append($("<edit>").text("false"));
                            //$field.append($("<edit>").text($visibleFieldsTableRow.find("> td:nth-child(4) > input").is(":checked")));
                            break;
                        }
                        case "List":
                        {
                            var $sortControl = $visibleFieldsTableRow.find("> td > span.sort-control");
                            if ($sortControl.find("> input.sort-control-enabled").is(":checked"))
                            {
                                $field.append($("<sort>").text($sortControl.find("> span.sort-control-direction > input").val() === "▲" ? "asc" : "desc"));
                            }
                            break;
                        }
                    }
                    $fields.append($field);
                });
                $hiddenFieldsInput.attr("value", $fields[0].outerHTML);

                $.ajax(
                {
                    data: $form.serialize(),
                    type: $form.prop("method"),
                    url: $form.prop("action"),
                })
                .always(function()
                {
                    $overlayDiv.hide(0);
                });
            }, saveDelay);
        };

        return context;
    })(window[context] || {});
};