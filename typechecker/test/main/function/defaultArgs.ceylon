class Z1() extends Y1() {
    shared actual default void foo(String str, Integer int) {}
}
class Y1() extends X1() {
    shared actual default void foo(String str, Integer int) {}
}
class X1() {
    shared default void foo(String str = "hello", Integer int=0) {}
}

void stuff() {
    Z1().foo();
    Anything(String=, Integer=) callable = Z1().foo;
}

void variableCapture() {
    @error class C(shared variable Integer i, j = 0,
            @error shared Integer f() => i,
            g = j) {
        shared variable Integer j;
        shared Integer g;
        shared void inc() { 
            i = i + 1;
        }
    }
    @error void f(variable Integer i = 1, j = 0,
           @error Integer f() => i,
           g = j) {
        variable Integer j;
        Integer g;
        i = 10;
    }
}