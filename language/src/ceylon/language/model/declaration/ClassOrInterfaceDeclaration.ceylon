import ceylon.language.model {
    AppliedClassOrInterface = ClassOrInterface,
    AppliedMember = Member,
    Type
}

shared interface ClassOrInterfaceDeclaration 
        of ClassDeclaration | InterfaceDeclaration 
        satisfies TopLevelOrMemberDeclaration & GenericDeclaration {
    
    shared formal OpenParameterisedType<ClassDeclaration>? extendedType;
    
    shared formal OpenParameterisedType<InterfaceDeclaration>[] satisfiedTypes;
    
    shared formal OpenType[] caseTypes;
    
    shared formal Boolean isAlias;
    
    // FIXME: should Kind default to TopLevelOrMemberDeclaration?
    shared formal Kind[] memberDeclarations<Kind>() 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    shared formal Kind[] annotatedMemberDeclarations<Kind, Annotation>() 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    "Looks up a member of this package by name and type."
    shared formal Kind? getMemberDeclaration<Kind>(String name) 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    shared formal AppliedClassOrInterface<Anything> apply(Type<Anything>* types);
    
    shared formal AppliedClassOrInterface<Anything> bindAndApply(Object instance, Type<Anything>* types);

    shared formal AppliedMember<Container, Kind> memberApply<Container, Kind>(Type<Anything>* types)
        given Kind satisfies AppliedClassOrInterface<Anything>;

}