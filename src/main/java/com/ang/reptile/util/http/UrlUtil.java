package com.ang.reptile.util.http;

public class UrlUtil {
    public static String generateUrl(String protocol,String domain,String uri){
        uri = uri.startsWith("/") ? uri : "/" + uri;
        return protocol + "://" + domain + uri;
    }
}
