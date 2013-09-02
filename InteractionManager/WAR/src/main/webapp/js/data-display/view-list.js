var defineCustomerDataDisplayViewList = function(context)
{
    window[context] = (function(context)
    {
        var $portletWindowDiv;
        var $overlayDiv;
        var overlaySpinner;
        var $table;
        var dataTable;
        var $tableWrapperDiv;

        var filterParameter;
        var entityKeyValues;

        context.initialize = function(renderParameters)
        {
            try
            {
                Globalize.culture(renderParameters.ExoLocale);

                $portletWindowDiv = $("div#" + renderParameters.WindowId);
                $overlayDiv = $portletWindowDiv.find("div#overlay");
                overlaySpinner = new Spinner(
                {
                    color: "#000",
                });
                $table = $portletWindowDiv.find("table#" + renderParameters.Entity);

                filterParameter = null;
                if (renderParameters.Filter && renderParameters.FilterEntity)
                {
                    filterParameter = getUrlParameter(renderParameters.FilterEntity);
                }

                var defaultSortingPreferences = new Array();
                var fieldNames = new Array();
                $table.find("> thead > tr > th").each(function(tableHeadIndex, tableHead)
                {
                    var $tableHead = $(tableHead);

                    if ($tableHead.attr("data-preference-sort"))
                    {
                        defaultSortingPreferences.push([ tableHeadIndex, $tableHead.attr("data-preference-sort") ]);
                    }

                    fieldNames.push($tableHead.attr("data-metadata-name"));

                    $tableHead.localize();
                });
                if (!defaultSortingPreferences.length)
                {
                    defaultSortingPreferences.push([ 0, "asc" ]);
                }

                dataTable = $table.dataTable(
                {
                    aaSorting: defaultSortingPreferences,
                    bDeferRender: true,
                    bJQueryUI: true,
                    bLengthChange: false,
                    bProcessing: false,
                    bServerSide: true,
                    bStateSave: false,
                    iDisplayLength: renderParameters.PageSize ? parseInt(renderParameters.PageSize) : 10,
                    oTableTools:
                    {
                        sSwfPath: renderParameters.RequestContextPath + "/swf/copy_csv_xls_pdf.swf",
                        aButtons: [ "csv", "pdf" ],
                    },
                    sAjaxSource: renderParameters.RequestContextPath + "/service/bridge/customer-connect/" + renderParameters.Controller,
                    sDom: "<'H'<'im-view-title'>" + (renderParameters.ExportControl === "true" ? "T" : "") + ">t<'F'ip>",
                    sPaginationType: "full_numbers",
                    fnDrawCallback: function()
                    {
                        var $tablePaginationButtonsDiv = $tableWrapperDiv.find("div#" + renderParameters.Entity + "_paginate");
                        var $paginationDiv = $tableWrapperDiv.find("div#" + renderParameters.Entity + "_info");
                        var paginationArguments = $paginationDiv.text().match(/(\d+)/g);
                        if (paginationArguments.length === 3 &&
                            (paginationArguments[0] === "0" || (paginationArguments[0] === "1" && paginationArguments[1] === paginationArguments[2])))
                        {
                            $paginationDiv.hide();
                            $tablePaginationButtonsDiv.hide();
                        }
                        else
                        {
                            $paginationDiv.localize("Pagination", paginationArguments);
                            $tablePaginationButtonsDiv.find("a#" + renderParameters.Entity + "_first").localize();
                            $tablePaginationButtonsDiv.find("a#" + renderParameters.Entity + "_previous").localize();
                            $tablePaginationButtonsDiv.find("a#" + renderParameters.Entity + "_next").localize();
                            $tablePaginationButtonsDiv.find("a#" + renderParameters.Entity + "_last").localize();
                        }

                        $table.find("> tbody > tr > td.dataTables_empty").localize("NoRecords");
                    },
                    fnRowCallback: function(tableRow, tableRowData, tableRowIndex)
                    {
                        var $tableRow = $(tableRow);
                        $table.find("> thead > tr > th").each(function(tableHeadIndex, tableHead)
                        {
                            var $tableHead = $(tableHead);
                            var $tableData = $tableRow.find("> td:nth-child(" + (tableHeadIndex + 1) + ")");

                            var targetEntity = $tableHead.attr("data-metadata-entity");
                            if (targetEntity && $tableData.html().length)
                            {
                                var keyValue = entityKeyValues[tableRowIndex].Keys[$tableHead.attr("data-metadata-name")];
                                var targetPage = $tableHead.attr("data-setting-detail");
                                if (typeof(keyValue) !== "undefined" && targetPage)
                                {
                                    var $anchorLink = $("<a>",
                                    {
                                        class: "im-link",
                                        href: renderParameters.PortalUri + targetPage + "?" + targetEntity + "=" + keyValue.Value.join("/"),
                                    });
                                    if ($tableHead.attr("data-metadata-basetype") === "DateTime")
                                    {
                                        $anchorLink.append(formatDateTime($tableData.html(), $tableHead.attr("data-metadata-types")));
                                    }
                                    else
                                    {
                                        $anchorLink.append($tableData.html());
                                    }

                                    $tableData.html($("<span>").append($anchorLink)[0].outerHTML);
                                }
                            }
                            else if ($tableHead.attr("data-metadata-basetype") === "DateTime")
                            {
                                $tableData.html(formatDateTime($tableData.html(), $tableHead.attr("data-metadata-types")));
                            }
                        });

                        return $tableRow[0];
                    },
                    fnServerParams: function(data)
                    {
                        data.push({ name: "Fields", value: fieldNames.join(","), });

                        if (filterParameter !== null)
                        {
//                            var filters = { op: "&&", conds: [], };
//                            data.push(
//                            {
//                                name: "Filters",
//                                value: JSON.stringify(filters),
//                            });
                        }

                        if (renderParameters.HistoryLimit > -1)
                        {
                            data.push({ name: "history", value: renderParameters.HistoryLimit, });
                        }
                    },
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
                        .done(function(tableData)
                        {
                            entityKeyValues = new Array();
                            if (tableData.aaData !== null)
                            {
                                $.each(tableData.aaData, function(entityDataIndex, entityData)
                                {
                                    entityKeyValues.push(entityData.pop());
                                });
                            }
                            else
                            {
                                tableData.aaData = [ ];
                            }

                            callback(tableData);
                            dataTable.fnAdjustColumnSizing(false);
                            $table.removeAttr("style");
                        })
                        .always(function()
                        {
                            $overlayDiv.hide(0);
                        });
                    },
                });
                $tableWrapperDiv = $portletWindowDiv.find("div#" + renderParameters.Entity + "_wrapper");

                var title;
                if (filterParameter !== null)
                {
                    title = Globalize.localize("FilteredListTitle", [ Globalize.localize(renderParameters.Entity + "Plural"),
                                                                      Globalize.localize(renderParameters.Filter) ]);
                }
                else
                {
                    title = Globalize.localize(renderParameters.Entity + "Plural");
                }
                $tableWrapperDiv.find("div.im-view-title").html(title);

                $tableWrapperDiv.find("a.DTTT_button_print > span").localize();
            }
            catch (exception)
            {
                throw exception;
            }
        };

        var formatDateTime = function(dateTime, types)
        {
            var dateTimeValues = dateTime.match(/\d+/g);
            var dateTime = new Date(dateTimeValues[0], dateTimeValues[1] - 1, dateTimeValues[2], dateTimeValues[3], dateTimeValues[4], dateTimeValues[5]);

            var timeFormat = Globalize.localize("None");
            if (types.indexOf("Date") > -1)
            {
                if (dateTimeValues[0] != 1900 || dateTimeValues[1] != 1 || dateTimeValues[2] != 1)
                {
                    timeFormat = Globalize.culture().calendar.patterns.d;
                }
            }
            else if (types.indexOf("Time") > -1)
            {
                if (dateTimeValues[3] != 0 || dateTimeValues[4] != 0 || dateTimeValues[5] != 0)
                {
                    timeFormat = Globalize.culture().calendar.patterns.t
                }
            }
            else
            {
                if (dateTimeValues[0] != 1900 || dateTimeValues[1] != 1 || dateTimeValues[2] != 1 ||
                    dateTimeValues[3] != 0 || dateTimeValues[4] != 0 || dateTimeValues[5] != 0)
                {
                    timeFormat = Globalize.culture().calendar.patterns.d + " " + Globalize.culture().calendar.patterns.t;
                }
            }

            return Globalize.format(dateTime, timeFormat);
        };

        var getUrlParameter = function(name)
        {
            var urlParameter = decodeURI((RegExp(name + "=" + "(.+?)(&|$)").exec(location.search) || [ ,null ])[1]);
            return urlParameter === "null" ? null : urlParameter;
        };

        return context;
    })(window[context] || {});
};