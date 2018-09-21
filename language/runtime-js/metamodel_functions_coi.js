//ClassOrInterface.getClassOrInterface
function coigetcoi$(coi,name$2,types$3,$$$mptypes,noInherit){
  if (noInherit && !extendsType($$$mptypes.Container$getClassOrInterface,{t:coi.tipo})) {
    throw IncompatibleTypeException$meta$model("Incompatible container");
  }
  if (!extendsType($$$mptypes.Kind$getClassOrInterface, {t:ClassOrInterface$meta$model})) {
    throw IncompatibleTypeException$meta$model("Kind must be ClassOrInterface");
  }
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getClassOrInterface);
  if(types$3===undefined){types$3=empty();}
  var mm = getrtmm$$(_tipo);
  var nom = name$2 + '$' + mm.d[mm.d.length-1];
  //When we get an object function we need to go deeper to get its type
  if (!_tipo.$$ && mm.$t) {
    _tipo=mm.$t.t;
  }
  var ic = _tipo.$$.prototype[nom];
  if (!ic) {
    //If we're looking for an attribute, the naming is different
    var nom2='$prop$get'+name$2[0].toUpperCase()+name$2.substring(1);
    ic=_tipo.$$.prototype[nom2];
  }
  if (!ic) {
    if (noInherit)return null;
    var pere=mm['super'];
    while (!ic && pere) {
      mm=getrtmm$$(pere.t);
      nom=mm&&mm.d?name$2+'$'+mm.d[mm.d.length-1]:undefined;
      if (nom)ic=_tipo.$$.prototype[nom];
      if (!ic)pere=mm['super'];
    }
  }
  if (ic) {
    mm = getrtmm$$(ic);
    var md = get_model(mm);
    var rv;
    var ict={t:ic};
    var _cont={t:_tipo};
    if (coi.$targs)_cont.a=coi.$targs;
    if (md.mt==='i') {
      if (!extendsType({t:Interface$meta$model},{t:$$$mptypes.Kind$getClassOrInterface.t}))throw IncompatibleTypeException$meta$model("Member " + name$2 + " is an interface");
      validate$typeparams(ict,ic.$m$.tp,types$3);
      rv=AppliedMemberInterface$jsint(ic, {Container$AppliedMemberInterface:_cont,Type$AppliedMemberInterface:ict});
    } else if (md.mt==='c'){
      if (!extendsType({t:Class$meta$model},{t:$$$mptypes.Kind$getClassOrInterface.t}))throw IncompatibleTypeException$meta$model("Member " + name$2 + " is a class");
      validate$typeparams(ict,ic.$m$.tp,types$3);
      var _a=tupleize$params(mm.ps);
      rv=AppliedMemberClass$jsint(ic, {Container$AppliedMemberClass:_cont,Type$AppliedMemberClass:ict, Arguments$AppliedMemberClass:_a});
    } else {
      throw IncompatibleTypeException$meta$model("Member " + name$2 + " is not a class or interface");
    }
    if (ict.a)rv.$targs=ict.a;
    rv.$parent=coi;
    return rv;
  }
  return null;
}
function coiclasse$(coi,anntypes,$$$mptypes,noInherit){
  var mems=[];
  if (anntypes===undefined)anntypes=empty();
  var ats=coi$get$anns(anntypes);
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getClasses);
  for (m in _tipo.$$.prototype) {
    var mem=getnpmem$(_tipo.$$.prototype,m);
    if (mem && mem.$$) {
      var mm=getrtmm$$(mem);
      if (mm && mm.d && mm.d[mm.d.length-2]==='$c') {
        if (noInherit && mm.$cont!==coi.tipo)continue;
        if (!extendsType({t:mem},$$$mptypes.Type$getClasses))continue;
        var anns=allann$(mm);
        if (anns && coi$is$anns(anns,ats) && validate$params(mm.ps,$$$mptypes.Arguments$getClasses,'',1)) {
          mems.push(AppliedMemberClass$jsint(mem, {Container$AppliedMemberClass:_tipo,Type$AppliedMemberClass:{t:mem}, Arguments$AppliedMemberClass:$$$mptypes.Arguments$getClasses}));
        }
      }
    }
  }
  return $arr$sa$(mems,{t:MemberClass$meta$model,a:{Arguments$MemberClass:$$$mptypes.Arguments$getClasses,Container$MemberClass:$$$mptypes.Container$getClasses,Type$MemberClass:$$$mptypes.Type$getClasses}});
}
function coicla$(coi,name,types,cont,noInherit) {
  var rv=coigetcoi$(coi,name,types,{Container$getClassOrInterface:cont,
    Kind$getClassOrInterface:Class$meta$model},noInherit);
  if (rv && !is$(rv, {t:AppliedMemberClass$jsint})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not a class");
  }
  return rv;
}
//used to be InterfaceModel.declaration
function coimoddcl$(ifc) {
  if (ifc._decl)return ifc._decl;
  var mm = getrtmm$$(ifc.tipo);
  var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
  var cls = is$(ifc,{t:ClassModel$meta$model});
  ifc._decl = (cls?openClass$jsint:OpenInterface$jsint)(fmp$(_m['$mod-name'],_m['$mod-version'],mm.d[0]), ifc.tipo);
  return ifc._decl;
}
//Class.parameterTypes (works also for constructors)
function clsparamtypes(cls) {
  var ps=cls.tipo.$m$.ps;
  if (!ps || ps.length==0)return empty();
  var r=[];
  for (var i=0; i < ps.length; i++) {
    var pt=ps[i].$t;
    if (typeof(pt)==='string'){
      if (!cls.$targs)throw TypeApplicationException$meta$model("This class model needs type parameters " + cls.string);
      pt=cls.$targs[pt];
      if (!pt)throw TypeApplicationException$meta$model("Class model is missing type argument for "
        + cls.string + "<" + ps[i].$t + ">");
    }
    r.push(typeLiteral$meta({Type$typeLiteral:pt},cls.$targs));
  }
  return $arr$sa$(r,{t:Type$meta$model,a:{t:Anything}});
}
function coigetifc$(coi,anntypes,$$$mptypes,noInherit){
  var mems=[];
  if (anntypes===undefined)anntypes=empty();
  var ats=coi$get$anns(anntypes);
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getInterfaces);
  for (m in _tipo.$$.prototype) {
    var mem=getnpmem$(_tipo.$$.prototype,m);
    if (mem && mem.$$) {
      var mm=getrtmm$$(mem);
      if (mm && mm.d && mm.d[mm.d.length-2]==='$i') {
        if (noInherit && mm.$cont!==coi.tipo)continue;
        if (!extendsType({t:mem},$$$mptypes.Type$getInterfaces))continue;
        var anns=allann$(mm);
        if (anns && coi$is$anns(anns,ats)) {
          mems.push(AppliedMemberInterface$jsint(mem, {Container$AppliedMemberInterface:_tipo,Type$AppliedMemberInterface:{t:mem}}));
        }
      }
    }
  }
  return $arr$sa$(mems,{t:MemberInterface$meta$model,a:{Container$MemberInterface:$$$mptypes.Container$getInterfaces,Type$MemberInterface:$$$mptypes.Type$getInterfaces}});
}
function coiifc$(coi,name,types,cont,noInherit){
  var rv=coigetcoi$(coi,name,types,{Container$getClassOrInterface:cont,
    Kind$getClassOrInterface:Interface$meta$model},noInherit);
  if (rv && !is$(rv, {t:AppliedMemberInterface$jsint})) {
    throw IncompatibleTypeException$meta$model("Member " + name + " is not an interface");
  }
  return rv;
}
function coiatr$(coi,name,m,noInherit){
  if (noInherit && !extendsType(m.Container$getAttribute,{t:coi.tipo})) {
    throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
  }
  var _tipo=mmfca$(coi.tipo,m.Container$getAttribute);
  var nom = '$prop$get' + name[0].toUpperCase() + name.substring(1);
  var at = _tipo.$$.prototype[nom];
  if (!at) {
    nom = '$prop$get$_' + name;
    at = _tipo.$$.prototype[nom];
    if (!at) {
      //Last resort: check every private attribute
      for (nom in _tipo.$$.prototype) {
        if (nom.startsWith("$prop$get$")) {
          var mm=getrtmm$$(_tipo.$$.prototype[nom]);
          if (mm && mm.d && mm.d[mm.d.length-1].startsWith(name+"$")) {
            at=_tipo.$$.prototype[nom];break;
          }
        }
      }
    }
  }
  if (!at)return null;
  var mm=getrtmm$$(at);
  var _t=m.Get$getAttribute;
  if (mm && mm.$t) {
    if (!extendsType(mm.$t,_t))throw IncompatibleTypeException$meta$model("Incompatible Get type argument");
    if (!extendsType(m.Set$getAttribute,at.set?mm.$t:{t:Nothing}))throw IncompatibleTypeException$meta$model("Incompatible Set type argument");
    _t=mm.$t;
  }
  if(rejectInheritedOrPrivate$(mm, coi.tipo, noInherit))
    return null;
  var rv=AppliedAttribute(name, at, {Get$Attribute:_t,Set$Attribute:at.set?_t:{t:Nothing}, Container$Attribute:{t:_tipo}});
  if (coi.$targs)rv.$a$.Container$Attribute.a=coi.$targs;
  rv.$parent=coi;
  return rv;
}
function coigetatr$(coi,anntypes,$$$mptypes,noInherit){
  var mems=[];
  if (anntypes===undefined)anntypes=empty();
  var ats=coi$get$anns(anntypes);
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getAttributes);
  for (m in _tipo.$$.prototype) {
    if (m.substring(0,6)==='$prop$') {
      var mm=getrtmm$$(_tipo.$$.prototype[m]);
      if (mm) {
        if(rejectInheritedOrPrivate$(mm, coi.tipo, noInherit))
          continue;
        if (!extendsType(mm.$t,$$$mptypes.Get$getAttributes))continue;
        var setter=_tipo.$$.prototype[m].set && extendsType($$$mptypes.Set$getAttributes,mm.$t);
        if ($$$mptypes.Set$getAttributes.t!==Nothing && !setter)continue;
        var anns=allann$(mm);
        if (anns && coi$is$anns(anns,ats)) {
          var atname=mm.d[mm.d.length-1];
          if (atname.indexOf('$')>0)atname=atname.substring(0,atname.indexOf('$'));
          mems.push(AppliedAttribute(atname,_tipo.$$.prototype[m],{Container$Attribute:{t:coi.tipo},Get$Attribute:mm.$t,Set$Attribute:setter?mm.$t:{t:Nothing}}));
        }
      }
    }
  }
  return $arr$sa$(mems,{t:Attribute$meta$model,a:{Set$Attribute:$$$mptypes.Set$getAttributes,Container$Attribute:$$$mptypes.Container$getAttributes,Get$Attribute:$$$mptypes.Get$getAttributes}});
}
function coimtd$(coi,name,types,$$$mptypes,noInherit){
  if (noInherit && !extendsType($$$mptypes.Container$getMethod,{t:coi.tipo})) {
    throw IncompatibleTypeException$meta$model("Incompatible Container type argument");
  }
  if (types===undefined)types=empty();
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getMethod);
  if (_tipo && !_tipo.$$) {
    //if _tipo is an object, go deeper to get its type
    var mm=getrtmm$$(_tipo);
    if (mm && mm.$t) {
      _tipo=mm.$t.t;
    }
  }
  var fun = findMethodByNameFromPrototype$(_tipo.$$.prototype, name);
  if(!fun)
    return null;
  var mm=getrtmm$$(fun);
  var _t=$$$mptypes.Type$getMethod;
  var _a=$$$mptypes.Arguments$getMethod;
  if (mm) {
    if (mm.$t){
      _t=mm.$t;
      if (typeof(_t)==='string') {
        var _ta;
        if (coi.$targs && coi.$targs[_t])_t=coi.$targs[_t];
        else if ($$$mptypes.Container$getMethod && $$$mptypes.Container$getMethod.a && $$$mptypes.Container$getMethod.a[_t])_t=$$$mptypes.Container$getMethod.a[_t];
        else if (mm.tp && mm.tp[_t])_ta=mm.tp[_t];
        else if (_tipo.$m$.tp && _tipo.$m$.tp[_t])_ta=_tipo.$m$.tp[_t];
        if (_ta && _ta.sts)_t=_ta.sts.length===1?_ta.sts[0]:{t:'i',l:_ta.sts};
        if (typeof(_t)==='string')_t={t:Anything};
      }
      if (!extendsType(_t,$$$mptypes.Type$getMethod))throw IncompatibleTypeException$meta$model("Incompatible Type argument");
    }
    validate$params(mm.ps,_a,"Wrong number of Arguments for getMethod");
    _a=tupleize$params(mm.ps);
  }
  if(rejectInheritedOrPrivate$(mm, coi.tipo, noInherit)){
    return null;
  }
  var containerTarg={t:_tipo};
  if ($$$mptypes.Container$getMethod.a) {
    containerTarg.a=$$$mptypes.Container$getMethod.a;
  }
  return AppliedMethod$jsint(fun,$ci2na$(types),{Container$Method:containerTarg,Type$Method:_t,Arguments$Method:_a});
}
function coigetmtd$(coi,anntypes,$$$mptypes,noInherit){
  var mems=[];
  if (anntypes===undefined)anntypes=empty();
  var ats=coi$get$anns(anntypes);
  var _tipo=mmfca$(coi.tipo,$$$mptypes.Container$getMethods);
  for (m in _tipo.$$.prototype) {
    var mem=getnpmem$(_tipo.$$.prototype,m);
    if (mem && mem.$$===undefined) {
      var mm=getrtmm$$(mem);
      if (mm && mm.d && mm.d[mm.d.length-2]=='$m') {
        if(rejectInheritedOrPrivate$(mm, coi.tipo, noInherit))
          continue;
        if (!extendsType(mm.$t,$$$mptypes.Type$getMethods))continue;
        var anns=allann$(mm);
        if (!mm.tp && anns && coi$is$anns(anns,ats) && validate$params(mm.ps,$$$mptypes.Arguments$getMethods,'',1)) {
          var types=[];
          if (mm.ps) for (var i=0; i<mm.ps.length;i++) {
            types.push(typeLiteral$meta({Type$typeLiteral:mm.ps[i].$t}));
          }
          var containerTarg={t:_tipo};
          if ($$$mptypes.Container$getMethods.a) {
            containerTarg.a=$$$mptypes.Container$getMethods.a;
          }
          mems.push(AppliedMethod$jsint(mem,undefined,{Container$Method:containerTarg,
                    Type$Method:mm.$t,Arguments$Method:types.length?{t:'T',l:types}:{t:Empty}}));
        }
      }
    }
  }
  return $arr$sa$(mems,{t:Method$meta$model,a:{Container$Method:$$$mptypes.Container$getMethods,Arguments$Method:$$$mptypes.Arguments$getMethods,Type$Method:$$$mptypes.Type$getMethods}});
}
//ClassOrInterface.satisfiedTypes
function coisattype$(coi){
  var ints = coi.tipo.$m$.sts;
  if (ints && ints.length) {
    var rv = [];
    for (var i=0; i < ints.length; i++) {
      var ifc = coirestarg$(coi,ints[i]);
      var mm=getrtmm$$(ifc.t);
      if (mm.$cont) {
        rv.push(AppliedMemberInterface$jsint(ifc.t, {Type$AppliedMemberInterface:ifc,Container$AppliedMemberInterface:{t:mm.$cont}}));
      } else {
        rv.push(AppliedInterface$jsint(ifc.t, {Type$AppliedInterface:ifc}));
      }
    }
    return $arr$(rv,{t:InterfaceModel$meta$model,a:{Type$InterfaceModel:{t:Anything}}});
  }
  return empty();
}
//ClassOrInterface.extendedType
function coiexttype$(coi){
  var mm = getrtmm$$(coi.tipo);
  var sc = mm['super'];
  if (sc === undefined)return null;
  var scmm = getrtmm$$(sc.t);
  var _t=coirestarg$(coi,sc);
  var ac;
  if (scmm.$cont) {
    ac=AppliedMemberClass$jsint(sc.t, {Type$AppliedMemberClass:_t,Arguments$AppliedMemberClass:{t:Sequential,a:{Element$Iterable:{t:Anything}}},Container$AppliedMemberClass:{t:scmm.$cont}});
  } else {
    ac=AppliedClass$jsint(sc.t, {Type$AppliedClass:_t,Arguments$AppliedClass:{t:Sequential,a:{Element$Iterable:{t:Anything}}}});
  }
  if (_t.a)ac.$targs=_t.a;
  return ac;
}
function coicase$(coi){
  var cts = coi.tipo.$m$.of;
  if (cts && cts.length > 0) {
    var rv=[];
    for (var i=0; i < cts.length; i++) {
      if (typeof(cts[i])==='function') {
        rv.push(cts[i]());
      }
    }
    return $arr$sa$(rv,{t:coi.tipo});
  }
  return empty();
}
//ClassOrInterface.string
function coistr$(coi) {
  if (coi.tipo===Tuple && coi.$a$.Target$Type.t==='T' && coi.$a$.Target$Type.l){
    var tn='[';
    var ttl=coi.$a$.Target$Type.l;
    for (var i=0; i<ttl.length;i++) {
      if (i>0)tn+=',';
      if (ttl[i].t==='u' && ttl[i].l.length===2 && ttl[i].seq) {
        if (ttl[i].l[0].t===Sequential || ttl[i].l[0].t===Sequence) {
          tn+=qname$(ttl[i].l[1])+(ttl[i].seq===2?'+':'*');
          continue;
        } else if (ttl[i].l[1].t===Sequential || ttl[i].l[1].t===Sequence) {
          tn+=qname$(ttl[i].l[0])+(ttl[i].seq===2?'+':'*');
          continue;
        }
      }
      tn+=qname$(ttl[i]);
      if (ttl[i].seq)tn+=ttl[i].seq===2?'+':'*';
    }
    tn+=']';
    return tn;
  }
  var mm=getrtmm$$(coi.tipo);
  var cc=[coi];
  var src=coicont$(coi);
  while(src) {
    cc.unshift(src);
    src=coicont$(src);
  }
  mm=getrtmm$$(cc[0].tipo);
  var qn=mm.d[0];
  if (qn==='$')qn='ceylon.language';
  qn+='::' + mm.d[mm.d.length-1];
  if (qn.indexOf('$')>0)qn=qn.substring(0,qn.indexOf('$'));
  function simplename(t) {
    var s='';
    if (t.t==='i'||t.t==='u') {
      for (var i=0;i<t.l.length;i++) {
        if (s.length)s+= t.t==='i'?'&':'|';
        s+=simplename(t.l[i]);
      }
    } else if (t.t==='T') {
      s+= '[';
      for (var tttt=0; tttt<t.l.length;tttt++) {
        if (tttt>0)s+=',';
        s+=qname$(t.l[tttt]);
      }
      s+=']';
    } else {
      if (t.uv) {
        s+=t.uv+' ';
      }
      s+=qname$(getrtmm$$(t.t));
      if (t.a)s+=addtargs(t);
    }
    return s;
  }
  function addtargs(x) {
    var mmm=getrtmm$$(x.t);
    var s='',tparms=mmm&&mmm.tp;
    if (tparms===undefined)return s;
    var tsrc=x.a || coi.$targs;
    for (var ta in tparms) {
      var t2=tsrc[ta];
      if (t2) {
        if (s.length>1)s+=',';else s='<';
        s+=simplename(t2);
      }
    }
    if (s.length)s+='>';
    return s;
  }
  if (cc[0].tipo===Callable) {
    qn+=addtargs({t:Callable});
  } else {
    qn+=addtargs(cc[0].$a$.Target$Type);
  }
  for (var i=1;i<cc.length;i++) {
    mm=getrtmm$$(cc[i].tipo)
    var nm=mm.d[mm.d.length-1];
    if (nm.indexOf('$')>0)nm=nm.substring(0,nm.indexOf('$'));
    qn+='.'+nm+addtargs(cc[i].$a$.Target$Type);
  }
  return qn;
}
//ClassOrInterface.hash
function coihash$(coi) {
  var mm = getrtmm$$(coi.tipo);
  var h=qname$(mm).hash;
  if (mm.tp) {
    for (var tp in mm.tp) {
      var targ;
      if (coi.$a$ && coi.$a$.Type$ClassOrInterface && coi.$a$.Type$ClassOrInterface.a && coi.$a$.Type$ClassOrInterface.a[tp]) {
        var _targ=coi.$a$.Type$ClassOrInterface.a[tp];
        if (typeof(_targ)==='string') {
          console.log("TODO buscar " + tp + "->" + _targ + " para " + coi.declaration.qualifiedName);
          _targ={t:Anything};
        }
        targ=typeLiteral$meta({Type$typeLiteral:_targ});
        if (_targ.uv==='out')h+=1;
        else if (_targ.uv==='in')h+=2;
      } else {
        targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
      }
      h+=targ.hash;
    }
  }
  if (coi.$bound)h+=coi.$bound.hash;
  return h;
}
//ClassOrInterface.container
function coicont$(coi) {
  if (coi.$parent)return coi.$parent;
  var cont = getrtmm$$(coi.tipo).$cont;
  if (cont===undefined)return null;
  if (cont===0)return coi.containingPackage;
  var cmm=getrtmm$$(cont);
  var mod=get_model(cmm);
  while (cont && !(mod.mt==='i' || mod.mt==='c')) {
    cont=getrtmm$$(cmm);
    if (cont) {
      cmm=getrtmm$$(cont);
      mod=get_model(cmm);
    }
  }
  if (!cont)return null;
  var _t={t:cont};
  var rv;
  var ttargs=coi.$a$ && (coi.$a$.Container$Member||coi.$a$.Container$AppliedMemberClass||coi.$a$.Container$AppliedMemberInterface);
  if (ttargs) {
    ttargs=ttargs.a;
  } else {
    ttargs=coi.$targs;
  }
  if (ttargs)_t.a=ttargs;
  if (coi.src$ && coi.src$.outer$) {
    var _out=coi.src$.outer$;
    if (_out.$a$) {
      _t.a=_out.$a$;
    }
    //TODO this could still be a member class
    rv=AppliedClass$jsint(cont,{Type$AppliedClass:_t,Arguments$AppliedClass:{t:Sequential,a:{Element$Iterable:{t:Anything}}}});
    rv.src$=_out;
  } else {
    if (get_model(cmm).mt === 'i') {
      rv=AppliedInterface$jsint(cont,{Type$Interface:_t});
    } else {
      //TODO tipos de parametros
      rv=AppliedClass$jsint(cont,{Type$AppliedClass:_t,Arguments$AppliedClass:{t:Sequential,a:{Element$Iterable:{t:Anything}}}});
    }
  }
  coi.$parent=rv;
  return rv;
}
//ClassOrInterface.typeArguments
function _coitarg_$(coi,makeItem,maptarg){
  var mm = getrtmm$$(coi.tipo);
  if (mm) {
    if (mm.tp) {
      var typeTargs=coi.$a$ && coi.$a$.Type$ClassOrInterface;
      if (typeTargs) {
        if (coi.tipo===Tuple && typeTargs.t==='T') {
          typeTargs=retpl$(typeTargs);
          coi.$a$.Type$ClassOrInterface=typeTargs;
        }
        typeTargs=typeTargs.a;
      }
      var targs={};
      var ord=[];
      for (var tp in mm.tp) {
        var param = OpenTypeParam$jsint(coi.tipo,tp);
        var targ,_targ=typeTargs && typeTargs[tp];
        if (_targ) {
          if (typeof(_targ)==='string') {
            console.log("TODO buscar " + tp + "->" + _targ + " para " + coi.declaration.qualifiedName);
            _targ={t:Anything};
          }
          targ=typeLiteral$meta({Type$typeLiteral:_targ});
        } else {
          targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
        }
        targs[param.qualifiedName]=[param,makeItem(coi,_targ||0,targ)];
        ord.push(param.qualifiedName);
      }
      return TpMap$jsint(targs,ord,{V$TpMap:maptarg});
    }
    return empty();
  }
  throw new Error("ClassOrInterface.typeArguments: missing metamodel!");
}

