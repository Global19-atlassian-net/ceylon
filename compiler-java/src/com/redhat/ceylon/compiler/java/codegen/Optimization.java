package com.redhat.ceylon.compiler.java.codegen;

/**
 * Enumerates compiler optimizations, which can be disabled and required 
 * by name.
 */
public enum Optimization {

    RangeOpIteration,
    SegmentOpIteration,
    ArrayIterationStatic,
    JavaArrayIterationStatic,
    TupleIterationStatic,
    ArrayIterationDynamic,
    TupleIterationDynamic,
    
    PowerUnroll
}
