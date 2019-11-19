package eu.antidotedb.client;

import com.google.protobuf.ByteString;
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
    public UpdateOp invoke(ByteString bin) {
        AntidotePB.ApbGenericUpdate.Builder genericUpdateInstruction = AntidotePB.ApbGenericUpdate.newBuilder();
        genericUpdateInstruction.setValue(bin);
        AntidotePB.ApbUpdateOperation.Builder updateOperation = AntidotePB.ApbUpdateOperation.newBuilder();
        updateOperation.setGenop(genericUpdateInstruction);
        return new UpdateOpDefaultImpl(this, updateOperation);
    }

}