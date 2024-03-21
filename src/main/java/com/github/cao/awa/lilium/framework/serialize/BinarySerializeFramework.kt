package com.github.cao.awa.lilium.framework.serialize

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.lilium.annotations.auto.serialize.AutoSerializer
import com.github.cao.awa.lilium.framework.reflection.ReflectionFramework
import com.github.cao.awa.lilium.framework.serialize.serializer.BinarySerializer
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import java.io.ByteArrayOutputStream
import java.lang.reflect.Field
import java.util.*

class BinarySerializeFramework : ReflectionFramework() {
    private val serializers: MutableMap<Class<*>, BinarySerializer<*>> = ApricotCollectionFactor.hashMap()

    override fun work() {
        reflection().getTypesAnnotatedWith(AutoSerializer::class.java)
            .stream()
            .filter(BinarySerializer::class.java::isAssignableFrom)
            .map(this::cast)
            .map(this::buildSerializer)
            .forEach { serializer ->
                val serializerAnnotation = getAnnotation(serializer, AutoSerializer::class.java)
                addSerializer(Arrays.stream(serializerAnnotation.value).map { c -> c.java }.toList(), serializer)
            }
    }

    fun addSerializer(targets: List<Class<*>>, serializer: BinarySerializer<*>) {
        for (target in targets) {
            this.serializers[target] = serializer
        }
    }

    private fun cast(clazz: Class<*>): Class<out BinarySerializer<*>> {
        return EntrustEnvironment.cast(clazz)!!
    }

    private fun buildSerializer(clazz: Class<out BinarySerializer<*>>): BinarySerializer<*> {
        return clazz.getConstructor().newInstance()
    }

    fun <T> getSerializer(type: Class<T>): BinarySerializer<T> {
        return EntrustEnvironment.cast(this.serializers[type]
            ?: throw NullPointerException("The serializer for '" + type.name + "' is not found, unable to done serialize"))!!
    }

    fun serializeObject(o: Any): ByteArray {
        try {
            ByteArrayOutputStream().use { output ->
                for (field in o.javaClass.declaredFields) {
                    ensureAccessible(field!!, o)
                    getSerializer(field.type).serialize(EntrustEnvironment.cast(o), output)
                }
                return output.toByteArray()
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun serialize(o: Any): ByteArray {
        try {
            ByteArrayOutputStream().use { output ->
                getSerializer(o::class.java).serialize(EntrustEnvironment.cast(o), output)

                return output.toByteArray()
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun deserialize(o: Any, data: ByteArray) {
        val reader = BytesReader.of(data)

        for (field in o.javaClass.declaredFields) {
            ensureAccessible(field!!, o)
            EntrustEnvironment.trys {
                field[o] = getSerializer(field.type).deserialize(reader)
            }
        }
    }

    fun <T> deserialize(type: Class<T>, data: BytesReader): T {
        return getSerializer(type).deserialize(data)
    }

    fun <T : Any> breakRefs(o: T): T {
        val result: T = deserialize(
            o::class.java,
            BytesReader.of(serialize(o))
        )
        return result
    }
}
