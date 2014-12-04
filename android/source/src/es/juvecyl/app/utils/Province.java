package es.juvecyl.app.utils;

public class Province {
    private String name;
    private int color;
    private int total;
    
    public Province(String name, int color) {
        super();
        this.name = name;
        this.color = color;
        this.total = 0;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    
    public void addLodging(){
        this.total += 1;
    }
    
}
