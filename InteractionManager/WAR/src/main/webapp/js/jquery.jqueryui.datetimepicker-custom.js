(function($)
{
    $.widget("ui.datetimePickerCustom",{
    	options:                
        {
    		showAnim: "blind",
            showOn: "both",
            minuteGrid: 10,
            hourGrid: 4,
            secondGrid: 10,
        },
        _create : function ()
        {
        	console.log('datepickercustome - create: '+this.options.pickertype);
        	
        	//LOCALIZE
        	this.options.dateFormat = Globalize.culture().calendar.patterns.d
        							  .replace("yyyy","yy").replace(/M/g,"m");
        	console.log(this.options.dateFormat);
        	this.options.dayNamesMin = Globalize.culture().calendar.days.namesShort;
        	this.options.monthNames = Globalize.culture().calendar.months.names;
        	this.options.monthNamesShort = Globalize.culture().calendar.months.namesShort;
        	this.options.closeText = Globalize.localize("Done");
        	this.options.amNames = Globalize.culture().calendar.AM;
        	this.options.pmNames = Globalize.culture().calendar.PM;
        	if (this.options.amNames==null) 
        	{this.options.amNames = ['AM','a'];}
        	if (this.options.pmNames==null) 
        	{this.options.pmNames = ['PM','p'];}
        	this.options.timeFormat = Globalize.culture().calendar.patterns.t;
        	console.log(this.options.timeFormat);
        	this.options.timeText = Globalize.localize("Time");
        	this.options.hourText = Globalize.localize("Hour");
        	this.options.minuteText = Globalize.localize("Minute");
        	this.options.secondText = Globalize.localize("Second");
        	this.options.timeOnlyTitle = Globalize.localize("Choose Time");
        	
        	
        	
        	switch (this.options.pickertype)
        	{
        	case "date":
        		console.log('datepickercustom-date');
        		this.options.currentText = Globalize.localize("Today");
        		this.element.datepicker(this.options);
        		break;
        	case "time":
        		console.log('datepickercustom-time');
        		this.options.currentText = Globalize.localize("Now");
        		this.element.timepicker(this.options);
        		break;
        	case "datetime":
        		console.log('datepickercustom-datetime');
        		this.options.currentText = Globalize.localize("Now");
        		this.element.datetimepicker(this.options);
        		break;
        	}
        	
        	
        }
                        
    });
    
})(jQuery);