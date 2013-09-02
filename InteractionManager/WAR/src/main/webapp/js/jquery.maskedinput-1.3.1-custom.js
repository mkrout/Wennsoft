/*
 * Modified from:
 *     Masked Input plugin for jQuery
 *     Copyright (c) 2007-2013 Josh Bush (digitalbush.com)
 *     Licensed under the MIT license (http://digitalbush.com/projects/masked-input-plugin/#license)
 *     Version: 1.3.1
 */
(function($)
{
    function getPasteEvent()
    {
        var input = document.createElement("input");
        input.setAttribute("onpaste", "");
        return (typeof input["onpaste"] === "function") ? "paste" : "input";             
    }
    
    var pasteEventName = getPasteEvent() + ".mask";
    var userAgent = navigator.userAgent;
    var iPhone = /iphone/i.test(userAgent);
    var android = /android/i.test(userAgent);
    var caretTimeoutId;

    //Predefined character definitions
    $.mask =
    {
        definitions:            
        {
            "9":       "[0-9]",
            "a":       "[A-Za-z]",
            "*":       "[A-Za-z0-9]",
        },
        dataName: "rawMaskFn",
        placeholder: "_",
    };

    $.fn.extend(
    {
        caret: function(begin, end)
        {
            var range;
            
            if (this.length !== 0 && !this.is(":hidden"))
            {
                if (typeof begin == "number")
                {
                    end = (typeof end === "number") ? end : begin;
                    return this.each(function()
                    {
                        if (this.setSelectionRange)
                        {
                            this.setSelectionRange(begin, end);
                        }
                        else if (this.createTextRange)
                        {
                            range = this.createTextRange();
                            range.collapse(true);
                            range.moveEnd("character", end);
                            range.moveStart("character", begin);
                            range.select();
                        }
                    });
                }
                else
                {
                    if (this[0].setSelectionRange)
                    {
                        begin = this[0].selectionStart;
                        end = this[0].selectionEnd;
                    }
                    else if (document.selection && document.selection.createRange)
                    {
                        range = document.selection.createRange();
                        begin = 0 - range.duplicate().moveStart("character", -100000);
                        end = begin + range.text.length;
                    }
                    return { begin: begin, end: end };
                }
            }
        },
        unmask: function()
        {
            return this.trigger("unmask");
        },
        mask: function(mask, settings)
        {
            var input;
            var definitions;
            var tests;
            var partialPosition;
            var firstNonMaskPos;
            var length;

            if (!mask && this.length > 0)
            {
                input = $(this[0]);
                return input.data($.mask.dataName)();
            }
            
            settings = $.extend(
            {
                placeholder:    $.mask.placeholder,
                completed:      null
            }, settings);
            
            definitions = $.mask.definitions;
            tests = [];
            partialPosition = length = mask.length;
            firstNonMaskPos = null;
            
            $.each(mask.split(""), function(index, value)
            {
                if (value == '?')
                {
                    length--;
                    partialPosition = index;
                }
                else if (definitions[value])
                {
                    tests.push(new RegExp(definitions[value]));
                    if (firstNonMaskPos === null)
                    {
                        firstNonMaskPos = tests.length - 1;
                    }
                }
                else
                {
                    tests.push(null);
                }
            });

            return this.trigger("unmask").each(function()
            {
                var input = $(this);
                var buffer = $.map(mask.split(""), function(element, index)
                {
                    if (element != '?')
                    {
                        return definitions[element] ? settings.placeholder : element;
                    }
                });
                var focusText = input.val();

                function seekNext(position)
                {
                    while (++position < length && !tests[position]);
                    return position;
                }

                function seekPrevious(position)
                {
                    while (--position >= 0 && !tests[position]);
                    return position;
                }

                function shiftLeft(begin, end)
                {
                    var index;
                    var next;
                    
                    if (begin >= 0)
                    {
                        for (index = begin, next = seekNext(end); index < length; index++)
                        {
                            if (tests[index])
                            {
                                if (next < length && tests[index].test(buffer[next]))
                                {
                                    buffer[index] = buffer[next];
                                    buffer[next] = settings.placeholder;
                                }
                                else
                                {
                                    break;
                                }
            
                                next = seekNext(next);
                            }
                        }
                        writeBuffer();
                        input.caret(Math.max(firstNonMaskPos, begin));
                    }
                }

                function shiftRight(position)
                {
                    var index;
                    var c;
                    var next;
                    var t;
        
                    for (index = position, c = settings.placeholder; index < length; index++)
                    {
                        if (tests[index])
                        {
                            next = seekNext(index);
                            t = buffer[index];
                            buffer[index] = c;
                            if (next < length && tests[next].test(t))
                            {
                                c = t;
                            }
                            else
                            {
                                break;
                            }
                        }
                    }
                }

                function keydownEvent(event)
                {
                    var key = event.which;
                    var position;
                    var begin;
                    var end;
                    
                    // Backspace and delete
                    if (key === 8 || key === 46 || (iPhone && key === 127))
                    {
                        position = input.caret();
                        begin = position.begin;
                        end = position.end;
                        
                        if (end - begin === 0)
                        {
                            begin = key !== 46 ? seekPrevious(begin) : (end = seekNext(begin - 1));
                            end = key === 46 ? seekNext(end) : end;
                        }
                        clearBuffer(begin, end);
                        shiftLeft(begin, end - 1);
        
                        event.preventDefault();
                    }
                    // Escape
                    else if (key == 27)
                    {
                        input.val(focusText);
                        input.caret(0, checkValue());
                        event.preventDefault();
                    }
                }

                function keypressEvent(event)
                {
                    var key = event.which;
                    var position = input.caret();
                    var index;
                    var character;
                    var next;
         
                    if (key && !(event.ctrlKey || event.altKey || event.metaKey || key < 32))
                    {
                        if (position.end - position.begin !== 0)
                        {
                            clearBuffer(position.begin, position.end);
                            shiftLeft(position.begin, position.end - 1);
                        }
        
                        index = seekNext(position.begin - 1);
                        if (index < length)
                        {
                            character = String.fromCharCode(key);
                            if (tests[index].test(character))
                            {
                                shiftRight(index);
                                
                                buffer[index] = character;
                                writeBuffer();
                                next = seekNext(index);
                                
                                if (android)
                                {
                                    setTimeout($.proxy($.fn.caret, input, next), 0);
                                }
                                else
                                {
                                    input.caret(next);
                                }
        
                                if (settings.completed && next >= length)
                                {
                                    settings.completed.call(input);
                                }
                            }
                        }
                        
                        event.preventDefault();
                    }
                }

                function clearBuffer(start, end)
                {
                    var index;
                    for (index = start; index < end && index < length; index++)
                    {
                        if (tests[index])
                        {
                            buffer[index] = settings.placeholder;
                        }
                    }
                }

                function writeBuffer()
                {
                    input.val(buffer.join(""));
                }

                function checkValue(allow)
                {
                    // Try to place characters where they belong
                    var test = input.val();
                    var lastMatch = -1;
                    var index;
                    var character;
        
                    for (index = 0, position = 0; index < length; index++)
                    {
                        if (tests[index])
                        {
                            buffer[index] = settings.placeholder;
                            while (position++ < test.length)
                            {
                                character = test.charAt(position - 1);
                                if (tests[index].test(character))
                                {
                                    buffer[index] = character;
                                    lastMatch = index;
                                    break;
                                }
                            }
                            if (position > test.length)
                            {
                                break;
                            }
                        }
                        else if (buffer[index] === test.charAt(position) && index !== partialPosition)
                        {
                            position++;
                            lastMatch = index;
                        }
                    }
                    if (allow)
                    {
                        writeBuffer();
                    }
                    else if (lastMatch + 1 < partialPosition)
                    {
                        input.val("");
                        clearBuffer(0, length);
                    }
                    else
                    {
                        writeBuffer();
                        input.val(input.val().substring(0, lastMatch + 1));
                    }
                    return (partialPosition ? index : firstNonMaskPos);
                }

                input.data($.mask.dataName, function()
                {
                    return $.map(buffer, function(element, index)
                    {
                        return tests[index] && element != settings.placeholder ? element : null;
                    }).join("");
                });

                if (!input.attr("readonly"))
                {
                    input.one("unmask", function()
                    {
                        input.unbind(".mask").removeData($.mask.dataName);
                    })
                    .bind("focus.mask", function()
                    {
                        var position;
                        
                        if (caretTimeoutId)
                        {
                            clearTimeout(caretTimeoutId);
                        }
                        
                        focusText = input.val();
                        position = checkValue(true);
                        
                        caretTimeoutId = setTimeout(function()
                        {
                            writeBuffer();
                            if (position == mask.length)
                            {
                                input.caret(0, position);
                            }
                            else
                            {
                                input.caret(position);
                            }
                        }, 10);
                    })
                    .bind("blur.mask", function()
                    {
                        // CUSTOM
                        //checkValue();
                        //if (input.val() != focusText)
                            //input.change();
                    })
                    .bind("keydown.mask", keydownEvent)
                    .bind("keypress.mask", keypressEvent)
                    .bind(pasteEventName, function()
                    {
                        setTimeout(function()
                        { 
                            var position = checkValue(true);
                            input.caret(position); 
                            if (settings.completed && position == input.val().length)
                            {
                                settings.completed.call(input);
                            }
                        }, 0);
                    });
                    
                    checkValue();
                }
            });
        }
    });
})(jQuery);