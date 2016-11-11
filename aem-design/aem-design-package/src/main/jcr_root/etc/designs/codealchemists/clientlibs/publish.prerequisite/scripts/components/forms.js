/*global base*/
/**
 * @fileOverview Provides placeholder functionality for the forms component
 */

(function($, util) {
    'use strict';

    /**
     * forms component
     * @namespace forms
     * @memberof base.comp
     */
    base.comp.forms = (function() {

        var selectors = {
                formsElementGroup: '.base-forms-element-group',
                formsElementWrapper: '.base-forms-element',
                submitTypeWrapper: '.base-forms-submit-type',
                responseWrapper: '.base-forms-wrapper',
                submitResponse: '.base-forms-response',
                serverErrorElement: '.base-forms-server-side-error',
                fieldErrorElement:'.base-forms-field-error',
                confirmationElement: '.base-forms-confirmation'
            },
            cssClass = {
                formsCheckbox: 'base-forms-checkbox',
                error: 'base-forms-error',
                hide: 'base-hidden',
                show:'base-visible'
            };

        /**
         * Validate Form
         * @memberOf base.comp.forms
         * @private
         * @param  {Object} $form The jQuery object of the component
         */
        function validateForm($form) {

            // Pattern validation and message
            $.validator.addMethod('cPattern', function(value, element) {
                var regexp = new RegExp($(element).data('paramCstPattern'));
                return regexp.test(value);
            }, function(value, element) {
                return $(element).data('msgCstPattern');
            });

            // Required field validation and message
            $.validator.addMethod('cRequired', $.validator.methods.required, function(value, element) {
                return $(element).data('msgCstRequired');
            });

            // Minlength validation and message
            $.validator.addMethod('cMinlength', function(value, element) {
                return value.length >= $(element).attr('minlength');
            }, function(value, element) {
                return $(element).data('msgCstMinlength');
            });

            // Max validation and message
            $.validator.addMethod('cMaxlength', function(value, element) {
                return value.length <= $(element).attr('maxlength');
            }, function(value, element) {
                return $(element).data('msgCstMaxlength');
            });

            // Confirm field validation and message
            $.validator.addMethod('confirm', function(value, element) {
                var elementId = $(element).data('confirm');
                return $('#' + elementId).val() === value;
            }, function(value, element) {
                return $(element).data('msgConfirm');
            });


            $.validator.addClassRules({
                'base-forms-textarea': {
                    normalizer: function(value) {
                        return $.trim(value);
                    }
                },
                'base-forms-input': {
                    normalizer: function(value) {
                        return $.trim(value);
                    }
                },
                'cst-required': {
                    cRequired: true
                },
                'cst-pattern': {
                    cPattern: true
                },
                'cst-number': {
                    number: true
                },
                'cst-maxlength' : {
                    cMaxlength: true
                },
                'cst-minlength' : {
                    cMinlength: true
                },
                'cst-confirm' : {
                    confirm: true
                }
            });

            // Validate the selected form using jquery validate
            $form.find('form').validate({
                debug: false,
                errorClass: 'base-forms-error',
                errorElement: 'span',
                onfocusout: function(element) { $(element).valid(); },
                errorPlacement: function(error, element) {
                    var $elem = $(element);
                    if ($elem.hasClass(cssClass.formsCheckbox)) {
                        error.insertAfter($elem.parents(selectors.formsElementGroup));
                    } else {
                        error.insertAfter(element);
                    }
                    $elem.attr('aria-invalid', 'true');
                    $elem.closest(selectors.formsElementWrapper).addClass(cssClass.error);
                },
                submitHandler: function(form) {
                    var $form = $(form),
                        $responseWrapper = $form.closest(selectors.responseWrapper);
                    if ($form.find(selectors.submitTypeWrapper).data('submitType') === 'ajaxCall') {
                        $.ajax({
                            url: form.action,
                            type: form.method,
                            data: $form.serialize(),
                            success: function(response) {
                                var formId = form.id,
                                    resultId = 'result_' + form.id,
                                    data = $(response).find(selectors.submitResponse).html();
                                data = $.parseJSON(data) || null;
                                if (data[resultId].success && data[resultId].redirectPath) {
                                    // redirect to url
                                    window.location.href = data[resultId].redirectPath;
                                } else if (data[resultId].success && data[resultId].successMessage) {
                                    // show confirmation message
                                    $responseWrapper.find(selectors.confirmationElement).html(data[resultId].successMessage).addClass(cssClass.show);
                                    $form.addClass(cssClass.hide);
                                } else {
                                    // push the invalid form html if base form wrapper available
                                    var $responseData = $(response).find('#' + formId).closest(selectors.responseWrapper);
                                    if ($responseData.length > 0) {
                                        $responseWrapper.html($responseData.html());
                                    } else {
                                        $responseWrapper.html(response);
                                    }
                                    // show error message if available in response
                                    if (data[resultId].errorMsg) {
                                        $responseWrapper.find(selectors.serverErrorElement).html(data[resultId].errorMsg);
                                    }
                                    util.initialise($responseWrapper);
                                }
                            },
                            error: function(response) {
                                if (response) {
                                    $responseWrapper.html(response);
                                }
                            }
                        });
                    } else {
                        $form.submit();
                    }
                }
            });
        }

        /**
         * Initialise a forms element.
         * @memberOf base.comp.forms
         */
        function init(obj) {
            var $form = $(obj);

            // validate form
            validateForm($form);
        }

        return {
            init: init
        };
    })();
}(base.$, base.util));
