import ceylon.language.meta.model { Class, Attribute, Function, Method, MemberClass }
import ceylon.language.meta { type, typeLiteral }
import java.lang { ByteArray, ShortArray, IntArray, LongArray, FloatArray, DoubleArray, CharArray, BooleanArray, ObjectArray,
    JByte = Byte, JShort = Short, JInteger = Integer, JLong = Long, JFloat = Float, JDouble = Double,
    JCharacter = Character, JBoolean = Boolean
}

import org.eclipse.ceylon.compiler.java.test.metamodel { JavaType { StaticClass, StaticInterface }}

void interopRuntime(){
    Class<JavaType,[Boolean,Byte,Integer,Integer,Integer,Float,Float,Character,String,Object]> javaType = `JavaType`;
    
    //
    // constructor
    
    value instance = javaType(true,1.byte,2,3,4,1.0,2.0,'a',"a","b");

    //
    // methods
    
    assert(exists method = javaType.getMethod<JavaType,Anything,[Boolean,Byte,Integer,Integer,Integer,Float,Float,Character,String,Object]>("method"));
    method(instance)(true,1.byte,2,3,4,1.0,2.0,'a',"a","b");

    assert(exists methodBoolean = javaType.getMethod<JavaType,Boolean,[]>("methodBoolean"));
    assert(true == methodBoolean(instance)());

    assert(exists methodByte = javaType.getMethod<JavaType,Byte,[]>("methodByte"));
    assert(1.byte == methodByte(instance)());

    assert(exists methodShort = javaType.getMethod<JavaType,Integer,[]>("methodShort"));
    assert(2 == methodShort(instance)());

    assert(exists methodInt = javaType.getMethod<JavaType,Integer,[]>("methodInt"));
    assert(3 == methodInt(instance)());

    assert(exists methodLong = javaType.getMethod<JavaType,Integer,[]>("methodLong"));
    assert(4 == methodLong(instance)());

    assert(exists methodFloat = javaType.getMethod<JavaType,Float,[]>("methodFloat"));
    assert(1.0 == methodFloat(instance)());

    assert(exists methodDouble = javaType.getMethod<JavaType,Float,[]>("methodDouble"));
    assert(2.0 == methodDouble(instance)());

    assert(exists methodChar = javaType.getMethod<JavaType,Character,[]>("methodChar"));
    assert('a' == methodChar(instance)());

    assert(exists methodStr = javaType.getMethod<JavaType,String,[]>("methodStr"));
    assert("a" == methodStr(instance)());

    assert(exists methodObject = javaType.getMethod<JavaType,Object,[]>("methodObject"));
    assert("b" == methodObject(instance)());

    // variadic

    assert(exists methodBooleanVarargs = javaType.getMethod<JavaType,Anything,[Integer, Boolean, Boolean*]>("methodBooleanVarargs"));
    methodBooleanVarargs(instance)(0, true);
    methodBooleanVarargs(instance)(1, true, true);
    methodBooleanVarargs(instance)(2, true, true, true);

    assert(exists methodByteVarargs = javaType.getMethod<JavaType,Anything,[Byte, Byte*]>("methodByteVarargs"));
    methodByteVarargs(instance)(1.byte, 1.byte);

    assert(exists methodShortVarargs = javaType.getMethod<JavaType,Anything,[Integer, Integer*]>("methodShortVarargs"));
    methodShortVarargs(instance)(2, 2);

    assert(exists methodIntVarargs = javaType.getMethod<JavaType,Anything,[Integer, Integer*]>("methodIntVarargs"));
    methodIntVarargs(instance)(3, 3);

    assert(exists methodLongVarargs = javaType.getMethod<JavaType,Anything,[Integer, Integer*]>("methodLongVarargs"));
    methodLongVarargs(instance)(4, 4);

    assert(exists methodFloatVarargs = javaType.getMethod<JavaType,Anything,[Float, Float*]>("methodFloatVarargs"));
    methodFloatVarargs(instance)(1.0, 1.0);

    assert(exists methodDoubleVarargs = javaType.getMethod<JavaType,Anything,[Float, Float*]>("methodDoubleVarargs"));
    methodDoubleVarargs(instance)(2.0, 2.0);

    assert(exists methodCharVarargs = javaType.getMethod<JavaType,Anything,[Character, Character*]>("methodCharVarargs"));
    methodCharVarargs(instance)('a', 'a');

    assert(exists methodJavaStringVarargs = javaType.getMethod<JavaType,Anything,[String, String*]>("methodJavaStringVarargs"));
    methodJavaStringVarargs(instance)("a", "a");

    assert(exists methodObjectVarargs = javaType.getMethod<JavaType,Anything,[Integer, Object, Object*]>("methodObjectVarargs"));
    methodObjectVarargs(instance)(0, "b");
    methodObjectVarargs(instance)(1, "b", "b");
    methodObjectVarargs(instance)(2, "b", "b", "b");

    assert(exists methodBoundObjectVarargs = javaType.getMethod<JavaType,Anything,[Integer, Integer, Integer*]>("methodBoundObjectVarargs", `Integer`));
    methodBoundObjectVarargs(instance)(0, 1);
    methodBoundObjectVarargs(instance)(1, 1, 1);
    methodBoundObjectVarargs(instance)(2, 1, 1, 1);

    //
    // properties
    
    assert(is Attribute<JavaType,Boolean,Boolean> booleanAttr = javaType.getAttribute<JavaType,Boolean,Boolean>("boolean"));
    assert(booleanAttr(instance).get() == true);
    booleanAttr(instance).set(true);

    assert(is Attribute<JavaType,Byte,Byte> byteAttr = javaType.getAttribute<JavaType,Byte,Byte>("byte"));
    assert(byteAttr(instance).get() == 1.byte);
    byteAttr(instance).set(1.byte);

    assert(is Attribute<JavaType,Integer,Integer> shortAttr = javaType.getAttribute<JavaType,Integer,Integer>("short"));
    assert(shortAttr(instance).get() == 2);
    shortAttr(instance).set(2);

    assert(is Attribute<JavaType,Integer,Integer> intAttr = javaType.getAttribute<JavaType,Integer,Integer>("int"));
    assert(intAttr(instance).get() == 3);
    intAttr(instance).set(3);
    
    assert(is Attribute<JavaType,Integer,Integer>  longAttr = javaType.getAttribute<JavaType,Integer,Integer>("long"));
    assert(longAttr(instance).get() == 4);
    longAttr(instance).set(4);

    assert(is Attribute<JavaType,Float,Float>  floatAttr = javaType.getAttribute<JavaType,Float,Float>("float"));
    assert(floatAttr(instance).get() == 1.0);
    floatAttr(instance).set(1.0);

    assert(is Attribute<JavaType,Float,Float>  doubleAttr = javaType.getAttribute<JavaType,Float,Float>("double"));
    assert(doubleAttr(instance).get() == 2.0);
    doubleAttr(instance).set(2.0);

    assert(is Attribute<JavaType,Character,Character>  charAttr = javaType.getAttribute<JavaType,Character,Character>("char"));
    assert(charAttr(instance).get() == 'a');
    charAttr(instance).set('a');

    assert(is Attribute<JavaType,String,String>  stringAttr = javaType.getAttribute<JavaType,String,String>("str"));
    assert(stringAttr(instance).get() == "a");
    stringAttr(instance).set("a");

    assert(is Attribute<JavaType,Object,Object> objectAttr = javaType.getAttribute<JavaType,Object,Object>("object"));
    assert(objectAttr(instance).get() == "b");
    objectAttr(instance).set("b");
    
    //
    // fields
    
    assert(is Attribute<JavaType,Boolean,Boolean> booleanFieldAttr = javaType.getAttribute<JavaType,Boolean,Boolean>("booleanField"));
    assert(booleanFieldAttr(instance).get() == true);
    booleanFieldAttr(instance).set(true);

    assert(is Attribute<JavaType,Byte,Byte> byteFieldAttr = javaType.getAttribute<JavaType,Byte,Byte>("byteField"));
    assert(byteFieldAttr(instance).get() == 1.byte);
    byteFieldAttr(instance).set(1.byte);

    assert(is Attribute<JavaType,Integer,Integer> shortFieldAttr = javaType.getAttribute<JavaType,Integer,Integer>("shortField"));
    assert(shortFieldAttr(instance).get() == 2);
    shortFieldAttr(instance).set(2);

    assert(is Attribute<JavaType,Integer,Integer> intFieldAttr = javaType.getAttribute<JavaType,Integer,Integer>("intField"));
    assert(intFieldAttr(instance).get() == 3);
    intFieldAttr(instance).set(3);
    
    assert(is Attribute<JavaType,Integer,Integer> longFieldAttr = javaType.getAttribute<JavaType,Integer,Integer>("longField"));
    assert(longFieldAttr(instance).get() == 4);
    longFieldAttr(instance).set(4);

    assert(is Attribute<JavaType,Float,Float> floatFieldAttr = javaType.getAttribute<JavaType,Float,Float>("floatField"));
    assert(floatFieldAttr(instance).get() == 1.0);
    floatFieldAttr(instance).set(1.0);

    assert(is Attribute<JavaType,Float,Float> doubleFieldAttr = javaType.getAttribute<JavaType,Float,Float>("doubleField"));
    assert(doubleFieldAttr(instance).get() == 2.0);
    doubleFieldAttr(instance).set(2.0);

    assert(is Attribute<JavaType,Character,Character> charFieldAttr = javaType.getAttribute<JavaType,Character,Character>("charField"));
    assert(charFieldAttr(instance).get() == 'a');
    charFieldAttr(instance).set('a');

    assert(is Attribute<JavaType,String,String> stringFieldAttr = javaType.getAttribute<JavaType,String,String>("stringField"));
    assert(stringFieldAttr(instance).get() == "a");
    stringFieldAttr(instance).set("a");

    assert(is Attribute<JavaType,Object,Object> objectFieldAttr = javaType.getAttribute<JavaType,Object,Object>("objectField"));
    assert(objectFieldAttr(instance).get() == "b");
    objectFieldAttr(instance).set("b");

    //
    // Static members
    
    value staticFieldAttr = `JavaType.staticField`;
    // FIXME: support later
    //assert(staticFieldAttr(null).get() == 2);
    //staticFieldAttr(null).set(3);
    //assert(staticFieldAttr(null).get() == 3);
    // make sure it also works with an instance
    staticFieldAttr(null).set(4);
    assert(staticFieldAttr(null).get() == 4);
    
    // make sure this passes too
    javaType.getAttribute<JavaType,Integer,Integer>("staticField");
    // FIXME: support later
    //javaType.getAttribute<Null,Integer,Integer>("staticField");

    value staticGetterAttr = `JavaType.staticGetter`;
    // FIXME: support later
    //assert(staticGetterAttr(null).get() == 2);
    //staticGetterAttr(null).set(3);
    //assert(staticGetterAttr(null).get() == 3);
    // make sure it also works with an instance
    staticGetterAttr(null).set(4);
    assert(staticGetterAttr(null).get() == 4);

    // make sure this passes too
    javaType.getAttribute<JavaType,Integer,Integer>("staticGetter");
    // FIXME: support later
    //javaType.getAttribute<Null,Integer,Integer>("staticGetter");

    value staticMethod = `JavaType.staticMethod`;
    // FIXME: support later
    //assert(staticMethod(null)(2) == 2);
    // make sure it also works with an instance
    assert(staticMethod(null)(4) == 4);

    // make sure this passes too
    javaType.getMethod<JavaType,Integer,[Integer]>("staticMethod");
    // FIXME: support later
    //javaType.getMethod<Null,Integer,[Integer]>("staticMethod");

    value staticClass = `JavaType.StaticClass`;
    // FIXME: support later
    //assert(staticClass(null)(2).v == 2);
    // make sure it also works with an instance
    assert(staticClass(null)(4).v == 4);
    
    assert(typeLiteral<JavaType.StaticClass>() 
        == typeLiteral<StaticClass>());

    // make sure this passes too
    javaType.getClass<JavaType,JavaType.StaticClass,[Integer]>("StaticClass");
    // FIXME: support later
    //javaType.getClass<Null,JavaType.StaticClass,[Integer]>("StaticClass");

    value staticInterface = `JavaType.StaticInterface`;

    // make sure this passes too
    javaType.getInterface<JavaType,JavaType.StaticInterface>("StaticInterface");
    // FIXME: support later
    //javaType.getInterface<Null,JavaType.StaticInterface>("StaticInterface");

    //
    // Member types
    
    assert(exists memberMember = javaType.getClassOrInterface<JavaType, Class<JavaType.Member, [Boolean]>>("Member"));
    memberMember(instance)(true);

    assert(exists memberVarargsMember = javaType.getClassOrInterface<JavaType, Class<JavaType.MemberVarargs, [Integer, Boolean, Boolean*]>>("MemberVarargs"));
    memberVarargsMember(instance)(0, true);
    memberVarargsMember(instance)(1, true, true);
    memberVarargsMember(instance)(2, true, true, true);
 
    //
    // Arrays
 
    testByteArray();
    testShortArray();
    testIntArray();
    testLongArray();
    testFloatArray();
    testDoubleArray();
    testCharArray();
    testBooleanArray();
    testObjectArray();
}

