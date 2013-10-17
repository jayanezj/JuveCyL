package es.juvecyl.app;

import java.util.ArrayList;

public class MainNav
	{
		private String title, bgcolor;
		private ArrayList<MainNav> navMain;

		public MainNav(String title, String bgcolor)
			{
				this.navMain = null;
				this.title = title;
				this.bgcolor = bgcolor;
			}

		public MainNav(int navType)
			{
				switch (navType)
					{
					case 1:
						this.navMain = new ArrayList<MainNav>();
						this.navMain.add(new MainNav("Búsqueda", "#4a4a4a"));
						this.navMain.add(new MainNav("Ávila", "#5b75c8"));
						this.navMain.add(new MainNav("Burgos", "#aead4f"));
						this.navMain.add(new MainNav("León", "#5bc1c8"));
						this.navMain.add(new MainNav("Palencia", "#aeaf73"));
						this.navMain.add(new MainNav("Salamanca", "#ae4f65"));
						this.navMain.add(new MainNav("Segovia", "#6499bf"));
						this.navMain.add(new MainNav("Soria", "#c38960"));
						this.navMain.add(new MainNav("Valladolid", "#9b78aa"));
						this.navMain.add(new MainNav("Zamora", "#724fae"));
						break;

					default:
						break;
					}
			}

		public ArrayList<MainNav> getNav()
			{
				return this.navMain;
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
