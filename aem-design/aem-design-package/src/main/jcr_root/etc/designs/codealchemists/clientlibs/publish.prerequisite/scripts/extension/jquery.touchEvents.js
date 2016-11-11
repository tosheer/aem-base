/* global base, Modernizr */
/* jshint nonew: false  */

/**
 * @fileOverview A plugin to trigger custom touch events based on human interaction
 */
(function($, Modernizr) {

    'use strict';

    var CONST = {
        SWIPE: 'swipe',
        SWIPE_LEFT: 'swipeLeft',
        SWIPE_RIGHT: 'swipeRight',
        SWIPE_UP: 'swipeUp',
        SWIPE_DOWN: 'swipeDown',
        TAP: 'tap'
    },

    /**
     * Default plugin settings to control behaviour
     * @type {Object}
     */
    settings_ = {
        // 'swipeUp', 'swipeRight', 'swipeDown' or 'swipLeft' will
        // prevent default touchmove behaviour in that direction
        preventMove: []
    },

    /**
     * Array of x touch coordinates, used for calculating average
     * @type {Array}
     */
    touchesX = [],

    /**
     * Array of y touch coordinates, used for calculating average
     * @type {Array}
     */
    touchesY = [],

    /**
     * TouchEvents - A plugin to trigger custom touch events
     *
     * @param {DOMelement} element
     * @param {Object} settings  Default settings overides
     * @class TouchEvents
     */
    TouchEvents = function(element, options) {
        this.$element = $(element);
        this.options = options;

        this.gesture = '';
        this.touchStartTriggered = false;
        this.touchStartPointX = 0;
        this.touchStartPointY = 0;
        this.touchEndPointX = 0;
        this.touchEndPointY = 0;

        this.init();
    };


    TouchEvents.prototype = {

        /**
         * Bind native touch events to target element to test against
         * @function init
         * @memberOf TouchEvents.prototype
         */
        init: function() {
            this.settings = $.extend({}, settings_, this.options);

            this.$element
                .on('touchstart', this.touchStart.bind(this))
                .on('touchmove', this.touchMove.bind(this))
                .on('touchend', this.touchEnd.bind(this));
        },

        /**
         * Record the first touch point time and position
         * @function touchStart
         * @memberOf TouchEvents.prototype
         * @param {Event} event The event that triggered touchstart
         */
        touchStart: function(event) {
            var touchPoint_ = event.originalEvent.changedTouches[0];
            this.touchStartPointX = touchPoint_.pageX;
            this.touchStartPointY = touchPoint_.pageY;
            this.touchStartTriggered = true;
        },

        /**
         * Detect whether user is swiping
         * @function touchMove
         * @memberOf TouchEvents.prototype
         * @param {Event} event The event that triggered touchmove
         */
        touchMove: function(event) {
            var touchPoint_ = event.originalEvent.changedTouches,
                horizontalSwipe_,
                verticalSwipe_;

            // make sure they're only touching with one finger so that swipe
            // gestures aren't fired when user is zooming
            if (touchPoint_.length === 1) {
                // horizontal distance swiped
                this.touchEndPointX = touchPoint_[0].pageX - this.touchStartPointX;
                // vertical distance swiped
                this.touchEndPointY = touchPoint_[0].pageY - this.touchStartPointY;

                touchesX.push(Math.abs(this.touchEndPointX));
                touchesY.push(Math.abs(this.touchEndPointY));

                // calculate averages in case user was swiping in one direction
                // but slightly flicks their finger in a different direction at the end
                horizontalSwipe_ = this.averageDistanceTravelled(touchesX);
                verticalSwipe_ = this.averageDistanceTravelled(touchesY);

                if (horizontalSwipe_ > 10 && verticalSwipe_ < 10) {
                    this.gesture = (this.touchEndPointX < 0) ? CONST.SWIPE_LEFT : CONST.SWIPE_RIGHT;
                } else {
                    this.gesture = (this.touchEndPointY < 0) ? CONST.SWIPE_UP : CONST.SWIPE_DOWN;
                }
            }

            if ($.inArray(this.gesture, this.settings.preventMove) > -1) {
                event.preventDefault();
            }
        },

        /**
         * Update gesture if user tapped and trigger detected gesture
         * @function touchEnd
         * @memberOf TouchEvents.prototype
         */
        touchEnd: function() {
            if (this.touchStartTriggered) {
                this.touchStartTriggered = false;

                if (this.touchEndPointX === this.touchStartPointX &&
                    this.touchEndPointY === this.touchStartPointY) {
                    //the user released at the same point at which they touched
                    this.gesture = CONST.TAP;
                }

                if (this.gesture) {
                    this.$element.trigger(this.gesture, [this]);
                    this.gesture = '';
                    touchesX = [];
                    touchesY = [];
                }
            }
        },

        /**
         * Calculates the average distance travelled based on multiple touch points
         * @param  {Array} touches List of touch points in a particular direction
         * @return {number} average distance
         */
        averageDistanceTravelled: function(touches) {
            return touches.reduce(function(prev, curr) {
                return (prev + curr) / touches.length;
            });
        }

    };

    /**
     * touchEvents - Touch Swipe jQuery plugin.
     *
     * @class touchEvents
     * @requires jQuery
     * @requires Modernizr
     * @memberOf jQuery.fn
     */
    $.fn.touchEvents = function(options) {

        if (Modernizr.touch) {
            return this.each(function() {
                new TouchEvents(this, options);
            });
        }
    };

}(base.$, Modernizr));
