package net.codealchemists.aem.forms.util;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Util for copying/cloning of bean.
 * cloning can happen for any bean, but copying data into other bean can happen only if both the
 * bean are having same parameters name. Like converting one type of SWSE address into the other type of SWSE address.
 * Created by tosheer.kalra on 15/08/2016.
 */
public final class BeanUtil {

    /**
     * Private constructor to deny direct instantiation.
     */
    private BeanUtil() { }

    /**
     * Create a toBean type instance with all writable properties from fromBean.
     *
     * @param fromBean
     *            original bean to be copy from
     * @param toBean
     *            destination bean
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     *
     * @return populated toBean
     */
    public static <T, V> V copyBean(T fromBean, V toBean)
            throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException {

        BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();

        Map<String, Object> propertiesMap =
                beanUtilsBean.getPropertyUtils().describe(fromBean);

        for (Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
            String property = entry.getKey();
            Object value = entry.getValue();
            if (value == null || property.equalsIgnoreCase("class")) {
                continue;
            }
            copyProperty(beanUtilsBean, toBean, property, value);
        }
        return toBean;
    }

    /**
     * Copy value to property into the toBean.
     */
    private static void copyProperty(BeanUtilsBean beanUtilsBean, Object toBean,
                                     String property, Object value)
            throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException {

        beanUtilsBean.copyProperty(toBean, property, value);
    }

}
