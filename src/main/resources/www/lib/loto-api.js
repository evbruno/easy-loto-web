define(
	['jquery'],

	function ($) {
        return {
            getLotofacil: function() {
                //return $.getJSON("http://127.0.0.1:9000/api/lotofacil");
                return $.getJSON("/api/lotofacil");
            },

            getLotofacilBets: function(concurso) {
                //return $.getJSON("http://127.0.0.1:9000/api/lotofacil/" + concurso + "/bets");
                return $.getJSON("/api/lotofacil/" + concurso + "/bets");
            },

            getHello: function () {
                return 'Hello World ' + new Date();
            }
        };

});