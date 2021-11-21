package com.example.hanut.models;

public class SliderItem {
     String imageUrl;
     long timestamp;

     // los contructores no llevan VOID
     public SliderItem(){}

     public SliderItem(String imageUrl, long timestamp) {
          this.imageUrl = imageUrl;
          this.timestamp = timestamp;
     }

     public String getImageUrl() {
          return imageUrl;
     }

     public void setImageUrl(String imageUrl) {
          this.imageUrl = imageUrl;
     }

     public long getTimestamp() {
          return timestamp;
     }

     public void setTimestamp(long timestamp) {
          this.timestamp = timestamp;
     }
}
