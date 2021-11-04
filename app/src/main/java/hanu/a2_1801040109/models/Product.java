package hanu.a2_1801040109.models;

public class Product {
    private long id;
    private String thumbnail;
    private String name;
    private double unitPrice;
    private int quantity;


    public Product(long id, String thumbnail, String name, double unitPrice, int quantity) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Product(long id, String thumbnail, String name, double unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public Product(String thumbnail, String name, double unitPrice, int quantity) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public  void increaseQty(){
        this.quantity++;
    }

    public  boolean decreaseQty(){
        if(quantity> 0){
            this.quantity--;
            return true;
        }else{
            return false;
        }
    }

    public double getPrice() {
        return  this.unitPrice*this.quantity;
    }
}
