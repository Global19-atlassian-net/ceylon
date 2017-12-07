@noanno
shared interface TestInterface<Key, Value>{
    shared class Inner() {}
}

@noanno
shared interface TestInterface2<out Key, out Value>{
    shared class Inner() {}
}

@noanno
shared class TestClass<Key, Value>() 
    satisfies TestInterface<Key, Integer>{}

@noanno
void test<Key,Value>(Object obj) given Key satisfies Object {
    if(is TestClass<String,Integer> obj){
    }
    if(is TestInterface<String,Value> obj){
    }
    if(is Key obj){
    }
    Key? objOrNot = nothing;
    // optimised away
    if(is Key objOrNot){
    }
    if(is Key|Integer objOrNot){
    }
    if(is Key fu = objOrNot){
    }
    Boolean b = objOrNot is Key;
    switch(objOrNot)
    case(is Key){
    }
    else{}
    // optimised away
    if(is TestInterface2<Anything,Anything> obj){
    }
    // not optimised
    if(is TestInterface<Anything,Anything> obj){
    }
    if(is TestInterface<Anything,Integer> obj){
    }
    // two use-site variance
    if(is TestInterface<out Anything,in Integer> obj){
    }
    // just one
    if(is TestInterface<out Anything,Integer> obj){
    }
    // optimised
    if (is TestInterface<out Anything, out Anything> obj){
    }    
    // optimised too
    if (is TestInterface<out Anything, out Anything>.Inner obj){
    }
}

shared void first<Value,Absent>()
        given Absent satisfies Null {
    Value? first = nothing;
    assert (is Absent|Value first);
}
