
package es.juvecyl.app.utils;

import java.util.ArrayList;
import es.juvecyl.app.R;

public class ProvinceSingleton {
    private static ProvinceSingleton mInstance = null;
    private ArrayList<Province> provinces;

    private ProvinceSingleton() {
        provinces = new ArrayList<Province>();
        provinces.add(new Province("Ávila",
                R.color.avila_color));
        provinces.add(new Province("Burgos",
                R.color.burgos_color));
        provinces.add(new Province("León",
                R.color.leon_color));
        provinces.add(new Province("Palencia",
                R.color.palencia_color));
        provinces.add(new Province("Salamanca",
                R.color.salamanca_color));
        provinces.add(new Province("Segovia",
                R.color.segovia_color));
        provinces.add(new Province("Soria",
                R.color.soria_color));
        provinces.add(new Province("Valladolid",
                R.color.valladolid_color));
        provinces.add(new Province("Zamora",
                R.color.zamora_color));
    }

    public static ProvinceSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new ProvinceSingleton();
        }
        return mInstance;
    }

    public ArrayList<Province> getProvinces() {
        return this.provinces;
    }
    
    public Province getProvince(String province){
        Province output = null;
        for(int i=0; i<this.provinces.size();i++){
            if(provinces.get(i).getName().equals(province)){
                output = provinces.get(i);
                break;
            }
        }
        return output;
    }
}
