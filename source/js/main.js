////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////// 
////// Script layer for JuveCyL
////// version 1.2
////// Last Updated: 04/05/2014
////// Authors: Marcos de la Calle Samaniego
//////          Enrique Diego Blanco
//////          Ricardo Rodríguez Vaca
//////          José Antonio Yáñez Jiménez -- http://www.jimenezfrontend.es
////// 
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
//====================================================================
//====================================================================
//SUMMARY
//==============
//JQUERY EXTRA PLUGINS
//	Draggable div
//MY FUNCTIONS
//	MUESTRA UN ALOJAMIENTO EN CONCRETO
//	EVENTO PARA ELEGIR UN ALOJAMIENTO A MOSTRAR
//	BÚSQUEDA POR PROVINCIA
//	FUNCIÓN DE BÚSQUEDA
//	BÚSQUEDA DE ALOJAMIENTOS
//	LECTURA DEL URI PARA BUSCAR PERMALINKS
//	DOCUMENT READY
//
//====================================================================
//====================================================================
function detectmob() {
  if (navigator.userAgent.match(/Android/i) ||
    navigator.userAgent.match(/webOS/i) ||
    navigator.userAgent.match(/iPhone/i) ||
    navigator.userAgent.match(/iPad/i) ||
    navigator.userAgent.match(/iPod/i) ||
    navigator.userAgent.match(/BlackBerry/i) ||
    navigator.userAgent.match(/Windows Phone/i)) {
    return true;
  } else {
    return false;
  }
}
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////// 
////// JQUERY EXTRA PLUGINS
////// 
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
//====================================================================
//====================================================================
// Draggable div
//====================================================================
//====================================================================
(function($) {
  $.fn.drags = function(opt) {
    opt = $.extend({
      handle: "",
      cursor: "move"
    }, opt);
    if (opt.handle === "") {
      var $el = this;
    } else {
      var $el = this.find(opt.handle);
    }
    return $el.css('cursor', opt.cursor).on("mousedown", function(e) {
      if (opt.handle === "") {
        var $drag = $(this).addClass('draggable');
      } else {
        var $drag = $(this).addClass(
          'active-handle').parent().addClass('draggable');
      }
      var z_idx = $drag.css('z-index'),
        drg_h = $drag.outerHeight(),
        drg_w = $drag.outerWidth(),
        pos_y = $drag.offset().top + drg_h - e.pageY,
        pos_x = $drag.offset().left + drg_w - e.pageX;
      $drag.css('z-index', 1000).parents().on("mousemove", function(e) {
        $('.draggable').offset({
          top: e.pageY + pos_y - drg_h,
          left: e.pageX + pos_x - drg_w
        }).on("mouseup", function() {
          $(this).removeClass('draggable').css('z-index', z_idx);
        });
      });
      e.preventDefault(); // disable selection
    }).on("mouseup", function() {
      if (opt.handle === "") {
        $(this).removeClass('draggable');
      } else {
        $(this).removeClass('active-handle').parent().removeClass('draggable');
      }
    });
  }
})(jQuery);

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////// 
////// MY FUNCTIONS
////// 
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
//====================================================================
//====================================================================
////////////////////////////////////////////////
////////////////////////////////////////////////
////   MUESTRA UN ALOJAMIENTO EN CONCRETO   ////
////////////////////////////////////////////////
////////////////////////////////////////////////
//====================================================================
//====================================================================
function getInn(id) {
  $.ajax({
    type: "GET",
    url: "db/inns.xml",
    dataType: "xml",
    success: function(xml) {
      $(xml).find('element').each(function() {
        if ($(this).find('attribute[name="Identificador"]').text() == id) {
          $('.infotop').remove();
          $('.infodown').remove();
          ////////////////////////////////////////////////
          //DIV DESTINO SUPERIOR
          ////////////////////////////////////////////////
          $('<div class="infotop"></div>').html('').appendTo('body');
          ////////////////////////////////////////////////
          //CIERRE
          ////////////////////////////////////////////////
          $('<p id="closeinfo"></p>').html(
            '<img id="closedetails" src="img/cerrar.png"' +
            ' class="fade" alt="cerrar"></img>').appendTo('.infotop');
          $('#closedetails').bind({
            click: function(evt) {
              $('.infodown').fadeOut(800, function() {
                $('.infodown').remove()
              });
              $('.infotop').fadeOut(800, function() {
                $('.infotop').remove()
              });
            },
            mouseenter: function() {
              $(this).fadeOut("fast");
              $(this).attr("src", "img/cerrarhover.png");
              $(this).fadeIn("fast");
            },
            mouseleave: function() {
              $(this).fadeOut("fast");
              $(this).attr("src", "img/cerrar.png");
              $(this).fadeIn("fast");
            }
          });
          ////////////////////////////////////////////////
          // INFORMACIÓN DEL ALOJAMIENTO
          ////////////////////////////////////////////////
          //FOTO DEL ALBERGUE
          if ($(this).find('attribute[name="Imagen"]').find(
            'link').find('reference').text() == "") {
            $('.infotop').append('<img src="img/albergue_sin_imagen.png"' +
              ' alt="foto del albergue" id="picture">');
          } else {
            $('.infotop').append('<img src="' + $(this).find(
              'attribute[name="Imagen"]').find('link').find(
              'reference').text() + '" alt="foto del albergue" id="picture">');
          }
          // TÍTULO
          $('<h1 id="infoname"></h1>').html($(this).find(
            'attribute[name="Titulo_es"]').find(
            'string').text()).appendTo('.infotop');
          // DIRECCIÓN
          $('<p id="address"></p>').html($(this).find(
            'attribute[name="Localizacion"]').find(
            'text').text()).appendTo('.infotop');
          ////////////////////////////////////////////////
          // CONTACTO
          ////////////////////////////////////////////////
          $('.infotop').append('<div class="contact"></div>');
          // TELÉFONOS
          var phones = "";
          $(this).find('attribute[name="Telefono"]').find(
            'array').find('string').each(function() {
            phones += $(this).text() + " /";
          });
          phones = phones.substring(0, phones.length - 1);
          $('<p id="phone"></p>').html('<span><img src="img/tfno.png"' +
            ' alt="tfno" /></span>' + phones).appendTo('.contact');
          // FAX
          var faxes = "";
          $(this).find('attribute[name="Fax"]').find('array').find(
            'string').each(function() {
            faxes += $(this).text() + " /";
          });
          faxes = faxes.substring(0, faxes.length - 1);
          $('<p id="fax"></p>').html('<span><img src="img/fax.png"' +
            ' alt="fax" /></span>' + faxes).appendTo('.contact');
          // MAIL
          var mails = "";
          $(this).find('attribute[name="Email"]').find('array').find(
            'string').each(function() {
            mails += '<a href="mailto:' + $(this).text() + '">' +
              $(this).text() + '</a> /';
          });
          mails = mails.substring(0, mails.length - 1);
          $('<p id="mail"></p>').html('<span><img src="img/mail.png"' +
            ' alt="eMail" /></span>' + mails).appendTo('.contact');
          // WEB
          var webs = "";
          $(this).find('attribute[name="Web"]').find('array').find(
            'string').each(function() {
            webs += '<a href="http://' + $(this).text() + '" target="_blank">' +
              $(this).text() + '</a> /';
          });
          //webs=webs+' <a target="_blank" href="http://192.168.100.150/juvenil/?id='+id+'">Enlace permanente</a>';
          //webs += ' <a target="_blank" href="http://www.juvecyl.tuars.com/?id=' + id + '">Enlace permanente</a>';
          webs += ' <a target="_blank" href="http://www.juvecyl.es/?id=' + id +
            '">Enlace permanente</a>';
          $('<p id="web"></p>').html(
            '<span><img src="img/web.png" alt="Webs" /></span>' +
            webs).appendTo('.contact');
          ////////////////////////////////////////////////
          //NAVEGADOR ENLACES
          ////////////////////////////////////////////////
          $('<nav id="navigation"></nav>').html('').appendTo('.infotop');
          $('<ul></ul>').html(
            '<li><img class="navlink fade" id="nav_activity"' +
            ' src="img/boton_actividades.png" alt="Actividades" /></li>' +
            '<li><img class="navlink fade" id="nav_howtoget"' +
            ' src="img/boton_como_llegar.png" alt="Cómo llegar" /></li>' +
            '<li><img class="navlink fade" id="nav_description"' +
            ' src="img/boton_descripcion.png" alt="Descripción" /></li>' +
            '<li><img class="navlink fade" id="nav_equipment"' +
            ' src="img/boton_equipamiento.png" alt="Equipamiento" /></li>' +
            '<li><img class="navlink fade" id="nav_services"' +
            ' src="img/boton_servicios.png" alt="Servicios" /></li>'
          ).appendTo('#navigation');
          $('.navlink').bind({
            click: function(evt) {
              $('#sec_description').fadeOut(500);
              $('#sec_activity').fadeOut(500);
              $('#sec_howtoget').fadeOut(500);
              $('#sec_equipment').fadeOut(500);
              $('#sec_services').fadeOut(500);
              switch ($(this).attr("id")) {
                case "nav_activity":
                  $('#sec_activity').delay(505).fadeIn(500);
                  break;
                case "nav_description":
                  $('#sec_description').delay(505).fadeIn(500);
                  break;
                case "nav_howtoget":
                  $('#sec_howtoget').delay(505).fadeIn(500);
                  break;
                case "nav_equipment":
                  $('#sec_equipment').delay(505).fadeIn(500);
                  break;
                case "nav_services":
                  $('#sec_services').delay(505).fadeIn(500);
                  break;
              }
            },
            mouseenter: function(evt) {
              var ids = $(this).attr("src");
              ids = ids.substring(0, (ids.length - 4));
              $(this).fadeOut("fast");
              $(this).attr("src", ids + "hover.png");
              $(this).fadeIn("fast");
              $('.info_nav').fadeIn(200);
              $('.info_nav').html($(this).attr("alt"));
              $('.info_nav').css('left', evt.clientX - 30);
              $('.info_nav').css('top', evt.clientY + 40);
            },
            mouseleave: function(evt) {
              var ids = $(this).attr("src");
              ids = ids.substring(0, (ids.length - 9));
              $(this).fadeOut("fast");
              $(this).attr("src", ids + ".png");
              $(this).fadeIn("fast");
              $('.info_nav').fadeOut(200);
            }
          });
          ////////////////////////////////////////////////
          //DIV DESTINO INFERIOR
          ////////////////////////////////////////////////
          $('<div class="infodown"></div>').html('').appendTo('body');
          // DESCRIPCIÓN
          $('<section id="sec_description"></section>').html(
            '<h3>Descripción</h3><p>' + $(this).find(
              'attribute[name="Descripcion_es"]').find(
              'text').text() + '</p>').appendTo('.infodown');
          // ACTIVIDAD
          var act = '<ul>';
          $(this).find('attribute[name="Actividades"]').find(
            'array').find('string').each(function() {
            act += '<li>' + $(this).text() + '</li>';
          });
          act += '</ul>';
          $('<section id="sec_activity"></section>').html(
            '<h3>Actividades</h3><p>' + act + '</p>').appendTo('.infodown');
          $('#sec_activity').hide();
          // COMO LLEGAR
          $('<section id="sec_howtoget"></section>').html(
            '<h3>Cómo llegar</h3><p>' + $(this).find(
              'attribute[name="ComoLlegar"]').find(
              'text').text() + '</p>').appendTo('.infodown');
          $('#sec_howtoget').hide();
          // EQUIPAMIENTO
          var eq = '<ul>';
          $(this).find('attribute[name="Equipamiento"]').find('array').find(
            'string').each(function() {
            eq += '<li>' + $(this).text() + '</li>';
          });
          eq += '</ul>';
          $('<section id="sec_equipment"></section>').html(
            '<h3>Equipamiento</h3><p>' + eq + '</p>').appendTo('.infodown');
          $('#sec_equipment').hide();
          // SERVICIOS
          var serv = '<ul>';
          $(this).find('attribute[name="Servicios"]').find('array').find(
            'string').each(function() {
            serv += '<li>' + $(this).text() + '</li>';
          });
          serv += '</ul>';
          $('<section id="sec_services"></section>').html(
            '<h3>Servicios</h3><p>' + serv + '</p>').appendTo('.infodown');
          $('#sec_services').hide();
          $('.infotop').fadeIn(800);
          $('.infodown').fadeIn(800);
          $('.infotop').bind("click", function(e) {
            e.stopPropagation();
          });
          $('.infodown').bind("click", function(e) {
            e.stopPropagation();
          });
          $('.infotop').css('left', ($(window).width() / 2) - 400 + 'px');
          $('.infodown').css('left', ($(window).width() / 2) - 400 + 'px');
          $('.infotop').drags();
          $('.infodown').drags();
          if (/Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(
            navigator.userAgent)) {
            $('.infotop').css('left', '5px');
            $('.infodown').css('left', '5px');
            $('.infotop').css('width', $(window).width() - 10 + 'px');
            $('.infodown').css('width', $(window).width() - 10 + 'px');
          }
          return false;
        }
      });
    }
  });
}

