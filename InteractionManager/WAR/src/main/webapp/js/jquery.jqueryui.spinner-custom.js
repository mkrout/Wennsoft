(function($)
{
    $.widget("ui.spinnerCustom",
    {
        options:
        {
            change: null,
            culture: null,
            default: null,
            disabled: false,
            icons: { down: "ui-icon-triangle-1-s", up: "ui-icon-triangle-1-n" },
            incremental: true,
            max: null,
            min: null,
            numberFormat: null,
            page: 10,
            step: 1,
            spin: null,
            stop: null,
        },
        _create: function()
        {
            if (!this.element.val().length && this.options.default !== null)
            {
                this.element.val(this.options.default);
            };

            this.element.spinner(this.options);
            this.previousValue = this.element.spinner("value");

            this.element.keypress($.proxy(function(event)
            {
                if ((event.which < 48 || event.which > 57) && event.which != 0 && event.which != 8)
                {
                    this.select(this.previousValue);
                    return false;
                }
            },
            this));

            this.element.spinner(
            {
                spin: $.proxy(function(event, ui)
                {
                    this._errorCheck(ui.value);
                    this._trigger("spin", event, { ui: ui });
                },
                this),
                change: $.proxy(function(event, ui)
                {
                    this._errorCheck(this.element.spinner("value"));
                    this._trigger("change", event, { ui: ui });
                },
                this),
            });
        },
        select: function(selection)
        {
            this.element.spinner("value", selection);
        },
        _errorCheck: function(testValue)
        {
            if ($.isNumeric(testValue))
            {
                if (testValue < this.options.min)
                {
                    this.select(this.options.min);
                    this.previousValue = this.options.min;
                }
                else if (testValue > this.options.max)
                {
                    this.select(this.options.max);
                    this.previousValue = this.options.max;
                }
                else
                {
                    this.previousValue = testValue;
                }
            }
            else
            {
                this.select(this.previousValue);
            }
        },
    });
})($);