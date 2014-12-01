
package es.juvecyl.app;

import android.content.Context;

public class LodgingSingleton {
    private static LodgingSingleton mInstance = null;
    private Context context;
    private XMLDB db;

    private LodgingSingleton(Context context) {
        this.context = context;
        db = new XMLDB(this.context);
    }

    public static LodgingSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LodgingSingleton(context);
        }
        return mInstance;
    }

    public XMLDB getDB() {
        return this.db;
    }

    public void setDB() {
        db = new XMLDB(this.context);
    }
}
