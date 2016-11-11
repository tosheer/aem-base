/*global base*/
/**
 * Lighbox util js
 */

(function() {
    'use strict';

    /**
     * CTA Util to open page in overlay
     * @namespace cta
     * @memberof base.util
     */
    base.util.lightbox = (function($, util) {
        // get all components
        var $body = $('body'),
            $lightboxWrapper,
            $lightboxCloseBtn,
            $lightboxContent,
            $lightboxOverlay,
            $lighboxSelector,
            /* selectors */
            selectors = {
                overlay: '.base-lightbox-overlay',
                wrapper: '.base-lightbox-wrapper',
                content: '.base-lightbox-content',
                close: '.base-lightbox-close'
            },
            cssClass = {
                lightboxOpen: 'base-lightbox-open'
            },
            EVENTS = util.customEvents.INTERACTION,
            pageName,
            $target,
            lightboxHref,
            type;

        /**
         * Set content inside lightbox content
         */
        function setContent(data) {
            var $page = $(data).find('[data-component-name]');

            $lightboxContent.empty().html(data);

            if ($page.length) {
                pageName = $page.data('component-name');
                util.initialise($lightboxContent);
            }
            $.publish('base:lightbox:opened', [{
                target: $target,
                type: type,
                pageName: pageName
            }]);
        }
        /**
         * [closeLightbox description]
         * @return {[type]} [description]
         */
        function closeLightbox() {
            $body.removeClass(cssClass.lightboxOpen);
            $.publish('base:lightbox:close', [pageName]);
        }
        /**
         * [errorHandler description]
         * @param  {[type]} error [description]
         * @return {[type]}       [description]
         */
        function errorHandler(error) {
            console.log('error', error, error.statusText);
        }
        /**
         * [getData description]
         * @return {[type]} [description]
         */
        function getData(settings) {
            $.ajax(settings).done(setContent).fail(errorHandler);
        }
        /**
         * [openLightbox description]
         * @return {[type]} [description]
         */
        function openLightbox(e) {
            $target = e ? $(e.target) : {};
            lightboxHref = $target.data('href') || undefined;
            type = $target.data('page-type') || undefined;

            // if its a URL then make a AJAX call and set content
            if (lightboxHref) {
                e.preventDefault();
                e.stopPropagation();
                getData({
                    url: lightboxHref + '?wcmmode=disabled',
                    type: 'GET'
                });
            }

            $body.addClass(cssClass.lightboxOpen);
        }

        /**
         * Init Lightbox
         * @return {[type]} [description]
         */
        function init() {
            // selector
            $lighboxSelector =  $('[data-lightbox]');
            //overlay
            $lightboxOverlay = $(selectors.overlay);
            //content
            $lightboxWrapper = $(selectors.wrapper);
            $lightboxContent = $lightboxWrapper.find(selectors.content);
            $lightboxCloseBtn = $lightboxWrapper.find(selectors.close);

            // bind events
            $lighboxSelector.on(EVENTS, openLightbox);
            $lightboxOverlay.on(EVENTS, closeLightbox);
            $lightboxCloseBtn.on(EVENTS, closeLightbox);
            // close lightbox on keyboard escape key press
            $(window).on('keyup', function(event) {
                var key = event.charCode || event.keyCode;
                if (key === 27) {
                    closeLightbox();
                }
            });
        }
        return {
            init: init,
            openLightbox: openLightbox,
            closeLightbox: closeLightbox,
            content: setContent
        };
    })(base.$, base.util);
}());

