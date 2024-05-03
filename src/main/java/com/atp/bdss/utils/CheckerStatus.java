package com.atp.bdss.utils;

import java.util.Arrays;
import java.util.List;

public class CheckerStatus {

    public static boolean findStatusProject(Short status) {
        List<Short> validStatuses = Arrays.asList(
                Constants.STATUS_PROJECT.COMING_SOON,
                Constants.STATUS_PROJECT.IN_PROGRESS,
                Constants.STATUS_PROJECT.COMPLETED
        );
        return validStatuses.contains(status);
    }

    public static boolean findStatusLand(Short status) {
        List<Short> validStatuses = Arrays.asList(
                Constants.STATUS_lAND.COMING_SOON,
                Constants.STATUS_lAND.IN_PROGRESS,
                Constants.STATUS_lAND.LOCKING,
                Constants.STATUS_lAND.LOCKED
        );
        return validStatuses.contains(status);
    }

    public static boolean findStatusTransaction(Short status) {
        List<Short> validTransactions = Arrays.asList(
                Constants.STATUS_TRANSACTION.WAIT_FOR_CONFIRMATION,
                Constants.STATUS_TRANSACTION.PAYMENT_SUCCESS,
                Constants.STATUS_TRANSACTION.PAYMENT_FAILED
        );
        return validTransactions.contains(status);
    }
}
