package com.wizvera.templet.model.converter;

import com.wizvera.templet.model.UserStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserStatus attribute) {
        if(attribute == null){
            return 0;
        }else {
            return attribute.getCode();
        }
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? new UserStatus(dbData) : null;
    }
}