function coitarg$(coi){
  return _coitarg_$(coi,function(c,t,a){return a;},
    {t:Type$meta$model,a:{Type$Type:{t:Anything}}});
}
//ClassOrInterface.typeArgumentWithVariances
function coitargv$(coi){
  return _coitarg_$(coi,function(c,t,a){
    var iance;
    if (t.uv==='out'){
      iance=covariant$meta$declaration();
    } else if (t.uv==='in'){
      iance=contravariant$meta$declaration();
    } else {
      iance=invariant$meta$declaration();
    }
    return tpl$([a,iance]);
  }, {t:'T', l:[{t:Type$meta$model,a:{t:Anything}},{t:Variance$meta$declaration}]});
}

//ClassOrInterface.typeArgumentList
function _coitargl_$(coi,makeItem,listarg){
  var mm = getrtmm$$(coi.tipo);
  if (mm) {
    if (mm.tp) {
      var typeTargs=coi.$a$ && coi.$a$.Type$ClassOrInterface;
      if (typeTargs) {
        if (coi.tipo===Tuple && typeTargs.t==='T') {
          typeTargs=retpl$(typeTargs);
          coi.$a$.Type$ClassOrInterface=typeTargs;
        }
        typeTargs=typeTargs.a;
      }
      var ord=[];
      for (var tp in mm.tp) {
        var targ;
        var _targ=typeTargs && typeTargs[tp];
        if (_targ) {
          if (typeof(_targ)==='string') {
            console.log("TODO buscar " + tp + "->" + _targ + " para " + coi.declaration.qualifiedName);
            _targ={t:Anything};
          }
          targ=typeLiteral$meta({Type$typeLiteral:_targ});
        } else {
          targ=typeLiteral$meta({Type$typeLiteral:{t:Anything}});
        }
        ord.push(makeItem(coi,_targ||0,targ));
      }
      return $arr$sa$(ord,listarg);
    }
    return empty();
  }
  throw new Error("ClassOrInterface.typeArgumentList: missing metamodel!");
}
function coitargl$(coi) {
  return _coitargl_$(coi,function(c,t,a){return a;},
    {t:Type$meta$model,a:{Target$Type:Anything}});
}
//ClassOrInterface.typeArgumentWithVarianceList
function coitargvl$(coi){
  return _coitargl_$(coi,function(c,t,a){
    var iance;
    if (t.uv==='out'){
      iance=covariant$meta$declaration();
    } else if (t.uv==='in'){
      iance=contravariant$meta$declaration();
    } else {
      iance=invariant$meta$declaration();
    }
    return tpl$([a,iance]);
  },{t:'T', l:[{t:Type$meta$model,a:{t:Anything}},{t:Variance$meta$declaration}]});
}

