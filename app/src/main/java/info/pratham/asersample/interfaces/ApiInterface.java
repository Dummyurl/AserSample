package info.pratham.asersample.interfaces;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("api")
    Call<ResponseBody> editUser(@Header("Content-Type") String header,@Body JSONObject jsonObject);
}
