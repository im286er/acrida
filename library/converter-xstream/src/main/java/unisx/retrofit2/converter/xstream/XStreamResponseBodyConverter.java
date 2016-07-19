package unisx.retrofit2.converter.xstream;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class XStreamResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Class<T> cls;

    XStreamResponseBodyConverter(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            T read = (T) XStreamConverterUtil.newXStream(cls).fromXML(value.byteStream());
            if (read == null) {
                throw new IllegalStateException("Could not deserialize body as " + cls);
            }
            return read;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            value.close();
        }
    }
}
