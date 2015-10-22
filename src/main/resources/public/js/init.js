(function ($) {
	$(function () {

		$('.button-collapse').sideNav();
		$('.parallax').parallax();


		var MyViewModel = function () {
			var self = this;

			self.now = ko.observable(new Date());
			self.lotofacil = ko.observableArray();

			self.init = function () {
				$.getJSON("http://127.0.0.1:9000/api/lotofacil", function (data) {
					console.log("deu certo:" + data.length);
					self.lotofacil.removeAll();
					for (var i = 0; i < data.length; i++)
						self.lotofacil.push(data[i]);
				});
			}

			self.init();
		}


		ko.applyBindings(new MyViewModel());


	}); // end of document ready
})(jQuery); // end of jQuery name space

