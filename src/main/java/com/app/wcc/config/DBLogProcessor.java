package com.app.wcc.config;

import org.springframework.batch.item.ItemProcessor;

import com.app.wcc.domain.PostCodeDetail;
 
public class DBLogProcessor implements ItemProcessor<PostCodeDetail, PostCodeDetail>
{
    public PostCodeDetail process(PostCodeDetail postcodedetail) throws Exception
    {
        System.out.println("Inserting postcode : " + postcodedetail);
        return postcodedetail;
    }
}