void testByteArray(){
    value byteArray = ByteArray(3);
    byteArray.set(0, 1.byte);
    byteArray.set(1, 2.byte);
    byteArray.set(2, 3.byte);
    // type
    assert("java.lang::ByteArray" == type(byteArray).string);
    // constructor
    value byteArray2 = `ByteArray`(4);
    assert(byteArray2.size == 4);
    assert(byteArray2.get(0) == 0.byte);
    value byteArray3 = `ByteArray`(4, 2.byte);
    assert(byteArray3.size == 4);
    assert(byteArray3.get(0) == 2.byte);
    // size
    value length = `ByteArray.size`;
    assert(length(byteArray).get() == 3);
    // get
    value get = `ByteArray.get`;
    assert(get(byteArray)(0) == 1.byte);
    assert(get(byteArray)(1) == 2.byte);
    assert(get(byteArray)(2) == 3.byte);
    // set
    value set = `ByteArray.set`;
    set(byteArray)(0, 10.byte);
    set(byteArray)(1, 11.byte);
    set(byteArray)(2, 12.byte);
    assert(byteArray.get(0) == 10.byte);
    assert(byteArray.get(1) == 11.byte);
    assert(byteArray.get(2) == 12.byte);
    // copyTo
    value dest = ByteArray(5);
    value copyTo = `ByteArray.copyTo`;
    copyTo(byteArray)(dest);
    assert(dest.get(0) == 10.byte);
    assert(dest.get(1) == 11.byte);
    assert(dest.get(2) == 12.byte);
    assert(dest.get(3) == 0.byte);//left-over
    copyTo(byteArray)(dest, 1);
    assert(dest.get(0) == 11.byte);
    assert(dest.get(1) == 12.byte);
    assert(dest.get(2) == 12.byte);//left-over
    assert(dest.get(3) == 0.byte);//left-over
    copyTo(byteArray)(dest, 0, 1);
    assert(dest.get(0) == 11.byte);//left-over
    assert(dest.get(1) == 10.byte);
    assert(dest.get(2) == 11.byte);
    assert(dest.get(3) == 12.byte);
    copyTo(byteArray)(dest, 0, 0, 2);
    assert(dest.get(0) == 10.byte);
    assert(dest.get(1) == 11.byte);
    assert(dest.get(2) == 11.byte);//left-over
    assert(dest.get(3) == 12.byte);//left-over
    // array
    value array = `ByteArray.array`;
    assert(array(byteArray).get() == {JByte(10.byte), JByte(11.byte), JByte(12.byte)}.sequence());
}

