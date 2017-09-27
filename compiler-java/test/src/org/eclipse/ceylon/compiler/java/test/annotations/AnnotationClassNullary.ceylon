import ceylon.language.meta.declaration { ClassOrInterfaceDeclaration }

@nomodel
final annotation class AnnotationClassNullary() satisfies OptionalAnnotation<AnnotationClassNullary, ClassOrInterfaceDeclaration>{}
@nomodel
final annotation class AnnotationClassNullarySequenced() satisfies SequencedAnnotation<AnnotationClassNullarySequenced, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassNullary annotationClassNullary() => AnnotationClassNullary();
@nomodel
annotation AnnotationClassNullarySequenced annotationClassNullarySequenced() => AnnotationClassNullarySequenced();
@nomodel
annotationClassNullary
annotationClassNullarySequenced
class AnnotationClassNullary_callsite() {}
