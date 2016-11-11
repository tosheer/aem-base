/* globals base */

(function(window, util) {
    'use strict';

    /**
     * Throttle calls to "callback" routine and ensure that it 
     * is not invoked any more often than "delay" milliseconds.
     * @param  {Function} callback  the callback to be executed
     * @param  {number}   delay     delay in miliseconds
     * @return {Function} function that fires callback
     */
    util.throttle = function(callback, delay) {
        var ticker,
            fn = delay ? 'setTimeout' : 'requestAnimationFrame';

        return function() {
            var context = this,
                args = arguments;

            if (!ticker) {
                // note that requestAnimationFrame is
                // polyfilled in requestanimationframe.js
                // also delay 2nd arg will just be ignored by requestAnimationFrame
                ticker = window[fn](function() {
                    callback.apply(context, args);
                    ticker = null;
                }, delay);
            }
        };
    };

}(window, base.util));
