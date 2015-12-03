define(
	['jquery'],

	function ($) {

        var URL_API_PREFIX = window.location.origin === "http://127.0.0.1:9001"
                            ? "http://127.0.0.1:9000"
                            : window.location.origin;
        return {
            getLotofacil: function() {
                return $.getJSON(URL_API_PREFIX + "/api/lotofacil");
            },

            getLotofacilBets: function(concurso) {
                return $.getJSON(URL_API_PREFIX + "/api/lotofacil/" + concurso + "/bets");
            },

            getHello: function () {
                return 'Hello World ' + new Date();
            }
        };

});