void testShortArray(){
    value shortArray = ShortArray(3);
    shortArray.set(0, 1);
    shortArray.set(1, 2);
    shortArray.set(2, 3);
    // type
    assert("java.lang::ShortArray" == type(shortArray).string);
    // constructor
    value shortArray2 = `ShortArray`(4);
    assert(shortArray2.size == 4);
    assert(shortArray2.get(0) == 0);
    value shortArray3 = `ShortArray`(4, 2);
    assert(shortArray3.size == 4);
    assert(shortArray3.get(0) == 2);
    // size
    value length = `ShortArray.size`;
    assert(length(shortArray).get() == 3);
    // get
    value get = `ShortArray.get`;
    assert(get(shortArray)(0) == 1);
    assert(get(shortArray)(1) == 2);
    assert(get(shortArray)(2) == 3);
    // set
    value set = `ShortArray.set`;
    set(shortArray)(0, 10);
    set(shortArray)(1, 11);
    set(shortArray)(2, 12);
    assert(shortArray.get(0) == 10);
    assert(shortArray.get(1) == 11);
    assert(shortArray.get(2) == 12);
    // copyTo
    value dest = ShortArray(5);
    value copyTo = `ShortArray.copyTo`;
    copyTo(shortArray)(dest);
    assert(dest.get(0) == 10);
    assert(dest.get(1) == 11);
    assert(dest.get(2) == 12);
    assert(dest.get(3) == 0);//left-over
    copyTo(shortArray)(dest, 1);
    assert(dest.get(0) == 11);
    assert(dest.get(1) == 12);
    assert(dest.get(2) == 12);//left-over
    assert(dest.get(3) == 0);//left-over
    copyTo(shortArray)(dest, 0, 1);
    assert(dest.get(0) == 11);//left-over
    assert(dest.get(1) == 10);
    assert(dest.get(2) == 11);
    assert(dest.get(3) == 12);
    copyTo(shortArray)(dest, 0, 0, 2);
    assert(dest.get(0) == 10);
    assert(dest.get(1) == 11);
    assert(dest.get(2) == 11);//left-over
    assert(dest.get(3) == 12);//left-over
    // array
    value array = `ShortArray.array`;
    assert(array(shortArray).get() == {JShort(10), JShort(11), JShort(12)}.sequence());
}

