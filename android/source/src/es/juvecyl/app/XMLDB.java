package es.juvecyl.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

public class XMLDB {
	private Document dom;
	private int totalLodgings;
	private ArrayList<Lodging> lodgings;
	private int[] provinces;

	public int getTotalLodgings() {
		return this.totalLodgings;
	}

	public ArrayList<Lodging> getLodgings() {
		return this.lodgings;
	}

	public int[] getProvinceCount() {
		return this.provinces;
	}

	public XMLDB(Context context) {
		try {
			this.lodgings = new ArrayList<Lodging>();
			FileInputStream dataBase = context.openFileInput("db.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.dom = builder.parse(dataBase);
			NodeList root = this.dom.getDocumentElement().getElementsByTagName(
					"element");
			this.totalLodgings = root.getLength();
			String title = null, province = null, description = null, aux = null, loc = null, phone = null, email = null;
			ArrayList<String> phones = null, emails = null;
			Node array_phones = null, array_emails = null;
			// //////////////////////////////////////////////////////////////////////////
			// GUARDAMOS EL NÚMERO DE ALOJAMIENTOS POR PROVINCIA
			// //////////////////////////////////////////////////////////////////////////
			this.provinces = new int[9];
			for (int i = 0; i < this.totalLodgings; i++) {
				title = null;
				province = description = aux = title = phone = loc = null;
				array_phones = null;
				phones = emails = new ArrayList<String>();
				for (int j = 0; j < root.item(i).getChildNodes().getLength(); j++) {
					aux = root.item(i).getChildNodes().item(j).getAttributes()
							.getNamedItem("name").getNodeValue();
					if (aux.equals("Provincia")) {
						province = root.item(i).getChildNodes().item(j)
								.getChildNodes().item(0).getChildNodes()
								.item(0).getNodeValue();
						if (province.equals("Ávila")) {
							provinces[0] += 1;
						}
						if (province.equals("Burgos")) {
							provinces[1] += 1;
						}
						if (province.equals("León")) {
							provinces[2] += 1;
						}
						if (province.equals("Palencia")) {
							provinces[3] += 1;
						}
						if (province.equals("Salamanca")) {
							provinces[4] += 1;
						}
						if (province.equals("Segovia")) {
							provinces[5] += 1;
						}
						if (province.equals("Soria")) {
							provinces[6] += 1;
						}
						if (province.equals("Valladolid")) {
							provinces[7] += 1;
						}
						if (province.equals("Zamora")) {
							provinces[8] += 1;
						}
					}
					if (aux.equals("Titulo_es")) {
						title = root.item(i).getChildNodes().item(j)
								.getChildNodes().item(0).getChildNodes()
								.item(0).getNodeValue();
					}
					if (aux.equals("Descripcion_es")) {
						try {
							description = root.item(i).getChildNodes().item(j)
									.getChildNodes().item(0).getChildNodes()
									.item(0).getNodeValue();
						} catch (NullPointerException e) {
						}
					}
					if (aux.equals("Localizacion")) {
						try {
							loc = root.item(i).getChildNodes().item(j)
									.getChildNodes().item(0).getChildNodes()
									.item(0).getNodeValue();
						} catch (NullPointerException e) {
						}

					}
					if (aux.equals("Telefono")) {
						array_phones = root.item(i).getChildNodes().item(j)
								.getChildNodes().item(0);
						for (int x = 0; x < array_phones.getChildNodes()
								.getLength(); x++)
							try {
								phone = array_phones.getChildNodes().item(x)
										.getChildNodes().item(0).getNodeValue();
								phones.add(phone);
							} catch (NullPointerException e) {
							}
					}
					if (aux.equals("Email")) {
						array_emails = root.item(i).getChildNodes().item(j)
								.getChildNodes().item(0);
						for (int x = 0; x < array_phones.getChildNodes()
								.getLength(); x++)
							try {
								email = array_emails.getChildNodes().item(x)
										.getChildNodes().item(0).getNodeValue();
								emails.add(email);
							} catch (NullPointerException e) {
							}
					}
				}
				Lodging newLodging = new Lodging(title, province, description,
						loc, phones, emails);
				this.lodgings.add(newLodging);
			}
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", e.toString());
		} catch (IOException e) {
			Log.e("IOException", e.toString());
		} catch (ParserConfigurationException e) {
			Log.e("ParserConfigurationException", e.toString());
		} catch (SAXException e) {
			Log.e("SAXException", e.toString());
		}
	}

	public Document getDOM() {
		return this.dom;
	}

}
