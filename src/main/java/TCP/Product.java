package TCP;

public class Product implements java.io.Serializable {
    private static final long serialVersionUID = 20231107L;

    private int id;
    private String name;
    private double price;
    private int discount;

    public Product() { }
    public Product(int id, String name, double price, int discount) { /* gán 4 thuộc tính */ }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}