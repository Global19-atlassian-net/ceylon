shared void run() {
    assert(exists resource = `module`.resourceByPath("bug6231/static/run.ceylon"));
    print(resource.textContent());
}