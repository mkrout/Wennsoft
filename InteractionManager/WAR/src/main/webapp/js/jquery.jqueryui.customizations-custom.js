$.widget("ui.autocomplete", $.extend({ }, $.ui.autocomplete.prototype,
{
    _renderItem: function(unorderedList, item)
    {
        var $lineItem;

        if (item.value === "")
        {
            $lineItem = $("<li>").append($("<a>").append($("<br>")));
        }
        else
        {
            $lineItem = $("<li>").append($("<a>").text(item.label));
        }
        $lineItem.appendTo(unorderedList);

        return $lineItem;
    }
}));