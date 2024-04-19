package com.atp.bdss.utils;

public class Constants {
    public static final String REQUEST_MAPPING_PREFIX = "/api";
    public static final String VERSION_API_V1 = "/v1";
    public static final String VERSION_API_V2 = "/v2";
    public interface ENTITY_STATUS {
        short ACTIVE = 1;
        short INACTIVE = 0;
    }

    public interface STATUS_PROJECT {
        short NOT_STARTED = 0;
        short COMING_SOON = 1;
        short IN_PROGRESS = 2;
        short COMPLETED = 3;
    }

    public interface STATUS_lAND {
        short NOT_STARTED = 0;
        short IN_PROGRESS = 1;
        short LOCKED = 1;

    }

    public interface STATUS_ACCOUNT {
        short INACTIVE = 0;
        short ACTIVE = 1;
    }
}