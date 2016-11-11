/*global base */

base.util.resize = (function($, util, window, undefined) {
    'use strict';
    var resizeEvent = 'resize.base-resize orientationchange.base-resize',
        delayTimer = 250,
        timer,
        wWidth = $(window).width(),
        wHeight = $(window).height(),

    onResizeEvent = function() {
        if (timer !== undefined) {
            window.clearTimeout(timer);
        }

        timer = window.setTimeout(function() {
            //adding this checkas on some mobiles when scrolling it things that we resized screen
            if ($(window).width() !== wWidth || $(window).height() !== wHeight) {
                wWidth = $(window).width();
                wHeight = $(window).height();
                $.publish(util.customEvents.resize);
            }
        }, delayTimer);
    };
    function init() {
        $(window).on(resizeEvent, onResizeEvent);
    }

    return {
        init : init
    };

})(base.$, base.util, window);
