(function($)
{
    $.fn.extend(
    {
        localize: function(override, arguments)
        {
            var text = "";

            if (override === null || typeof(override) === "undefined")
            {
                text = Globalize.localize(this.text().trim(), arguments);
            }
            else
            {
                text = Globalize.localize(override, arguments);
            }

            return this.text(text);
        }
    });
})($);

var originalLocalize = $.proxy(Globalize.localize, Globalize);
Globalize.localize = function(key, arguments)
{
	var text = originalLocalize(key);

	if (typeof(text) !== "undefined" && text.length)
	{
		if (typeof(arguments) !== "undefined")
		{
            arguments.forEach(function(argument, index)
            {
                text = text.replace("{" + index + "}", argument);
            });
		}
	}
	else
	{
		text = key;
	}

    return text;
};