void testIntArray(){
    value intArray = IntArray(3);
    intArray.set(0, 1);
    intArray.set(1, 2);
    intArray.set(2, 3);
    // type
    assert("java.lang::IntArray" == type(intArray).string);
    // constructor
    value intArray2 = `IntArray`(4);
    assert(intArray2.size == 4);
    assert(intArray2.get(0) == 0);
    value intArray3 = `IntArray`(4, 2);
    assert(intArray3.size == 4);
    assert(intArray3.get(0) == 2);
    // size
    value length = `IntArray.size`;
    assert(length(intArray).get() == 3);
    // get
    value get = `IntArray.get`;
    assert(get(intArray)(0) == 1);
    assert(get(intArray)(1) == 2);
    assert(get(intArray)(2) == 3);
    // set
    value set = `IntArray.set`;
    set(intArray)(0, 10);
    set(intArray)(1, 11);
    set(intArray)(2, 12);
    assert(intArray.get(0) == 10);
    assert(intArray.get(1) == 11);
    assert(intArray.get(2) == 12);
    // copyTo
    value dest = IntArray(5);
    value copyTo = `IntArray.copyTo`;
    copyTo(intArray)(dest);
    assert(dest.get(0) == 10);
    assert(dest.get(1) == 11);
    assert(dest.get(2) == 12);
    assert(dest.get(3) == 0);//left-over
    copyTo(intArray)(dest, 1);
    assert(dest.get(0) == 11);
    assert(dest.get(1) == 12);
    assert(dest.get(2) == 12);//left-over
    assert(dest.get(3) == 0);//left-over
    copyTo(intArray)(dest, 0, 1);
    assert(dest.get(0) == 11);//left-over
    assert(dest.get(1) == 10);
    assert(dest.get(2) == 11);
    assert(dest.get(3) == 12);
    copyTo(intArray)(dest, 0, 0, 2);
    assert(dest.get(0) == 10);
    assert(dest.get(1) == 11);
    assert(dest.get(2) == 11);//left-over
    assert(dest.get(3) == 12);//left-over
    // array
    value array = `IntArray.array`;
    assert(array(intArray).get() == {JInteger(10), JInteger(11), JInteger(12)}.sequence());
    // codePointArray
    value codePointArray = `IntArray.codePointArray`;
    assert(codePointArray(intArray).get() == {'\{#0A}', '\{#0B}', '\{#0C}'}.sequence());
}

