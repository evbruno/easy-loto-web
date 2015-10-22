// For any third party dependencies, like jQuery, place them in the lib folder.

// Configure loading modules from the lib directory,
// except for 'app' ones, which are in a sibling
// directory.
requirejs.config({
    baseUrl: 'lib/vendor',
    paths: {
        //app: '../app'
        'jquery'	: 'jquery/dist/jquery.min',
        'knockout': 'knockout/dist/knockout',
        'api'			: '../loto-api'
    }
});

// Start loading the main app file. Put all of
// your application logic in there.
requirejs(['api', 'knockout'], 

	function(api, ko) {
		var xx = api.getHello();

		var MyModel = function() {
			var self = this;
			var timer = undefined;

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

		console.log("main api xx " + xx);

		ko.applyBindings(new MyModel());
	}

);