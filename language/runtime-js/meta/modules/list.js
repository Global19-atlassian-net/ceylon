var mods=[];
for (var m in $loadedModules$) {
  var slashPos = m.lastIndexOf('/');
  mods.push(this.find(m.substring(0,slashPos), m.substring(slashPos+1)));
}
return $arr$sa$(mods,{t:Module$meta$declaration});

