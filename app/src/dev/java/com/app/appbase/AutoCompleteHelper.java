package com.app.appbase;

import android.widget.Filter;
import android.widget.Filterable;

import com.medy.retrofitwrapper.WebRequest;
import com.medy.retrofitwrapper.WebServiceException;
import com.medy.retrofitwrapper.WebServiceResponseListener;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author Manish Kumar
 * @since 28/9/17
 */


/**
 * This class is use for setup auto complete using web service
 */
public abstract class AutoCompleteHelper implements Filterable {

    AppBaseActivity context;

    //WebRequest for autocomplete
    WebRequest webRequest;

    //this is call when webRequest is completed successfully.
    ResultAvailableListener resultAvailableListener;

    private WebServiceResponseListener webServiceResponseListener = new WebServiceResponseListener() {
        @Override
        public void onWebRequestCall(WebRequest webRequest) {
            context.onWebRequestCall(webRequest);
        }

        @Override
        public void onWebRequestResponse(WebRequest webRequest) {
//            parseResponse(webRequest);
        }

        @Override
        public void onWebRequestPreResponse(WebRequest webRequest) {

        }
    };
    //use for perform task and return result
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (webRequest != null) {
                webRequest.cancel();
            }
            FilterResults results = new FilterResults();
            if (constraint != null && !constraint.toString().trim().isEmpty()) {

                webRequest = getWebRequest(constraint.toString());

                webServiceResponseListener.onWebRequestCall(webRequest);
                try {
                    Response<ResponseBody> response = webRequest.generateCall(context).execute();
                    int responseCode = -1;
                    HashSet<String> cookies = null;
                    String webRequestResponse = null;
                    if (response != null) {
                        responseCode = response.code();
                        okhttp3.Response raw = response.raw();
                        if (!raw.headers("Set-Cookie").isEmpty()) {
                            cookies = new HashSet<>();
                            for (String header : raw.headers("Set-Cookie")) {
                                cookies.add(header);
                            }
                        }
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                webRequestResponse = response.body().string().trim();
                            } else if (response.errorBody() != null) {
                                webRequestResponse = response.errorBody().string().trim();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    webRequest.onRequestCompleted(null, responseCode, webRequestResponse, cookies);
                    webRequest.printResponseLog();
                    AppBaseResponseModel appBaseResponseModel = parseResponse(webRequest);
                    results.values = appBaseResponseModel;
                } catch (IOException | WebServiceException e) {
                    e.printStackTrace();
                    webRequest.onRequestCompleted(e, -1, null, null);
                    webRequest.printResponseLog();
                    results.values = e;
                } catch (OutOfMemoryError e) {
                    System.gc();
                    String msg = e.getMessage() == null ? "Exception in call webRequest" : e.getMessage();
                    WebServiceException e1 = new WebServiceException(msg,
                            WebServiceException.EXCEPTION_IN_CALL);

                    webRequest.onRequestCompleted(e1, -1, null, null);
                    webRequest.printResponseLog();
                    results.values = e;
                }
                webServiceResponseListener.onWebRequestResponse(webRequest);
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values == null || results.values instanceof AppBaseResponseModel) {
                if (getResultAvailableListener() != null) {
                    getResultAvailableListener().onResultAvailable((AppBaseResponseModel) results.values);
                }
            }
        }
    };


    /**
     * @param context
     */
    public AutoCompleteHelper(AppBaseActivity context) {
        this.context = context;
    }

    /**
     * use for get {@link AutoCompleteHelper#resultAvailableListener}
     *
     * @return
     */
    public ResultAvailableListener getResultAvailableListener() {
        return resultAvailableListener;
    }

    /**
     * use for set {@link ResultAvailableListener}
     *
     * @param resultAvailableListener
     */
    public void setResultAvailableListener(ResultAvailableListener resultAvailableListener) {
        this.resultAvailableListener = resultAvailableListener;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    public abstract WebRequest getWebRequest(String text);

    public abstract AppBaseResponseModel parseResponse(WebRequest webRequest);

    /**
     * ResultAvailableListener
     * use for notify when result is available
     */
    public interface ResultAvailableListener {
        void onResultAvailable(AppBaseResponseModel appBaseResponseModel);
    }
}
