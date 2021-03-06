package com.matthausen.pricecomparison;

public class Product {
    private String image;
    private String title;
    private String currency;
    private String price;
    private String condition;
    private String shipping;
    private String viewItemURL;

    public Product(String img, String title, String currency, String price, String condition, String shipping, String url) {
        this.image=img;
        this.title=title;
        this.currency=currency;
        this.price=price;
        this.condition=condition;
        this.shipping=shipping;
        this.viewItemURL=url;
    }

    public String getImage() { return image; }

    public String getTitle() {
        return title;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public String getShipping() {
        return shipping;
    }

    public String getViewItemURL() {
        return viewItemURL;
    }
}
