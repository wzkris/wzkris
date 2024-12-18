package com.wzkris.common.security.field;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.field.annotation.FieldPerms;
import com.wzkris.common.security.field.enums.FieldPerm;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.ExpressionUtils;

import java.io.IOException;

public class DefaultFieldJsonDeserializer extends JsonDeserializer implements ContextualDeserializer {

    private FieldPerms fieldPerms;

    private JavaType javaType;

    private final ExpressionParser spel = new SpelExpressionParser();

    private volatile static StandardEvaluationContext context;

    private StandardEvaluationContext createContext() {
        if (context == null) {
            synchronized (StandardEvaluationContext.class) {
                if (context == null) {
                    context = new StandardEvaluationContext();
                    context.setBeanResolver(new BeanFactoryResolver(SpringUtil.getFactory()));
                }
            }
        }
        return context;
    }

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        if (jp.currentToken() == JsonToken.VALUE_NULL) {
            return null;
        }

        if (fieldPerms.perm() == FieldPerm.ALL || fieldPerms.perm() == FieldPerm.WRITE) {
            if (ExpressionUtils.evaluateAsBoolean(spel.parseExpression(fieldPerms.value()), this.createContext())) {
                return jp.getCodec().readValue(jp, this.javaType);
            }
            else {
                // 无权限返回 null
                return null;
            }
        }

        return jp.getCodec().readValue(jp, this.javaType);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return this;
        }

        this.fieldPerms = property.getAnnotation(FieldPerms.class);
        if (this.fieldPerms == null) {
            this.fieldPerms = property.getContextAnnotation(FieldPerms.class);
        }

        if (this.fieldPerms == null) {
            throw new IllegalStateException("不能单独使用此类反序列化");
        }

        if (ctxt.getContextualType().isPrimitive()) {
            throw new IllegalStateException("不能在基本类型字段使用该类反序列化");
        }

        this.javaType = ctxt.getContextualType();
        return this;
    }

}
