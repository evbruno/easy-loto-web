define(['text!../lib/lotofacil/loto_template.html','knockout', 'underscore', 'api'],
        function(template, ko, _, api) {

    var Wrapped = function(value) {
        this.name = value;

        this.name.toString = function() {
            return this.join(', ');
        }
    };

    var LotofacilViewModel = function(params) {
        var self = this;

        var AMOUNT = 15;

        self.lotofacil = ko.observableArray();
        self.currentDraw = ko.observable(null);

        self.isLotoLoading = ko.observable(false);

        self.lotofacilHitsOk = ko.observable(0);

        self.lotofacilCurrentPrize = ko.pureComputed(function() {
            if (!self.currentBet() || !self.currentDraw() || !self.lotofacilHitsOk() || self.lotofacilHitsOk() < 10)
                return "0,00";

            var idx = 15 - self.lotofacilHitsOk();
            return self.currentDraw().prizes[idx][1];
        });

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

        self.isMissHit = function(val) {
            if (! self.currentDraw() || ! self.currentBet()) return false;
            return self.currentDraw().numbers.indexOf(val) >= 0 && self.currentBet().numbers.indexOf(val) < 0;
        };

        self.isHit = function(val) {
            if (! self.currentDraw() || ! self.currentBet()) return false;
            return self.currentDraw().numbers.indexOf(val) >= 0 && self.currentBet().numbers.indexOf(val) >= 0;
        };

        self.isWrongGuess = function(val) {
            if (! self.currentDraw() || ! self.currentBet()) return false;
            return self.currentDraw().numbers.indexOf(val) < 0 && self.currentBet().numbers.indexOf(val) >= 0;
        };

        self.getLoto = function() {
            self.isLotoLoading(true);
            $.when(api.getLotofacil()).done(function (data) {
                self.lotofacil.removeAll();
                var dataArr = [];
                for (var i = 0; i < data.length; i++) {
                    data[i].numbersStr = data[i].numbers.join(', ');
                    dataArr.push(data[i]);
                }

                self.lotofacil(dataArr);

                if (dataArr.length > 0)
                    self.updateCurrentDraw(dataArr[0]);

                self.isLotoLoading(false);
            });
        };

        self.bets = ko.observableArray();
        self.currentBet = ko.observable();
        self.updateCurrentBet = function() {
            var newBet = arguments[0];
            console.log(newBet);
            var miss = _(self.currentDraw().numbers).difference(newBet.numbers);
            self.lotofacilHitsOk(AMOUNT - miss.length);
            self.currentBet(newBet);
        };

        self.getBets = function(draw) {
            $.when(api.getLotofacilBets(draw)).done(function(data) {
                self.bets.removeAll();
                var dataArr = [];

                for (var i = 0; i < data.length; i++) {
                    dataArr.push(data[i]);
                }

                self.bets(dataArr);

                if (dataArr.length > 0)
                    self.updateCurrentBet(dataArr[0]);
                else
                    self.updateCurrentBet(null);
            });
        };

        self.updateCurrentDraw = function() {
            var newDraw = arguments[0];
            self.currentDraw(newDraw);
            self.getBets(newDraw.draw)
        };

        self.init = function () {
            self.getLoto();
        };

        self.init();
    };

    return {
        viewModel: LotofacilViewModel,
        template: template
    };
});