void testLongArray(){
    value longArray = LongArray(3);
    longArray.set(0, 1);
    longArray.set(1, 2);
    longArray.set(2, 3);
    // type
    assert("java.lang::LongArray" == type(longArray).string);
    // constructor
    value longArray2 = `LongArray`(4);
    assert(longArray2.size == 4);
    assert(longArray2.get(0) == 0);
    value longArray3 = `LongArray`(4, 2);
    assert(longArray3.size == 4);
    assert(longArray3.get(0) == 2);
    // size
    value length = `LongArray.size`;
    assert(length(longArray).get() == 3);
    // get
    value get = `LongArray.get`;
    assert(get(longArray)(0) == 1);
    assert(get(longArray)(1) == 2);
    assert(get(longArray)(2) == 3);
    // set
    value set = `LongArray.set`;
    set(longArray)(0, 10);
    set(longArray)(1, 11);
    set(longArray)(2, 12);
    assert(longArray.get(0) == 10);
    assert(longArray.get(1) == 11);
    assert(longArray.get(2) == 12);
    // copyTo
    value dest = LongArray(5);
    value copyTo = `LongArray.copyTo`;
    copyTo(longArray)(dest);
    assert(dest.get(0) == 10);
    assert(dest.get(1) == 11);
    assert(dest.get(2) == 12);
    assert(dest.get(3) == 0);//left-over
    copyTo(longArray)(dest, 1);
    assert(dest.get(0) == 11);
    assert(dest.get(1) == 12);
    assert(dest.get(2) == 12);//left-over
    assert(dest.get(3) == 0);//left-over
    copyTo(longArray)(dest, 0, 1);
    assert(dest.get(0) == 11);//left-over
    assert(dest.get(1) == 10);
    assert(dest.get(2) == 11);
    assert(dest.get(3) == 12);
    copyTo(longArray)(dest, 0, 0, 2);
    assert(dest.get(0) == 10);
    assert(dest.get(1) == 11);
    assert(dest.get(2) == 11);//left-over
    assert(dest.get(3) == 12);//left-over
    // array
    value array = `LongArray.array`;
    assert(array(longArray).get() == {JLong(10), JLong(11), JLong(12)}.sequence());
    value integerArrayAttribute = `LongArray.integerArray`;
    assert(integerArrayAttribute(longArray).get() == {10, 11, 12}.sequence());
}

