package es.juvecyl.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

public class XMLDB
	{
	private Document dom;
	private int totalLodgings;
	private ArrayList<Lodging> lodgings;
	private int[] provinces;

	public int getTotalLodgings()
		{
		return this.totalLodgings;
		}

	public ArrayList<Lodging> getLodgings()
		{
		return this.lodgings;
		}

	public int[] getProvinceCount()
		{
		return this.provinces;
		}

	public XMLDB(Context context, String targetLodging)
		{
		try
			{
			this.lodgings = new ArrayList<Lodging>();
			FileInputStream dataBase = context.openFileInput("db.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.dom = builder.parse(dataBase);
			NodeList root = this.dom.getDocumentElement().getElementsByTagName("element");
			this.totalLodgings = root.getLength();
			String title, province, desc, loc;
			ArrayList<String> phones, emails;
			phones = new ArrayList<String>();
			emails = new ArrayList<String>();
			// //////////////////////////////////////////////////////////////////////////
			// GUARDAMOS LOS DATOS DEL ALOJAMIENTO ESCOGIDO
			// //////////////////////////////////////////////////////////////////////////
			for (int i = 0; i < this.totalLodgings; i++)
				{
				// ___________PADRE<list>________<atribute name="titulo_es">_____<string>__________<bloque CDATA>
				title = root.item(i).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
				if (title.toLowerCase(Locale.ENGLISH).equals(targetLodging.toLowerCase(Locale.ENGLISH)))
					{
					// ________________PADRE<list>_______<atribute name="Provincia">____<string>_____________<Provincias>
					province = root.item(i).getChildNodes().item(30).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
					// //////////////////////////////////////////////////////////////////////////
					// COMPROBAMOS QUE HAYA AL MENOS UN EMAIL A GUARDAR
					// SI NO AGREGAMOS UN EMAIL EN BLANCO
					// //////////////////////////////////////////////////////////////////////////
					// __________PADRE<list>_______<atribute name="email">____<array>_______________LONGITUD
					if (root.item(i).getChildNodes().item(6).getChildNodes().item(0).getChildNodes().getLength() < 1)
						{
						emails.add("");
						}
					else
						{
						// ______________________PADRE<list>_______<atribute name="email">____<array>_______________LONGITUD
						for (int j = 0; j < root.item(i).getChildNodes().item(6).getChildNodes().item(0).getChildNodes().getLength(); j++)
							{
							// _________________PADRE<list>___________<atribute name="email">____<array>______________<string>________________<bloque CDATA>
							emails.add(root.item(i).getChildNodes().item(6).getChildNodes().item(0).getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
							}
						}
					// __________PADRE<list>________<atribute name="Descripcion_es">_____<text>______________<bloque CDATA>
					desc = root.item(i).getChildNodes().item(2).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
					// __________PADRE<list>________<atribute name="localizacion">_______<text>______________<bloque CDATA>
					loc = root.item(i).getChildNodes().item(31).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
					// //////////////////////////////////////////////////////////////////////////
					// COMPROBAMOS QUE HAYA AL MENOS UN TELEFONO A GUARDAR
					// SI NO AGREGAMOS UN TELEFONO EN BLANCO
					// //////////////////////////////////////////////////////////////////////////
					// __________PADRE<list>______<atribute name="telefono">_________<array>__________LONGITUD
					if (root.item(i).getChildNodes().item(4).getChildNodes().item(0).getChildNodes().getLength() < 1)
						{
						phones.add("");
						}
					else
						{
						// ______________________PADRE<list>_______<atribute name="telefono">____<array>_______________LONGITUD
						for (int j = 0; j < root.item(i).getChildNodes().item(4).getChildNodes().item(0).getChildNodes().getLength(); j++)
							{
							// _________________PADRE<list>_______<atribute name="telefono">____<array>______________<string>________________<bloque CDATA>
							phones.add(root.item(i).getChildNodes().item(4).getChildNodes().item(0).getChildNodes().item(j).getChildNodes().item(0).getNodeValue());
							}
						}
					// //////////////////////////////////////////////////////////////////////////
					// AGREGAMOS EL ALOJAMIENTO Y SALIMOS DEL BUCLE
					// PUESTO QUE SOLO BUSCAMOS UN ALOJAMIENTO EN CONCRETO
					// //////////////////////////////////////////////////////////////////////////
					Lodging newLodging = new Lodging(title, province, desc, loc, emails, phones);
					this.lodgings.add(newLodging);
					break;
					}
				}
			}
		catch (FileNotFoundException e)
			{
			Log.e("FileNotFoundException", e.toString());
			}
		catch (IOException e)
			{
			Log.e("IOException", e.toString());
			}
		catch (ParserConfigurationException e)
			{
			Log.e("ParserConfigurationException", e.toString());
			}
		catch (SAXException e)
			{
			Log.e("SAXException", e.toString());
			}
		}

	public XMLDB(Context context)
		{
		try
			{
			this.lodgings = new ArrayList<Lodging>();
			FileInputStream dataBase = context.openFileInput("db.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.dom = builder.parse(dataBase);
			NodeList root = this.dom.getDocumentElement().getElementsByTagName("element");
			this.totalLodgings = root.getLength();
			String title, province;
			// //////////////////////////////////////////////////////////////////////////
			// GUARDAMOS EL NÚMERO DE ALOJAMIENTOS POR PROVINCIA
			// //////////////////////////////////////////////////////////////////////////
			this.provinces = new int[9];
			for (int i = 0; i < this.totalLodgings; i++)
				{
				province = root.item(i).getChildNodes().item(30).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
				if (province.equals("Ávila"))
					{
					provinces[0] += 1;
					}
				if (province.equals("Burgos"))
					{
					provinces[1] += 1;
					}
				if (province.equals("León"))
					{
					provinces[2] += 1;
					}
				if (province.equals("Palencia"))
					{
					provinces[3] += 1;
					}
				if (province.equals("Salamanca"))
					{
					provinces[4] += 1;
					}
				if (province.equals("Segovia"))
					{
					provinces[5] += 1;
					}
				if (province.equals("Soria"))
					{
					provinces[6] += 1;
					}
				if (province.equals("Valladolid"))
					{
					provinces[7] += 1;
					}
				if (province.equals("Zamora"))
					{
					provinces[8] += 1;
					}
				}
			// //////////////////////////////////////////////////////////////////////////
			// GUARDAMOS EL NOMBRE Y LA PROVINCIA DE CADA ALOJAMIENTO
			// //////////////////////////////////////////////////////////////////////////
			for (int i = 0; i < this.totalLodgings; i++)
				{
				// ___________PADRE<list>________<atribute name="titulo_es">_____<string>__________<bloque CDATA>
				title = root.item(i).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
				// ________________PADRE<list>_______<atribute name="Provincia">____<string>_____________<Provincias>
				province = root.item(i).getChildNodes().item(30).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
				Lodging newLodging = new Lodging(title, province, null, null, null, null);
				this.lodgings.add(newLodging);
				}
			}
		catch (FileNotFoundException e)
			{
			Log.e("FileNotFoundException", e.toString());
			}
		catch (IOException e)
			{
			Log.e("IOException", e.toString());
			}
		catch (ParserConfigurationException e)
			{
			Log.e("ParserConfigurationException", e.toString());
			}
		catch (SAXException e)
			{
			Log.e("SAXException", e.toString());
			}
		}

	public Document getDOM()
		{
		return this.dom;
		}

	}
