/*globals base*/
// initialisation file for base

// initialise components
(function($, util) {
    'use strict';

    $(function() {
        // initialise components
        util.initialise(document);

        //initialise lightboxes
        util.lightbox.init();
    });
})(base.$, base.util);
