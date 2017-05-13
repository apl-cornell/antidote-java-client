package eu.antidotedb.client;

import com.basho.riak.protobuf.AntidotePB;
import com.google.protobuf.ByteString;

import java.util.Collections;
import java.util.List;

/**
 * The Class AntidoteOuterMVRegister.
 */
public final class CrdtMVRegister<T> extends AntidoteCRDT {


    private MVRegisterRef<T> ref;

    private List<T> values;

    private boolean changed = false;


    CrdtMVRegister(MVRegisterRef<T> ref) {
        this.ref = ref;
    }

    @Override
    public MVRegisterRef<T> getRef() {
        return ref;
    }

    @Override
    void updateFromReadResponse(AntidotePB.ApbReadObjectResp resp) {
        List<ByteString> values = resp.getMvreg().getValuesList();
        this.values = Collections.unmodifiableList(ref.getFormat().decodeList(values));
    }

    @Override
    public void push(AntidoteTransaction tx) {
        if (changed) {
            ref.set(tx, values.get(0));
            changed = false;
        }
    }

    public List<T> getValues() {
        return values;
    }

    public void set(T value) {
        values = Collections.singletonList(value);
        changed = true;
    }

    public static <V> CrdtCreator<CrdtMVRegister<V>> creator(ValueCoder<V> valueCoder) {
        return new CrdtCreator<CrdtMVRegister<V>>() {
            @Override
            public AntidotePB.CRDT_type type() {
                return AntidotePB.CRDT_type.MVREG;
            }

            @Override
            public CrdtMVRegister<V> create(CrdtContainer c, ByteString key) {
                return c.multiValueRegister(key, valueCoder).createAntidoteMVRegister();
            }

            @Override
            public CrdtMVRegister<V> cast(AntidoteCRDT value) {
                return (CrdtMVRegister<V>) value;
            }
        };

    }

    @Deprecated // TODO inline
    public List<T> getValueList() {
        return getValues();
    }
}
