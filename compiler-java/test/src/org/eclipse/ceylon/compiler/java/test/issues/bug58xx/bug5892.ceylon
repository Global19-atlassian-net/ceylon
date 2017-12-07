import ceylon.buffer.charset {
    utf8 {
        encodeSTEF=encode,
        decodeSTEF=decode
    },
    Charset
}
import ceylon.buffer { ByteBuffer }
import ceylon.language { runtime { maxIntegerValue }}

@noanno
shared void bug5892( String input, String expectedEncode, Charset charset){
    value encoded = encodeSTEF(nothing);
    decodeSTEF(encoded);
    
    value f = decodeSTEF;
    value i = maxIntegerValue;
}