//====================================================================
//====================================================================
////////////////////////////////////////////////
////////////////////////////////////////////////
//EVENTO PARA ELEGIR UN ALOJAMIENTO A MOSTRAR///
////////////////////////////////////////////////
////////////////////////////////////////////////
//====================================================================
//====================================================================
function showInn() {
  $('.linked').click(function(evt) {
    evt.preventDefault();
    var id = evt.target.id;
    getInn(id);
  });
}
//====================================================================
//====================================================================
////////////////////////////////////////////////
////////////////////////////////////////////////
////         BÚSQUEDA POR PROVINCIA         ////
////////////////////////////////////////////////
////////////////////////////////////////////////
//====================================================================
//====================================================================
function searchProvinces() {
  $('.linkedprov').click(function(evt) {
    evt.preventDefault();
    $('#textbox').val($(this).text());
    var box = $('#textbox').val();
    searchc(box);
  });
}

//====================================================================
//====================================================================
////////////////////////////////////////////////
////////////////////////////////////////////////
////          FUNCIÓN DE BÚSQUEDA           ////
////////////////////////////////////////////////
////////////////////////////////////////////////
//====================================================================
//====================================================================
function searchc(box) {
  $('.result').remove();
  $('<div id="loadingGif"></div>').html(
    '<img src="img/ajax-loader.gif" alt="Cargando..." />').appendTo('#maindiv');
  searching = 1;
  $.ajax({
    type: "GET",
    url: "db/inns.xml",
    dataType: "xml",
    success: function(xml) {
      ////////////////////////////////////////////////
      ////////////////////////////////////////////////
      ////CREAMOS Y BORRAMOS LAS CAPAS PERTINENTES////
      ////////////////////////////////////////////////
      ////////////////////////////////////////////////
      $('#nullresult').remove();
      $('#resultnames').remove();
      $('#resultprovinces').remove();
      $('<div id="resultnames"></div>').html(
        '<h2>Resultados por nombre</h2>').appendTo('#resultsdiv');
      $('<div id="resultprovinces"></div>').html(
        '<h2>Resultados por provincia</h2>').appendTo('#resultsdiv');
      var shownames = false;
      var showprovinces = false;
      $(xml).find('element').each(function() {
        var aux = $(this).find('attribute[name="Titulo_es"]').find(
          'string').text().toLowerCase();
        var aux2 = $(this).find('attribute[name="Provincia"]').find(
          'string').text().toLowerCase();
        var id = $(this).find('attribute[name="Identificador"]').text();
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        ////        ALOJAMIENTOS POR NOMBRE         ////
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        if (aux.search(box) != -1) {
          shownames = true;
          switch (aux2) {
            case 'salamanca':
              $('<div class="result sal"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span' +
                ' class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
            case 'ávila':
              $('<div class="result avi"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span' +
                ' class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
            case 'burgos':
              $('<div class="result bur"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
            case 'segovia':
              $('<div class="result seg"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
            case 'soria':
              $('<div class="result sor"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
            case 'valladolid':
              $('<div class="result val"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
            case 'palencia':
              $('<div class="result pal"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
            case 'zamora':
              $('<div class="result zam"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
            case 'león':
              $('<div class="result leo"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
            default:
              $('<div class="result"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultnames');
              break;
          }
        }
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        ////       ALOJAMIENTOS POR PROVINCIA       ////
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        if (aux2.search(box) != -1) {
          showprovinces = true;
          switch (aux2) {
            case 'salamanca':
              $('<div class="result sal"></div>').html(
                '<p><span class="provname"><a href="#" class="linkedprov">' +
                aux2 + '</a></span><span class="resultname"><a href="#" id="' +
                id + '" class="linked">' + aux +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
            case 'ávila':
              $('<div class="result avi"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
            case 'burgos':
              $('<div class="result bur"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
            case 'segovia':
              $('<div class="result seg"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
            case 'soria':
              $('<div class="result sor"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
            case 'valladolid':
              $('<div class="result val"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
            case 'palencia':
              $('<div class="result pal"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
            case 'zamora':
              $('<div class="result zam"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
            case 'león':
              $('<div class="result leo"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
            default:
              $('<div class="result"></div>').html(
                '<p><span class="resultname"><a href="#" id="' + id +
                '" class="linked">' + aux + '</a></span> <span ' +
                'class="provname"><a href="#" class="linkedprov">' + aux2 +
                '</a></span></p>').appendTo('#resultprovinces');
              break;
          }
        }
      });
      if (showprovinces == false) {
        $('#resultprovinces').remove();
      }
      if (shownames == false) {
        $('#resultnames').remove();
      }
      if (showprovinces == false && shownames == false) {
        $('<div id="nullresult"></div>').html(
          'No se encontraron resultados').appendTo('#resultsdiv');
      }
      $('#loadingGif').remove();
      searching = 0;
      showInn();
      searchProvinces();
      ////////////////////////////////////////////////
      ////////////////////////////////////////////////
      ////         REPOSICIONADO DEL PIE          ////
      ////////////////////////////////////////////////
      ////////////////////////////////////////////////
      $('#footerimg').removeAttr('style');
      $('#footerimg').css('top', $('.main-container').height() + 100);
      if ($('.main-container').height() + 100 < ($(window).height() - $('#footerimg').height())) {
        $('#footerimg').removeAttr('style');
      }
    }
  });
}
//====================================================================
//====================================================================
////////////////////////////////////////////////
////////////////////////////////////////////////
////        BÚSQUEDA DE ALOJAMIENTOS        ////
////////////////////////////////////////////////
////////////////////////////////////////////////
//====================================================================
//====================================================================
function searchInnByKey() {
  var timer = null;
  var searching = 0;
  $('#textbox').keydown(function(e) {
    if (e.keyCode == 10 || e.keyCode == 13) {
      e.preventDefault();
    }
    clearTimeout(timer);
    timer = setTimeout(function() {
      box = jQuery('#textbox').val().toLowerCase();
      if (box.length > 2) {
        if (searching != 1) {
          searchc(box);
        } else {
          $('<div id="loadingGif"></div>').html(
            '<div class="center"><p>¡Espera a que termine la búsqueda ' +
            'actual!</p><img src="img/ajax-loader.gif" alt="Cargando..." />' +
            '</div>').appendTo('#maindiv');
        }
      } else {
        $('#footerimg').removeAttr('style');
        $('#nullresult').html('Búsqueda demasiado corta');
        $('#resultprovinces').remove();
        $('#resultnames').remove();
        $('.result').remove();
      }
    }, 800);
  });
  $('#finderb').click(function(e) {
    e.preventDefault();
  });
}

//====================================================================
//====================================================================
////////////////////////////////////////////////
////////////////////////////////////////////////
//// LECTURA DEL URI PARA BUSCAR PERMALINKS ////
////////////////////////////////////////////////
////////////////////////////////////////////////
//====================================================================
//====================================================================
function getParameterByName(name) {
  name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
    results = regex.exec(location.search);
  return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
//====================================================================
//====================================================================
////////////////////////////////////////////////
////////////////////////////////////////////////
////             DOCUMENT READY             ////
////////////////////////////////////////////////
////////////////////////////////////////////////
//====================================================================
//====================================================================
$(document).ready(function() {
  if (detectmob()) {
    $('#apk').css('display', 'block');
    $('#closeapk').click(function(e) {
      e.preventDefault();
      $('#apk').css('display', 'none');
    });
  }
  $('body').click(function() {
    if ($('.infodown').length) {
      $('.infodown').fadeOut(800, function() {
        $('.infodown').remove()
      });
      $('.infotop').fadeOut(800, function() {
        $('.infotop').remove()
      });
    }
  });
  $('img[usemap]').rwdImageMaps();
  searchInnByKey();
  var res = getParameterByName('id');
  if (res != null) {
    getInn(res);
  }
});