void testFloatArray(){
    value floatArray = FloatArray(3);
    floatArray.set(0, 1.0);
    floatArray.set(1, 2.0);
    floatArray.set(2, 3.0);
    // type
    assert("java.lang::FloatArray" == type(floatArray).string);
    // constructor
    value floatArray2 = `FloatArray`(4);
    assert(floatArray2.size == 4);
    assert(floatArray2.get(0) == 0.0);
    value floatArray3 = `FloatArray`(4, 2.0);
    assert(floatArray3.size == 4);
    assert(floatArray3.get(0) == 2.0);
    // size
    value length = `FloatArray.size`;
    assert(length(floatArray).get() == 3);
    // get
    value get = `FloatArray.get`;
    assert(get(floatArray)(0) == 1.0);
    assert(get(floatArray)(1) == 2.0);
    assert(get(floatArray)(2) == 3.0);
    // set
    value set = `FloatArray.set`;
    set(floatArray)(0, 10.0);
    set(floatArray)(1, 11.0);
    set(floatArray)(2, 12.0);
    assert(floatArray.get(0) == 10.0);
    assert(floatArray.get(1) == 11.0);
    assert(floatArray.get(2) == 12.0);
    // copyTo
    value dest = FloatArray(5);
    value copyTo = `FloatArray.copyTo`;
    copyTo(floatArray)(dest);
    assert(dest.get(0) == 10.0);
    assert(dest.get(1) == 11.0);
    assert(dest.get(2) == 12.0);
    assert(dest.get(3) == 0.0);//left-over
    copyTo(floatArray)(dest, 1);
    assert(dest.get(0) == 11.0);
    assert(dest.get(1) == 12.0);
    assert(dest.get(2) == 12.0);//left-over
    assert(dest.get(3) == 0);//left-over
    copyTo(floatArray)(dest, 0, 1);
    assert(dest.get(0) == 11.0);//left-over
    assert(dest.get(1) == 10.0);
    assert(dest.get(2) == 11.0);
    assert(dest.get(3) == 12.0);
    copyTo(floatArray)(dest, 0, 0, 2);
    assert(dest.get(0) == 10.0);
    assert(dest.get(1) == 11.0);
    assert(dest.get(2) == 11.0);//left-over
    assert(dest.get(3) == 12.0);//left-over
    // array
    value array = `FloatArray.array`;
    assert(array(floatArray).get() == {JFloat(10.0), JFloat(11.0), JFloat(12.0)}.sequence());
}

void testDoubleArray(){
    value doubleArray = DoubleArray(3);
    doubleArray.set(0, 1.0);
    doubleArray.set(1, 2.0);
    doubleArray.set(2, 3.0);
    // type
    assert("java.lang::DoubleArray" == type(doubleArray).string);
    // constructor
    value doubleArray2 = `DoubleArray`(4);
    assert(doubleArray2.size == 4);
    assert(doubleArray2.get(0) == 0.0);
    value doubleArray3 = `DoubleArray`(4, 2.0);
    assert(doubleArray3.size == 4);
    assert(doubleArray3.get(0) == 2.0);
    // size
    value length = `DoubleArray.size`;
    assert(length(doubleArray).get() == 3);
    // get
    value get = `DoubleArray.get`;
    assert(get(doubleArray)(0) == 1.0);
    assert(get(doubleArray)(1) == 2.0);
    assert(get(doubleArray)(2) == 3.0);
    // set
    value set = `DoubleArray.set`;
    set(doubleArray)(0, 10.0);
    set(doubleArray)(1, 11.0);
    set(doubleArray)(2, 12.0);
    assert(doubleArray.get(0) == 10.0);
    assert(doubleArray.get(1) == 11.0);
    assert(doubleArray.get(2) == 12.0);
    // copyTo
    value dest = DoubleArray(5);
    value copyTo = `DoubleArray.copyTo`;
    copyTo(doubleArray)(dest);
    assert(dest.get(0) == 10.0);
    assert(dest.get(1) == 11.0);
    assert(dest.get(2) == 12.0);
    assert(dest.get(3) == 0.0);//left-over
    copyTo(doubleArray)(dest, 1);
    assert(dest.get(0) == 11.0);
    assert(dest.get(1) == 12.0);
    assert(dest.get(2) == 12.0);//left-over
    assert(dest.get(3) == 0);//left-over
    copyTo(doubleArray)(dest, 0, 1);
    assert(dest.get(0) == 11.0);//left-over
    assert(dest.get(1) == 10.0);
    assert(dest.get(2) == 11.0);
    assert(dest.get(3) == 12.0);
    copyTo(doubleArray)(dest, 0, 0, 2);
    assert(dest.get(0) == 10.0);
    assert(dest.get(1) == 11.0);
    assert(dest.get(2) == 11.0);//left-over
    assert(dest.get(3) == 12.0);//left-over
    // array
    value array = `DoubleArray.array`;
    assert(array(doubleArray).get() == {JDouble(10.0), JDouble(11.0), JDouble(12.0)}.sequence());
    value floatArrayAttribute = `DoubleArray.floatArray`;
    assert(floatArrayAttribute(doubleArray).get() == {10.0, 11.0, 12.0}.sequence());
}

