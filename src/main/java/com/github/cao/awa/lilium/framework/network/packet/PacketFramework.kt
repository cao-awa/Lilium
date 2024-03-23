package com.github.cao.awa.lilium.framework.network.packet

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoData
import com.github.cao.awa.lilium.annotation.auto.network.unsolve.AutoSolvedPacket
import com.github.cao.awa.lilium.env.LiliumEnv
import com.github.cao.awa.lilium.framework.reflection.ReflectionFramework
import com.github.cao.awa.lilium.network.packet.Packet
import com.github.cao.awa.lilium.network.packet.UnsolvedPacket
import com.github.cao.awa.lilium.network.packet.factor.unsolve.UnsolvedPacketFactor
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.ByteArrayOutputStream
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.*

class PacketFramework : ReflectionFramework() {
    private val LOGGER: Logger = LogManager.getLogger("PacketFramework")
    private val constructors: MutableMap<Class<out Packet<*>>, Constructor<out Packet<*>>> =
        ApricotCollectionFactor.hashMap()
    private val ids: MutableMap<Class<out Packet<*>>, ByteArray> = ApricotCollectionFactor.hashMap()

    override fun work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(AutoSolvedPacket::class.java)
            .stream()
            .filter(Objects::nonNull)
            .filter { match(it!!) }
            .map { cast(it) }
            .forEach { build(it!!) }
    }

    private fun match(clazz: Class<*>): Boolean {
        return clazz.isAnnotationPresent(AutoSolvedPacket::class.java) && Packet::class.java.isAssignableFrom(clazz)
    }

    private fun cast(clazz: Class<*>): Class<out Packet<*>>? {
        return EntrustEnvironment.cast(clazz)
    }

    private fun build(packet: Class<out Packet<*>>) {
        try {
            val idField =
                fetchField(packet, "ID") ?: throw NullPointerException("The 'ID' of packet '${packet.name}' is unknown")

            val id = idField.get(null) as ByteArray

            this.constructors[packet] = packet.getConstructor(BytesReader::class.java)
            this.ids[packet] = id

            val registerFunction = { bytes: ByteArray ->
                AutoUnsolved(bytes, packet, this)
            }

            UnsolvedPacketFactor.register(
                SkippedBase256.readLong(BytesReader.of(id)),
                registerFunction
            )
        } catch (e: Exception) {
            LOGGER.warn("Unable to register auto solver for packet '{}'", packet.name, e)
        }
    }

    fun payload(target: Packet<*>): ByteArray {
        try {
            ByteArrayOutputStream().use { output ->
                getFields(target).filter {
                    it.isAnnotationPresent(AutoData::class.java)
                }.filter(
                    Objects::nonNull
                ).forEach {
                    ensureAccessible(it)

                    output.write(LiliumEnv.BINARY_SERIALIZE_FRAMEWORK.serialize(it[target]))
                }

                return output.toByteArray()
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun create(target: Packet<*>, reader: BytesReader) {
        getFields(target).filter {
            it.isAnnotationPresent(AutoData::class.java)
        }.filter(
            Objects::nonNull
        ).forEach {
            ensureAccessible(it)

            it[target] = LiliumEnv.BINARY_SERIALIZE_FRAMEWORK.deserialize(it.type, reader)
        }
    }

    fun solve(clazz: Class<out Packet<*>>, reader: BytesReader): Packet<*>? {
        return (this.constructors[clazz] ?: return null).newInstance(reader)
    }

    fun id(type: Class<out Packet<*>>): ByteArray? {
        return this.ids[type]
    }

    fun id(packet: Packet<*>): ByteArray? = id(packet::class.java)

    private class AutoUnsolved(
        data: ByteArray?,
        private val clazz: Class<out Packet<*>>,
        private val framework: PacketFramework
    ) : UnsolvedPacket<Packet<*>>(data) {
        override fun packet(): Packet<*>? {
            return create()
        }

        @Throws(InvocationTargetException::class, InstantiationException::class, IllegalAccessException::class)
        private fun create(): Packet<*>? {
            return (this.framework.solve(
                this.clazz,
                reader()
            ) ?: return null).receipt(receipt())
        }

        override fun requireCrypto(): Boolean {
            return this.clazz.getAnnotation(AutoSolvedPacket::class.java).crypto
        }
    }
}
