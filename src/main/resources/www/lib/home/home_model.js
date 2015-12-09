define(['text!../lib/home/home_template.html','knockout'], function(template, ko) {

    var HomeViewModel = function(params) {
        var self = this;
    };

    return {
        viewModel: HomeViewModel,
        template: template
    };
});