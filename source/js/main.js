//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////// 
////// Script layer for JuvenilXML
////// version 0.15
////// Last Updated: 19/06/2013
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
		$.ajax(
			{
			type: "GET",
			url: "db/inns.xml",
			dataType: "xml",
			success: function(xml) 
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
							$('<li></li>').html('<a href="http://'+$(this).text()+'" target="_blank">'+$(this).text()+'</a>').appendTo('#web_list');
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
			}});
		});
	}
function searchInn() 
	{
	////////////////////////////////////////////////
	////////////////////////////////////////////////
	////        BÚSQUEDA DE ALOJAMIENTOS        ////
	////////////////////////////////////////////////
	////////////////////////////////////////////////
	var timer = null;
	var searching = 0;
	$('#textbox').keydown(function (e) 
		{
   if (event.keyCode == 10 || event.keyCode == 13) 
        {event.preventDefault();}
		clearTimeout(timer);
		timer = setTimeout(function () 
			{
			box = jQuery('#textbox').val().toLowerCase();
			if(box.length > 2) 
				{
				if(searching != 1) 
					{
					$('.result').remove();
					$('<div id="loadingGif"></div>').html('<img src="img/ajax-loader.gif" alt="Cargando..." />').appendTo('#maindiv');
					searching = 1;
					$.ajax(
						{
						type: "GET",
						url: "db/inns.xml",
						dataType: "xml",
						success: function(xml) 
						{
						////////////////////////////////////////////////
						////////////////////////////////////////////////
						////CREAMOS Y BORRAMOS LAS CAPAS PERTINENTES////
						////////////////////////////////////////////////
						////////////////////////////////////////////////
						$('#nullresult').remove();
						$('#resultnames').remove();
						$('#resultprovinces').remove();
						$('<div id="resultnames"></div>').html('<h2>Resultados por nombre</h2>').appendTo('#resultsdiv');
						$('<div id="resultprovinces"></div>').html('<h2>Resultados por provincia</h2>').appendTo('#resultsdiv');
						var shownames=false;
						var showprovinces=false;
						$(xml).find('element').each(function () 
							{
							var aux = $(this).find('attribute[name="Titulo_es"]').find('string').text().toLowerCase();
							var aux2 = $(this).find('attribute[name="Provincia"]').find('Provincias').text().toLowerCase();
							////////////////////////////////////////////////
							////////////////////////////////////////////////
							////        ALOJAMIENTOS POR NOMBRE         ////
							////////////////////////////////////////////////
							////////////////////////////////////////////////
							if(aux.search(box) != -1) 
								{
								shownames=true;
								var id = $(this).find('attribute[name="Identificador"]').text();
								$('<div class="result"></div>').html('<p><span class="resultname"><a href="#" id="' + id + '" class="linked">'+aux+'</a></span> <span class="provname"><a href="#" id="' + id + '" class="linked">'+aux2+'</a></span></p>').appendTo('#resultnames');
								}
							////////////////////////////////////////////////
							////////////////////////////////////////////////
							////       ALOJAMIENTOS POR PROVINCIA       ////
							////////////////////////////////////////////////
							////////////////////////////////////////////////
							if(aux2.search(box) != -1) 
								{
								showprovinces=true;
								var id = $(this).find('attribute[name="Identificador"]').text();
								$('<div class="result"></div>').html('<p><span class="resultname"><a href="#" id="' + id + '" class="linked">'+aux+'</a></span> <span class="provname"><a href="#" id="' + id + '" class="linked">'+aux2+'</a></span></p>').appendTo('#resultprovinces');
								}
							});
						if (showprovinces==false)
							{$('#resultprovinces').remove();}
						if (shownames==false)
							{$('#resultnames').remove();}
						if (showprovinces==false && shownames==false)
							{$('<div id="nullresult"></div>').html('No se encontraron resultados').appendTo('#resultsdiv');}
						$('#loadingGif').remove();
						searching = 0;
						showInn();
						}});
					} 
				else 
					{
					$('<div id="loadingGif"></div>').html('<div class="center"><p>¡Espera a que termine la búsqueda actual!</p><img src="img/ajax-loader.gif" alt="Cargando..." /></div>').appendTo('#maindiv');
					}
				}
			else
				{
				$('#nullresult').html('Búsqueda demasiado corta');
				$('#resultprovinces').remove();
				$('#resultnames').remove();
				$('.result').remove();
				}
			}, 800);
		});
$('#finderb').click(function (e) 
		{event.preventDefault();
		});
	}



function loadResult() 
	{
	$.ajax(
		{
		type: "GET",
		url: "db/inns.xml",
		dataType: "xml",
		success: function(xml) 
		{
		$(xml).find('attribute[name="Titulo_es"]').each(function () 
			{
			var aux = $(this).find('string').text();
			$('<p></p>').html(aux).appendTo('#result');
			});
		}});
	}
$(document).ready(function () 
	{
	//loadResult();
	$('img[usemap]').rwdImageMaps();
	searchInn();
	});

