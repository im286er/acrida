
package unisx.retrofit2.converter.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} which uses Simple Framework for XML.
 * <p>
 * This converter only applies for class types. Parameterized types (e.g., {@code List<Foo>}) are
 * not handled.
 */
public final class XStreamConverterFactory extends Converter.Factory {

    public static XStreamConverterFactory create() {
        return new XStreamConverterFactory();
    }

    private final HashMap<Class<?>, XStream> xStreamMap;

    private XStreamConverterFactory() {
        xStreamMap = new HashMap<>();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (!(type instanceof Class)) {
            return null;
        }
        Class<?> cls = (Class<?>) type;
        return new XStreamResponseBodyConverter<>(cls, getXStream(cls));
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (!(type instanceof Class)) {
            return null;
        }
        Class<?> cls = (Class<?>) type;
        return new XStreamRequestBodyConverter<>(cls, getXStream(cls));
    }

    private XStream getXStream(Class<?> cls) {
        XStream xStream = xStreamMap.get(cls);
        if (xStream == null) {
            xStream = newXStream(cls);
            xStreamMap.put(cls, xStream);
        }
        return xStream;
    }

    private XStream newXStream(Class<?> type) {
        XStream xStream = new XStream(new DomDriver("UTF-8"));
        // 设置可忽略为在javabean类中定义的界面属性
        xStream.ignoreUnknownElements();
        xStream.registerConverter(new DefaultIntConverter());
        xStream.registerConverter(new DefaultLongConverter());
        xStream.registerConverter(new DefaultFloatConverter());
        xStream.registerConverter(new DefaultDoubleConverter());
        xStream.processAnnotations(type);

        return xStream;
    }

    private static class DefaultIntConverter extends IntConverter {

        @Override
        public Object fromString(String str) {
            int value;
            try {
                value = (Integer) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }

    private static class DefaultLongConverter extends LongConverter {
        @Override
        public Object fromString(String str) {
            long value;
            try {
                value = (Long) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }

    private static class DefaultFloatConverter extends FloatConverter {
        @Override
        public Object fromString(String str) {
            float value;
            try {
                value = (Float) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }

    private static class DefaultDoubleConverter extends DoubleConverter {
        @Override
        public Object fromString(String str) {
            double value;
            try {
                value = (Double) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }
}
