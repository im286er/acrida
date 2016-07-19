
package unisx.retrofit2.converter.xstream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} which XStream for XML.
 */
public final class XStreamConverterFactory extends Converter.Factory {

    public static XStreamConverterFactory create() {
        return new XStreamConverterFactory();
    }

    private XStreamConverterFactory() {
        // empty
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (!(type instanceof Class)) {
            return null;
        }
        Class<?> cls = (Class<?>) type;
        return new XStreamResponseBodyConverter<>(cls);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (!(type instanceof Class)) {
            return null;
        }
        Class<?> cls = (Class<?>) type;
        return new XStreamRequestBodyConverter<>(cls);
    }
}
