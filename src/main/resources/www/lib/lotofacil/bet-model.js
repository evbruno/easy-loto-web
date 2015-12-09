define(['text!../lib/lotofacil/bet-template.html','knockout', 'underscore', 'api'],
	function(template, ko, _, api) {

		var BetViewModel = function(params) {
			var self = this;

			self.dateNow = ko.observable(new Date());
			self.currentBet = ko.observable();

			if (params && params.id) {
				console.log("Current bet: " + params.id);
				self.currentBet(params.id);
			} else {
				console.log("Current bet empty... ");
            }

		};

		return {
			viewModel: BetViewModel,
			template: template
		};

	}
);