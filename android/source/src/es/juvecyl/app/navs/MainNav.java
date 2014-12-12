
package es.juvecyl.app.navs;

import java.util.ArrayList;

import android.content.Context;
import es.juvecyl.app.R;
import es.juvecyl.app.utils.Province;
import es.juvecyl.app.utils.ProvinceSingleton;

public class MainNav
{
    private String title, bgcolor;
    private ArrayList<MainNav> navMain;

    public MainNav(String title, String bgcolor) {
        this.navMain = null;
        this.title = title;
        this.bgcolor = bgcolor;
    }

    public MainNav(Context res) {
        this.navMain = new ArrayList<MainNav>();
        this.navMain.add(
                new MainNav(
                        res.getString(R.string.coding_search),
                        (String) res.getText(R.color.search_color)));
        for (Province prov : ProvinceSingleton.getInstance().getProvinces()) {
            this.navMain.add(
                    new MainNav(
                            prov.getName(),
                            (String) res.getText(prov.getColor())));
        }
    }

    public ArrayList<MainNav> getNav() {
        return this.navMain;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBgColor() {
        return this.bgcolor;
    }
}
