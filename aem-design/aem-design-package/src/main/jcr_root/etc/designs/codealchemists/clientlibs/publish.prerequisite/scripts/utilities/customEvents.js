/*global base*/
/**
 * @fileOverview utility to provide getter in one place for all custom events
 */

(function() {
    'use strict';

    /**
     * Custom events util
     * @namespace customEvents
     * @memberof base.util
     */
    base.util.customEvents = (function() {
        var isTouch = (typeof window.ontouchstart !== 'undefined'),
        events = {
            RESIZE: 'base:resize',
            INTERACTION: isTouch ? 'touchend' : 'click'
        };
        window.baseEvents = events;
        return events;
    })();
}());
