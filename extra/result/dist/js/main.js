//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////// 
////// Script layer for JuvenilXML
////// version 0.1
////// Last Updated: 16/06/2013
////// Author: José Antonio Yáñez Jiménez -- http://www.jimenezfrontend.es
////// 
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function showInn() 
	{
	////////////////////////////////////////////////
	////////////////////////////////////////////////
	////   MUESTRA UN ALOJAMIENTO EN CONCRETO   ////
	////////////////////////////////////////////////
	////////////////////////////////////////////////
	$('.linked').click(function (evt) 
		{
		evt.preventDefault();
		var id = evt.target.id;
		$.get('getxml.php', function (xml) 
			{
			$(xml).find('element').each(function () 
				{
				if($(this).find('attribute[name="Identificador"]').text() == id) 
					{
					////////////////////////////////////////////////
					//DIV DESTINO QUE SOBRESALE
					////////////////////////////////////////////////
					$('<div id="showinfo"></div>').html('').appendTo('body');
					////////////////////////////////////////////////
					// INFORMACIÓN DEL ALOJAMIENTO
					////////////////////////////////////////////////
					// TÍTULO
					$('<p></p>').html($(this).find('attribute[name="Titulo_es"]').find('string').text()).appendTo('#showinfo');
					// DESCRIPCIÓN
					$('<p></p>').html($(this).find('attribute[name="Descripcion_es"]').find('text').text()).appendTo('#showinfo');
					// TEMÁTICA
					$('<p id="tem"></p>').html('').appendTo('#showinfo');
						$('<ul id="tem_list"></ul>').html('').appendTo('#tem');
						$(this).find('attribute[name="Tematica"]').find('array').find('string').each(function (list) 
							{
							$('<li></li>').html($(this).text()).appendTo('#tem_list');
							});
					// TELÉFONOS
					$('<p id="phone"></p>').html('').appendTo('#showinfo');
						$('<ul id="phone_list"></ul>').html('').appendTo('#phone');
						$(this).find('attribute[name="Telefono"]').find('array').find('string').each(function () 
							{
							$('<li></li>').html($(this).text()).appendTo('#phone_list');
							});
					// FAXES
					$('<p id="fax"></p>').html('').appendTo('#showinfo');
						$('<ul id="fax_list"></ul>').html('').appendTo('#fax');
						$(this).find('attribute[name="Fax"]').find('array').find('string').each(function () 
							{
							$('<li></li>').html($(this).text()).appendTo('#fax_list');
							});
					// EMAILS
					$('<p id="mail"></p>').html('').appendTo('#showinfo');
						$('<ul id="mail_list"></ul>').html('').appendTo('#mail');
						$(this).find('attribute[name="Email"]').find('array').find('string').each(function () 
							{
							$('<li></li>').html($(this).text()).appendTo('#mail_list');
							});
					// WEBS
					$('<p id="web"></p>').html('').appendTo('#showinfo');
						$('<ul id="web_list"></ul>').html('').appendTo('#web');
						$(this).find('attribute[name="Web"]').find('array').find('string').each(function () 
							{
							$('<li></li>').html("<a href=\"http://"+$(this).text()+"\" target=\"_blank\">"+$(this).text()+"</a>").appendTo('#web_list');
							});
					// SERVICIOS
					$('<p id="services"></p>').html('').appendTo('#showinfo');
						$('<ul id="services_list"></ul>').html('').appendTo('#services');
						$(this).find('attribute[name="Servicios"]').find('array').find('Servicios').each(function () 
							{
							$('<li></li>').html($(this).text()).appendTo('#services_list');
							});
					}
				});
			});
		});
	}
function searchInn() 
	{
	var timer = null;
	var searching = 0;
	$('#finder').keypress(function (e) 
		{
   if (event.keyCode == 10 || event.keyCode == 13) 
        {event.preventDefault();}
		clearTimeout(timer);
		timer = setTimeout(function () 
			{
			box = jQuery('#finder').val().toLowerCase();
			if(box.length > 2) 
				{
				if(searching != 1) 
					{
					$('.resultado').remove();
					$('<div id="loadingGif"></div>').html('<img src="img/ajax-loader.gif" alt="Cargando..." />').appendTo('#maindiv');
					searching = 1;
					$.get('getxml.php', function (xml) 
						{
						$(xml).find('element').each(function () 
							{
							var aux = $(this).find('attribute[name="Titulo_es"]').find('string').text().toLowerCase();
							var aux2 = $(this).find('attribute[name="Provincia"]').find('Provincias').text();
							if(aux.search(box) != -1) 
								{
								var id = $(this).find('attribute[name="Identificador"]').text();
								$('<div class="resultado"></div>').html('<a href="#" id="' + id + '" class="linked">'+aux+'</a> <span class="prov"><a href="#" id="' + id + '" class="linked">'+aux2+'</a></span>').appendTo('#result');
								}
							});
						$('#loadingGif').remove();
						searching = 0;
						showInn();
						});
					} 
				else 
					{
					$('<div id="loadingGif"></div>').html("<div class=\"center\"><p>¡Espera a que termine la búsqueda actual!</p><img src=\"img/ajax-loader.gif\" alt=\"Cargando...\" /></div>").appendTo('#maindiv');
					}
				}
			}, 800);
		});
$('#finderb').click(function (e) 
		{event.preventDefault();
		});
	}



function loadResult() 
	{
	$.get('getxml.php', function (xml) 
		{
		$(xml).find('attribute[name="Titulo_es"]').each(function () 
			{
			var aux = $(this).find('string').text();
			$('<p></p>').html(aux).appendTo('#result');
			});
		});
	}
$(document).ready(function () 
	{
	//loadResult();
	searchInn();
	});
