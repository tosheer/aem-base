/* globals base */

(function(win, util) {
    'use strict';

    /**
     * attempt to call a function but don't break entire site if
     * there happens to be an error finding function or in that function
     *
     * @namespace attempt
     * @memberof base.util
     *
     * @param  {string} fnLoc The location of the function to call e.g. 'componentName.init'
     * @param  {Object} [ns] The namespace that contains the function to attempt to call
     * @return {Function} will attempt to call fnLoc with passed params
     */
    util.attempt = function(fnLoc, ns) {
        var fn;

        // fnLoc is required
        if (typeof fnLoc !== 'string') {
            return;
        }

        win.console = win.console || { error: function() {} };

        // splits each part of fnLoc and assigns method as value if
        // it exists or undefined if it doesn't
        fn = fnLoc.split('.').reduce(function(prev, next) {
            if (prev && prev[next]) {
                return prev[next];
            }
        }, ns || win);

        return function() {
            try {
                if (fn) {
                    fn.apply(this, arguments);
                }
            } catch (e) {
                win.console.error(fnLoc, (e.stack || e.toString()));
            }
        };
    };

}(window, base.util));
