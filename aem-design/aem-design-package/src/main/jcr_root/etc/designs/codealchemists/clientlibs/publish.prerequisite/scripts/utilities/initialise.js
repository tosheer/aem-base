/*global base*/
/**
 * @fileOverview Provides a utility function to initialise any component by calling the
 * components' returned init function.
 */
(function(base, $, util) {
    'use strict';

    /**
     * Initialise DOM Element
     * @memberof base.util
     */
    util.initialise = function(element) {
        // get all components
        var $element = $(element),
            $components = $element.find('.base-component[data-component-name]');

        $.each($components, function(index, item) {
            var $item = $(item),
                name = $item.data('component-name');

            util.attempt(name + '.init', base.comp)(item, document, $);
        });
    };

})(base, base.$, base.util);