package ru.psb.masking.transformers.deterministic;

import ru.psb.masking.transformers.api.TransformContext;
import ru.psb.masking.transformers.api.Transformer;

public class DeterministicFakeFullNameTransformer implements Transformer {

    private static final String[] FIRST = {"Алексей","Иван","Дмитрий","Сергей","Михаил","Андрей","Николай","Виктор","Павел","Олег"};
    private static final String[] LAST  = {"Иванов","Петров","Сидоров","Козлов","Новиков","Морозов","Попов","Лебедев","Соколов","Волков"};

    @Override
    public String name() { return "DETERMINISTIC_FAKE_FULL_NAME"; }

    @Override
    public Object transform(Object value, TransformContext ctx) {
        if (value == null) return null;
        int hash = Math.abs(value.toString().hashCode());
        return FIRST[hash % FIRST.length] + " " + LAST[(hash / FIRST.length) % LAST.length];
    }
}
