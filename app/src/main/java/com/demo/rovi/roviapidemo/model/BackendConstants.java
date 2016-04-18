package com.demo.rovi.roviapidemo.model;

public interface BackendConstants {
    String ROVI_BASE_URL = "http://cloud.rovicorp.com/";

    String CONSUMER_KEY = "86273bfd5ea822f5c455ddde5c527a3f884523955edeece10b7ae32efcd3963e";
    String CONSUMER_SECRET = "bc2a235f23470733fadcd54bc38d6c01216322d27506022e3e4d7852a8c20d28";

    int CONNECTING_TIMEOUT = 10; //10s
    int READ_TIMEOUT = 10; //10s

    int CURRENT_TEMPLATE_VERSION = 2;
    long TELEVISION_SERVICE = 1952967572;

    String URL_TO_LOAD_TEMPLATE_FILE = "http://cloud.rovicorp.com/template/v1/" +
            "{id}/{current_version}/templates.json";

    String STRING_ID = "id";
    String STRING_CURRENT_VERSION = "current_version";

}
