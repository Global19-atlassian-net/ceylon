void parameterInference() {
    
    $type:"Iterable<Character,Null>" value mapped = "hello world".map((c) => c.uppercased);
    $type:"Iterable<Character,Null>" value mapped1 = "hello world".map(=> element.uppercased);
    $type:"Iterable<Character,Null>" value filtered = "hello world".filter((c) { return !c.whitespace; });
    $type:"Integer" value folded = (1..10).fold(0, (r, x) => r+x);
    $type:"Integer" value folded2 = (1..10).fold(0, (r, x) { Integer r; Integer x; return r+x; });
    $type:"Integer" value folded3 = (1..10).fold(0, (r, x) { return r+x; });
    $type:"Integer" value reduced = (1..10).reduce<Integer>((r, x) => r+x);
    $type:"Integer" value reduced1 = (1..10).reduce<Integer>(=> partial+element);
    $type:"Float|Integer" value reduced2 = (1..10).reduce<Float>((r, x) {
        switch (r) 
        case (is Integer) { return (r+x).float; } 
        case (is Float) { return r+x; } 
    });
    function float(Float|Integer num) {
        switch (num) 
        case (is Integer) { return num.float; } 
        case (is Float) { return num; } 
    }
    $type:"Float|Integer" value reduced3 
            = (1..10).reduce<Float>((r, x) => float(r)+x);
    $type:"Float|Integer" value reduced4 
            = (1..10).reduce<Float> { (r, x) => float(r)+x; };
    
    T? fun<T>({T*} ts)(Boolean match(T t)) => ts.find(match);
    Character?(Boolean(Character)) fun1 = fun("goodbye*world");
    
    Character? found
            = fun("hello-world")
    ((ch)=>!ch.letter);
    Character? found1
            = fun1((ch)=>!ch.letter);
    
    void accept(String(Float) fun) => fun(1.0);
    accept((f) => f.string);
    accept(=> it.string);
    accept { (f) => f.string; };
    accept { (f) { return f.string; }; };
    
    void variadic(Anything(String)* args) {}
    variadic((string)=>print(string), (string)=>print(string));
    void iterable({Anything(String)*} args) {}
    iterable { (string)=>print(string),
        (string)=>print(string) };

    String hello(String(String)? fun) 
            => if (exists fun) then fun("hello") else "";
    print(hello((str) => str + "world")); 
    print(hello(null));
    
    $type:"Iterable<Character,Null>" value mapped_ = "hello world".map((value c) => c.uppercased);
    $type:"Iterable<Character,Null>" value filtered_ = "hello world".filter((value c) { return !c.whitespace; });
    $type:"Integer" value folded_ = (1..10).fold(0, (value r, value x) => r+x);
    accept((value f)=>f.string);
    variadic((value string)=>print(string), (value string)=>print(string));
    
    $error value funWithNoParamType = (p) => 0;
    value funWithNoParamType_ = ($error value p) => 0;
    
    $error value funWithNoParams = => "";
    
    T silly<T>(T t, T(T) f) => f(t);
    $type:"String" silly("", => it);
}

void testNotVariable() {
    void fun(void asdfas(Integer i)) {}
    fun(void (y) {
        $error y = 1; 
        $error print(y=2);
        $error y++;
    });
}

void moreParamInference() {
    function fold<E,R>({E*} es, R i)(R f(R j, E e)) => es.fold(i, f);
    
    $type:"Float" value folded0 = fold(1..10, 0.0)((value r, value x) => r+x);
    $type:"Float" value folded1 = fold(1..10, 0.0)((r, x) => r+x);
    $type:"Float" value folded2 = fold(1..10, 0.0)((r, x) { Float r; Integer x; return r+x; });
    $type:"Float" value folded3 = fold(1..10, 0.0)((r, x) { return r+x; });

}

void fun(String|Anything(String) foo) {}

alias Alias => String|Anything(String);

void fun2(Alias foo) {}

void funrun() {
    fun((str) => 1);
    fun(=> 1);
    fun2((str) => 1); 
    fun2(=> 1);
}

void parameterInferenceFromAssignment() {
    interface Request {
        shared formal String thing;
    }
    interface Response {}
    
    alias Route => Object(Request, Response);
    Route bar = (req, res) => req.thing;
    Route baz = (req, res) => req.thing;
    
    String(Integer) form1 
            = (i) => Integer.format(i, 16);
    String(Integer) form2;
    form2 = (j) => Integer.format(j, 16);
    String(Integer) form3() {
        return (k) => Integer.format(k, 16);
    }
    String(Integer) form4() 
            => (k) => Integer.format(k, 16);
    
    void accept(String(Integer) f) {}
    accept { f = (k) => Integer.format(k, 16); };
    accept { String(Integer) f = (k) => Integer.format(k, 16); };
    accept { value f = (k) => Integer.format(k, 16); };
    
    variable String(Integer) form5 = nothing;
    $type:"String(Integer)"
    value nnn = form5 = (l) => Integer.format(l, 16);
    
    [Float+](Float,Float) fun 
            = (x, y) => Singleton(x).withTrailing(y);
    
    [Float,Integer,String](Float)(Integer)(String) multi 
            = (s)(i)(f) => [f,i,s];
}

void invokedAnonymousFunctionParameterTypeInference() {
    $type:"String" ((i) => Integer.format(i, 16))(100);
    $error ((i) => Integer.format(i, 16))(100.0);
}