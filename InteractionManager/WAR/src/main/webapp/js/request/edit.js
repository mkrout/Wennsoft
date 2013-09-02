var defineRequestEdit = function(context)
{
    window[context] = (function(context)
    {
        var initialEntityPreference;
        var initialColumnsPreference;
        var $initialFieldsPreferences;
        var webServiceUri;

        var $portletWindowDiv;
        var $overlayDiv;
        var overlaySpinner;
        var $form;
        var $accordion;
        var $displayPreferencesDiv;
        var $entitySelect;
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

        var saveTimer;
        var saveDelay;
        
        context.initialize = function(renderParameters)
        {
            try
            {
                // IE9 AJAX fix.
                $.support.cors = true;
                
                requestContextPath = renderParameters.RequestContextPath;

                Globalize.culture(renderParameters.ExoLocale);

                initialEntityPreference = renderParameters.Entity;
                initialColumnsPreference = renderParameters.Columns;
                if (initialColumnsPreference===null) {initialColumnsPreference=1;}
                $initialFieldsPreferences = null;
                if (renderParameters.Fields)
                {
                    try
                    {
                        $initialFieldsPreferences = $($.parseXML(renderParameters.Fields));
                    }
                    catch (exception) { }
                }
                webServiceUri = renderParameters.WebServiceUri;

                $portletWindowDiv = $("div#" + renderParameters.WindowId);
                $overlayDiv = $portletWindowDiv.find("div#overlay");
                overlaySpinner = new Spinner(
                {
                    color: "#000",
                });
                $form = $portletWindowDiv.find("form");
                $accordion = $portletWindowDiv.find("div#accordion");
                $displayPreferencesDiv = $portletWindowDiv.find("div#displayPreferences");
                $entitySelect = $portletWindowDiv.find("select#entity");
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
                $inputSelect = $("<select>",{
                	class: "input-control ui-widget-content",
                });
                $defaultInput = $("<input>",{
                	name: "field",
                	type: "text",
                	class: "default-control ui-widget-content",
                });

                saveTimer = null;
                saveDelay = 750;

                // Prevent errors when the page editor is refreshed with the portlet edit modal open.
                if (!$.fn.DataTable.fnIsDataTable($visibleFieldsTable[0]))
                {
                    // Disable scrolling for the portlet edit modal to prevent UI animation issues.
                    var uiTabContentDiv = $("div.uiTabContent");
                    if (uiTabContentDiv.length)
                    {
                        uiTabContentDiv.css("overflow", "hidden");
                    }

                    $fieldPreferencesHeader.localize();
                    $accordion.find("h3#displayPreferencesHeader").localize();
                    $displayPreferencesDiv.find("> div.im-detail-cell > label").each(function(entityPreferenceLabelIndex, entityPreferenceLabel)
                    {
                        $(entityPreferenceLabel).localize();
                    });
                    $entitySelect.find("> option").each(function(entityOptionIndex, entityOption)
                    {
                        $(entityOption).localize();
                    });

                    $availableFieldsTable.find("> thead > tr > th").each(function(availableFieldsTableHeaderIndex, availableFieldsTableHeader)
                    {
                        $(availableFieldsTableHeader).localize();
                    });
                    $visibleFieldsTable.find("> thead > tr > th").each(function(visibleFieldsTableHeaderIndex, visibleFieldsTableHeader)
                    {
                        $(visibleFieldsTableHeader).localize();
                    });

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
                    .combobox("alphaSort")
                    .combobox("select",initialEntityPreference);

                    $displayPreferencesDiv.find("input#columns").spinnerCustom(
                    {
                        min: 1,
                        max: 5,
                        stop: savePreferences,
                    })
                    .spinner("value",initialColumnsPreference);

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
                        sDom: "<'H'F<'im-view-title'>>tS<'F'>",
                        sScrollY: "141px",
                    });
                    $availableFieldsTableWrapperDiv = $portletWindowDiv.find("div#availableFields_wrapper");
                    $availableFieldsTableWrapperDiv.find("div.im-view-title").localize("Available");
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
                            { bSortable: false, },
                            { bSortable: false, },
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
                        sDom: "<'H'F<'im-view-title'>>tS<'F'>",
                        sScrollY: "141px",
                    })
                    .rowReordering(
                    {
                        "callback": savePreferences,
                    });
                    $visibleFieldsWrapperDiv = $portletWindowDiv.find("div#visibleFields_wrapper");
                    $visibleFieldsWrapperDiv.find("div.im-view-title").localize("Visible");

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

        // Load the available and visible field data for the current entity and display.
        var loadFields = function()
        {
        	console.log('loadFields');
            var $selectedEntityOption = $entitySelect.find("> option:selected");
            //var $selectedDisplayOption = $displaySelect.find("> option:selected");
            if ($selectedEntityOption.length)
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
                        	if (fieldMetadata.IsRequired)
                        	{
                        		visibleFieldsDataTable.fnAddData([ visibleFieldsDataTable.fnSettings().fnRecordsTotal() + 1,
                        		                                   $rowButton.clone()[0].outerHTML,
                        		                                   Globalize.localize(fieldMetadata.Name),
                        		                                   $fieldTypeSpan[0].outerHTML,
                        		                                   $inputSelect.clone()[0].outerHTML,
                        		                                   $defaultInput.clone()[0].outerHTML,
                        		                                   ]);
                        		var $currentTableRow = $visibleFieldsTable.find("> tbody > tr:last");
                        		$currentTableRow.find("> td:first").addClass("required-field");
                                var $availableFieldsTableRowButton = $currentTableRow.find("> td:first > button");
                                $availableFieldsTableRowButton.button(
                                {
                                    icons: { primary: "ui-icon-minus", },
                                    text: false,
                                    disabled: true,
                                }); 
                                
                        	}
                        	else
                        	{
                                availableFieldsDataTable.fnAddData([ Globalize.localize(fieldMetadata.Name),
                                                                     $fieldTypeSpan[0].outerHTML,
                                                                     $rowButton.clone()[0].outerHTML ]);
                                var $currentTableRow = $availableFieldsTable.find("> tbody > tr:last");
                                
                                var $availableFieldsTableRowButton = $currentTableRow.find("> td:last > button");
                                $availableFieldsTableRowButton.button(
                                {
                                    icons: { primary: "ui-icon-plus", },
                                    text: false,
                                });
                                $availableFieldsTableRowButton.on("click", moveAvailableRow);

                        	}
                        		
                        	$currentTableRow.attr("data-metadata-name", fieldMetadata.Name);
                        	$currentTableRow.attr("data-metadata-required", fieldMetadata.IsRequired);
                        	$currentTableRow.attr("data-metadata-type", fieldMetadata.BaseType);
                        	if (fieldMetadata.LookupFor !== null)
                        	{
                        		$currentTableRow.attr("data-metadata-foreign-key","true");
                        	}
                        	else
                        	{
                        		$currentTableRow.attr("data-metadata-foreign-key","false");
                        	}
                        	if (fieldMetadata.Types !== null)
                        	{
                        		fieldMetadata.Types.forEach( function (type,index) {
                        			$currentTableRow.attr("data-metadata-type"+index, type);
                        		});
                        	}
                        	
                        	if (fieldMetadata.IsRequired)
                        	{
                            	updateVisibleFieldsRow($currentTableRow);
                        	}
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
                            var fieldIsRequired = null; 
                            var fieldBaseType = null;
                            var fieldTypes = null;
                            var fieldIsForeignKey = null;
                            
                            $.each(fieldsMetadata.Fields, function(fieldMetadataIndex, fieldMetadata)
                            {
                                var fieldMetadataProcessed = false;

                                if ($initialFieldPreference.find("name").text() === fieldMetadata.Name)
                                {
                                    validPreference = true;
                                    $fieldTypeSpan = $("<span>").append(fieldMetadata.BaseType);
                                	fieldIsRequired = fieldMetadata.IsRequired;
                                	fieldBaseType = fieldMetadata.BaseType;
                                    fieldTypes = fieldMetadata.Types;
                                    if (fieldMetadata.LookupFor !== null) {fieldIsForeignKey = true;}
                                    else {fieldIsForeignKey = false;}
                                	
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
             
                                visibleFieldsDataTable.fnAddData([ initialFieldPreferenceIndex + 1,
                                                                   $rowButton.clone()[0].outerHTML,
                                                                   Globalize.localize($initialFieldPreference.find("name").text()),
                                                                   $fieldTypeSpan[0].outerHTML,
                                                                   $inputSelect.clone()[0].outerHTML,
                                                                   $defaultInput.clone()[0].outerHTML, ]);

                                var $visibleFieldsTableRow = $visibleFieldsTable.find("> tbody > tr:last");
                                $visibleFieldsTableRow.attr("data-metadata-name", $initialFieldPreference.find("name").text());
                            	$visibleFieldsTableRow.attr("data-metadata-required", fieldIsRequired);
                            	$visibleFieldsTableRow.attr("data-metadata-type", fieldBaseType);
                            	$visibleFieldsTableRow.attr("data-metadata-foreign-key",fieldIsForeignKey);

                            	if (fieldTypes !== null)
                            	{
                            		fieldTypes.forEach( function (type,index) {
                            			$visibleFieldsTableRow.attr("data-metadata-type"+index, type);
                            		});
                            	}
                                
                                var $visibleFieldsTableRowButton = $visibleFieldsTableRow.find("> td > button");
                                $visibleFieldsTableRowButton.button(
                                {
                                    icons: { primary: "ui-icon-minus", },
                                    text: false,
                                    disabled: fieldIsRequired,
                                });
                                
                                $visibleFieldsTableRowButton.on("click", moveVisibleRow);
                                
                                updateVisibleFieldsRow($visibleFieldsTableRow);
                                
                                console.log('input pref: '+$initialFieldPreference.find("input-type").text());
                                
                                $visibleFieldsTableRow.find("select.input-control")
                                	.combobox("select",$initialFieldPreference.find("input-type").text());
                              
                                $visibleFieldsTableRow.find("input.default-control")
                                	.val($initialFieldPreference.find("default").text());
                                
                                $visibleFieldsTableRow.find("select.default-control")
                                	.combobox("select",$initialFieldPreference.find("default").text());
                                
                                updateVisibleFieldsRow($visibleFieldsTableRow);

   
                            }
                        });
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
        
        var updateVisibleFieldsRow = function($row) 
        {
        	
        	$rowInputSelect = $row.find("select.input-control");
        	$rowDefaultInput = $row.find("input.default-control");
        	
        	if ($rowInputSelect.find("option").length === 0)
        	{
        		$rowInputSelect.append($("<option>",{
        			value : "userEntry"
        		})
        		.append("UserEntry"));
        	
        		if ($row.attr("data-metadata-foreign-key") === "false")
        		{
            		$rowInputSelect.append($("<option>",{
            			value : "locked"
            		}).
            		append("Locked"));
            		
            		$rowInputSelect.append($("<option>",{
        			    value : "hidden"
                    }).
                    append("Hidden"));
        		}
        		
        		$rowInputSelect.combobox({
        			selected: function()
                    {
        				$getRow = $(this).closest("tr");
        				updateVisibleFieldsRow($getRow);
        				savePreferences();
                    }
        		});
        		        		
        		$rowDefaultInput.on("blur",savePreferences);

        	}
        	
        	if ($row.attr("data-metadata-foreign-key") === "true")
        	{
        		$rowDefaultInput.prop('disabled',true).addClass('ui-disabled');
        	}
        	        	
        	
        	if ($rowInputSelect.val()==="locked" || $rowInputSelect.val()==="hidden")
        	{
        		console.log('hidden/locked');
        		if ($row.attr("data-metadata-type") === "String")
        		{
        			if (!$rowDefaultInput.val().length)
        			{
        				$rowDefaultInput.val("Default");
        			}
        		}
        		
        	}
        	
        	
        	if ($row.attr("data-metadata-type") === "DateTime")
        	{
       			console.log('dateTimedefaultSwitch');
       			
       			$rowDefaultSelect = $row.find("select.default-control");
       			
       			var timeDefaults = new Array();
        		
        		if ($row.attr("data-metadata-type0") === "Date")
       			{
       				timeDefaults.push('Today');
       			}
       			else if ($row.attr("data-metadata-type0") === "Time")
       			{
       				timeDefaults.push('CurrentTime');
       			}
        			
       			if ($rowInputSelect.val()==="userEntry") 
       			{  	
       				if ($rowDefaultSelect.find("option[value='None']").length === 0)
       				{
       					$rowDefaultSelect.append($("<option>",{
       						value: "None"
       					}).append("None"));
       				}
       				else
       				{
       					timeDefaults.push('None');
       				}
       			}
       			else
       			{
       				
       				if ($rowDefaultSelect.val()==="None")
       				{
       					console.log('None Selected');
       					$rowDefaultSelect.find("option[value='None']").remove();
       					$rowDefaultSelect.combobox("select",timeDefaults[0]);
       				}
       				else
       				{
       					$rowDefaultSelect.find("option[value='None']").remove();
       				}
       			}
       			

       			if ($rowDefaultSelect.length === 0)
       			{
       				$rowDefaultSelect = $('<select>',{
       					class: "default-control",
       				});
       			
       				$rowDefaultInput = $rowDefaultInput.replaceWith($rowDefaultSelect);
        		
       				timeDefaults.forEach(function (timeopt,index) {
           				$rowDefaultSelect.append($('<option>',{
           					value: timeopt,
           				}).append(timeopt));
       				});
        			
       				$rowDefaultSelect.combobox({
       					selected: function() {
       						savePreferences();
       					},
       				});
       			
        		}
       			
       		}
        	
        	
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
                
                $visibleFieldDefaultInput = $defaultInput.clone();

                visibleFieldsDataTable.fnAddData([ visibleFieldsDataTable.fnSettings().fnRecordsTotal() + 1,
                                                   $rowButton.clone()[0].outerHTML,
                                                   $availableFieldsTableRow.find("> td:nth-child(1)").text(),
                                                   $availableFieldsTableRow.find("> td:nth-child(2)").text(),
                                                   $inputSelect.clone()[0].outerHTML,
                                                   $visibleFieldDefaultInput[0].outerHTML,]);

                var $visibleFieldsTableRow = $visibleFieldsTable.find("> tbody > tr:last");
                $visibleFieldsTableRow.attr("data-metadata-name", $availableFieldsTableRow.attr("data-metadata-name"));
                $visibleFieldsTableRow.attr("data-metadata-type", $availableFieldsTableRow.attr("data-metadata-type"));
                $visibleFieldsTableRow.attr("data-metadata-required", $availableFieldsTableRow.attr("data-metadata-required"));
                $visibleFieldsTableRow.attr("data-metadata-type0", $availableFieldsTableRow.attr("data-metadata-type0"));
                $visibleFieldsTableRow.attr("data-metadata-type1", $availableFieldsTableRow.attr("data-metadata-type1"));
                $visibleFieldsTableRow.attr("data-metadata-foreign-key", $availableFieldsTableRow.attr("data-metadata-foreign-key"));
                updateVisibleFieldsRow($visibleFieldsTableRow);
                
                var $visibleFieldsTableRowButton = $visibleFieldsTableRow.find("> td > button");
                $visibleFieldsTableRowButton.button(
                {
                    icons: { primary: "ui-icon-minus", },
                    text: false,
                });
                $visibleFieldsTableRowButton.on("click", moveVisibleRow);

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
                        $availableFieldsTableRow.attr("data-metadata-required", $visibleFieldsTableRow.attr("data-metadata-required"));
                        $availableFieldsTableRow.attr("data-metadata-type", $visibleFieldsTableRow.attr("data-metadata-type"));
                        $availableFieldsTableRow.attr("data-metadata-type0", $visibleFieldsTableRow.attr("data-metadata-type0"));
                        $availableFieldsTableRow.attr("data-metadata-type1", $visibleFieldsTableRow.attr("data-metadata-type1"));
                        $availableFieldsTableRow.attr("data-metadata-foreign-key", $visibleFieldsTableRow.attr("data-metadata-foreign-key"));

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
                    $field.append($("<input-type>").text($visibleFieldsTableRow.find("select.input-control").val()));
                    $field.append($("<default>").text($visibleFieldsTableRow.find(".default-control").val()));

                    $fields.append($field);
                });
                $hiddenFieldsInput.attr("value", $fields[0].outerHTML);

                $.ajax(
                {
                    data: $form.serialize(),
                    type: $form.prop("method"),
                    url: $form.prop("action"),
                    error: function(jqXHR, textStatus, errorThrown)
                    {
                        alert("ErrorSavingPreferences");
                    },
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