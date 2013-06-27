//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////// 
////// Maps layer for JuveCyL
////// version 0.7
////// Last Updated: 24/06/2013
////// Author: José Antonio Yáñez Jiménez -- http://www.jimenezfrontend.es
////// 
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//====================================================================
//====================================================================
//SUMMARY
//==============
//Global vars
//
//====================================================================
//====================================================================

//====================================================================
//====================================================================
// Global vars
//====================================================================
//====================================================================
var map_origin = new Array(2);
var map_destiny = new Array(2);
var tits = new Array();
var ids = new Array();
var total_distance = 0;
var map;
var aux;

//====================================================================
//====================================================================
	////////////////////////////////////////////////
	////////////////////////////////////////////////
	////   CREA UN DISEÑO DE RUTA EN EL MAPA    ////
	////////////////////////////////////////////////
	////////////////////////////////////////////////
//====================================================================
//====================================================================
function traceRoute() {
	if($('#mapinstructions').length) 
		{
		$('#mapinstructions').remove();
		}
	$('<div id="mapinstructions"></div>').html('').appendTo('#generalmap');
	$('#mapinstructions').append('<img id="closeinstructions" src="img/cerrar.png" class="fade" alt="cerrar"></img>');
	$('#closeinstructions').bind({
		click: function (evt) {
			map_origin = new Array(2);
			map_destiny = new Array(2);
			total_distance = 0;
			$('#mapinstructions').fadeOut(800, function () {
				$('#mapinstructions').remove()
			});
		}
	});
	$('#mapinstructions').append('<h2 id="totalkm"></h2>');
	$('#mapinstructions').append('<div style="height:80%; overflow:scroll; " id="instructions"></div>');
	window.map.travelRoute(
		{
		origin: window.map_origin,
		destination: window.map_destiny,
		travelMode: 'driving',
		step: function (e) 
			{
			var aux = e.distance.value;

			window.total_distance = window.total_distance + e.distance.value;
			if(aux > 1000) {
				aux = aux / 1000;
				aux = aux.toLocaleString() + " Kilómetros";
			} else {
				aux = aux.toLocaleString() + " Metros";
			}
			$('#instructions').append('<li>' + e.instructions + ' - ' + aux + '</li>');

			$('#instructions li:eq(' + e.step_number + ')').delay(450 * e.step_number).fadeIn(200, function () 
				{
				window.map.drawPolyline(
					{
					path: e.path,
					strokeColor: '#131540',
					strokeOpacity: 0.6,
					strokeWeight: 6
					});
				if(window.total_distance > 1000) 
					{
					window.total_distance = window.total_distance / 1000;
					window.total_distance = window.total_distance.toLocaleString() + " Kilómetros";
					}
				$('#totalkm').html('Distancia total: ' + window.total_distance);
				$('#mapinstructions').fadeIn(800);
				});
			}
		});
		map_origin = new Array(2);
		map_destiny = new Array(2);
		total_distance = 0;
	}

function loadMap() {
	$('#map').click(function (e) {
		e.stopPropagation();
		$('<span class="closemapspan"></span>').html('<img id="closemap" src="img/cerrar.png" class="fade" alt="cerrar"></img>').appendTo('body');
		$('<div id="generalmap"></div>').html('').appendTo('body');
		$('#generalmap').bind({
			click: function (evt) {
				evt.stopPropagation();
			}
		});
		$('#generalmap').css('height', '80%');
		$('#generalmap').css('width', '80%');
		$('#generalmap').css('position', 'absolute');
		$('#generalmap').fadeIn(800);
		$.when(window.map = new GMaps({
			div: '#generalmap',
			lat: 41.652393,
			lng: -4.762573,
			zoom: 8
		})).done(function () {
			$('#closemap').bind({
				click: function (evt) 
					{
					evt.preventDefault();
					$('.closemapspan').fadeOut(800, function () 
						{
						$('.closemapspan').remove()
						});
					$('#gmaps_context_menu').remove();
					$('#generalmap').fadeOut(800, function () 
						{
						$('#generalmap').remove()
						});
					},
				mouseenter: function () {
					$(this).fadeOut("fast");
					$(this).attr("src", "img/cerrarhover.png");
					$(this).fadeIn("fast");
				},
				mouseleave: function () {
					$(this).fadeOut("fast");
					$(this).attr("src", "img/cerrar.png");
					$(this).fadeIn("fast");
				}
			});
			$('#closemap').bind('click', function (evt) {
				evt.preventDefault();
				$('.closemapspan').fadeOut(800, function () {
					$('.closemapspan').remove()
				});
				$('#gmaps_context_menu').remove();
				$('#generalmap').fadeOut(800, function () {
					$('#generalmap').remove()
				});
			});
			loadMarks();
		});
	});
}

function addMark() {
	if(aux < ids.length) {
		var marker = $.ajax({
			type: "GET",
			url: "db/locs.xml",
			dataType: "xml",
			success: function (xml) {
				$(xml).find('loc').each(function () {
					if($(this).find('id').text() == ids[aux]) {
						window.map.addMarker({
							lat: $(this).find('lat').text(),
							lng: $(this).find('long').text(),
							title: tits[aux],
							icon: 'img/casa_mapa.png',
							infoWindow: {
								content: '<p>' + tits[aux] + '</p> <p>' + $(this).find('name').text() + '</p>'
							}
						});
						return false;
					}
				});
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log(errorThrown.toString());
			}
		});
		marker.done(function () {
			aux++;
			addMark();
		});
	}
}

function loadMarks() {
	var getids = $.ajax({
		type: "GET",
		url: "db/inns.xml",
		dataType: "xml",
		success: function (xml) {
			$(xml).find('element').each(function () {
				ids.push($(this).find('attribute[name="Identificador"]').text());
				tits.push($(this).find('attribute[name="Titulo_es"]').find('string').text());
			});
		}
	});
	getids.done(function () {
		aux = 0;
		addMark();
		loadResult();
	});
}

function loadResult() {
	window.map.setContextMenu({
		control: 'map',
		options: [{
				title: 'Ruta desde aquí',
				name: 'route_origin',
				action: function (e) {
					window.map_origin[0] = e.latLng.lat();
					window.map_origin[1] = e.latLng.lng();
					if(window.map_origin[0] !== undefined && window.map_destiny[0] !== undefined) {
						traceRoute();
					}
				}
			}, {
				title: 'Ruta hasta aquí',
				name: 'route_destiny',
				action: function (e) {
					window.map_destiny[0] = e.latLng.lat();
					window.map_destiny[1] = e.latLng.lng();
					if(window.map_origin[0] !== undefined && window.map_destiny[0] !== undefined) {
						traceRoute();
					}
				}
			}
		]
	});
	$('#gmaps_context_menu').bind("click", function (e) {
		e.stopPropagation();
	});
}


$(document).ready(function () {
	$(document).bind("contextmenu", function (e) {
		//return false;
	});
	$('body').click(function () {
		if($('#generalmap').length) {
			$('.closemapspan').fadeOut(800, function () {
				$('.closemapspan').remove()
			});
			$('#gmaps_context_menu').remove();
			$('#generalmap').fadeOut(800, function () {
				$('#generalmap').remove()
			});
		}
	});
	loadMap();

});