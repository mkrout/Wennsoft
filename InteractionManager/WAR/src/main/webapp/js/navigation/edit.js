var defineNavigationEdit = function(context)
{
    window[context] = (function(context)
    {
        var $portletWindowDiv;
        var $overlayDiv;
        var overlaySpinner;
        var $form;
        var $connectSelect;

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
                $form = $portletWindowDiv.find("form");
                $connectSelect = $portletWindowDiv.find("select#connect");

                $connectSelect.find("> option").each(function(connectOptionIndex, connectOption)
                {
                    $(connectOption).localize();
                });

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

                $form.find("input#formatNavigation").on("change", savePreferences);
                $form.find("input#formatContainers").on("change", savePreferences);
                $form.find("input#userPlatformToolbar").on("change", savePreferences);
            }
            catch (exception)
            {
                throw exception;
            }
        };

        var savePreferences = function()
        {
            $overlayDiv.show();
            overlaySpinner.spin($overlayDiv[0]);
            $overlayDiv.delay(1000);

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
        };

        return context;
    })(window[context] || {});
};