function(f){
  var a=this.arr$;
  return for$(function(){
    var i=0,l=a.length;
    return function() {
      var r=i<l?a[i++]:finished();
      while (r!==finished() && !f(r)) {
        r=i<l?a[i++]:finished();
      }
      return r;
    };
  },{Element$Iterable:this.$a$.Element$Array,Absent$Iterable:{t:Null}});
}
