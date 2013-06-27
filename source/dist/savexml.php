<?php 
//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
////////////////
//////////////// SCRIPT CRON PARA OBTENER EL XML DE ALOJAMIENTOS JUVENILES DE DATOSABIERTOS
//////////////// SE EJECUTA CADA 15 MINUTOS Y ASÍ TENEMOS UNA VERSIÓN LOCAL DEL FICHER
//////////////// CON UNA CARGA MUCHO MÁS RÁPIDA
//////////////// 
//////////////// POR JOSÉ ANTONIO YÁÑEZ JIMÉNEZ -- HTTP://WWW.JIMENEZFRONTEND.ES
////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
////////////////
header('Access-Control-Allow-Origin: *');
// Location of XML content
$url = 'http://www.datosabiertos.jcyl.es/web/jcyl/risp/es/directorio/AlberguesJuveniles/1284198756067.xml';
$pointer = fopen($url, 'r');
$tempname = "/home/u639914671/public_html/db/inns.xml.tmp";
$filename = "/home/u639914671/public_html/db/inns.xml";
// If the file/content exists, loop through the file until end of file
if ($pointer)
	{
	$handle = fopen($tempname, "a+");
	ftruncate($handle,0);
	fclose($handle);
	while (!feof($pointer)) 
		{
		$line = fgets($pointer); // Get data from current line
		$handle = fopen($tempname, "a+");
		fwrite($handle,$line);
		fclose($handle);
		}
	fclose($pointer); // Close connection to file
	if (!copy($tempname, $filename)) {
    echo "Fallo en $filename...\n";
	}
	else
	{echo("Correcto");}
	}

?>