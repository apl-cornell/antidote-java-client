package eu.antidotedb.client;

import com.google.protobuf.ByteString;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;

import javax.annotation.CheckReturnValue;
import eu.antidotedb.antidotepb.AntidotePB;

public class GenericKey extends Key<ByteString> {

    GenericKey(AntidotePB.CRDT_type type, ByteString key) {
        super(type, key);
    }

    @Override
    ByteString readResponseToValue(AntidotePB.ApbReadObjectResp resp) {
        return ResponseDecoder.generic().readResponseToValue(resp);
    }

    @CheckReturnValue
    public final UpdateOpDefaultImpl invoke(ByteString value) {
        return invokeAll(Collections.singletonList(value));
    }

    @SafeVarargs
    @CheckReturnValue
    public final UpdateOpDefaultImpl invokeAll(ByteString... values) {
        return invokeAll(Arrays.asList(values));
    }

    @CheckReturnValue
    public UpdateOpDefaultImpl invokeAll(Iterable<? extends ByteString> values) {
        AntidotePB.ApbGenericUpdate.Builder op = AntidotePB.ApbGenericUpdate.newBuilder();
        for (ByteString value : values) {
            op.addValue(value);
        }

        AntidotePB.ApbUpdateOperation.Builder update = AntidotePB.ApbUpdateOperation.newBuilder();
        update.setGenop(op);
        return new UpdateOpDefaultImpl(this, update);
    }
/*
    @CheckReturnValue
    public UpdateOpDefaultImpl invokeAll(ByteString bin) {
        AntidotePB.ApbGenericUpdate.Builder genericUpdateInstruction = AntidotePB.ApbGenericUpdate.newBuilder();
        genericUpdateInstruction.setValue(bin);

        AntidotePB.ApbUpdateOperation.Builder updateOperation = AntidotePB.ApbUpdateOperation.newBuilder();
        updateOperation.setGenop(genericUpdateInstruction);
        return new UpdateOpDefaultImpl(this, updateOperation);
    }
*/
}