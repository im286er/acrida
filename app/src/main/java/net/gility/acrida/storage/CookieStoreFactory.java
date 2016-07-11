package net.gility.acrida.storage;

import android.content.Context;

import net.gility.acrida.android.ApplicationLoader;

import java.net.CookieStore;

/**
 * Cookie store Factory
 *
 * @author Alimy
 * @version 0.9 Created on 10/29/15.
 */
public final class CookieStoreFactory {
    public static final int TYPE_PREFERENCE_BASED = 1;
    public static final int TYPE_SQLITE_BASED = 2;
    // public static final int TYPE_PALDB_BASED = 3;

    public static CookieStore create() {
        return create(ApplicationLoader.instance);
    }
    public static CookieStore create(int type) {
        return create(ApplicationLoader.instance, type);
    }

    public static CookieStore create(Context context) {
        return new PrefsCookieStore(context);
    }

    public static CookieStore create(Context context, int type) {
        switch (type) {
            case TYPE_PREFERENCE_BASED:
                return new PrefsCookieStore(context);
            case TYPE_SQLITE_BASED:
                return new SqliteCookieStore(context);
            default:
                return new PrefsCookieStore(context);
        }
    }
}