void testCharArray(){
    value charArray = CharArray(3);
    charArray.set(0, '1');
    charArray.set(1, '2');
    charArray.set(2, '3');
    // type
    assert("java.lang::CharArray" == type(charArray).string);
    // constructor
    value charArray2 = `CharArray`(4);
    assert(charArray2.size == 4);
    assert(charArray2.get(0) == '\{#00}');
    value charArray3 = `CharArray`(4, '2');
    assert(charArray3.size == 4);
    assert(charArray3.get(0) == '2');
    // size
    value length = `CharArray.size`;
    assert(length(charArray).get() == 3);
    // get
    value get = `CharArray.get`;
    assert(get(charArray)(0) == '1');
    assert(get(charArray)(1) == '2');
    assert(get(charArray)(2) == '3');
    // set
    value set = `CharArray.set`;
    set(charArray)(0, 'a');
    set(charArray)(1, 'b');
    set(charArray)(2, 'c');
    assert(charArray.get(0) == 'a');
    assert(charArray.get(1) == 'b');
    assert(charArray.get(2) == 'c');
    // copyTo
    value dest = CharArray(5);
    value copyTo = `CharArray.copyTo`;
    copyTo(charArray)(dest);
    assert(dest.get(0) == 'a');
    assert(dest.get(1) == 'b');
    assert(dest.get(2) == 'c');
    assert(dest.get(3) == '\{#00}');//left-over
    copyTo(charArray)(dest, 1);
    assert(dest.get(0) == 'b');
    assert(dest.get(1) == 'c');
    assert(dest.get(2) == 'c');//left-over
    assert(dest.get(3) == '\{#00}');//left-over
    copyTo(charArray)(dest, 0, 1);
    assert(dest.get(0) == 'b');//left-over
    assert(dest.get(1) == 'a');
    assert(dest.get(2) == 'b');
    assert(dest.get(3) == 'c');
    copyTo(charArray)(dest, 0, 0, 2);
    assert(dest.get(0) == 'a');
    assert(dest.get(1) == 'b');
    assert(dest.get(2) == 'b');//left-over
    assert(dest.get(3) == 'c');//left-over
    // array
    value array = `CharArray.array`;
    assert(array(charArray).get() == {JCharacter('a'), JCharacter('b'), JCharacter('c')}.sequence());
}

