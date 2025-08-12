package com.korih.finance_app.models.reddit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import lombok.Data;

@Data
public class RedditThing {
    private String kind;

    @JsonProperty("data")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "kind")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = com.korih.finance_app.models.reddit.data.CommentData.class, name = "t1"),
        @JsonSubTypes.Type(value = com.korih.finance_app.models.reddit.data.PostData.class, name = "t3"),
        @JsonSubTypes.Type(value = com.korih.finance_app.models.reddit.data.ListingData.class, name = "Listing"),
    })
    private RedditData data;
}
