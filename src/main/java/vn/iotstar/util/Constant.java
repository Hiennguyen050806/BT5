package vn.iotstar.util;

public final class Constant {
    private Constant() {
    }

    public static final String SESSION_ACCOUNT = "account";
    public static final String SESSION_USERNAME = "username";
    public static final String COOKIE_REMEMBER = "remember_username";

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_MANAGER = 2;
    public static final int ROLE_USER = 3;

    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_LOCKED = 0;

    public static final int DEFAULT_PAGE_SIZE = 5;
}
