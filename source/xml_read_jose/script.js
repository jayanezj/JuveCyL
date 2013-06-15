// JavaScript Document

function searchA()
{
	//Para envitar que se envíe el formulario normal
//Para enviar la consulta al teclear algo
var timer = null;
var searching = 0;
$('#finder').keyup(function() 
	{
    clearTimeout(timer);
	timer = setTimeout(function()
		{
		box = jQuery('#finder').val().toLowerCase();
		if (box.length > 2)
			{
			if (searching != 1)
				{
				jQuery('#result').html("<div class=\"center\" id=\"loadingGif\"><img src=\"img/ajax-loader.gif\" alt=\"Cargando...\" /></div>");
				searching = 1;
        $.get('getxml.php', function(xml)
        {
                // Search for the XML element you want, perform an action on each occurrence of found element
$(xml).find('attribute[name="Titulo_es"]').each(function()
			{
				var aux = $(this).find('string').text().toLowerCase();
				if (aux.search(box)!=-1)
				{
				$('<p></p>').html(aux).appendTo('#result');
							}
							});
		$('#loadingGif').remove();
				searching=0;
		});

				}
			else 
				{
				$('#result').html("<div class=\"center\"><p>¡Espera a que termine la búsqueda actual!</p><img src=\"img/ajax-loader.gif\" alt=\"Cargando...\" /></div>");
				}
			}
		}, 800);
	});
	
	}



function loadResult()
	{
        // Load data from proxy.php using GET request
        $.get('getxml.php', function(xml)
        {
                // Search for the XML element you want, perform an action on each occurrence of found element
$(xml).find('attribute[name="Titulo_es"]').each(function()
			{
				var aux = $(this).find('string').text();
				$('<p></p>').html(aux).appendTo('#result');
							});
		});

			
	}
	$(document).ready(function()
	{
	//loadResult();
	searchA();
	});