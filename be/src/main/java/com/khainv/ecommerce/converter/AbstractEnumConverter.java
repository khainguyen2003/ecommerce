package com.khainv.ecommerce.converter;

import com.khainv.ecommerce.enums.BaseEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;
import java.util.stream.Stream;

@Converter
public abstract class AbstractEnumConverter <E extends Enum<E> & BaseEnum>
        implements AttributeConverter<E, Integer> {
    private final Class<E> enumClass;

    protected AbstractEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Integer convertToDatabaseColumn(E attribute) {
        return (attribute == null) ? null : attribute.getKey();
    }

    @Override
    public E convertToEntityAttribute(Integer dbData) {
        if(dbData == null) return null;

        return Stream.of(enumClass.getEnumConstants())
                .filter(constant -> Objects.equals(constant.getKey(), dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giá trị cho enum " + enumClass.getName() + " có key " + dbData));
    }
}
