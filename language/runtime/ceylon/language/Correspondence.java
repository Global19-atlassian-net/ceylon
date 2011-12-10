package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Ignore;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Key", variance = Variance.IN,
            satisfies="ceylon.language.Equality"),
    @TypeParameter(value = "Item", variance = Variance.OUT)
})
public interface Correspondence<Key,Item> {
    
    @TypeInfo("Item|ceylon.language.Nothing")
    public Item item(@Name("key") Key key);

    public boolean defines(@Name("key") Key key);

    public Category getKeys();

    public boolean definesEvery(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Key>")
    Iterable<? extends Key> keys);

    public boolean definesAny(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Key>")
    Iterable<? extends Key> keys);

    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Item|ceylon.language.Nothing>")
    public Iterable<? extends Item> items(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Key>")
    Iterable<? extends Key> keys);

    @Ignore
    class Items<Key,Item>
            implements Sequence<Item> {
        private Sequence<? extends Key> keys;
        private Correspondence<Key, Item> $this;
        Items(Correspondence<Key,Item> $this, Sequence<? extends Key> keys){
            this.keys = keys;
            this.$this = $this;
        }
        public final long getLastIndex() {
            return keys.getLastIndex();
        }
        public final Item getFirst() {
            return $this.item(keys.getFirst());
        }
        public final Iterable<? extends Item> getRest() {
            return $this.items(keys.getRest());
        }
        public final Item item(Integer index) {
            Key key = keys.item(index);
            if (key != null) {
                return $this.item(key);
            }
            else {
                return null;
            }
        }
        @Override
        public java.lang.String toString() {
            return Sequence$impl.toString(this);		
        }
        public final Sequence<Item> getClone() {
            return this;
        }
        @Override
        public Category getKeys() {
            return Correspondence$impl.getKeys(this);
        }
        @Override
        public boolean definesEvery(Iterable<? extends Integer> keys) {
            return Correspondence$impl.definesEvery(this, keys);
        }
        @Override
        public boolean definesAny(Iterable<? extends Integer> keys) {
            return Correspondence$impl.definesAny(this, keys);
        }
        @Override
        public Iterable<? extends Item> items(Iterable<? extends Integer> keys) {
            return Correspondence$impl.items(this, keys);
        }
        @Override
        public boolean getEmpty() {
            return Sequence$impl.getEmpty(this);
        }
        @Override
        public long getSize() {
            return Sequence$impl.getSize(this);
        }
        @Override
        public Item getLast() {
            return Sequence$impl.getLast(this);
        }
        @Override
        public boolean defines(Integer index) {
            return Sequence$impl.defines(this, index);
        }
        @Override
        public Iterator<? extends Item> getIterator() {
            return Sequence$impl.getIterator(this);
        }
        @Override
        public Iterable<? extends Item> segment(Integer from, Integer length) {
        	Iterable<? extends Key> keys = this.keys.segment(from, length);
        	if (keys.getEmpty()) {
        		return $empty.getEmpty();
        	}
        	else {
        		return new Items<Key,Item>($this, (Sequence<? extends Key>)keys);
        	}
        }
        @Override
        public Iterable<? extends Item> span(Integer from, Integer to) {
        	Iterable<? extends Key> keys = this.keys.span(from, to);
        	if (keys.getEmpty()) {
        		return $empty.getEmpty();
        	}
        	else {
        		return new Items<Key,Item>($this, (Sequence<? extends Key>)keys);
        	}
        }
    }
}
