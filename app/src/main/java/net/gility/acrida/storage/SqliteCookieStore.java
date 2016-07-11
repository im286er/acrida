package net.gility.acrida.storage;

import android.content.Context;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Sqlite based cookie store
 *
 * @author Alimy
 * @version 0.9 Created on 10/29/15.
 */
class SqliteCookieStore implements CookieStore{
    private Context mContext;

    public SqliteCookieStore(Context context) {
        mContext = context;
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {

    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return null;
    }

    @Override
    public List<HttpCookie> getCookies() {
        return null;
    }

    @Override
    public List<URI> getURIs() {
        return null;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }
}
