package com.github.cao.awa.lilium.framework.config

import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.io.IOUtil
import com.github.cao.awa.lilium.annotations.auto.config.AutoConfig
import com.github.cao.awa.lilium.annotations.auto.config.AutoConfigTemplate
import com.github.cao.awa.lilium.annotations.auto.config.UseConfigTemplate
import com.github.cao.awa.lilium.config.LiliumConfig
import com.github.cao.awa.lilium.config.bootstrap.Inner1Config
import com.github.cao.awa.lilium.config.instance.ConfigEntry
import com.github.cao.awa.lilium.config.template.ConfigTemplate
import com.github.cao.awa.lilium.debug.dependency.circular.CircularDependency
import com.github.cao.awa.lilium.debug.dependency.circular.RequiredDependency
import com.github.cao.awa.lilium.env.LiliumEnv
import com.github.cao.awa.lilium.exception.auto.config.FieldParamMismatchException
import com.github.cao.awa.lilium.exception.auto.config.WrongConfigTemplateException
import com.github.cao.awa.lilium.framework.reflection.ReflectionFramework
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.FileReader
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.BiConsumer

class ConfigFramework : ReflectionFramework() {
    companion object {
        val LOGGER: Logger = LogManager.getLogger("ConfigFramework")
    }

    private val templates: MutableMap<Class<out ConfigTemplate<*>>, ConfigTemplate<*>> =
        ApricotCollectionFactor.hashMap()
    private val configToTemplates: MutableMap<Class<out LiliumConfig>, LiliumConfig> =
        ApricotCollectionFactor.hashMap()

    override fun work() = loadTemplates()

    private fun loadTemplates() {
        reflection().getTypesAnnotatedWith(AutoConfigTemplate::class.java)
            .filter { template ->
                template == ConfigTemplate::class.java
                        || ConfigTemplate::class.java.isAssignableFrom(template)
            }
            .filter(Objects::nonNull)
            .map(this::cast)
            .forEach { templateClass ->
                // 获取此配置的模板路径
                val templateData = templateClass.getAnnotation(AutoConfigTemplate::class.java)

                // 获取实际类型然后创建实例
                val configType: Class<out LiliumConfig> =
                    EntrustEnvironment.cast(toClass(getArgType(templateClass.genericSuperclass)))!!
                val config = configType.getConstructor().newInstance() as LiliumConfig

                // 读取模板格式
                val json = JSONObject.parse(IOUtil.read(FileReader(templateData.value, StandardCharsets.UTF_8)))

                // 创建模板
                try {
                    // 当类型不正确时构建异常链，用以debug
                    EntrustEnvironment.reThrow(
                        { createTemplate(config, json, CircularDependency()) },
                        FieldParamMismatchException::class.java,
                        { WrongConfigTemplateException(templateClass, it.field, it) }
                    )
                } catch (ex: WrongConfigTemplateException) {
                    LOGGER.warn(
                        "Failed to resolve the template '{}' for config '{}'",
                        templateClass.name,
                        config::class.java.name,
                        ex
                    )
                }
                val template = templateClass.getConstructor().newInstance() as ConfigTemplate<*>

                // 给模板设置配置内容并存储备用
                fetchField(template, "config").set(template, config)
                this.templates[templateClass] = template
                this.configToTemplates[configType] = config
            }
    }

    private fun cast(clazz: Class<*>): Class<out ConfigTemplate<*>> = EntrustEnvironment.cast(clazz)!!

