package org.eclipse.ceylon.compiler.java.codegen;

/**
 * Enumerates compiler optimizations, which can be disabled and required 
 * by name.
 */
public enum Optimization {

    SpanOpIteration,
    SegmentOpIteration,
    ArrayIterationStatic,
    JavaArrayIterationStatic,
    TupleIterationStatic,
    ArrayIterationDynamic,
    TupleIterationDynamic,
    
    PowerUnroll
}
