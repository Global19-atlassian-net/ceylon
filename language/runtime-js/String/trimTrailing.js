function(/*Boolean(Character)*/chars) {
    var to = this.size;
    if (to > 0) {
        do {--to} while (to>=0 && chars(this.$_get(to)));
        ++to;
    }
    if (to===this.size) {return this;}
    else if (to===0) { return ""; }
    var result = this.substring(0, to);
    return result;
}
