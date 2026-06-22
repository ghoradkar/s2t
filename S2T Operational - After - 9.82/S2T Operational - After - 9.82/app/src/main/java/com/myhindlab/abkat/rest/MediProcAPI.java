package com.myhindlab.abkat.rest;

import com.myhindlab.abkat.activities.regularcampcreation.model.ProductStockListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MediProcAPI {

    @FormUrlEncoded
    @POST(ApplicationConstants.GetLabProductStockListByLab)
    Call<ProductStockListModel> getLabProductStockListByLab(@Field("LabId") String LabId);

    @FormUrlEncoded
    @POST(ApplicationConstants.AllocatedProductListForCamp)
    Call<ResponseBody> allocatedProductListForCamp(
            @Field("CampAllocatedProduct") String CampAllocatedProduct,
            @Field("CreatedBy") String CreatedBy,
            @Field("ProjectCode") String ProjectCode
    );

    @FormUrlEncoded
    @POST(ApplicationConstants.ConsumeProductListForCamp)
    Call<ResponseBody> consumeProductListForCamp(
            @Field("CampConsumeProduct") String CampAllocatedProduct,
            @Field("CreatedBy") String CreatedBy
    );

}
