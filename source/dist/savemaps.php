<?php
class location {
		private $url;
		private $id;
		private $location;
		private $lat;
		private $long;
     	static private function curl_file_get_contents($URL)
     		{
	        $c = curl_init();
	        curl_setopt($c, CURLOPT_RETURNTRANSFER, 1);
	        curl_setopt($c, CURLOPT_URL, $URL);
	        $contents = curl_exec($c);
	        curl_close($c);
			if ($contents) return $contents;
            else return FALSE;
    		}
		public function __construct($id, $location)
  			{
    		$this->id = $id;
    		$this->location = $location;
    		$this->url ='http://maps.google.com/maps/api/geocode/json?sensor=false&address=';
			$this->url = self::$this->url.urlencode($this->location);
  			}
  		public function addcoords()
  			{
			$resp_json = self::curl_file_get_contents($this->url);
			$resp = json_decode($resp_json, true);
			if($resp['status']='OK')
				{
				if ($resp['results'][0]['formatted_address']!=""){$this->location=$resp['results'][0]['formatted_address'];}
				$this->lat=$resp['results'][0]['geometry']['location']['lat'];
				$this->long=$resp['results'][0]['geometry']['location']['lng'];
				}
			else
				{
				echo "false<br><br>";
				}
  			}
  		public function getId() {return $this->id;}
  		public function getLocation() {return $this->location;}
  		public function getLat() {return $this->lat;}
  		public function getLong() {return $this->long;}
      }
$locations=array();
$file = '/home/u639914671/public_html/db/inns.xml';
$file2 = '/home/u639914671/public_html/db/locs.xml';
if (file_exists($file))
	{
	$inns = simplexml_load_file($file);
	foreach ($inns->list->element as $element) 
		{
		$id = $element->attribute[0];
		$location = $element->attribute[31]->text;
		array_push($locations,new location($id,$location));
		}
	if (file_exists($file2))
		{
		foreach($locations as $locs)
			{
			$locsxml = simplexml_load_file($file2);
			$found=false;
			foreach ($locsxml as $locxml) 
				{
				if (strcmp($locxml->id,$locs->getId())==0)
					{
					$found=true;
					//echo ("salimos<br><br>");
					break;
					}
				}
			if ($found==false)
				{
				$locs->addcoords();
				$newloc = $locsxml->addChild('loc');
				$newloc->addChild('id',$locs->getId());
				$newloc->addChild('name',$locs->getLocation());
				$newloc->addChild('lat',$locs->getLat());
				$newloc->addChild('long',$locs->getLong());
				$locsxml->asXml($file2);
				}
			}
			echo ("Todo ok");
		} 
		else 
		{
			echo ("Error abriendo"+$file);
		}
	} 
	else 
	{
		echo ("Error abriendo"+$file);
	}  
?> 