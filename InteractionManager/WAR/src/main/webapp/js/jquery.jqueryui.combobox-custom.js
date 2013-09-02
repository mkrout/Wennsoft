// Adapted from http://jqueryui.com/autocomplete/#combobox
// Probable jQuery UI plugin in 2014 - https://github.com/jquery/jqueryui.com/issues/42
(function($)
{
    $.widget("ui.combobox",
    {
        options:                
        {
            autocomplete: false,
            class: "",
            selected: null,
            required: false,
        },
        alphaSort: function()
        {
            var $sortedOptions = this.element.find("> option").sort(function(option1, option2)
            {
                return $(option1).text() > $(option2).text() ? 1 : -1;
            });
            this.element.empty().append($sortedOptions);
        },
        disable: function()
        {
            this._super();
            this._refresh();
        },
        enable: function()
        {
            this._super();
            this._refresh();
        },
        select: function(selection)
        {
            var $selectionOption = this.element.find("> option[value='" + selection + "']");
            if ($selectionOption.length)
            {
                this.element.val(selection);
                this.input.val($selectionOption.text().trim());
            }
            else
            {
                this.element.val("");
                this.input.val("");
            }
            
            this._errorCheck();  
        },
        _errorCheck: function()
        {
            if (!$(this.element).find("option:selected").length && this.options.required)
            {
            	this.input.addClass("ui-state-error");
            }
            else
        	{
            	this.input.removeClass("ui-state-error");
        	}
            
            this.previousValue = this.input.val();
        },
        _confirmSelection: function(event, ui)
        {
            // Item was selected; value and callback already handled.
            if (ui && ui.item)
            {
                return;
            }

            var value = this.input.val();
            var validMatch = false;
            this.element.children("option").each(function()
            {
                if ($(this).text().trim() === value)
                {
                    this.selected = validMatch = true;
                    return false;
                }
            });

            if (validMatch)
            {
                this.input.autocomplete("close");
                this._trigger("selected", event, { value: value });
            }
            else
            {
                // TODO Revert to previous selection?
                this.select(this.previousValue);
                this.input.data("ui-autocomplete").term = "";
                this.input.autocomplete("search", "");
            }
            this._errorCheck();  

            return;
        },
        _create: function()
        {
            var selected = this.element.children(":selected");
            var value = selected.val() ? selected.text().trim() : "";

            this.element.hide();

            this.wrapper = $("<span>",
            {
                class: "ui-combobox ui-corner-all ui-widget ui-widget-content " + this.options.class,
            })
            .insertAfter(this.element);

            var input = this.input = $("<input>",
            {
                class: "ui-combobox-input " + (!this.options.autocomplete ? "ui-combobox-unselectable " : "") + "ui-corner-all ui-widget ui-widget-content",
                id: this.element.attr("name") + "Combobox",
                val: value,
            })
            .appendTo(this.wrapper)
            .tooltip({ tooltipClass: "ui-state-highlight" })
            .autocomplete(
            {
                delay: 0,
                minLength: 0,
                source: $.proxy(this, "_source"),
            })
            .on("click", $.proxy(this._toggle, this));

            this.anchor = $("<a>",
            {
                tabIndex: -1,
            })
            .appendTo(this.wrapper)
            .button(
            {
                icons: { primary: "ui-icon-triangle-1-s" },
                text: false
            })
            .removeClass("ui-corner-all")
            .addClass("ui-corner-right ui-combobox-toggle")
            .on("click", $.proxy(this._toggle, this));

            this._createAutocomplete();
            this._refresh();
            this._errorCheck();
            
            this.previousValue = this.input.val();
        },
        _createAutocomplete: function()
        {
            this._on(this.input,
            {
                autocompleteselect: function(event, ui)
                {
                    if (ui.item.option.text.trim() !== this.input.val())
                    {
                        ui.item.option.selected = true;
                        this.input.val(ui.item.option.text.trim());
                        this._trigger("selected", event, { item: ui.item.option });
                        this._errorCheck();  
                    }
                    
                },
                autocompletechange: "_confirmSelection",
                keydown: function(event)
                {
                    if (event.keyCode === 13)
                    {
                        this._confirmSelection();
                    }
                },
            });
        },
        _destroy: function()
        {
            this.wrapper.remove();
            this.element.show();
        },
        _refresh: function()
        {
            if (this.options.disabled)
            {
                this.input.addClass("ui-state-disabled");
                this.anchor.addClass("ui-state-disabled");
                this.anchor.button("disable");
            }
            else
            {
                this.input.removeClass("ui-state-disabled");
                this.anchor.removeClass("ui-state-disabled");
                this.anchor.button("enable");
            }
            
            this._errorCheck();
            
        },
        _setOptions: function()
        {
            this._superApply(arguments);
            this._refresh();
        },
        _source: function(request, response)
        {
            var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
            response(this.element.children("option").map(function()
            {
                var text = $(this).text().trim();
                if (this.value && (!request.term || matcher.test(text)))
                {
                    return { label: text, value: text, option: this };
                }
            }));
        },
        _toggle: function()
        {
            if (!this.options.disabled)
            {
                if (this.input.autocomplete("widget").is(":visible"))
                {
                    this.input.autocomplete("close");
                }
                else
                {
                    this.input.focus();
                    this.input.autocomplete("search", "");
                }
            }
        },
    });
})($);