//Resolve Type Argument
function coirestarg$(root,type) {
  if (type.a) {
    var t2 = {t:type.t, a:{}};
    for (var targ in type.a) {
      if (typeof(type.a[targ])==='string') {
        var ttt=root.$a$.Type$ClassOrInterface;
        if (ttt.t==='T') {
          t2.a[targ]=ttt;
        } else {
          t2.a[targ]=ttt.a[type.a[targ]]
        }
      } else {
        t2.a[targ]=type.a[targ];
      }
      if (t2.a[targ] && t2.a[targ].a) {
        t2.a[targ]=coirestarg$(root,t2.a[targ]);
      }
    }
    type=t2;
  }
  return type;
}
function coiTypeOf(coi,instance$8){
  var _t={t:coi.tipo};
  if (coi.$targs)_t.a=coi.$targs;
  return is$(instance$8,_t);
}
//Union Type of the receiving type with a new one
function coiut$(coi,t,ot){
  if (is$(t,{t:AppliedUnionType$jsint})) {
    return t.union(coi,{Other$union:ot});
  }
  var _ut={t:'u',l:[coi.$a$.Type$ClassOrInterface,t.$a$.Target$Type]};
  return AppliedUnionType$jsint(_ut,$arr$([coi,t],_ut,1),{Union$AppliedUnionType:_ut});
}
//Intersection type of the receiving type with a new one
function coiit$(coi,t,ot){
  if (is$(t,{t:AppliedIntersectionType$jsint})) {
    return t.intersection(coi,{Other$intersection:ot});
  }
  var _ut={t:'i',l:[coi.$a$.Type$ClassOrInterface,t.$a$.Target$Type]};
  return AppliedIntersectionType$jsint(_ut,$arr$([coi,t],_ut,1),{Union$AppliedIntersectionType:_ut});
}
//Get annotations from ClassOrInterface, based on the specified annotation types
function coi$get$anns(anntypes) {
  var ats=[];
  if (!anntypes)return ats;
  var iter=anntypes.iterator();
  var a;while((a=iter.next())!==finished()){
    ats.push({t:a.tipo});
  }
  return ats;
}
function coi$is$anns(anns,ats) {
  for (var i=0;i<ats.length;i++) {
    var f=false;
    for (var j=0;j<anns.length;j++) {
      f|=(is$(anns[j],ats[i]));
    }
    if (!f)return false;
  }
  return true;
}
//Compare type argument use-site variance
function cmp$targ$uv$(a,ta) {
  if (a===undefined&&ta===undefined) {
    return true;
  }
  if (a&&ta) {
    for (var _t in a) {
      if (!ta[_t] || a[_t].uv!==ta[_t].uv)return false;
    }
    return true;
  }
  return false;
}
//Return a list of the constructors with the specified
//annotations and the specified prefix in its name
//and the specified metatype
//coi: the ClassOrInterface
//anntypes: the annotation types
//prefix: The name we're looking for
//tipo:The type of the constructor (used for type argument lookup)
//arg$: the argument types for callable constructors (pass undefined for value constructors)
function coiclsannconstrs$(coi,anntypes,prefix,tipo,arg$) {
  var ats=coi$get$anns(anntypes);
  var cs=[];
  for (var cn in coi.tipo) {
    if (cn.startsWith(prefix)) {
      var mm=getrtmm$$(coi.tipo[cn]);
      if (mm.d[mm.d.length-2]==='$cn' && coi$is$anns(allann$(mm),ats)) {
        var parms=mm.ps && mm.ps.slice(0);
        if (parms && arg$ && arg$.t!==Nothing && tipo.a) {
          for (var i=0;i<parms.length;i++) {
            parms[i] = {$t:restype$(tipo,parms[i].$t)};
          }
        }
        if ((arg$===undefined&&mm.ps===undefined) || (arg$!==undefined&&mm.ps!==undefined && validate$params(parms,arg$,"",1))) {
          var args=parms?tupleize$params(parms,coi.$targs):empty();
          cs.push({tipo:coi.tipo[cn],args:args});
        }
      }
    }
  }
  if (cs.length===0 && coi.defaultConstructor && coi.defaultConstructor.fakeConstr$) {
    mm=getrtmm$$(coi.tipo);
    for (var i=0;i<ats.length;i++) {
      if (ats[i].t===DocAnnotation) {
        return cs;
      }
    }
    if (coi$is$anns(allann$(mm),ats)) {
      var parms=mm.ps && mm.ps.slice(0);
      if (parms && arg$&&arg$.t!==Nothing && tipo.a) {
        for (var i=0;i<parms.length;i++) {
          parms[i] = {$t:restype$(tipo,parms[i].$t)};
        }
      }
      if ((mm.ps===undefined&&arg$===undefined) || (arg$!==undefined&&mm.ps!==undefined&&validate$params(parms,arg$,"",1))) {
        var args=parms?tupleize$params(parms,coi.$targs):empty();
        cs.push({tipo:coi.tipo,args:args,fakeConstr:true});
      }
    }
  }
  return cs;
}
