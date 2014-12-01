package es.juvecyl.app;

import java.util.ArrayList;

public class DetailsNav
	{
		private String title, bgcolor;
		private ArrayList<DetailsNav> navDetail;

		public DetailsNav(String title, String bgcolor)
			{
				this.navDetail = null;
				this.title = title;
				this.bgcolor = bgcolor;
			}

		public DetailsNav(int navType)
			{
				switch (navType)
					{
					case 1:
						this.navDetail = new ArrayList<DetailsNav>();
						this.navDetail.add(new DetailsNav("Portada", "#4a4a4a"));
						this.navDetail.add(new DetailsNav("Contacto", "#5b75c8"));
						this.navDetail.add(new DetailsNav("Servicios", "#aead4f"));
						this.navDetail.add(new DetailsNav("Atrás", "#5bc1c8"));
						break;

					default:
						break;
					}
			}

		public ArrayList<DetailsNav> getNav()
			{
				return this.navDetail;
			}

		public String getTitle()
			{
				return this.title;
			}

		public String getBgColor()
			{
				return this.bgcolor;
			}
	}
