/*global base*/

base.util.matchmedia = (function() {
    'use strict';

    var MEDIA_NARROW = '(max-width: 767px)',
        MEDIA_TABLET = '(max-width: 959px)',
        MEDIA_WIDE = '(min-width: 960px)';

    return {
        narrow: window.matchMedia(MEDIA_NARROW),
        tablet: window.matchMedia(MEDIA_TABLET),
        wide: window.matchMedia(MEDIA_WIDE)
    };
})();