void testBooleanArray(){
    value booleanArray = BooleanArray(3);
    booleanArray.set(0, true);
    booleanArray.set(1, false);
    booleanArray.set(2, true);
    // type
    assert("java.lang::BooleanArray" == type(booleanArray).string);
    // constructor
    value booleanArray2 = `BooleanArray`(4);
    assert(booleanArray2.size == 4);
    assert(booleanArray2.get(0) == false);
    value booleanArray3 = `BooleanArray`(4, true);
    assert(booleanArray3.size == 4);
    assert(booleanArray3.get(0) == true);
    // size
    value length = `BooleanArray.size`;
    assert(length(booleanArray).get() == 3);
    // get
    value get = `BooleanArray.get`;
    assert(get(booleanArray)(0) == true);
    assert(get(booleanArray)(1) == false);
    assert(get(booleanArray)(2) == true);
    // set
    value set = `BooleanArray.set`;
    set(booleanArray)(0, false);
    set(booleanArray)(1, true);
    set(booleanArray)(2, false);
    assert(booleanArray.get(0) == false);
    assert(booleanArray.get(1) == true);
    assert(booleanArray.get(2) == false);
    // copyTo
    value dest = BooleanArray(5);
    value copyTo = `BooleanArray.copyTo`;
    copyTo(booleanArray)(dest);
    assert(dest.get(0) == false);
    assert(dest.get(1) == true);
    assert(dest.get(2) == false);
    assert(dest.get(3) == false);//left-over
    copyTo(booleanArray)(dest, 1);
    assert(dest.get(0) == true);
    assert(dest.get(1) == false);
    assert(dest.get(2) == false);//left-over
    assert(dest.get(3) == false);//left-over
    copyTo(booleanArray)(dest, 0, 1);
    assert(dest.get(0) == true);//left-over
    assert(dest.get(1) == false);
    assert(dest.get(2) == true);
    assert(dest.get(3) == false);
    copyTo(booleanArray)(dest, 0, 0, 2);
    assert(dest.get(0) == false);
    assert(dest.get(1) == true);
    assert(dest.get(2) == true);//left-over
    assert(dest.get(3) == false);//left-over
    // array
    value array = `BooleanArray.array`;
    assert(array(booleanArray).get() == {JBoolean(false), JBoolean(true), JBoolean(false)}.sequence());
    value booleanArrayAttribute = `BooleanArray.booleanArray`;
    assert(booleanArrayAttribute(booleanArray).get() == {false, true, false}.sequence());
}

Boolean eq(Anything a, Anything b){
    if(exists a){
        if(exists b){
            return a == b;
        }
        return false;
    }
    return !b exists;
}

void testObjectArray(){
    value objectArray = ObjectArray<Integer>(3);
    objectArray.set(0, 1);
    objectArray.set(1, 2);
    objectArray.set(2, 3);
    // type
    assert("java.lang::ObjectArray<ceylon.language::Integer>" == type(objectArray).string);
    // constructor
    value objectArray2 = `ObjectArray<Integer>`(4);
    assert(objectArray2.size == 4);
    assert(eq(objectArray2.get(0), null));
    assert("java.lang::ObjectArray<ceylon.language::Integer>" == type(objectArray2).string);
    
    value objectArray3 = `ObjectArray<Integer>`(4, 2);
    assert(objectArray3.size == 4);
    assert(eq(objectArray3.get(0), 2));
    // size
    value length = `ObjectArray<Integer>.size`;
    assert(length(objectArray).get() == 3);
    // get
    value get = `ObjectArray<Integer>.get`;
    assert(eq(get(objectArray)(0), 1));
    assert(eq(get(objectArray)(1), 2));
    assert(eq(get(objectArray)(2), 3));
    // set
    value set = `ObjectArray<Integer>.set`;
    set(objectArray)(0, 10);
    set(objectArray)(1, 11);
    set(objectArray)(2, 12);
    assert(eq(objectArray.get(0), 10));
    assert(eq(objectArray.get(1), 11));
    assert(eq(objectArray.get(2), 12));
    // copyTo
    value dest = ObjectArray<Integer>(5);
    value copyTo = `ObjectArray<Integer>.copyTo`;
    copyTo(objectArray)(dest);
    assert(eq(dest.get(0), 10));
    assert(eq(dest.get(1), 11));
    assert(eq(dest.get(2), 12));
    assert(eq(dest.get(3), null));//left-over
    copyTo(objectArray)(dest, 1);
    assert(eq(dest.get(0), 11));
    assert(eq(dest.get(1), 12));
    assert(eq(dest.get(2), 12));//left-over
    assert(eq(dest.get(3), null));//left-over
    copyTo(objectArray)(dest, 0, 1);
    assert(eq(dest.get(0), 11));//left-over
    assert(eq(dest.get(1), 10));
    assert(eq(dest.get(2), 11));
    assert(eq(dest.get(3), 12));
    copyTo(objectArray)(dest, 0, 0, 2);
    assert(eq(dest.get(0), 10));
    assert(eq(dest.get(1), 11));
    assert(eq(dest.get(2), 11));//left-over
    assert(eq(dest.get(3), 12));//left-over
    // array
    value array = `ObjectArray<Integer>.array`;
    assert(array(objectArray).get() == {10, 11, 12}.sequence());
    
    // make sure java's Object doesn't leak here
    assert("java.lang::ObjectArray<ceylon.language::Object>" == type(ObjectArray<Object>(0)).string);

}
