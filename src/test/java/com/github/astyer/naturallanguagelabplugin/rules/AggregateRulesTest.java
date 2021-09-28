package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Class;
import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Optional;

public class AggregateRulesTest extends TestCase {
    AggregateRules instance;

    @Override
    public void setUp() {
        instance = new AggregateRules();
    }

    @Test
    public void testRunAll(){
        Identifier id = new Class("training_example", 0, 0, Class.ClassType.Class, "Array",null);
        Optional<Result> result = instance.runAll(id);
        assertTrue(result.isPresent());
        assertEquals("NM* NPL", result.get().recommendation);
        assertEquals(1, result.get().priority);
    }

}