package net.gility.acrida.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import net.gility.acrida.content.SerializableCookie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Disk based CookieStore
 *
 * @author Alimy
 * @version 0.9 Created on 10/28/15.
 */
class PrefsCookieStore implements CookieStore {
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private final ConcurrentHashMap<URI, List<HttpCookie>> map;
    private final SharedPreferences cookiePrefs;

    /**
     * Construct a Disk based persistent cookie store
     *
     * @param context Context to attach cookie store to
     */
    public PrefsCookieStore(Context context) {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
        map = new ConcurrentHashMap<>();

        // Load any previously stored cookies into the store
        String storedCookieNames = cookiePrefs.getString(COOKIE_NAME_STORE, null);
        if (null != storedCookieNames) {
            String[] cookieNames = TextUtils.split(storedCookieNames, ",");
            for (String name : cookieNames) {
                String encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                if (null != encodedCookie) {
                    List<HttpCookie> decodedCookie = decodeCookies(encodedCookie);
                    if (null != decodedCookie) {
                        map.put(decodeUri(name), decodedCookie);
                    }
                }
            }
        }

        // Clear out expired cookies
        // clearExpired();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        uri = cookiesUri(uri);

        // add cookie into memory store
        List<HttpCookie> cookies = map.get(uri);
        if (cookies == null) {
            cookies = new ArrayList<>();
            map.put(uri, cookies);
        } else {
            cookies.remove(cookie);
        }
        cookies.add(cookie);
        updateToDisk(uri, cookies);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        if (uri == null) {
            throw new NullPointerException("uri == null");
        }

        List<HttpCookie> result = new ArrayList<>();

        // get cookies associated with given URI. If none, returns an empty list
        List<HttpCookie> cookiesForUri = map.get(uri);
        if (cookiesForUri != null) {
            for (Iterator<HttpCookie> i = cookiesForUri.iterator(); i.hasNext(); ) {
                HttpCookie cookie = i.next();
                if (cookie.hasExpired()) {
                    i.remove(); // remove expired cookies
                } else {
                    result.add(cookie);
                }
            }
        }

        // get all cookies that domain matches the URI
        for (Map.Entry<URI, List<HttpCookie>> entry : map.entrySet()) {
            if (uri.equals(entry.getKey())) {
                continue; // skip the given URI; we've already handled it
            }

            List<HttpCookie> entryCookies = entry.getValue();
            for (Iterator<HttpCookie> i = entryCookies.iterator(); i.hasNext(); ) {
                HttpCookie cookie = i.next();
                if (!HttpCookie.domainMatches(cookie.getDomain(), uri.getHost())) {
                    continue;
                }
                if (cookie.hasExpired()) {
                    i.remove(); // remove expired cookies
                } else if (!result.contains(cookie)) {
                    result.add(cookie);
                }
            }
        }

        return Collections.unmodifiableList(result);
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> result = new ArrayList<>();
        for (List<HttpCookie> list : map.values()) {
            for (Iterator<HttpCookie> i = list.iterator(); i.hasNext(); ) {
                HttpCookie cookie = i.next();
                if (cookie.hasExpired()) {
                    i.remove(); // remove expired cookies
                } else if (!result.contains(cookie)) {
                    result.add(cookie);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<URI> getURIs() {
        List<URI> result = new ArrayList<URI>(map.keySet());
        result.remove(null); // sigh
        return Collections.unmodifiableList(result);
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            throw new NullPointerException("cookie == null");
        }

        Boolean result = false;
        List<HttpCookie> cookies = map.get(cookiesUri(uri));
        if (cookies != null) {
            result = cookies.remove(cookie);
            // update disk store
            if (result) updateToDisk(uri, cookies);
        }
        return result;
    }

    @Override
    public boolean removeAll() {
        boolean result = !map.isEmpty();
        map.clear();
        removeCookies();
        return result;
    }

    /**
     * update cookie to disk store
     *
     * @param uri
     * @param cookies
     */
    private void updateToDisk(URI uri, List<HttpCookie> cookies) {
        // save cookie into persistent store
        SharedPreferences.Editor editor = cookiePrefs.edit();
        editor.putString(COOKIE_NAME_STORE, encodeUris(getURIs()));
        editor.putString(COOKIE_NAME_PREFIX + encodeUri(uri), encodeCookies(cookies));
        editor.commit();
    }

    /**
     * remove all cookies that store in disk
     */
    private void removeCookies() {
        SharedPreferences.Editor editor = cookiePrefs.edit();
        String storedCookieNames = cookiePrefs.getString(COOKIE_NAME_STORE, null);
        if (null != storedCookieNames) {
            String[] cookieNames = TextUtils.split(storedCookieNames, ",");
            for (String name : cookieNames) {
                editor.remove(COOKIE_NAME_PREFIX + name);
            }
            editor.remove(COOKIE_NAME_STORE);
        }
        editor.commit();
    }

    private boolean clearExpired() {
        boolean clearedAny = false;
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();

        for (ConcurrentHashMap.Entry<URI, List<HttpCookie>> entry : map.entrySet()) {
            URI uri = entry.getKey();
            List<HttpCookie> cookies = entry.getValue();
            boolean clearedAnyCookies = false;
            for (HttpCookie cookie : cookies) {
                if (cookie.hasExpired()) {
                    // Clear cookies from local store
                    cookies.remove(cookie);
                    // We've cleared at least one
                    clearedAnyCookies = true;
                }
            }// Clear cookies from persistent store
            if (clearedAnyCookies) {
                clearedAny = true;
                prefsWriter.putString(COOKIE_NAME_PREFIX + encodeUri(uri), encodeCookies(cookies));
            }
        }

        // Update names in persistent store
        if (clearedAny) {
            prefsWriter.putString(COOKIE_NAME_STORE, encodeUris(getURIs()));
        }
        prefsWriter.commit();

        return clearedAny;
    }

    private URI cookiesUri(URI uri) {
        if (uri == null) {
            return null;
        }
        try {
            return new URI("http", uri.getHost(), null, null);
        } catch (URISyntaxException e) {
            return uri; // probably a URI with no host
        }
    }

    private String encodeUri(URI uri) {
        if (null == uri) return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(bos);
            outputStream.writeObject(uri);
        } catch (IOException e) {
            return null;
        }

        return byteArrayToHexString(bos.toByteArray());
    }

    private URI decodeUri(String uriString) {
        byte[] bytes = hexStringToByteArray(uriString);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        URI uri = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(bis);
            uri = (URI) objectInputStream.readObject();
        } catch (IOException e) {
            // some code here
        } catch (ClassNotFoundException e) {
            // some code here
        }

        return uri;
    }

    /**
     * encode uris
     *
     * @param uris
     * @return
     */
    private String encodeUris(List<URI> uris) {
        ArrayList<String> encodedUris = new ArrayList<>(uris.size());
        for (URI item : uris) {
            encodedUris.add(encodeUri(item));
        }
        return TextUtils.join(",", encodedUris.toArray());
    }


    /**
     * Serializes Cookie object into String
     *
     * @param cookie cookie to be encoded, can be null
     * @return cookie encoded as String
     */
    private String encodeCookie(HttpCookie cookie) {
        if (cookie == null) return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(bos);
            outputStream.writeObject(new SerializableCookie(cookie));
        } catch (IOException e) {
            return null;
        }

        return byteArrayToHexString(bos.toByteArray());
    }

