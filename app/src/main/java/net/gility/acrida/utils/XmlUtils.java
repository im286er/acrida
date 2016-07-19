package net.gility.acrida.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import net.gility.acrida.model.AppException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * xml解析工具类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年9月27日 下午2:04:19
 * 
 */

public class XmlUtils {

    private final static String TAG = XmlUtils.class.getSimpleName();

    private final static HashMap<Class<?>, XStream> xStreamMap = new HashMap<>();

    /**
     * 将一个xml流转换为bean实体类
     * 
     * @param type
     * @param is
     * @return
     * @throws AppException
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(Class<T> type, InputStream is) {
        //*******************************************************
        XStream xmStream = new XStream(new DomDriver("UTF-8"));
        // 设置可忽略为在javabean类中定义的界面属性
        xmStream.ignoreUnknownElements();
        xmStream.registerConverter(new MyIntCoverter());
        xmStream.registerConverter(new MyLongCoverter());
        xmStream.registerConverter(new MyFloatCoverter());
        xmStream.registerConverter(new MyDoubleCoverter());
        xmStream.processAnnotations(type);
        //*******************************************************/
        //XStream xmStream = getXStream(type);
        //XStreamHolder.xStream.processAnnotations(type);
        T obj = null;
        try {
             obj = (T) xmStream.fromXML(is);
            // obj = (T) XStreamHolder.xStream.fromXML(is);
        } catch (Exception e) {
            TLog.log(TAG, "解析xml发生异常：" + e.getMessage());
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    TLog.log(TAG, "关闭流出现异常：" + e.getMessage());
                }
            }
        }
        return obj;
    }
    
    public static <T> T toBean(Class<T> type, byte[] bytes) {
        if (bytes == null) return null;
        return toBean(type, new ByteArrayInputStream(bytes));
    }

    private static XStream getXStream(Class<?> type) {
        XStream xStream = xStreamMap.get(type);
        if (xStream == null) {
            xStream = newXStream(type);
            xStreamMap.put(type, xStream);
        }

        return xStream;
    }

    private static XStream newXStream(Class<?> type) {
        XStream xStream = new XStream(new DomDriver("UTF-8"));
        // 设置可忽略为在javabean类中定义的界面属性
        xStream.ignoreUnknownElements();
        xStream.registerConverter(new MyIntCoverter());
        xStream.registerConverter(new MyLongCoverter());
        xStream.registerConverter(new MyFloatCoverter());
        xStream.registerConverter(new MyDoubleCoverter());
        xStream.processAnnotations(type);

        return xStream;
    }

    private static class MyIntCoverter extends IntConverter {

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

    private static class MyLongCoverter extends LongConverter {
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

    private static class MyFloatCoverter extends FloatConverter {
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

    private static class MyDoubleCoverter extends DoubleConverter {
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
