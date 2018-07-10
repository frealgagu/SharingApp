package com.example.sharingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Item class
 */
@SuppressWarnings("WeakerAccess")
public class Item extends Observable {

    private String title;
    private String maker;
    private String description;
    private Dimensions dimensions;
    private String status;
    private Float minimumBid;
    private User borrower;
    private String ownerId;
    private transient Bitmap image;
    private String imageBase64;
    private String id;

    public Item(String title, String maker, String description, String ownerId, String minimumBid, Bitmap image, String id) {
        this.title = title;
        this.maker = maker;
        this.description = description;
        this.dimensions = null;
        this.ownerId = ownerId;
        this.status = "Available";
        this.minimumBid = Float.valueOf(minimumBid);
        this.borrower = null;
        addImage(image);

        if (id == null){
            setId();
        } else {
            updateId(id);
        }
    }

    public String getId(){
        return this.id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
        notifyObservers();
    }

    public void updateId(String id){
        this.id = id;
        notifyObservers();
    }

    public void setTitle(String title) {
        this.title = title;
        notifyObservers();
    }

    public String getTitle() {
        return title;
    }

    public void setMaker(String maker) {
        this.maker = maker;
        notifyObservers();
    }

    public String getMaker() {
        return maker;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyObservers();
    }

    public String getDescription() {
        return description;
    }

    public Float getMinBid() {
        return this.minimumBid;
    }

    public void setMinBid(Float minimumBid) {
        this.minimumBid = minimumBid;
        notifyObservers();
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        notifyObservers();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setDimensions(String length, String width, String height) {
        dimensions = new Dimensions(length, width, height);
        notifyObservers();
    }

    public String getLength(){
        return dimensions.getLength();
    }

    public String getWidth(){
        return dimensions.getWidth();
    }

    public String getHeight(){
        return dimensions.getHeight();
    }

    public void setStatus(String status) {
        this.status = status;
        notifyObservers();
    }

    public String getStatus() {
        return status;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
        notifyObservers();
    }

    public User getBorrower() {
        return borrower;
    }

    public String getBorrowerUsername() {
        if (borrower != null){
            return borrower.getUsername();
        }
        return null;
    }

    public void addImage(Bitmap newImage){
        if (newImage != null) {
            image = newImage;
            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            newImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);

            byte[] b = byteArrayBitmapStream.toByteArray();
            imageBase64 = Base64.encodeToString(b, Base64.DEFAULT);
        }
        notifyObservers();
    }

    public Bitmap getImage(){
        if (image == null && imageBase64 != null) {
            byte[] decodeString = Base64.decode(imageBase64, Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
            notifyObservers();
        }
        return image;
    }
}

