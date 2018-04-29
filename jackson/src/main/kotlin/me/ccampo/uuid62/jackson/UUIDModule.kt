package me.ccampo.uuid62.jackson

import com.fasterxml.jackson.databind.module.SimpleModule
import java.util.*

class UUIDModule : SimpleModule() {

    init {
        addDeserializer<UUID>(UUID::class.java, UUIDDeserializer())
        addSerializer<UUID>(UUID::class.java, UUIDSerializer())
    }

    //TODO: add support for toggling base62 serialization/deserialization -ccampo 2018-04-29
    /*
    private val introspector: UUIDAnnotationIntrospector

    override fun setupModule(context: SetupContext) {
        context.insertAnnotationIntrospector(introspector)
    }
    */
}
