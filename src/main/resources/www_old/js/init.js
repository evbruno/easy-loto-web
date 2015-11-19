(function ($) {
	$(function () {

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


	}); // end of document ready
})(jQuery); // end of jQuery name space

