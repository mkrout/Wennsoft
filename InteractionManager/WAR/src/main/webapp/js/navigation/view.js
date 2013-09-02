var defineNavigationView = function(context)
{
    window[context] = (function(context)
    {
        var $portletWindowDiv;
        var $navigationNav;

        context.initialize = function(renderParameters)
        {
            try
            {
                if (renderParameters.Administrator !== "true" && renderParameters.UserPlatformToolbar !== "true")
                {
                    $("#PlatformAdminToolbarContainer").hide();
                }

                Globalize.culture(renderParameters.ExoLocale);

                $portletWindowDiv = $("div#" + renderParameters.WindowId);
                $navigationNav = $portletWindowDiv.find("nav#navigation");

                if (renderParameters.FormatNavigation === "true")
                {
                    $navigationNav.find("a").not("[id='remoteUser']").each(function(anchorIndex, anchor)
                    {
                        $(anchor).localize();
                    });
                }

                $(document).click(function()
                {
                    $navigationNav.find("> ul > li > ul:visible").not(":animated").animate({ height: "hide", });
                });

                $navigationNav.find("> ul").each(function(navigationUnorderedListIndex, navigationUnorderedList)
                {
                    var $navigationUnorderedList = $(navigationUnorderedList);
                    var $navigationTopLineItems = $navigationUnorderedList.find("> li");
                    $navigationTopLineItems.each(function(navigationTopLineItemIndex, navigationTopLineItem)
                    {
                        var $navigationTopLineItem = $(navigationTopLineItem);
                        var $navigationElement = $navigationTopLineItem.find("> a");

                        $navigationElement.button().removeClass("ui-corner-all");
                        if (navigationTopLineItemIndex === 0)
                        {
                            $navigationElement.addClass("ui-corner-left");
                        }
                        if (navigationTopLineItemIndex === $navigationTopLineItems.length - 1)
                        {
                            $navigationElement.addClass("ui-corner-right");
                        }
                        if ($navigationElement.attr("href") && $navigationElement.attr("href").match(window.location.pathname + "$"))
                        {
                            $navigationElement.addClass("ui-state-highlight");
                        }

                        var $navigationMenu = $navigationTopLineItem.find("> ul");
                        if ($navigationMenu.length)
                        {
                            $navigationMenu.menu().hide();

                            $navigationElement.button("option", "icons",
                            {
                                secondary: "ui-icon-triangle-1-s",
                            });

                            if ($navigationElement.attr("href"))
                            {
                                $navigationTopLineItem.on("mouseenter", function()
                                {
                                    $navigationNav.find("> ul > li > ul:visible").animate({ height: "hide", });
                                    $navigationMenu.animate({ height: "show", });
                                });
                                $navigationTopLineItem.on("mouseleave", function()
                                {
                                    $navigationMenu.animate({ height: "hide", });
                                });
                            }
                            else
                            {
                                $navigationTopLineItem.on("click", function()
                                {
                                    if ($navigationMenu.is(":visible"))
                                    {
                                        $navigationMenu.animate({ height: "hide", });
                                    }
                                    else
                                    {
                                        $navigationMenu.animate({ height: "show", });
                                    }
                                });
                            }
                        }
                    });
                });

                $navigationNav.show();

                if (renderParameters.FormatContainers === "true")
                {
                    $("div.uiTabContainer").hide();
                    $("div.uiTabContainer").each(function(tabContainerDivIndex, tabContainerDiv)
                    {
                        var $tabContainerDiv = $(tabContainerDiv);
                        var $tabContainerUnorderedList = $tabContainerDiv.find("ul.nav-tabs");

                        $tabContainerDiv.removeClass("uiTabContainer");
                        $tabContainerUnorderedList.removeClass("nav nav-tabs");
                        $tabContainerUnorderedList.find("> li a").each(function(tabContainerListItemAnchorIndex, tabContainerListItemAnchor)
                        {
                            var $tabContainerListItemAnchor = $(tabContainerListItemAnchor);
                            $tabContainerListItemAnchor.attr("href", $($tabContainerListItemAnchor).attr("onclick").replace("javascript:ajaxGet('", "").replace("')" , ""));
                            $tabContainerListItemAnchor.removeAttr("onclick");
                            $tabContainerListItemAnchor.localize();
                        });

                        $tabContainerDiv.tabs();
                        $tabContainerDiv.show();
                    });
                }
            }
            catch (exception)
            {
                throw exception;
            }
        };

        return context;
    })(window[context] || {});
};