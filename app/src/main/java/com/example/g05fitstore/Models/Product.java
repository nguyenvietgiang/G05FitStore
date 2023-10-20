package com.example.g05fitstore.Models;

public class Product {
    public String Id;
    public String Name;
    public String Image;
    public String Discription;

    public Product() {
    }

    public Product(String Id, String Name, String Image, String Discription) {
        this.Id = Id;
        this.Name = Name;
        this.Image = Image;
        this.Discription = Discription;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String Discription) {
        this.Discription = Discription;
    }
}
