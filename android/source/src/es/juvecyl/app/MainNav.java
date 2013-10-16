package es.juvecyl.app;

public class MainNav
	{
		private String title, bgcolor;

		public MainNav(String title, String bgcolor)
			{
				this.title = title;
				this.bgcolor = bgcolor;
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
