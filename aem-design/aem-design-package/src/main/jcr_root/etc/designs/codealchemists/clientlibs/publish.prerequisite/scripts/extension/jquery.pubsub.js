/*global base */
//jscs:disable

// Source : https://gist.github.com/addyosmani/1321768
(function(d) {
    'use strict';

    // the topic/subscription hash
    var cache = {};

    // Publish some data on a named topic.
    d.publish = function( /* String */ topic, /* Array? */ args) {
        // topic: String - The channel to publish on
        // args: Array - The data to publish. Each array item is converted into an ordered
        // arguments on the subscribed functions.
        cache[topic] && d.each(cache[topic], function() {
            (typeof this === 'function') && this.apply(d, args || []);
        });
    };

    // Register a callback on a named topic.
    d.subscribe = function( /* String */ topic, /* Function */ callback) {
        // @topic: String - The channel to subscribe to
        // @callback: Function - The handler event. Anytime something is $.publish'ed on a
        // subscribed channel, the callback will be called with the published array as
        // ordered arguments.
        //
        // returns: Array - A handle which can be used to unsubscribe this
        // particular subscription.

        if (!cache[topic]) {
            cache[topic] = [];
        }
        cache[topic].push(callback);
        return [topic, callback]; // Array
    };

    // Disconnect a subscribed function for a topic.
    d.unsubscribe = function( /* Array */ handle) {
        // handle: Array - The return value from a $.subscribe call.
        var t = handle[0];
        cache[t] && d.each(cache[t], function(idx) {
            if (this === handle[1]) {
                cache[t].splice(idx, 1);
            }
        });
    };

})(base.$);