    /**
     * Returns cookie decoded from cookie string
     *
     * @param cookieString string of cookie as returned from http request
     * @return decoded cookie or null if exception occured
     */
    private HttpCookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        HttpCookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(bis);
            cookie = ((SerializableCookie) objectInputStream.readObject()).getCookie();
        } catch (IOException e) {
            // some code here
        } catch (ClassNotFoundException e) {
            // some code here
        }

        return cookie;
    }

    /**
     * encode cookies
     *
     * @param cookies
     * @return
     */
    private String encodeCookies(List<HttpCookie> cookies) {
        ArrayList<String> encodedCookies = new ArrayList<>(cookies.size());
        for (HttpCookie item : cookies) {
            encodedCookies.add(encodeCookie(item));
        }
        return TextUtils.join(",", encodedCookies.toArray());
    }

    /**
     * from encoded Cookies string decode encoded cookies
     *
     * @param encodedCookies
     * @return
     */
    private List<HttpCookie> decodeCookies(String encodedCookies) {
        String[] _encodedCookies = TextUtils.split(encodedCookies, ",");
        List<HttpCookie> result = new ArrayList<>(_encodedCookies.length);
        for (String encodedCookie : _encodedCookies) {
            HttpCookie cookie = decodeCookie(encodedCookie);
            if (cookie.hasExpired())    // if has expired then add
                result.add(decodeCookie(encodedCookie));
        }
        return result;
    }

    /**
     * Using some super basic byte array &lt;-&gt; hex conversions so we don't have to rely on any
     * large Base64 libraries. Can be overridden if you like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * Converts hex values from strings to byte arra
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
