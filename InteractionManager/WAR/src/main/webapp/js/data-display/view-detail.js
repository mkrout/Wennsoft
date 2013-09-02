var defineCustomerDataDisplayViewDetail = function(context)
{
    window[context] = (function(context)
    {
        var portalUri;
        var requestContextPath;

        var $portletWindowDiv;
        var $overlayDiv;
        var overlaySpinner;
        var $form;
        var $detailContentDiv;
        var fieldSpinner;

        var entityId;

        context.initialize = function(renderParameters)
        {
            try
            {
                Globalize.culture(renderParameters.ExoLocale);

                portalUri = renderParameters.PortalUri;
                requestContextPath = renderParameters.RequestContextPath;

                $portletWindowDiv = $("div#" + renderParameters.WindowId);
                $overlayDiv = $portletWindowDiv.find("div#overlay");
                overlaySpinner = new Spinner(
                {
                    color: "#000",
                });
                $form = $portletWindowDiv.find("form");
                $detailContentDiv = $portletWindowDiv.find("div#detailContent");
                fieldSpinner = new Spinner(
                {
                    length: 5,
                    width: 2,
                    radius: 3,
                    color: "#000",
                });

                entityId = getUrlParameter(renderParameters.Entity);

                var title = Globalize.localize(renderParameters.Entity);
                $portletWindowDiv.find("div.im-view-title").html(title);
                $detailContentDiv.find("> div.detail-column > div.im-detail-row").each(function(detailRowIndex, detailRow)
                {
                    $(detailRow).find("> label.im-detail-row-label").localize();
                });

                if (entityId !== null)
                {
                    var fieldNames = new Array();
                    $detailContentDiv.find("> div.detail-column > div.im-detail-row").each(function(detailRowIndex, detailRow)
                    {
                        fieldNames.push($(detailRow).attr("data-metadata-name"));
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
                            if (fieldName !== "Keys")
                            {
                                var $detailRow = $detailContentDiv.find("> div.detail-column > div.im-detail-row[data-metadata-name='" + fieldName + "']");
                                if ($detailRow.length)
                                {
                                    var keyValue = entityData["Keys"][fieldName];
                                    createDetailRowValueControl($detailRow, entityData[fieldName], (typeof(keyValue) !== "undefined" ? keyValue.Value : null));
                                }
                            }
                        }
                    })
                    .always(function()
                    {
                        $overlayDiv.hide(0);
                    });
                }
            }
            catch (exception)
            {
                throw exception;
            }
        };

        var createDetailRowValueControl = function($detailRow, value, keyValue)
        {
            if ($detailRow.attr("data-metadata-basetype") === "DateTime")
            {
                value = formatDateTime(value, $detailRow.attr("data-metadata-types"));
            }

            if ($detailRow.attr("data-metadata-basetype") === "Byte")
            {
                var $fieldInput = $("<input>",
                {
                    id: $detailRow.attr("data-metadata-name"),
                    name: $detailRow.attr("data-metadata-name"),
                    type: "checkbox",
                });
                $fieldInput.prop("checked", value);
                $fieldInput.prop("disabled", true);
                $detailRow.append($fieldInput);
            }
            else
            {
                var entity = $detailRow.attr("data-metadata-entity");
                var targetPage = $detailRow.attr("data-setting-detail");
                if (entity && targetPage && keyValue !== null)
                {
                    var $anchorLink = $("<a>",
                    {
                        class: "im-link",
                        href: portalUri + targetPage + "?" + entity + "=" + keyValue.join("/"),
                    })
                    .append(value);
                    $detailRow.append($("<span>").append($anchorLink));
                }
                else
                {
                    $detailRow.append($("<label>").append(value));
                }
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