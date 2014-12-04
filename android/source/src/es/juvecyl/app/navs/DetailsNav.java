
package es.juvecyl.app.navs;

import java.util.ArrayList;

import es.juvecyl.app.R;
import android.content.Context;

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

    public DetailsNav(Context res) {
        this.navDetail = new ArrayList<DetailsNav>();
        this.navDetail.add(new DetailsNav(
                (String) res.getText(R.string.coding_desc),
                (String) res.getText(R.color.contact_color)
                )
                );
        this.navDetail.add(new DetailsNav(
                (String) res.getText(R.string.coding_loc),
                (String) res.getText(R.color.contact_color)
                )
                );
        this.navDetail.add(new DetailsNav(
                (String) res.getText(R.string.coding_contact),
                (String) res.getText(R.color.contact_color)
                )
                );
        this.navDetail.add(new DetailsNav(
                (String) res.getText(R.string.coding_fav),
                (String) res.getText(R.color.fav_color)
                )
                );
        this.navDetail.add(new DetailsNav(
                (String) res.getText(R.string.coding_back),
                (String) res.getText(R.color.back_color)
                )
                );
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
