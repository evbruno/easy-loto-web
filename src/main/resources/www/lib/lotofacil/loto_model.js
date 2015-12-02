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
        self.numeroDoJogoAtual = ko.pureComputed(function(){
           if (!self.currentDraw()) return "";
            return self.currentDraw().draw;
        });

        self.bets = ko.observableArray();

        self.lotofacilHitsOk = ko.observable(0);

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
            return self.currentDraw().numbers.indexOf(val) >= 0 && self.currentBet.indexOf(val) < 0;
        };

        self.isHit = function(val) {
            if (! self.currentDraw() || ! self.currentBet()) return false;
            return self.currentDraw().numbers.indexOf(val) >= 0 && self.currentBet.indexOf(val) >= 0;
        };

        self.isWrongGuess = function(val) {
            if (! self.currentDraw() || ! self.currentBet()) return false;
            return self.currentDraw().numbers.indexOf(val) < 0 && self.currentBet.indexOf(val) >= 0;
        };

        self.currentBet = ko.observableArray();
        self.currentBetValue = ko.observable();

        self.currentBetValue.subscribe(function(newValue) {
            var b = newValue.split(",");
            var newBet = _(b).map(function(each){ return parseInt(each); });

            var miss = _(self.currentDraw().numbers).difference(newBet);
            self.lotofacilHitsOk(AMOUNT - miss.length);
            self.currentBet(newBet);

        }, this);

        self.getLoto = function() {
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

            });
        };

        self.getBets = function(draw) {
            $.when(api.getLotofacilBets(draw)).done(function(data) {
                self.bets.removeAll();
                var dataArr = [];

                for (var i = 0; i < data.length; i++)
                    dataArr.push(new Wrapped(data[i].numbers));

                self.bets(dataArr);

                if (dataArr.length > 0)
                    self.currentBetValue(dataArr[0].name.toString());
                else
                    self.currentBetValue("-1");
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