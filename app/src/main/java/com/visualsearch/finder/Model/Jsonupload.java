package com.visualsearch.finder.Model;

class Jsonupload {
    String categoryId,categoryName,productDescription,prdouctID,productURL,productImages,productStocks,
            productName,productPrice,productSearch,websiteName,colors;


    public Jsonupload() {

    }

    public Jsonupload(String categoryId, String categoryName, String productDescription, String prdouctID, String productURL, String productImages, String productStocks, String productName, String productPrice, String productSearch, String websiteName, String colors) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productDescription = productDescription;
        this.prdouctID = prdouctID;
        this.productURL = productURL;
        this.productImages = productImages;
        this.productStocks = productStocks;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productSearch = productSearch;
        this.websiteName = websiteName;
        this.colors = colors;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPrdouctID() {
        return prdouctID;
    }

    public void setPrdouctID(String prdouctID) {
        this.prdouctID = prdouctID;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

    public String getProductStocks() {
        return productStocks;
    }

    public void setProductStocks(String productStocks) {
        this.productStocks = productStocks;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSearch() {
        return productSearch;
    }

    public void setProductSearch(String productSearch) {
        this.productSearch = productSearch;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }
}
