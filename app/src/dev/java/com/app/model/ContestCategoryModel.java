package com.app.model;

import com.app.appbase.AppBaseModel;
import com.utilities.DeviceScreenUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContestCategoryModel extends AppBaseModel {

    long id;
    String name;
    String description;
    String image;
    String is_discounted;
    String discount_image;
    int discount_image_width;
    int discount_image_height;

    List<ContestModel> contests;
    List<ContestModel> practice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return getValidString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return getValidString(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIs_discounted() {
        return getValidString(is_discounted);
    }

    public void setIs_discounted(String is_discounted) {
        this.is_discounted = is_discounted;
    }

    public boolean isDiscounted(){
        return getIs_discounted().equals("Y");
    }

    public String getDiscount_image() {
        return getValidString(discount_image);
    }

    public void setDiscount_image(String discount_image) {
        this.discount_image = discount_image;
    }

    public int getDiscount_image_width() {
        return discount_image_width;
    }

    public void setDiscount_image_width(int discount_image_width) {
        this.discount_image_width = discount_image_width;
    }

    public int getDiscount_image_height() {
        return discount_image_height;
    }

    public void setDiscount_image_height(int discount_image_height) {
        this.discount_image_height = discount_image_height;
    }

    public List<ContestModel> getContests() {
        return contests;
    }

    public void setContests(List<ContestModel> contests) {
        this.contests = contests;
    }

    public List<ContestModel> getPractice() {
        return practice;
    }

    public void setPractice(List<ContestModel> practice) {
        this.practice = practice;
    }



    public int[] getDiscountImageSizeForCategory(){
        int deviceWidth = DeviceScreenUtil.getInstance().convertDpToPixel(80.0f);
        int deviceHeight = DeviceScreenUtil.getInstance().convertDpToPixel(30.0f);

        int imageWidth = deviceWidth;
        int imageHeight = deviceHeight;

        long width = getDiscount_image_width();
        long height = getDiscount_image_height();

        if (width > height) {
            float ratio = ((float) height) / ((float) width);

            imageHeight = Math.round(imageWidth * ratio);

            if(imageHeight>deviceHeight){
                imageHeight=deviceHeight;
                ratio = ((float) width) / ((float) height);
                imageWidth = Math.round(imageHeight * ratio);
            }

        } else if (height > width) {
            float ratio = ((float) width) / ((float) height);

            imageWidth = Math.round(imageHeight * ratio);

            if(imageWidth>deviceWidth){
                imageWidth=deviceWidth;
                ratio = ((float) height) / ((float) width);
                imageHeight = Math.round(imageWidth * ratio);
            }
        } else {
            imageWidth=Math.min(imageWidth,imageHeight);
            imageHeight = imageWidth;
        }
        return new int[]{imageWidth,imageHeight};
    }

    public void sortContest(int tag,int sortBy){
        if(contests!=null && contests.size()>0){
            listSorter.setTag(tag);
            listSorter.setSortType(sortBy);
            Collections.sort(contests, listSorter);
        }
    }


    ContestFilter listSorter = new ContestFilter() {


        @Override
        public int compare(ContestModel o1, ContestModel o2) {

           if(tag==1){
               if (currentSortType == 1) {
                   return Double.compare(o2.getTotal_price(),o1.getTotal_price());
               } else {
                   return Double.compare(o1.getTotal_price(),o2.getTotal_price());
               }
           }else if(tag==2){
               if (currentSortType == 1) {
                   return Double.compare(o2.getEntry_fees(),o1.getEntry_fees());
               } else {
                   return Double.compare(o1.getEntry_fees(),o2.getEntry_fees());
               }
           }else if(tag==3){
               if (currentSortType == 1) {
                   return Long.compare(o2.getTotal_winners(),o1.getTotal_winners());
               } else {
                   return Long.compare(o1.getTotal_winners(),o2.getTotal_winners());
               }
           }else{
               return 1;
           }
        }
    };


    public class ContestFilter implements Comparator<ContestModel>{

        int tag;

        int currentSortType;

        public void setSortType(int sortType) {
            this.currentSortType = sortType;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        @Override
        public int compare(ContestModel o1, ContestModel o2) {
            return 1;
        }
    }

}
