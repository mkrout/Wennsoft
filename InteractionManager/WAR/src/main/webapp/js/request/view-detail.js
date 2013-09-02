var defineRequestViewDetail = function(context)
{
    window[context] = (function(context)
    {
        var portalUri;
        var webServiceUri;

        var $portletWindowDiv;
        var $overlayDiv;
        var overlaySpinner;
        var $form;
        var $detailContentDiv;
        var $hiddenFieldsMetadataDiv;
        var fieldSpinner;
        var $hiddenSaveInput;
        var $saveButton;
        var errorData;
        var errorMessage;

        var entityId;
        var asyncFieldLoadCount;

        context.initialize = function(renderParameters)
        {
            try
            {
                // IE9 AJAX fix.
                $.support.cors = true;

                Globalize.culture(renderParameters.ExoLocale);
                
                requestContextPath = renderParameters.RequestContextPath;

                portalUri = renderParameters.PortalUri;
                webServiceUri = renderParameters.WebServiceUri;

                $portletWindowDiv = $("div#" + renderParameters.WindowId);
                $overlayDiv = $portletWindowDiv.find("div#overlay");
                overlaySpinner = new Spinner(
                {
                    color: "#000",
                });
                $form = $portletWindowDiv.find("form");
                $detailContentDiv = $portletWindowDiv.find("div#detailContent");
                $hiddenFieldsMetadataDiv = $portletWindowDiv.find("div#hiddenFieldsMetadata");
                fieldSpinner = new Spinner(
                {
                    length: 5,
                    width: 2,
                    radius: 3,
                    color: "#000",
                });
                $hiddenSaveInput = $portletWindowDiv.find("input#save");
                $saveButton = $portletWindowDiv.find("div#actionButtons button#saveButton");
                errorData = renderParameters.ErrorDataJson ? JSON.parse(renderParameters.ErrorDataJson) : null;
                errorMessage = renderParameters.ErrorMessageJson ? JSON.parse(renderParameters.ErrorMessageJson) : null;

                entityId = getUrlParameter(renderParameters.Entity);
                asyncFieldLoadCount = 1;

                var title = Globalize.localize(renderParameters.Entity);
                $portletWindowDiv.find("div.im-view-title").html(title);
                $detailContentDiv.find("> div.detail-column > span.im-detail-row").each(function(detailCellIndex, detailCell)
                {
                    $(detailCell).find("> label:first").localize();
                });
                $saveButton.localize();

                if (true) //(entityId === "Create")
                {
                    try
                    {
                        $overlayDiv.show();
                        overlaySpinner.spin($overlayDiv[0]);
                        $overlayDiv.delay(1000);

                        $detailContentDiv.find("> div.detail-column > span.im-detail-row").each(function(detailCellIndex, detailCell)
                        {
                            var $detailCellSpan = $(detailCell);

                            var value = $detailCellSpan.attr("data-metadata-default");
                            console.log($detailCellSpan.attr("data-metadata-default"));
                            if (errorData !== null)
                            {
                                value = errorData[$detailCellSpan.attr("data-metadata-name")];
                            }

                            var error = false;
                            if (errorMessage)
                            {
                                for (var errorMessageField in errorMessage.Message)
                                {
                                    if (errorMessageField.indexOf($detailCellSpan.attr("data-metadata-name")) > -1)
                                    {
                                        $detailCellSpan.addClass("ui-state-error");
                                        break;
                                    }
                                }
                            }

                            createDetailCellValueControl($detailCellSpan,null, value);
                        });

                        asyncFieldLoadCount--;
                        enableActionButtons();
                    }
                    finally
                    {
                        $overlayDiv.hide(0);
                    }
                }
                else if (entityId !== null)
                {
                    var fieldNames = new Array();
                    $detailContentDiv.find("> div.detail-column > span.im-detail-cell").each(function(detailCellIndex, detailCell)
                    {
                        fieldNames.push($(detailCell).attr("data-metadata-name"));
                    });
                    $hiddenFieldsMetadataDiv.find(" > span.im-detail-cell-hidden").each(function(detailCellHiddenIndex, detailCellHidden)
                    {
                        fieldNames.push($(detailCellHidden).attr("data-metadata-name"));
                    });

                    $.ajax(
                    {
                        beforeSend: function()
                        {
                            $overlayDiv.show();
                            overlaySpinner.spin($overlayDiv[0]);
                            $overlayDiv.delay(1000);
                        },
                        cache: false,
                        data:
                        {
                            fields: fieldNames.join(","),
                        },
                        dataType: "json",
                        url: requestContextPath + "/service/bridge/customer-connect/" + renderParameters.Controller + "/" + entityId,
                    })
                    .fail(function()
                    {

                    })
                    .done(function(entityData)
                    {
                        for (var fieldName in entityData)
                        {
                            var fieldValue = entityData[fieldName];
                            if (errorData !== null && typeof errorData[fieldName] !== "undefined")
                            {
                                fieldValue = errorData[fieldName];
                            }

                            var $detailCellSpan = $detailContentDiv.find("> div.detail-column > span.im-detail-cell[data-metadata-name='" + fieldName + "']");
                            if ($detailCellSpan.length)
                            {
                                var error = false;
                                if (errorMessage)
                                {
                                    for (var errorMessageField in errorMessage.Message)
                                    {
                                        if (errorMessageField.indexOf(fieldName) > -1)
                                        {
                                            $detailCellSpan.addClass("ui-state-error");
                                            break;
                                        }
                                    }
                                }

                                createDetailCellValueControl($detailCellSpan, entityData, fieldValue);
                            }
                            else
                            {
                                var $detailCellHiddenSpan = $hiddenFieldsMetadataDiv.find("> span.im-detail-cell-hidden[data-metadata-name='" + fieldName + "']");
                                $detailCellHiddenSpan.attr("data-value", fieldValue);
                            }
                        }
                    })
                    .always(function()
                    {
                        $overlayDiv.hide(0);
                        asyncFieldLoadCount--;
                        enableActionButtons();
                    });
                }

                $saveButton.button(
                {
                    disabled: true,
                });

                $form.on("submit", preSubmit);
            }
            catch (exception)
            {
                throw exception;
            }
        };

        var createDetailCellValueControl = function($detailCellSpan, entityData, value)
        {
        	if ($detailCellSpan.attr("data-metadata-input-type") === "hidden")
        	{
        		$detailCellSpan.hide();
        		var $fieldInput = $("<input>",
                        {
                            id: $detailCellSpan.attr("data-metadata-name"),
                            name: $detailCellSpan.attr("data-metadata-name"),
                            type: "hidden",
                            value: value,
                        });
        		
        		$detailCellSpan.append($fieldInput);
        	}
        	else if ($detailCellSpan.attr("data-metadata-input-type") === "locked")
        	{
        		var $fieldInput = $("<input>",
                        {
                            id: $detailCellSpan.attr("data-metadata-name"),
                            name: $detailCellSpan.attr("data-metadata-name"),
                            type: "hidden",
                            value: value,
                        });
        		$detailCellSpan.append($fieldInput);
        	}
            if ($detailCellSpan.attr("data-metadata-basetype") === "DateTime" && value.length>0)
            {
            	
            	
            	var timeObject = null;
            	var timevals = new Array();
            	if (value==="Today" || value==="CurrentTime")
            	{
            	    timeObject =  new Date();	
            	    timevals[0] = timeObject.getYear();
            	    timevals[1] = timeObject.getMonth();
            	    timevals[2] = timeObject.getDay();
            	    timevals[3] = timeObject.getHours();
            	    timevals[4] = timeObject.getMinutes();
            	    timevals[5] = timeObject.getSeconds();
            	}
            	else if (value !== "None")
            	{
            		timevals = value.match(/\d+/g);
            		timeObject = new Date(timevals[0],timevals[1],timevals[2],timevals[3],timevals[4],timevals[5]);
            	}
            	
            	if (value != "None")
            	{
            		if ($detailCellSpan.attr("data-metadata-types").indexOf("Date") > -1)
                	{
                    	if (timevals[0]==1900 && timevals[1] == 1 && timevals[2] == 1)
                    	{timeformat = Globalize.localize("None");}
                    	else
                    	{timeformat = Globalize.culture().calendar.patterns.d;}
                	}
                	else if ($detailCellSpan.attr("data-metadata-types").indexOf("Time") > -1)
                	{
                		if (timevals[3]==0 && timevals[4] == 0 && timevals[5] == 0)
                		{timeformat = Globalize.localize("None");}
                		else
                		{timeformat = Globalize.culture().calendar.patterns.t;}
                	}
                	else
                	{
                    	if (timevals[0]==1900 && timevals[1] == 1 && timevals[2] == 1 && timevals[3] == 0
                        		&& timevals[4]==0 && timevals[5] == 0)
                        	{timeformat = Globalize.localize("None");}
                        	else
                        	{timeformat = Globalize.culture().calendar.patterns.d + ' '
                        	+ Globalize.culture().calendar.patterns.t;}
                	}
                
                	value = Globalize.format(timeObject,timeformat);
            	}
            	else
            	{
            		value = "";
            	}
            }

            if ($detailCellSpan.attr("data-metadata-basetype") === "Byte")
            {
                var $fieldInput = $("<input>",
                {
                    id: $detailCellSpan.attr("data-metadata-name"),
                    name: $detailCellSpan.attr("data-metadata-name"),
                    type: "checkbox",
                });
                $fieldInput.prop("checked", value);
                $fieldInput.prop("disabled", $detailCellSpan.attr("data-preference-edit") !== "true" &&
                                             $detailCellSpan.attr("data-metadata-types").indexOf("Immutable") === -1);
                $fieldInput.prop("required", ($detailCellSpan.attr("data-metadata-required") === "true"));
                $detailCellSpan.append($fieldInput);
            }
            else if ($detailCellSpan.attr("data-metadata-input-type") === "userEntry" &&
                     $detailCellSpan.attr("data-metadata-types").indexOf("Immutable") === -1)
            {
                switch ($detailCellSpan.attr("data-metadata-basetype"))
                {
                    case "DateTime":
                    {
                        if ($detailCellSpan.attr("data-metadata-types").indexOf("Date") > -1)
                        {
                            var $fieldInput = $("<input>",
                            {
                                class: "ui-widget ui-widget-content ui-corner-all",
                                id: $detailCellSpan.attr("data-metadata-name"),
                                name: $detailCellSpan.attr("data-metadata-name"),
                                value: value,
                            });
                            $fieldInput.prop("required", ($detailCellSpan.attr("data-metadata-required") === "true"));
                            $detailCellSpan.append($("<span>", { class: "ui-datetimepicker ui-corner-all ui-widget-content" }).append($fieldInput));

                            $fieldInput.datetimePickerCustom(
                            {
                                pickertype: "date",
                            })
                            .next("button").text("").button(
                            {
                                text: false,
                                icons:
                                {
                                    primary: "ui-icon-calendar",
                                },
                            })
                            .removeClass("ui-corner-all").addClass("ui-corner-right");

                        }
                        else if ($detailCellSpan.attr("data-metadata-types").indexOf("Time") > -1)
                        {
                            var $fieldInput = $("<input>",
                            {
                                class: "ui-widget ui-widget-content ui-corner-all",
                                id: $detailCellSpan.attr("data-metadata-name"),
                                name: $detailCellSpan.attr("data-metadata-name"),
                                value: value,
                            });
                            $fieldInput.prop("required", ($detailCellSpan.attr("data-metadata-required") === "true"));
                            $detailCellSpan.append($("<span>", { class: "ui-datetimepicker ui-corner-all ui-widget-content" }).append($fieldInput));
                            $fieldInput.datetimePickerCustom(
                            {
                                pickertype: "time",
                            })
                            .next("button").text("").button(
                            {
                                text: false,
                                icons:
                                {
                                    primary: "ui-icon-clock",
                                },
                            })
                            .removeClass("ui-corner-all").addClass("ui-corner-right");
                        }
                        else
                        {
                            var $fieldInput = $("<input>",
                            {
                                class: "ui-widget ui-widget-content ui-corner-all",
                                id: $detailCellSpan.attr("data-metadata-name"),
                                name: $detailCellSpan.attr("data-metadata-name"),
                                value: value,
                            });
                            $fieldInput.prop("required", ($detailCellSpan.attr("data-metadata-required") === "true"));
                            $detailCellSpan.append($("<span>", { class: "ui-datetimepicker ui-corner-all ui-widget-content" }).append($fieldInput));
                            $fieldInput.datetimePickerCustom(
                            {
                                pickertype : "datetime",
                            })
                            .next("button").text("").button(
                            {
                                text: false,
                                icons:
                                {
                                    primary: "ui-icon-calendar",
                                },
                            })
                            .removeClass("ui-corner-all").addClass("ui-corner-right");
                        }

                        break;
                    }
                    case "Decimal":
                    {
                        // TODO
                        $detailCellSpan.append(value);

                        break;
                    }
                    case "Email":
                    {
                        var $fieldInput = $("<input>",
                        {
                            class: "ui-widget ui-widget-content ui-corner-all",
                            id: $detailCellSpan.attr("data-metadata-name"),
                            name: $detailCellSpan.attr("data-metadata-name"),
                            type: "email",
                            value: value,
                        });
                        $fieldInput.prop("required", ($detailCellSpan.attr("data-metadata-required") === "true"));
                        $detailCellSpan.append($fieldInput);

                        break;
                    }
                    case "Int16":
                    case "Int32":
                    {
                        var $fieldInput = $("<input>",
                        {
                            id: $detailCellSpan.attr("data-metadata-name"),
                            name: $detailCellSpan.attr("data-metadata-name"),
                            value: value,
                        });
                        $fieldInput.prop("required", ($detailCellSpan.attr("data-metadata-required") === "true"));
                        $detailCellSpan.append($fieldInput);
                        $fieldInput.spinnerCustom();

                        break;
                    }
                    case "String":
                    {
                        if ($detailCellSpan.attr("data-metadata-lookup-for").length)
                        {
                            asyncFieldLoadCount++;

                            var $fieldSelect = $("<select>",
                            {
                                id: $detailCellSpan.attr("data-metadata-name"),
                                name: $detailCellSpan.attr("data-metadata-name"),
                            });
                            $detailCellSpan.append($fieldSelect);
                            $fieldSelect.combobox(
                            {
                                disabled: true,
                                required: ($detailCellSpan.attr("data-metadata-required") === "true"),
                            });

                            $.ajax(
                            {
                                beforeSend: function()
                                {
                                    fieldSpinner.spin($fieldSelect.next("span.ui-combobox")[0]);
                                },
                                cache: false,
                                data:
                                {
                                    fields: $detailCellSpan.attr("data-metadata-name"),
                                },
                                dataType: "json",
                                url: requestContextPath + "/service/bridge/customer-connect/" + $detailCellSpan.attr("data-metadata-controller"),
                            })
                            .fail(function()
                            {

                            })
                            .done(function(entityPrimaryKeys)
                            {
                            	console.log('ajax done');
                                $.each(entityPrimaryKeys.aaData, function(entityPrimaryKeyIndex, entityPrimaryKey)
                                {
                                    if (entityPrimaryKey[0] !== "")
                                    {
                                        $fieldSelect.append($("<option>",
                                        {
                                            value: entityPrimaryKey[0],
                                            text: entityPrimaryKey[0],
                                        }));
                                    }
                                });
                                $fieldSelect.combobox("select", value);
                                $fieldSelect.combobox("enable");
                            })
                            .always(function()
                            {
                                fieldSpinner.spin(false);
                                asyncFieldLoadCount--;
                                enableActionButtons();
                            });
                        }
                        else
                        {
                            var $fieldInput = $("<input>",
                            {
                                class: "ui-corner-all ui-widget ui-widget-content",
                                id: $detailCellSpan.attr("data-metadata-name"),
                                name: $detailCellSpan.attr("data-metadata-name"),
                                value: value,
                            });
                            $fieldInput.prop("required", ($detailCellSpan.attr("data-metadata-required") === "true"));
                            $detailCellSpan.append($fieldInput);
                        }

                        break;
                    }
                    default:
                    {
                        var $fieldInput = $("<input>",
                        {
                            class: "ui-corner-all ui-widget ui-widget-content",
                            id: $detailCellSpan.attr("data-metadata-name"),
                            name: $detailCellSpan.attr("data-metadata-name"),
                            value: value,
                        });
                        $fieldInput.prop("required", ($detailCellSpan.attr("data-metadata-required") === "true"));
                        $detailCellSpan.append($fieldInput);

                        break;
                    }
                }
            }
            else
            {
                var targetPage = $detailCellSpan.attr("data-setting-detail");
                if ($detailCellSpan.attr("data-metadata-types").indexOf("ForeignKey") > -1 && targetPage)
                {
                    var entity = $detailCellSpan.attr("data-metadata-entity");

                    var entityKeys = new Array();
                    $detailContentDiv.find("span[data-metadata-" + entity.toLowerCase() + "]").sort(function(detailCell1, detailCell2)
                    {
                        return $(detailCell1).attr("data-metadata-" + entity.toLowerCase()) > $(detailCell2).attr("data-metadata-" + entity.toLowerCase()) ? 1 : -1;
                    })
                    .each(function(entityKeyDetailCellIndex, entityKeyDetailCell)
                    {
                        entityKeys.push(encodeURIComponent(entityData[$(entityKeyDetailCell).attr("data-metadata-name")]));
                    });

                    var $anchorLink = $("<a>",
                    {
                        class: "im-link",
                        href: portalUri + targetPage + "?" + $detailCellSpan.attr("data-metadata-entity") + "=" + entityKeys.join("/"),
                    })
                    .append(value);
                    $detailCellSpan.append($("<span>").append($anchorLink));
                }
                else
                {
                    $detailCellSpan.append($("<label>").append(value));
                }
            }
        };

        var enableActionButtons = function()
        {
            if (asyncFieldLoadCount === 0)
            {
                $saveButton.button("enable");
            }
        };

        var preSubmit = function()
        {
            $detailContentDiv.find("> div.detail-column > span.im-detail-cell").each(function(detailCellIndex, detailCell)
            {
                var $detailCellSpan = $(detailCell);
                if ($detailCellSpan.attr("data-metadata-basetype") === "DateTime" &&
                    $detailCellSpan.attr("data-metadata-edit") === "true")
                {
                    var $detailCellDateTimeInput = $detailCellSpan.find("input");
                    if ($detailCellSpan.attr("data-metadata-types").indexOf("Date") > -1)
                    {
                        $detailCellDateTimeInput.val($detailCellDateTimeInput.val() + "T00:00:00");
                    }
                    if ($detailCellSpan.attr("data-metadata-types").indexOf("Time") > -1)
                    {
                        $detailCellDateTimeInput.val("1900-01-01T" + $detailCellDateTimeInput.val());
                    }
                }
            });

            return true;
        };

        var getUrlParameter = function(name)
        {
            var urlParameter = decodeURI((RegExp(name + "=" + "(.+?)(&|$)").exec(location.search) || [ ,null ])[1]);
            return urlParameter === "null" ? null : urlParameter;
        };

        return context;
    })(window[context] || {});
};