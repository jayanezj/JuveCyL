package es.juvecyl.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
		private int[]provinces;

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
		public XMLDB(Context context)
			{
				try {
					this.provinces=new int[9];
					FileInputStream dataBase = context.openFileInput("db.xml");
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					this.dom = builder.parse(dataBase);
					NodeList root = this.dom.getDocumentElement().getElementsByTagName("element");
					this.totalLodgings = root.getLength();
					String province;
					for (int i = 0; i < this.totalLodgings; i++) {
						
						province = root.item(i).getChildNodes().item(30).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
						if(province.equals("Ávila"))
						{provinces[0]+=1;}
						if(province.equals("Burgos"))
						{provinces[1]+=1;}
						if(province.equals("León"))
						{provinces[2]+=1;}
						if(province.equals("Palencia"))
						{provinces[3]+=1;}
						if(province.equals("Salamanca"))
						{provinces[4]+=1;}
						if(province.equals("Segovia"))
						{provinces[5]+=1;}
						if(province.equals("Soria"))
						{provinces[6]+=1;}
						if(province.equals("Valladolid"))
						{provinces[7]+=1;}
						if(province.equals("Zamora"))
						{provinces[8]+=1;}
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		public XMLDB(Context context,boolean complete)
			{
				try {
					this.lodgings=new ArrayList<Lodging>();
					FileInputStream dataBase = context.openFileInput("db.xml");
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					this.dom = builder.parse(dataBase);
					NodeList root = this.dom.getDocumentElement().getElementsByTagName("element");
					this.totalLodgings = root.getLength();
					String title, province, desc, loc;
					ArrayList<String> phones, emails;
					for (int i = 0; i < this.totalLodgings; i++) {
						phones = new ArrayList<String>();
						emails = new ArrayList<String>();
						// PADRE<list> <atribute name="titulo_es"> <string>
						// <bloque CDATA>
						title = root.item(i).getChildNodes().item(1).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
						// PADRE<list> <atribute name="Provincia"> <string>
						// <Provincias>
						province = root.item(i).getChildNodes().item(30).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
						// PADRE<list> <atribute name="Provincia"> <string>
						// <Provincias>
						if (root.item(i).getChildNodes().item(6).getChildNodes().item(0).getChildNodes().getLength() < 1) {
							emails.add("");
						} else {
							for (int j = 0; j < root.item(i).getChildNodes().item(6).getChildNodes().item(0).getChildNodes().getLength(); j++) {
								// PADRE<list> <atribute name="email"> <array>
								// <string> <bloque CDATA>
								//emails.add(root.item(i).getChildNodes().item(6).getChildNodes().item(j).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
								//Log.d("Emails", root.item(i).getChildNodes().item(6).getChildNodes().item(j).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
								emails.add("asdsa");
							}
						}
						// PADRE<list> <atribute name="Descripcion_es"> <text>
						// <bloque CDATA>
						desc = root.item(i).getChildNodes().item(2).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
						// PADRE<list> <atribute name="localizacion"> <text>
						// <bloque CDATA>
						loc = root.item(i).getChildNodes().item(31).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
						Log.d("numeros", root.item(i).getChildNodes().item(4).getChildNodes().item(0).getChildNodes().getLength() + "");
						if (root.item(i).getChildNodes().item(4).getChildNodes().item(0).getChildNodes().getLength() < 1) {
							phones.add("");
						} else {
							for (int j = 0; j < root.item(i).getChildNodes().item(4).getChildNodes().item(0).getChildNodes().getLength(); j++) {
								// PADRE<list> <atribute name="telefono">
								// <array> <string> <bloque CDATA>
								//phones.add(root.item(i).getChildNodes().item(4).getChildNodes().item(j).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
								phones.add("111");
							}
						}
						Lodging newLodging=new Lodging(title, province, desc, loc, emails, phones);
						this.lodgings.add(newLodging);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		public Document getDOM()
			{
				return this.dom;
			}

	}