    private fun createTemplate(
        target: LiliumConfig,
        getter: JSONObject,
        configChain: CircularDependency
    ) {
        prepareToHandles(target, "", null) { field, _ ->
            // 确保ConfigEntry不为空
            val configEntry = ensureEntryNotNull(target, field)

            // 获取配置要求的类型
            val argType = getArgType(field)

            postProcessing(target, configEntry, null, argType, configChain,
                { parameterized ->
                    // 当依赖是泛型而不是Entry也不是数据

                    // ConfigEntry只能有一层泛型，不允许List<List<List<...>>>
                    val clazz = toClass(parameterized.rawType)

                    // 如果是集合，只创建array list和hash set，其他特殊指定类型会被忽略
                    if (Collection::class.java.isAssignableFrom(clazz)) {
                        var newDelegate: MutableCollection<Any>? = null
                        if (List::class.java == clazz) {
                            newDelegate = ApricotCollectionFactor.arrayList()
                        }
                        if (Set::class.java == clazz) {
                            newDelegate = ApricotCollectionFactor.hashSet()
                        }

                        // 确保已经创建了集合对象
                        if (newDelegate == null) {
                            return@postProcessing
                        }

                        // 获得实际的类型
                        val listTemplate = toClass(parameterized.actualTypeArguments[0])

                        // 添加数据并设置ConfigEntry的值
                        for (value in getTemplateJsonArray(getter, field)) {
                            // 当实际类型是一个依赖时，进一步创建，否则在检查后直接添加
                            if (LiliumConfig::class.java.isAssignableFrom(listTemplate)) {
                                newDelegate.add(
                                    createTemplateObject(
                                        listTemplate,
                                        value as JSONObject,
                                        configChain
                                    )
                                )
                            } else {
                                // 当类型不正确时构建异常链，用以debug
                                EntrustEnvironment.reThrow(
                                    { checkType(listTemplate, value, newDelegate::add) },
                                    ClassCastException::class.java,
                                    { FieldParamMismatchException(field, listTemplate, value::class.java, it) }
                                )
                            }
                        }
                        fetchField(configEntry, "value").set(configEntry, newDelegate)
                    }

                    // 如果是Map，只创建hash map，其他特殊指定类型会被忽略
                    if (Map::class.java.isAssignableFrom(clazz)) {
                        if (parameterized.actualTypeArguments[0] != String::class.java) {
                            throw IllegalArgumentException("The config entry can only use 'java.lang.String' come as the key type")
                        }

                        // 创建并确保已经创建了集合对象
                        val newDelegate: Object2ObjectOpenHashMap<Any, Any> =
                            ApricotCollectionFactor.hashMap() ?: return@postProcessing

                        // 添加数据并设置ConfigEntry的值
                        for (entry in getTemplateJsonData(getter, field)) {
                            newDelegate[entry.key] = entry.value.toString()
                        }
                        fetchField(configEntry, "value").set(configEntry, newDelegate)
                    }
                },
                {
                    // 当依赖是Entry而不是数据

                    // 处理此配置的依赖
                    // 配置对象内所有字段都应为ConfigEntry<LiliumConfig>
                    if (isTemplateDataPresent(getter, field)) {
                        try {
                            // 处理此配置要求的其他依赖
                            createTemplate(
                                configEntry.get() as LiliumConfig,
                                getTemplateJsonData(getter, field),
                                configChain
                            )
                        } catch (ignored: Exception) {
                            // 错误时不要处理，后续的创建会自动换为这一字段的另一个模板
                            // 成功：后续创建遇到此字段都从这拿
                            // 失败：后续创建遇到此字段都从这个字段的模板拿
                            // 其实就是一个覆盖的功能，组装时优先选择来自同一个文件的
                        }
                    }
                },
                {
                    // 当依赖是数据而不是Entry
                    fetchField(configEntry, "value").set(configEntry, getTemplateData(getter, field))
                }
            )
        }
    }

    private fun createTemplateObject(
        configType: Class<*>,
        value: JSONObject,
        configChain: CircularDependency
    ): LiliumConfig {
        val config = configType.getConstructor().newInstance() as LiliumConfig
        createTemplate(
            config,
            value,
            configChain
        )
        return config
    }

    private fun isTemplateDataPresent(getter: JSONObject, field: Field): Boolean {
        return getter.containsKey(field.name) || getter.containsKey(field.getAnnotation(AutoConfig::class.java).value)
    }

    private fun getTemplateData(getter: JSONObject, field: Field): Any {
        var data = getter.get(field.name)
        if (data == null) {
            data = getter.get(field.getAnnotation(AutoConfig::class.java).value)
        }
        return data
    }

    private fun getTemplateJsonData(getter: JSONObject, field: Field): JSONObject {
        var data = getter.getJSONObject(field.name)
        if (data == null) {
            data = getter.getJSONObject(field.getAnnotation(AutoConfig::class.java).value)
        }
        return data
    }

    private fun getTemplateJsonArray(getter: JSONObject, field: Field): JSONArray {
        var data = getter.getJSONArray(field.name)
        if (data == null) {
            data = getter.getJSONArray(field.getAnnotation(AutoConfig::class.java).value)
        }
        return data
    }

