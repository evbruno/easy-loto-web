define(['text!../lib/home/home_template.html','knockout'], function(template, ko) {

    var HomeViewModel = function(params) {
        var self = this;
        console.log("new HomeViewModel");

        //this.h3c = ko.observable("HomeViewModel " + new Date());
    };

    return {
        viewModel: HomeViewModel,
        template: template
    };
});