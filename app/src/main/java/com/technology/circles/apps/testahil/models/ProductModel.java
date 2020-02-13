package com.technology.circles.apps.testahil.models;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {

    private int id;
    private String price;
    private String company_id;
    private String cat_id;
    private String likes_counts;
    private String dislikes_counts;
    private String favorite_counts;
    private String comment_counts;
    private String views_count;
    private String followers_count;
    private String save_counts;
    private String have_offer;
    private String offer_type;
    private String offer_image;
    private String offer_active;
    private String rating;
    private String main_image;
    private String amount;
    private String title;
    private String desc;
    private int is_follow;
    private int is_like;
    private int is_dislike;
    private int is_favourite;
    private String company_title;
    private String category_title;
    private String offer_title;
    private String offer_des;
    private String offer_used;
    private ProductDataModel.CompanyModel company;
    private List<ProductDataModel.ProductImage> product_images;

    public int getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getCompany_id() {
        return company_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getLikes_counts() {
        return likes_counts;
    }

    public String getDislikes_counts() {
        return dislikes_counts;
    }

    public String getFavorite_counts() {
        return favorite_counts;
    }

    public String getComment_counts() {
        return comment_counts;
    }

    public String getViews_count() {
        return views_count;
    }

    public String getFollowers_count() {
        return followers_count;
    }

    public String getSave_counts() {
        return save_counts;
    }

    public String getHave_offer() {
        return have_offer;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public String getOffer_image() {
        return offer_image;
    }

    public String getOffer_active() {
        return offer_active;
    }

    public String getRating() {
        return rating;
    }

    public String getMain_image() {
        return main_image;
    }

    public String getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public int getIs_like() {
        return is_like;
    }

    public int getIs_dislike() {
        return is_dislike;
    }

    public int getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(int is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getCompany_title() {
        return company_title;
    }

    public String getCategory_title() {
        return category_title;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public String getOffer_des() {
        return offer_des;
    }

    public String getOffer_used() {
        return offer_used;
    }

    public ProductDataModel.CompanyModel getCompany() {
        return company;
    }

    public List<ProductDataModel.ProductImage> getProduct_images() {
        return product_images;
    }
}
