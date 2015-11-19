define(['text!../lotofacil/template.html','knockout'], function(template, ko) {

    var LotofacilViewModel = function(params) {
        var self = this;
        console.log("new LotofacilModel");

        this.h3c = ko.observable("Loto Facil");

        console.log("lotofacil template: " +  template);
    };

    return {
        viewModel: LotofacilViewModel,
        template: template
    };
});