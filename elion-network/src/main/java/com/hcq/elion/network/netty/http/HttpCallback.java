
package com.hcq.elion.network.netty.http;


import io.netty.handler.codec.http.HttpResponse;

public interface HttpCallback {

    void onResponse(HttpResponse response);

    void onFailure(int statusCode, String reasonPhrase);

    void onException(Throwable throwable);

    void onTimeout();

    boolean onRedirect(HttpResponse response);
}
