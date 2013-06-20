var map_origin=new Array(2);
var map_destiny=new Array(2);
var tits=new Array();
var ids=new Array();
var total_distance=0;
var map;
var aux;
function traceRoute()
{
$('#instructions').append('<h2 id="totalkm"></h2>');
window.map.travelRoute({
	origin: window.map_origin,
	destination: window.map_destiny,
	travelMode: 'driving',
	step: function(e) {
		var aux=e.distance.value;

				window.total_distance=window.total_distance+e.distance.value;
										if (aux>1000)
		{
			aux=aux/1000;
			aux=aux.toLocaleString()+" Kilómetros";
		}
		else
			{aux=aux.toLocaleString()+" Metros";}
		$('#instructions').append('<li>'+e.instructions+' - '+aux+'</li>');

		$('#instructions li:eq(' + e.step_number + ')').delay(450 * e.step_number).fadeIn(200, function() {
			window.map.drawPolyline({
				path: e.path,
				strokeColor: '#131540',
				strokeOpacity: 0.6,
				strokeWeight: 6
			
		 });  
						if (window.total_distance>1000)
		{
			window.total_distance=window.total_distance/1000;
			window.total_distance=window.total_distance.toLocaleString()+" Kilómetros";
		}
$('#totalkm').html('Distancia total: '+window.total_distance);
		});

	}
});

}

function loadMap()
	{
	$('#map').click(function(e)
		{
		$('<script src="js/vendor/gmaps.js"></script>').html('').appendTo('body');
		$('<div id="generalmap"></div>').html('').appendTo('body');
		$('#generalmap').css('height',$(window).height()-100);
		$('#generalmap').css('width',$(window).width()-100);
		$('#generalmap').css('position','absolute');
		$('#generalmap').css('top',25);
		$('#generalmap').css('left',25);
		$('#generalmap').css('display','none');
		$('#generalmap').fadeIn(800);
		$.when(window.map= new GMaps(
			{
			div: '#generalmap',
			lat: 41.652393,
			lng: -4.762573,
			zoom: 8
			})).done(function(){
			$('<a href="#" id="closemap"></a>').html('X<br />').prependTo('#generalmap');
			$('#closemap').bind('click', function(evt)
				{
				evt.preventDefault();
				$('#generalmap').fadeOut(800,function(){$('#generalmap').remove()});
				});
			loadMarks();
			});
		});
	} 
function addMark()
	{
	if (aux<ids.length)
		{
		var marker=$.ajax(
			{
			type: "GET",
			url: "db/locs.xml",
			dataType: "xml",
			success: function(xml) 
				{
				$(xml).find('loc').each(function ()
					{
					if($(this).find('id').text()==ids[aux])
						{
						window.map.addMarker(
							{
							lat: $(this).find('lat').text(),
							lng: $(this).find('long').text(),
							title: tits[aux],
							icon: 'https://www.mediterraneocomunidades.com/wp-content/uploads/2012/11/icono_casa.png',
							infoWindow: 
								{
								content: '<p>' + tits[aux] + '</p> <p>' + $(this).find('name').text() + '</p>'
								}
							});
						return false;
						}
					});
				},
			error: function(jqXHR,textStatus,errorThrown){console.log(errorThrown.toString());}
			});
		marker.done(function(){aux++;addMark();});
		}
	}
function loadMarks()
	{ 
	var getids=$.ajax(
		{
		type: "GET",
		url: "db/inns.xml",
		dataType: "xml",
		success: function(xml) 
			{
			$(xml).find('element').each(function ()
				{
				ids.push($(this).find('attribute[name="Identificador"]').text());
				tits.push($(this).find('attribute[name="Titulo_es"]').find('string').text());
				});
			}
		});
	getids.done(function ()
		{
		aux=0;
		addMark();
		loadResult();
		});
	}

	function loadResult() 
	{
	window.map.setContextMenu({
	control: 'map',
	options: [{
		title: 'Ruta desde aquí',
		name: 'route_origin',
		action: function(e) {
				window.map_origin[0]= e.latLng.lat();
				window.map_origin[1]= e.latLng.lng();
				if (window.map_origin[0]!== undefined && window.map_destiny[0]!== undefined)
				{
					traceRoute();
				}
		}
	}, {
		title: 'Ruta hasta aquí',
		name: 'route_destiny',
		action: function(e) {
				window.map_destiny[0]= e.latLng.lat();
				window.map_destiny[1]= e.latLng.lng();
								if (window.map_origin[0]!== undefined && window.map_destiny[0]!== undefined)
				{
					traceRoute();
				}
		}
	}//,{title: 'imprime',name: 'printer',action: function(e) {alert(window.map_origin[0]+" : "+window.map_origin[1]+" - "+window.map_destiny[0]+" : "+window.map_destiny[1]);}}
	]
});
	}


$(document).ready(function () 
	{
	loadMap();
	});