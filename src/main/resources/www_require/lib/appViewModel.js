define(['sammy', 'knockout', 'api'], function(Sammy, ko, api) {

    var ViewModel = function(params) {
        var self = this;

        // var sammy = Sammy('body', function () {});

        // function goTo(where) {
        //     return function() {
        //         console.log("goint to " + where);
        //     };
        // };

        // sammy.get("#/", goTo("home"));
        // sammy.get("#/lotofacil", goTo("loto facil"));

        console.log("new App View Model");
    };

    return new ViewModel();
});