// For any third party dependencies, like jQuery, place them in the lib folder.

// Configure loading modules from the lib directory,
// except for 'app' ones, which are in a sibling
// directory.
requirejs.config({
    baseUrl: 'lib',
    paths: {
        //app: '../app'
        'text'		: 'vendor/requirejs-text/text',
        'knockout'	: 'vendor/knockout/dist/knockout',
        'sammy'		: 'vendor/sammy/lib/sammy',

        'jquery'	: 'vendor/jquery/dist/jquery.min',
        'bootstrap'	: 'vendor/bootstrap/dist/js/bootstrap',
        'material'	: 'vendor/bootstrap-material-design/dist/js/material',
        'ripples'	: 'vendor/bootstrap-material-design/dist/js/ripples',
        'arrive'	: 'vendor/arrive/src/arrive',
        'underscore': 'vendor/underscore/underscore-min',

        'api'			: 'loto-api',

        //'appViewModel'	: 'appViewModel',
        'lotofacil'		: 'lotofacil/loto_model',
        'loto-bet'		: 'lotofacil/bet-model',
        'home'		    : 'home/home_model'


    },
    shim: {
        bootstrap: {
            deps: ["jquery"],
            exports: 'jquery'
        },
        ripples : {
            deps: ["jquery"]
        },
        material: {
            deps: ["bootstrap", 'ripples', 'arrive']
        },
        arrive: {
            deps: ['jquery']
        }
    }
});

// Start loading the main app file. Put all of
// your application logic in there.
requirejs(
    ['api', 'knockout', 'sammy', 'jquery', 'lotofacil', 'loto-bet', 'home', 'material'],

	function(api, ko, Sammy, $, lotofacilModel, lotoBetModel, homeModel) {
		var xx = api.getHello();
		console.log("main1 " + xx);

        var MyViewModel = function () {
            var self = this;

            ko.components.register("home", homeModel);
            ko.components.register("loto", lotofacilModel);
            ko.components.register("loto/bets", lotoBetModel);

            this.pageComponent = ko.observable("home");
            this.pageParams = ko.observable();
        };

        var myModel = new MyViewModel();
        ko.applyBindings(myModel);


        // routes ---------

        var sammy = Sammy('body', function () {});

        var goHome = function() {
            console.log("home");
            myModel.pageComponent("home");
            myModel.pageParams(null);
        };

        var goLoto = function() {
            console.log("loto");
            myModel.pageComponent("loto");
            myModel.pageParams(null);
        };

        function goComponent(name) {
        	return function() {
				console.log("new component: " + name);
				myModel.pageComponent(name);
				myModel.pageParams(null);
			}
        }

        sammy.get("#/", goHome);
        sammy.get("#/loto", goLoto);

        sammy.get("#/loto/bets", goComponent("loto/bets"));

		sammy.get("#/loto/bets/:id", function() {
			var _id = this.params['id'];
        	console.log("loto bets " + _id);
        	myModel.pageComponent("loto/bets");
        	myModel.pageParams({id: _id});
        });

        // init ---------------

        sammy.run("#/");

        $.material.init();
        $.material.ripples();

	}

);