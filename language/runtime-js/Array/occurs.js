function(e,from,len){
  if (from===undefined||from<0)from=0;
  else if (from>this.arr$.length)return false;
  if (len===undefined)len=this.arr$.length-from;
  var lim=len+from;
  if (lim>this.arr$.length)lim=this.arr$.length;
  if (e===null) {
    for (var i=from;i<lim;i++) {
      if (this.arr$[i]===null)return true;
    }
  } else {
    for (var i=from;i<lim;i++) {
      if (this.arr$[i]!==null && $eq$(e,this.arr$[i]))return true;
    }
  }
  return false;
}