    /**
     * @param target 此参数为要处理的对象
     * @param parentTemplate 此模板用以覆盖某种模板的原始数据，用在特定的模板中
     * @param handler 在进行预处理完成后会被调用，进行中间处理
     *
     * @see postProcessing
     */
    private fun prepareToHandles(
        target: Any,
        currentKey: String,
        parentTemplate: LiliumConfig?,
        handler: BiConsumer<Field, LiliumConfig?>
    ) {
        val clazz = target.javaClass

        // 获取此配置对象使用的模板
        val useTemplate = getAnnotation(target, UseConfigTemplate::class.java)
        val configTemplate = useTemplate?.value?.java?.let { getTemplate(it) }

        var template = fetchTemplateConfig(configTemplate)

        // 当parentTemplate存在时，这意味着当前的配置的模板重写了目标模板
        if (parentTemplate != null && template != null) {
            for (field in getFields(parentTemplate)) {
                val configEntry = fetchField(
                    parentTemplate,
                    field.name
                )
                if (getArgType(configEntry) == template!!::class.java && field.name.equals(currentKey)) {
                    template = (configEntry[parentTemplate] as ConfigEntry<*>).get() as LiliumConfig
                }
            }
        }

        if (template != null) {
            for (field in getFields(template)) {
                if (fetchField(target, field.name) == null) {
                    throw IllegalStateException("The '" + clazz.name + "' doesn't match to the config template '" + useTemplate.value.java.name + "'")
                }
            }
        }

        Arrays.stream(
            clazz.declaredFields
        ).filter {
            it.isAnnotationPresent(AutoConfig::class.java)
        }.filter(
            Objects::nonNull
        ).forEach {
            // 当声明的自动配置字段不是ConfigEntry时不做处理
            if (it.type != ConfigEntry::class.java && !ConfigEntry::class.java.isAssignableFrom(it.type)) {
                LOGGER.warn(
                    "The field '{}' is not ConfigEntry, unable to be process",
                    it.name
                )
                return@forEach
            }
            if (!Modifier.isFinal(it.modifiers)) {
                LOGGER.warn(
                    "The field '{}' declared in '{}' is not final modified, may cause explicit changes",
                    it.name,
                    target::class.java.name
                )
            }
            ensureAccessible(it, target)

            handler.accept(it, template)
        }
    }

    fun getTemplate(o: Any): LiliumConfig? {
        return fetchTemplateConfig(
            getAnnotation(
                o,
                UseConfigTemplate::class.java
            )?.value?.java?.let { getTemplate(it) }
        )
    }

    private fun fetchTemplateConfig(configTemplate: ConfigTemplate<*>?): LiliumConfig? {
        return if (configTemplate != null) fetchField(
            configTemplate,
            "config"
        )[configTemplate] as LiliumConfig else null
    }

    fun createConfig(o: Any) {
        createConfig(o, "", getTemplate(o), CircularDependency())
    }

    private fun createConfig(
        target: Any,
        currentKey: String,
        parentTemplate: LiliumConfig?,
        configChain: CircularDependency
    ) {
        prepareToHandles(target, currentKey, parentTemplate) { field, template ->
            // 确保ConfigEntry不为空不
            val configEntry = ensureEntryNotNull(target, field)

            val creatingWithTemplate = {
                // 当依赖是数据而不是Entry

                // 读取模板获得默认值
                val value = if (template != null) {
                    // 首先从当前配置模板中获取
                    val fetchedTemplate = fetchField(template, field.name)[template]
                    var fetchResult: Any? = null
                    if (fetchedTemplate != null && fetchedTemplate != ConfigEntry.ENTRY) {
                        fetchResult = (fetchedTemplate as ConfigEntry<*>).get()
                    }

                    // 若当前配置模板中不存在需要的目标，则使用此模板的原始数据
                    if (fetchResult == null) {
                        val sourceTemplate = this.configToTemplates[template::class.java]!!
                        // 不要忘记fetch到需要的字段后get，这个原始数据是LiliumConfig而不是此字段的值()
                        fetchResult =
                            (fetchField(sourceTemplate, field.name)[sourceTemplate] as ConfigEntry<*>).get()
                    }

                    // 返回结果
                    fetchResult
                } else {
                    // 这边没有template，所以永远是null
                    null
                }

                // 当值存在时则设定
                if (value != null) {
                    fetchField(configEntry, "value").set(configEntry, value)
                }
            }

            postProcessing(
                target, configEntry, parentTemplate, getArgType(field), configChain,
                // 处理泛型的方式和处理普通数据一样
                { creatingWithTemplate() },
                {
                    // 当依赖是Entry而不是数据

                    // 处理此配置的依赖
                    // 配置对象内所有字段都应为ConfigEntry<LiliumConfig>
                    val config = configEntry.get() as LiliumConfig
                    createConfig(config, configEntry.key(), it ?: getTemplate(config), configChain)
                },
                // 处理普通数据
                creatingWithTemplate
            )
        }
    }

