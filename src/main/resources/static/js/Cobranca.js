$(function() {
	$('.js-currency').maskMoney({
		decimal : ',',
		thousands : '.',
		allowZero : true
	});
	$('.js-atualizar-status').on('click', function(event) {
		event.preventDefault();
		console.log('Clicou')

		var botaoReceber = $(event.currentTarget);
		var urlReceber = botaoReceber.attr('href');

		console.log('urlrceber', urlReceber);

		var response = $.ajax({
			url : urlReceber,
			type : 'PUT',

		});

		response.done(function(e) {
			var codigoTitulo = botaoReceber.data('codigo'); 
			$('[data-role=' + codigoTitulo + ']' ).html('<span class= "badge badge-success">Recebido</span>');
			botaoReceber.hide();
			
		});
		
		response.fail(function(e) {
			console.log(e)
			alert('Erro no recebimento da cobrança')
		});

	})

});