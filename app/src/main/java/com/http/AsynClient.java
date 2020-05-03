package com.http;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.ggmap.LoginFragment;
import com.example.ggmap.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class AsynClient {
    /**
     * Đây là URL của server
     * Mọi người muốn test thì nhớ nói mình mở server với copy URL mới nha
     * Vì URL ni là ip của cục router, nó nhảy mỗi ngày mỗi khác à
     * */
    private static final String BASE_URL = "http://116.98.208.64";

    /**
     * AsyncHttpClient là 1 lớp hỗ trợ các phương thức cho phép gọi request tới server
     *
     * */
    private static AsyncHttpClient client = new AsyncHttpClient();


    /**
     * Dưới đây là các phương thức như GET, POST được viết lại dưới
     * dạng phương thức của Java
     * Các tham số đầu vào lần lượt là
     * @url: {type: String} {meaning: url của route}
     * @params: {type: RequestParams} {meaning: đây là dữ liệu mà request sẽ gửi lên server, kiểu JSON}
     * @responseHandler: {type: AsyncHttpResponseHandler} {meaning: đối tượng này sẽ xử lý response mà
     * server gửi về, mình có thể custom nó được để phục vụ cho mục đích của mình}
     **/


    /**
     * VD: cần lấy thông tin của người dùng
     * RequestParams params = new RequestParams();
     * params.put("username", "Ramen"); => {
     *     "username": "Ramen"
     * }
     * get("/user-info", params, new mJsonHttpResponseHandler(contex) {
     * @Overide
     * onSuccess(int statuscode, header[] headers, JSONObject res) {
     *     txt.setText(res.toString());
     * }
     *
     * onFailure() {
     *     do  sth
     * }
     * })
     * */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(10000);
        client.get(getAbsoluteUrl(url), params, responseHandler);
        Log.v("AsyncClient - GET", getAbsoluteUrl(url));
    }
    /**
     * khi mà get và post, thì thường sẽ gửi dữ liệu thông qua body của request
     * params <=> body của request
     * VD: Login
     *
     * */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
        Log.v("AsyncClient - POST", getAbsoluteUrl(url));
    }


    /**
     * Dưới đây là các phương thức như DELETE, POST được viết lại dưới
     * dạng phương thức của Java
     * Các tham số đầu vào lần lượt là
     * @url: {type: String} {meaning: url của route}
     * @headers: {type: Array Header} {meaning: DELETE và PUT thường sẽ truyền dữ liệu ngay trên url,
     * vì vậy các headers ở đây chứa các dữ liệu muốn truy vấn ở server}
     * @responseHandler: {type: AsyncHttpResponseHandler} {meaning: đối tượng này sẽ xử lý response mà
     * server gửi về, mình có thể custom nó được để phục vụ cho mục đích của mình}
     **/
    public static void delete(String url, Header[] headers, Context context, AsyncHttpResponseHandler responseHandler) {
        client.delete(context, getAbsoluteUrl(url), headers, responseHandler);
        Log.v("AsyncClient - DELETE", getAbsoluteUrl(url));
    }

    public static void put(String url, Header[] headers, Context context, AsyncHttpResponseHandler responseHandler) {
        client.delete(context, getAbsoluteUrl(url), headers, responseHandler);
        Log.v("AsyncClient - PUT", getAbsoluteUrl(url));
    }

    /**
     * Phương thức này trả về 1 URL hoàn chỉnh
     * bao gồm URL của server và URL của route
     * cụ thể: muốn login, thì URL phải sư sau: {http://171.238.154.2}{/login}
     *                                                URL server ---- URL route
     * */
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    /**
     * Phương thức này sẽ hiển thị lỗi ra cho người dùng trong trường hơp
     * - Internet quá chậm, request bị quá thời gian
     * - Hoặc request tới 1 route không tồn tại
     * */
    public static void doOnFailure(Context context, int errorCode) {
        switch (errorCode) {
            case 0:
                Toast.makeText(context, R.string.code_0, Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText(context, R.string.code_404, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}