    private fun postProcessing(
        o: Any,
        configEntry: ConfigEntry<*>,
        parentTemplate: LiliumConfig?,
        argType: Type,
        configChain: CircularDependency,
        actionWhenParameterized: Consumer<ParameterizedType>,
        actionWhenEntry: Consumer<LiliumConfig>,
        actionWhenData: Runnable
    ) {
        // 当ConfigEntry为空时创建其依赖
        if (configEntry.get() == null) {

            if (argType is ParameterizedType) {
                // 当依赖是泛型而不是Entry也不是数据
                actionWhenParameterized.accept(argType)
            } else {
                val actualClass = toClass(argType)

                // 当依赖是Entry而不是数据
                if (LiliumConfig::class.java.isAssignableFrom(actualClass)) {
                    // 设置新的配置对象
                    fetchField(configEntry, "value").set(configEntry, actualClass.getConstructor().newInstance())

                    // 提交当前的依赖项，同时检查是否循环依赖
                    // 用以快速打断循环依赖，避免发生StackOverflowError
                    configChain.pushRequirement(o.javaClass.name, RequiredDependency().add(actualClass.name))

                    actionWhenEntry.accept(parentTemplate)
                } else {
                    // 当依赖是数据而不是Entry
                    actionWhenData.run()
                }
            }
        }
    }

    private fun getTemplate(clazz: Class<out ConfigTemplate<*>>): ConfigTemplate<*>? {
        return this.templates[clazz]
    }

    private fun ensureEntryNotNull(target: Any, field: Field): ConfigEntry<*> {
        val fieldValue = field[target]
        var configEntry: ConfigEntry<*>? =
            if (fieldValue == null || fieldValue == ConfigEntry.ENTRY) null else fieldValue as ConfigEntry<*>
        configEntry = configEntry ?: field.type.getConstructor().newInstance() as ConfigEntry<*>
        // 设置此ConfigEntry的key为字段名称，要用它来获取多个相同模板的数据以及debug
        fetchField(configEntry, "key").set(configEntry, field.name)
        // 将配置设置回字段
        fetchField(target, field).set(target, configEntry)
        return configEntry
    }

    fun <T : Any> deepCopy(o: T, another: T) {
        deepCopy(o, another, CircularDependency())
    }

    private fun <T : Any> deepCopy(o: T, another: T, configChain: CircularDependency) {
        val clazz = o::class.java

        Arrays.stream(
            clazz.declaredFields
        ).filter {
            it.isAnnotationPresent(AutoConfig::class.java)
        }.filter(
            Objects::nonNull
        ).forEach {
            // 当声明的自动配置字段不是ConfigEntry时不做处理
            if (it.type != ConfigEntry::class.java && !ConfigEntry::class.java.isAssignableFrom(it.type)) {
                LOGGER.warn(
                    "The field '{}' is not ConfigEntry, unable to be process",
                    it.name
                )
                return@forEach
            }
            // 确保ConfigEntry不为空不
            val fieldValue = it[o]
            val configEntry = if (fieldValue == null) null else fieldValue as ConfigEntry<*>?
            if (configEntry == null) {
                return@forEach
            }
            // 先创建用来复制的ConfigEntry
            val newConfigEntry = it.type.getConstructor().newInstance() as ConfigEntry<*>
            // 设置此ConfigEntry的key为字段名称
            fetchField(newConfigEntry, "key").set(newConfigEntry, it.name)
            // 将ConfigEntry设置到新字段
            fetchField(another, it.name).set(another, newConfigEntry)

            // 将配置内容深拷贝到新的ConfigEntry
            postProcessing(o, newConfigEntry, null, getArgType(it), configChain,
                {
                    // TODO 泛型复制
                },
                { template ->
                    // 当依赖是Entry而不是数据

                    // 处理此配置的依赖
                    // 配置对象内所有字段都应为ConfigEntry<LiliumConfig>
                    val oldConfig = configEntry.get() as LiliumConfig
                    val newConfig = newConfigEntry.get() as LiliumConfig
                    deepCopy(oldConfig, newConfig, configChain)
                },
                {
                    // 使用序列化器破坏引用
                    EntrustEnvironment.notNull(
                        fetchField(newConfigEntry, "value")
                    ) { field ->
                        field.set(
                            newConfigEntry,
                            LiliumEnv.BINARY_SERIALIZE_FRAMEWORK.breakRefs(configEntry.get() ?: return@notNull)
                        )
                    }
                }
            )
        }
    }
}
