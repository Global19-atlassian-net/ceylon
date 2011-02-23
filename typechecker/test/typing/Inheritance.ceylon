class Inheritance() {
    
    interface I<T> {
        void doIt(T t) {}
    }
    class X<T>(T t) satisfies I<T> 
            given T satisfies Equality<T> {
        T it = t;
        shared T getIt() { return it; }
        shared Boolean isIt(T t) { return t==it; }
    }
    class Y<T>(T t) extends X<T>(t) {}
    class Z() extends X<String>("Hello") {
        String sss = getIt();
        Boolean b = isIt("hi");
    }
    class W<U,V>(U u, V v) extends X<V>(v) {
        V vv = getIt();
    }
    class ZZ(Natural n) extends X<Natural>(n) {
        @error String sss = getIt();
        @error Boolean b = isIt("hi");
    }
        
    X<String> ys = Y<String>("foo");
    ys.doIt("to a string");
    @error ys.doIt(1);
    @type["String"] ys.getIt();
    I<String> iys = ys;
    Object oys = iys;
    
    X<Natural> yn = Y<Natural>(1);
    yn.doIt(6);
    @type["Natural"] yn.getIt();
    I<Natural> iyn = yn;
    
    X<String> z = Z();
    z.doIt("to a string");
    @type["String"] z.getIt();
    I<String> iz = z;
    
    X<Float> w = W<String,Float>("amount", 1.3);
    w.doIt(4.5);
    @type["Float"] w.getIt();
    I<Float> iw = w;
    
    @type["String"] X<String>("goodbye").getIt();
    
    @type["Inheritance.Y<String>"] Y<String>("hello");
    
    @type["String"] Y<String>("adios").getIt();
    
    @type["Inheritance.Z"] Z();
    
    @type["String"] Z().getIt();
    
    @type["Inheritance.W<Float,Natural>"] W<Float,Natural>(1.2, 1);
    
    @type["Natural"] W<Float,Natural>(1.2, 1).getIt();
        
}