package com.foodbe.constants;

import javax.persistence.Id;
import javax.persistence.PostPersist;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Logger;

public class PrefixedCodeListener {
    private static final Logger log = Logger.getLogger(PrefixedCodeListener.class.getName());

    @PostPersist
    public void assignCode(Object entity) {
        PrefixedCode cfg = entity.getClass().getAnnotation(PrefixedCode.class);
        if (cfg == null) return;

        try {
            Field idField = Arrays.stream(entity.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findFirst().orElse(null);
            if (idField == null) return;

            idField.setAccessible(true);
            Object idVal = idField.get(entity);
            if (idVal == null) return;

            String code = cfg.prefix() + String.valueOf(idVal);

            Field codeField = entity.getClass().getDeclaredField(cfg.field());
            codeField.setAccessible(true);
            Object current = codeField.get(entity);
            if (current == null || String.valueOf(current).isEmpty()) {
                codeField.set(entity, code);
            }
        } catch (Exception e) {
            log.warning("PrefixedCodeListener failed: " + e.getMessage());
        }
    }
}
