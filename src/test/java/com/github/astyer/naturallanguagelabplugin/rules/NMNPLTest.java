package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Class;
import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Optional;

public class NMNPLTest extends TestCase {
    NMNPL instance;

    @Override
    public void setUp() {
        instance = new NMNPL();
    }

    @Test
    public void testMatchViolation(){
        Identifier id = new Variable("training_example", 0, 0, "Array",null);
        Optional<Result> result = id.accept(instance);
        assertTrue(result.isPresent());
        assertEquals("NM* NPL", result.get().recommendation);
        assertEquals(1, result.get().priority);
    }

    @Test
    public void testMatchNoViolation(){
        Identifier id = new Variable("training_examples", 0, 0, "Array",null);
        Optional<Result> result = id.accept(instance);
        assertFalse(result.isPresent());
    }

    @Test
    public void testNoMatch(){
        Identifier id = new Variable("training_examples", 0, 0, "Int",null);
        Optional<Result> result = id.accept(instance);
        assertFalse(result.isPresent());
    }
}