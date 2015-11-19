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
        'knockout': 'vendor/knockout/dist/knockout',
        'sammy'		: 'vendor/sammy/lib/sammy',

        'api'						: 'loto-api',
        'appViewModel'	: 'appViewModel',
        'lotofacil'			: '../lotofacil/model',
    }
});

// Start loading the main app file. Put all of
// your application logic in there.
requirejs(['api', 'knockout', 'appViewModel', 'sammy', 'lotofacil'], 

	function(api, ko, appViewModel, Sammy, lotofacilModel) {
		var xx = api.getHello();

		var MyModel = function() {
			var self = this;
			var timer = undefined;

			var homeModel = { 
							viewModel: function(){},
							template: '<p>oi mundo</p>'
					};

			this.pageComponent = ko.observable("home");
			ko.components.register("home", homeModel);

			this.pageParams = ko.observable();

			this.helloMsg = ko.observable(api.getHello());
			this.helloCount = ko.observable(0);
			
			this.isUpdating = ko.observable(false);
			
			this.actionText = ko.pureComputed(function(){
				return self.isUpdating() ? "Auto Update ON" :  "Auto Update OFF";
			});

			this.action = function() {
					var up = self.isUpdating();

					if (! up ) {
							timer = setInterval(function(){
								self.helloMsg(api.getHello());
								self.helloCount(self.helloCount() + 1);
							}, 2000);
					} else {
						if (timer) {
							clearInterval(timer);
							timer = undefined;
						}
					}

					self.isUpdating(! up );
			};

		};

		var myModel = new MyModel();

		var sammy = Sammy('body', function () {});

    function goTo(where) {
        return function() {
            console.log("goint to " + where);
        };
    };

    sammy.get("#/", goTo("home"));
    sammy.get("#/lotofacil", goTo("loto facil"));

		console.log("lotofacilModel 1: " + lotofacilModel.viewModel);
		console.log("lotofacilModel 2: " + lotofacilModel.template);

    ko.components.register("lotofacil", lotofacilModel);

    sammy.get("#/lotofacil2", function() {
    	myModel.pageComponent("lotofacil");
    });

    sammy.run("#/");

		console.log("main api xx " + xx);

		ko.applyBindings(myModel);
	}

);