<?php 
header('Access-Control-Allow-Origin: *');
// proxy.php
// Set Content-type so jQuery accepts output as XML
header('Content-type: application/xml');
// Location of XML content
$url = 'http://www.datosabiertos.jcyl.es/web/jcyl/risp/es/directorio/AlberguesJuveniles/1284198756067.xml';
// Open the stream to access XML content as read-only with file pointer at beginning of file
$pointer = fopen($url, 'r');
// If the file/content exists, loop through the file until end of file
if ($pointer) {
    while (!feof($pointer)) {
        $line = fgets($pointer); // Get data from current line
        echo $line; // Output date from current line
    }
    fclose($pointer); // Close connection to file
}
?>