// For any third party dependencies, like jQuery, place them in the lib folder.

// Configure loading modules from the lib directory,
// except for 'app' ones, which are in a sibling
// directory.
requirejs.config({
    baseUrl: 'lib',
    paths: {
        //app: '../app'
        'text'		: 'vendor/requirejs-text/text',
        'jquery'	: 'vendor/jquery/dist/jquery.min',
        'knockout'	: 'vendor/knockout/dist/knockout',
        'sammy'		: 'vendor/sammy/lib/sammy',

        'api'			: 'loto-api',
        //'appViewModel'	: 'appViewModel',
        //'lotofacil'		: '../lotofacil/model',

        //'materialize': 'vendor/materializecss-amd/dist/js/materialize'

        //'materialize': 'vendor/materialize/dist/js/materialize',

        // materialize stuff
        'materialize': 'vendor/materialize/bin/materialize',
        'hammerjs': 'vendor/materialize/js/hammer.min',
        'jquery.hammer': 'vendor/materialize/js/jquery.hammer',

        'jquery.easing': 'vendor/materialize/js/jquery.easing.1.3',
        'velocity': 'vendor/materialize/js/velocity.min',
        'picker': 'vendor/materialize/js/date_picker/picker',
        'picker.date': 'vendor/materialize/js/date_picker/picker.date',

        'waves': 'vendor/materialize/js/waves',
        'global': 'vendor/materialize/js/global',

        'animation': 'vendor/materialize/js/animation',
        'collapsible': 'vendor/materialize/js/collapsible',
        'dropdown': 'vendor/materialize/js/dropdown',
        'leanModal': 'vendor/materialize/js/leanModal',
        'materialbox': 'vendor/materialize/js/materialbox',
        'tabs': 'vendor/materialize/js/tabs',
        'sideNav': 'vendor/materialize/js/sideNav',
        'parallax': 'vendor/materialize/js/parallax',
        'scrollspy': 'vendor/materialize/js/scrollspy',
        'tooltip': 'vendor/materialize/js/tooltip',
        'slider': 'vendor/materialize/js/slider',
        'cards': 'vendor/materialize/js/cards',
        'buttons': 'vendor/materialize/js/buttons',
        'pushpin': 'vendor/materialize/js/pushpin',
        'character_counter': 'vendor/materialize/js/character_counter',
        'toasts': 'vendor/materialize/js/toasts',
        'forms': 'vendor/materialize/js/forms',
        'scrollFire': 'vendor/materialize/js/scrollFire',
        'transitions': 'vendor/materialize/js/transitions'
    },
    shim: {
        /** my stuff .. */

        'materialize': {
            deps: ['jquery', 'hammerjs', 'velocity']
        },

        'jquery.easing': {
            deps: ['jquery']
        },
        'animation': {
            deps: ['jquery']
        },
        'jquery.hammer': {
            deps: ['jquery', 'hammerjs']
        },
        'global': {
            deps: ['jquery']
        },
        'toasts': {
            deps: ['global', 'jquery.hammer']
        },
        'collapsible': {
            deps: ['jquery']
        },
        'dropdown': {
            deps: ['jquery']
        },
        'leanModal': {
            deps: ['jquery']
        },
        'materialbox': {
            deps: ['jquery']
        },
        'parallax': {
            deps: ['jquery']
        },
        'tabs': {
            deps: ['jquery']
        },
        'tooltip': {
            deps: ['jquery']
        },
        'sideNav': {
            deps: ['jquery']
        },
        'scrollspy': {
            deps: ['jquery']
        },
        'forms': {
            deps: ['jquery', 'global']
        },
        'slider': {
            deps: ['jquery']
        },
        'cards': {
            deps: ['jquery']
        },
        'pushpin': {
            deps: ['jquery']
        },
        'buttons': {
            deps: ['jquery']
        },
        'transitions': {
            deps: ['jquery','scrollFire']
        },
        'scrollFire': {
            deps: ['jquery', 'global']
        },
        'waves': {
            exports: 'Waves'
        },
        'character_counter': {
            deps: ['jquery']
        },
        'velocity': {
            deps: ['jquery']
        }
    }
});

// Start loading the main app file. Put all of
// your application logic in there.
requirejs(
    ['api', 'knockout', 'sammy', 'jquery',
        'materialize',
    // mat
        'jquery.easing',
        'animation',
        'velocity',

        'hammerjs',
        'jquery.hammer',

        'global', // very important do not remove!
        'collapsible',
        'dropdown',
        'leanModal',
        'materialbox',
        'parallax',
        'tabs',
        'tooltip',


        'waves',


        'toasts',
        'sideNav',
        'scrollspy',
        'forms',
        'slider',
        'cards',
        'pushpin',
        'buttons',
        'scrollFire',
        'transitions',
        'picker',
        'picker.date',
        'character_counter'
    ],

	function(api, ko, Sammy, $) {
		var xx = api.getHello();
		console.log("main1 " + xx);
		//console.log("main2 " + ko);
		//console.log("main3 " + Sammy);

        $('.button-collapse').sideNav();
        $('.parallax').parallax();

        var MyViewModel = function () {
            var self = this;

            self.now = ko.observable(new Date());
            self.lotofacil = ko.observableArray();
            self.lotofacilHits = ko.observableArray([2, 3, 5, 7, 8, 10, 12, 14, 15, 16, 17, 19, 22, 23, 25]);
            self.currentDraw = ko.observable(null);

            self.lotofacilGrid = ko.pureComputed(function(){
                var r = [];
                for(var i = 1; i <= 5; i++) {
                    var s =[];
                    for(var j = 1; j <= 5; j++)
                        s.push((i - 1) * 5 + j);
                    r.push(s);
                }
                return r;
            });

            self.init = function () {
                $.getJSON("http://127.0.0.1:9000/api/lotofacil", function (data) {
                    console.log("deu certo:" + data.length);
                    self.lotofacil.removeAll();
                    var dataArr = [];
                    for (var i = 0; i < data.length; i++)
                        dataArr.push(data[i]);

                    self.lotofacil(dataArr);

                    if (dataArr.length > 0)
                        self.updateCurrentDraw(dataArr[0]);
                });
            };

            self.updateCurrentDraw = function(newDraw) {
                self.currentDraw(newDraw);
            };

            self.init();

            DBG = self;
        };

        ko.applyBindings(new MyViewModel());

	}

);