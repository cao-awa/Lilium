package com.github.cao.awa.lilium.network.packet;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotOverride;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.lilium.env.LiliumEnv;
import com.github.cao.awa.lilium.network.packet.handler.PacketHandler;
import com.github.cao.awa.lilium.network.router.request.RequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @param <T>
 * @see UnsolvedPacket
 */
public abstract class Packet<T extends PacketHandler<T>> {
    private static final Logger LOGGER = LogManager.getLogger("Packet");
    private byte[] receipt = createReceipt();
    private T handler;

    public Packet(byte[] receipt) {
        this.receipt = checkReceipt(receipt);
    }

    public Packet(BytesReader reader) {
        try {
            LiliumEnv.PACKET_FRAMEWORK.create(
                    this,
                    reader
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Packet() {

    }

    @DoNotOverride
    public final byte[] receipt() {
        return this.receipt;
    }

    @DoNotOverride
    public final byte[] encodeReceipt() {
        return this.receipt;
    }

    public static byte[] checkReceipt(byte[] receipt) {
        if (receipt.length != 16) {
            throw new IllegalArgumentException("Receipt data only allowed 16 bytes");
        }
        return receipt;
    }

    public static byte[] readReceipt(BytesReader reader) {
        return reader.read(16);
    }

    public static byte[] createReceipt() {
        return BytesRandomIdentifier.create(16);
    }

    @Auto
    @DoNotOverride
    public byte[] payload() {
        return EntrustEnvironment.trys(
                // 编码数据包内容
                () -> LiliumEnv.PACKET_FRAMEWORK.payload(this),
                // 处理错误
                e -> {
                    LOGGER.error("Unexpected exception happened when encoding payload, please report this",
                            e
                    );
                }
        );
    }

    @Auto
    public byte[] id() {
        // 编码ID
        return LiliumEnv.PACKET_FRAMEWORK.id(this);
    }

    @Auto
    @DoNotOverride
    public final byte[] encode(RequestRouter router) {
        byte[] payload = BytesUtil.concat(
                // 每个数据包都有不重复的随机标识符以及时间戳
                // 用以防止重放攻击
                BytesRandomIdentifier.create(16),
                SkippedBase256.longToBuf(TimeUtil.millions()),
                // 数据包ID，用以确定是哪个数据包
                id(),
                // 数据包回执，用来回复请求
                encodeReceipt(),
                // 数据包内容
                payload()
        );

        byte[] digest = MessageDigger.digestToBytes(payload,
                MessageDigger.Sha3.SHA_256
        );

        return router.encode(BytesUtil.concat(
                new byte[]{(byte) digest.length},
                digest,
                payload
        ));
    }

    @Auto
    @DoNotOverride
    public void inbound(RequestRouter router, T handler) {
        this.handler = handler;
        try {
//            LiliumEnv.NETWORK_EVENT_FRAMWOEK.fireEvent(router,
//                    handler,
//                    this
//            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Auto
    @DoNotOverride
    public final T handler() {
        return this.handler;
    }

    @DoNotOverride
    public final <X extends Packet<T>> X receipt(byte[] receipt) {
        this.receipt = checkReceipt(receipt);
        return EntrustEnvironment.cast(this);
    }

    @DoNotOverride
    public int size() {
        return
                // 24是随机标识符(16)加时间戳(8)的和
                24 +
                        // ID长度
                        id().length +
                        // 回执长度，16或者1
                        receipt().length +
                        // 数据包内容长度
                        payload().length;
    }
}
