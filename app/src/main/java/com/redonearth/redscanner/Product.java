package com.redonearth.redscanner;

public class Product {

    private int _id;
    private String _productName;
    private int _quantity;

    public Product() {}

    public Product(int id, String productName, int quantity) {
        this._id = id;
        this._productName = productName;
        this._quantity = quantity;
    }

    public Product(String productName, int quantity) {
        this._productName = productName;
        this._quantity = quantity;
    }

    public int getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getProductName() {
        return this._productName;
    }

    public void setProductName(String productName) {
        this._productName = productName;
    }

    public int getQuantity() {
        return this._quantity;
    }

    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }
}
