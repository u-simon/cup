/*
 * Cron jQuery plugin (version 1.0.0)
 * https://github.com/null-d3v/jquery-cron
 *
 * Copyright 2016 null-d3v
 * Released under the MIT license
 */
(function($) {
    var intervals = {
        "none": "none",
        "minute": "cron-minute",
        "hour": "cron-hour",
        "day": "cron-day",
        "week": "cron-week",
        "month": "cron-month",
        "year": "cron-year",
    };
	var intervals_cn = {
        "none": "none",
        "minute": "分钟",
        "hour": "小时",
        "day": "天",
        "week": "周",
        "month": "月",
        "year": "年",
    };
    var weekdays = [
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    ];
	var weekdays_cn = [
        "周日",	
        "周一",	
        "周二",	
        "周三",	
        "周四",	
        "周五",	
        "周六"	
    ];
    // TODO Maximum days per month for year interval.
    var months = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    ];

	var months_cn = [
        "一月",
        "二月",
        "三月",
        "四月",
        "五月",
        "六月",
        "七月",
        "八月",
        "九月",
        "十月",
        "十一月",
        "十二月"
    ];
	
    var defaultOptions = {
        disabled: false,
        value: "",
    };

    var Cron = function() {
        var $input = null;
        var $cronSpan = null;
        var $intervalSelect = null;
        var $minuteSelect = null;
        var $hourSelect = null;
        var $weekdaySelect = null;
        var $monthdaySelect = null;
        var $monthSelect = null;
        var $cronCrontrols = null;

        var currentInterval = intervals.minute;
        var currentOptions = null;

        this._disabled = function(disabled) {
            if (typeof(disabled) !== "undefined") {
                currentOptions.disabled = disabled;
                $input.prop("disabled", currentOptions.disabled);
                $cronControls.prop("disabled", currentOptions.disabled);
            }
            else {
                return currentOptions.disabled;
            }
        };

        this._getValue = function() {
            var cron =  {
            	second: "0",
                minute: "*",
                hour: "*",
                monthday: "*",
                month: "*",
                weekday: "*",
            };

            switch (currentInterval) {
                case intervals.minute: {
                    cron.weekday = "?";
                    break;
                }
                case intervals.hour: {
                    cron.minute = $minuteSelect.val();
                    cron.weekday = "?";
                    break;
                }
                case intervals.day: {
                    cron.minute = $minuteSelect.val();
                    cron.hour = $hourSelect.val();
                    cron.weekday = "?";
                    break;
                }
                case intervals.week: {
                    cron.minute = $minuteSelect.val();
                    cron.hour = $hourSelect.val();
                    cron.monthday = "?";
                    cron.weekday = $weekdaySelect.val();
                    break;
                }
                case intervals.month: {
                    cron.minute = $minuteSelect.val();
                    cron.hour = $hourSelect.val();
                    cron.monthday = $monthdaySelect.val();
                    cron.weekday = "?";
                    break;
                }
                case intervals.year: {
                    cron.minute = $minuteSelect.val();
                    cron.hour = $hourSelect.val();
                    cron.monthday = $monthdaySelect.val();
                    cron.month = $monthSelect.val();
                    cron.weekday = "?";
                    break;
                }
            }

            return [cron.second, cron.minute, cron.hour, cron.monthday, cron.month, cron.weekday].join(" ");
        };

        this._initialize = function($element, options) {
            var scope = this;

            if ($input !== null) {
                $.error("Element has already been initialized.");
            }

            $input = $element;

            if ($input.length !== 1 || !$input.is("input")) {
                $.error("Element must be a single input.");
            }

            currentOptions = $.extend({ }, defaultOptions, options);

            $intervalSelect = $("<select>")
                .addClass("cron-control")
                .on("change", function() {
                    scope._setInterval($intervalSelect.val());
                });
            for (var interval in intervals) {
                if (intervals[interval] !== intervals.none) {
                    $("<option>")
                        .attr("value", intervals[interval])
                        .text(intervals_cn[interval])
                        .appendTo($intervalSelect);
                }
            }

            $minuteSelect = $("<select>")
                .addClass("cron-control cron-hour cron-day cron-week cron-month cron-year");
            for (var index = 0; index < 60; index++) {
                var test = $("<option>")
                    .attr("value", index)
                    .text(index < 10 ? "0" + index + "分" : index + "分")
                    .appendTo($minuteSelect);
            }

            $hourSelect = $("<select>")
                .addClass("cron-control cron-day cron-week cron-month cron-year");
            for (var index = 0; index < 24; index++) {
                $("<option>")
                    .attr("value", index)
                    .text(index < 10 ? "0" + index + "时" : index + "时")
                    .appendTo($hourSelect);
            }

            // Day-of-Week from 1, 1=sunday
            $weekdaySelect = $("<select>")
                .addClass("cron-control cron-week");
            for (var index = 0; index < weekdays.length; index++) {
                $("<option>")
                    .attr("value", index + 1)
                    .text(weekdays_cn[index])
                    .appendTo($weekdaySelect);
            }

            $monthdaySelect = $("<select>")
                .addClass("cron-control cron-month cron-year");
            for (var index = 1; index < 32; index++) {
                $("<option>")
                    .attr("value", index)
                    .text(index + "日")
                    .appendTo($monthdaySelect);
            }

            $monthSelect = $("<select>")
                .addClass("cron-control cron-year");
            for (var index = 0; index < months.length; index++) {
                $("<option>")
                    .attr("value", index + 1)
                    .text(months_cn[index])
                    .appendTo($monthSelect);
            }

            $cronSpan = $("<span>")
                .addClass("cron")
                .append($("<span>")
                    .text("每 "))
                .append($intervalSelect)
                .append($("<span>")
                    .text(" : ")
                    .addClass("cron-week cron-month cron-year"))
                .append($("<span>")
                    .addClass("cron-month"))
                .append($weekdaySelect)
                .append($monthSelect)
                .append($monthdaySelect)
                .append($("<span>")
                    .addClass("cron-month"))
                .append($("<span>")
                    .text(" : ")
                    .addClass("cron-hour cron-day cron-week cron-month cron-year"))
                .append($hourSelect)
                .append($("<span>")
                    .text(" : ")
                    .addClass("cron-day cron-week cron-month cron-year"))
                .append($minuteSelect)

            $cronControls = $cronSpan
                .find("> select.cron-control")
                .on("change", function() {
                    $input
                        .val(scope._getValue())
                        .trigger("change");
                });

            $input .hide().after($cronSpan);

            this._disabled(currentOptions.disabled);
            this._setValue($input.val().substr(2).replace(/\?/g,"*") 
            		|| currentOptions.value.substr(2).replace(/\?/g,"*") 
            		|| "0 * * * * *");
            if (currentOptions.value != null) {
            	$input.val(currentOptions.value)
            }
        };

        this._setInterval = function(newInterval) {
            for (var interval in intervals) {
                if (intervals[interval] !== intervals.none) {
                    $cronSpan.find("> ." + intervals[interval]).hide();
                }
            }
            $cronSpan.find("> ." + newInterval).show();
            $intervalSelect.val(newInterval);
            currentInterval = newInterval;
        };

        this._setValue = function(newValue) {
            currentInterval = intervals.none;

            var cron = this._validateCron(newValue);

            if (cron.minute === "*") {
                this._setInterval(intervals.minute);
            }
            else {
                if (cron.hour === "*") {
                    this._setInterval(intervals.hour);
                    $minuteSelect.val(cron.minute);
                }
                else {
                    if (cron.weekday === "*") {
                        if (cron.monthday === "*") {
                            this._setInterval(intervals.day);
                            $minuteSelect.val(cron.minute);
                            $hourSelect.val(cron.hour);
                        }
                        else if (cron.month === "*")  {
                            this._setInterval(intervals.month);
                            $minuteSelect.val(cron.minute);
                            $hourSelect.val(cron.hour);
                            $monthdaySelect.val(cron.monthday);
                        }
                        else {
                            this._setInterval(intervals.year);
                            $minuteSelect.val(cron.minute);
                            $hourSelect.val(cron.hour);
                            $monthdaySelect.val(cron.monthday);
                            $monthSelect.val(cron.month);
                        }
                    }
                    else {
                        this._setInterval(intervals.week);
                        $minuteSelect.val(cron.minute);
                        $hourSelect.val(cron.hour);
                        $weekdaySelect.val(cron.weekday);
                    }
                }
            }
            $input.val(newValue);
        };

        this._validateCron = function(cronString) {
            var cronComponents = cronString.split(" ");
            if (cronComponents.length !== 5) {
                $.error("Invalid cron format.");
            }

            var cron = {
                minute: cronComponents[0],
                hour: cronComponents[1],
                monthday: cronComponents[2],
                month: cronComponents[3],
                weekday: cronComponents[4],
            };

            if (cron.minute !== "*" &&
                !this._validateInteger(cron.minute, 0, 59)) {
                $.error("Invalid minute format.");
            }

            if (cron.hour !== "*" &&
                !this._validateInteger(cron.hour, 0, 23)) {
                $.error("Invalid hour format.");
            }

            if (cron.monthday !== "*" &&
                !this._validateInteger(cron.monthday, 1, 31))  {
                $.error("Invalid monthday format.");
            }

            if (cron.month !== "*" &&
                !this._validateInteger(cron.month, 0, 11)) {
                $.error("Invalid month format.");
            }

            if (cron.weekday !== "*" &&
                !this._validateInteger(cron.weekday, 0, 7)) {
                $.error("Invalid weekday format.");
            }

            return cron;
        };

        this._validateInteger = function(integer, minimum, maximum) {
            var parsedInteger = parseInt(integer);
            var valid =
                !isNaN(parsedInteger) &&
                Number.isInteger(parsedInteger) &&
                parsedInteger >= minimum &&
                parsedInteger <= maximum;
            return valid;
        };
    };

    Cron.prototype.initialize = function($element, options) {
        this._initialize($element, options);
    };

    Cron.prototype.disabled = function(disabled) {
        this._disabled(disabled);
    };

    Cron.prototype.value = function(value) {
        if (typeof(value) !== "undefined") {
            this._setValue(value);
        }
        else {
            //return this._getValue();
        	var v = this._getValue();
        	console.log(v);
        	return v;
        }
    };

    $.fn.jqcron = function(parameter) {
        var $this = $(this);
        if (typeof($this.data("cron")) !== "undefined") {
            if (typeof(Cron[parameter]) !== "undefined" &&
                Cron[parameter] !== Cron.initialize) {
                return Cron[parameter].apply(this, Array.prototype.slice.call(arguments, 1));
            }
            else {
                $.error("Unsupported cron function.");
            }
        }
        else {
            if (typeof(parameter) === "undefined" || typeof(parameter) === "object") {
                var cron = new Cron();
                $this.data("cron", cron);
                cron.initialize($this, parameter);
                return cron;
            }
        }
    };
})($);