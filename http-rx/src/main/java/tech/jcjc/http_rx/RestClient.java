package tech.jcjc.http_rx;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by CHENQIAO on 2017/12/07.
 */

public class RestClient {

    private final String URL;
//    private final Map<String, Object> PARAMS;

    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final RequestBody BODY;
    private final File FILE;


    public RestClient(String mUrl,
                      Map<String, Object> mParams,
                      RequestBody mRequestbody,
                      File file) {
        this.URL = mUrl;
        PARAMS.putAll(mParams);
        this.BODY = mRequestbody;
        this.FILE = file;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private Observable<String> request(HTTP_METHOD http_method) {

        RestService restService = RestCreator.getRestService();

        Observable<String> observable = null;
        switch (http_method) {

            case GET:
                observable = restService.get(URL, PARAMS);
                break;
            case POST:
                observable = restService.post(URL, PARAMS);
                break;
            case POST_RAW:
                observable = restService.postRaw(URL, BODY);
                break;
            case PUT:
                observable = restService.put(URL, PARAMS);
                break;
            case PUT_RAW:
                observable = restService.putRaw(URL, BODY);
                break;
            case DELETE:
                observable = restService.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body = MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                observable = restService.upload(URL, body);
                break;
            default:
                break;
        }

        return observable;

    }


    public final Observable<String> get() {
       return request(HTTP_METHOD.GET);
    }

    public final Observable<String> post() {
        if (BODY == null) {
            return request(HTTP_METHOD.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("request params must be null");
            }
            return request(HTTP_METHOD.POST_RAW);
        }
    }

    public final Observable<String> put() {

        if (BODY == null) {
            return request(HTTP_METHOD.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("request params must be null");
            }
            return request(HTTP_METHOD.PUT_RAW);

        }
    }

    public final Observable<String> delete() {
        return request(HTTP_METHOD.DELETE);
    }

    public final Observable<String> upload(){
        return request(HTTP_METHOD.UPLOAD);
    }

    public final Observable<ResponseBody> download(){
        return RestCreator.getRestService().download(URL, PARAMS);
    }


}
