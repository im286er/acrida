package net.gility.acrida.content;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpCookie;

/**
 * a serializeble HttpCookie;
 * @author Alimy
 * @version 0.9 Created by winsx on 10/28/15.
 */
public class SerializableCookie implements Serializable {
    private static final long serialVersionUID = 3110075144502681528L;

    private HttpCookie cookie;
    private HttpCookie serializeCookie;

    public SerializableCookie(HttpCookie cookie) {
        this.cookie = cookie;
    }

    public HttpCookie getCookie() {
        HttpCookie bestCookie = cookie;
        if (null != serializeCookie) {
            bestCookie =  serializeCookie;
        }
        return bestCookie;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(cookie.getName());
        out.writeObject(cookie.getValue());
        out.writeObject(cookie.getComment());
        out.writeObject(cookie.getCommentURL());
        out.writeObject(cookie.getDomain());
        out.writeObject(cookie.getPath());
        out.writeObject(cookie.getPortlist());
        out.writeInt(cookie.getVersion());
        out.writeLong(cookie.getMaxAge());
        out.writeBoolean(cookie.getDiscard());
        out.writeBoolean(cookie.getSecure());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        serializeCookie = new HttpCookie(name, value);
        serializeCookie.setComment((String) in.readObject());
        serializeCookie.setCommentURL((String) in.readObject());
        serializeCookie.setDomain((String) in.readObject());
        serializeCookie.setPath((String) in.readObject());
        serializeCookie.setPortlist((String) in.readObject());
        serializeCookie.setVersion(in.readInt());
        serializeCookie.setMaxAge(in.readLong());
        serializeCookie.setDiscard(in.readBoolean());
        serializeCookie.setSecure(in.readBoolean());
    }
}
