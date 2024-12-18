package com.wzkris.common.security.field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.security.field.annotation.FieldPerms;
import com.wzkris.common.security.field.enums.FieldPerm;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.ExpressionUtils;

import java.io.IOException;

/**
 * 默认字段权限JSON序列化器
 *
 * @author wzkris
 * @since 2024/12/18 9：40
 */
public class DefaultFieldJsonSerializer extends JsonSerializer implements ContextualSerializer {

    private FieldPerms fieldPerms;

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
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        if (fieldPerms.perm() == FieldPerm.ALL || fieldPerms.perm() == FieldPerm.READ) {
            if (ExpressionUtils.evaluateAsBoolean(spel.parseExpression(fieldPerms.value()), this.createContext())) {
                serializers.defaultSerializeValue(value, gen);
            }
            else {
                // 无权限返回 null
                gen.writeNull();
            }
        }
        else {
            serializers.defaultSerializeValue(value, gen);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return this;
        }

        this.fieldPerms = property.getAnnotation(FieldPerms.class);
        if (this.fieldPerms == null) {
            this.fieldPerms = property.getContextAnnotation(FieldPerms.class);
        }

        if (this.fieldPerms == null) {
            throw new IllegalStateException("不能单独使用此类序列化");
        }

        return this;
    }
}
