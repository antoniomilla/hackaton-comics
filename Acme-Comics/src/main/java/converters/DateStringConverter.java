
package converters;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jdk.nashorn.internal.runtime.Context;

/**
 * DO NOT REMOVE!
 * This class fixes rounding issues (01/01/1939 appearing as 31/12/1938) with Spring's default date conversion for form values.
 */
@Component
@Transactional
public class DateStringConverter implements GenericConverter
{
    private static final Set<ConvertiblePair> CONVERTIBLE_TYPES = new HashSet<>(Arrays.asList(new ConvertiblePair(Date.class, String.class), new ConvertiblePair(String.class, Date.class)));

    @Override
    public Set<ConvertiblePair> getConvertibleTypes()
    {
        return CONVERTIBLE_TYPES;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
    {
        if (source == null) return null;
        if (source instanceof Date) {
            DateTimeFormat annotation = sourceType.getAnnotation(DateTimeFormat.class);
            if (annotation == null) throw new IllegalStateException("Missing DateTimeFormat annotation! (" + sourceType.getName() + ")");
            if (annotation.pattern().isEmpty()) throw new IllegalStateException("Missing DateTimeFormat pattern attribute! (" + sourceType.getName() + ")");

            String pattern = annotation.pattern();
            return FastDateFormat.getInstance(pattern).format(((Date) source));
        } else {
            String formattedDate = (String) source;
            if (formattedDate.trim().isEmpty()) return null;

            DateTimeFormat annotation = targetType.getAnnotation(DateTimeFormat.class);
            if (annotation == null) throw new IllegalStateException("Missing DateTimeFormat annotation! (" + targetType.getName() + ")");
            if (annotation.pattern().isEmpty()) throw new IllegalStateException("Missing DateTimeFormat pattern attribute! (" + targetType.getName() + ")");

            String pattern = annotation.pattern();
            try {
                return FastDateFormat.getInstance(pattern).parse(formattedDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
