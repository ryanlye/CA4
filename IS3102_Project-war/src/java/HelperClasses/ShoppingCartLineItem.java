package HelperClasses;
//###
public class ShoppingCartLineItem{
    private String id;
    private String SKU;
    private String name;
    private String imageURL;
    private double price;
    private int quantity;
    private long countryID;
    
    //Done by Ryan Lye (p1638611)
    
    public ShoppingCartLineItem() {
    }

    public ShoppingCartLineItem(String id, String SKU, String name, String imageURL, double price, int quantity, Long countryID) {
        this.id=id;
        this.SKU = SKU;
        this.name = name;
        this.imageURL = imageURL;
        this.price = price;
        this.quantity = quantity;
        this.countryID = countryID;
    }
    


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getCountryID() {
        return countryID;
    }

    public void setCountryID(long countryID) {
        this.countryID = countryID;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ShoppingCartLineItem) {
            ShoppingCartLineItem shoppingCartLineItem = (ShoppingCartLineItem) obj;
            result = shoppingCartLineItem.SKU.equals(this.SKU);
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.SKU != null ? this.SKU.hashCode() : 0);
        return hash;
    }

}
