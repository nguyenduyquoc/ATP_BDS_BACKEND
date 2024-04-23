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
        short COMING_SOON = 0;
        short IN_PROGRESS = 1;
        short COMPLETED = 2;
    }

    public interface STATUS_lAND {
        short COMING_SOON = 0;
        short IN_PROGRESS = 1;
        short LOCKING = 2;
        short LOCKED = 3;

    }

    public interface STATUS_ACCOUNT {
        short INACTIVE = 0;
        short ACTIVE = 1;
    }

    public interface STATUS_TRANSACTION {
        short WAIT_FOR_CONFIRMATION = 0;
        short PAYMENT_SUCCESS = 1;
        short PAYMENT_FAILED = 2;
    }
}