package com.github.ricky12awesome.jss.dsl

import com.github.ricky12awesome.jss.globalJson
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

private val DEFAULT_ENUM = JsonArray(listOf())

var <T> PropertyBuilder<T>.enum
  get() = globalJson.decodeFromJsonElement(
    ListSerializer(serializer), data[null ?: "enum"] ?: DEFAULT_ENUM
  )
  set(value) {
    data["enum"] = globalJson.encodeToJsonElement(ListSerializer(serializer), value)
  }

var <T> PropertyBuilder<T>.const
  get() = globalJson.decodeFromJsonElement(serializer, data["const"] ?: JsonNull)
  set(value) {
    data["const"] = globalJson.encodeToJsonElement(serializer, value)
  }

private val EMPTY_JSON_STRING = JsonPrimitive("")

var <T> PropertyBuilder<T>.description
  get() = globalJson.decodeFromJsonElement(
    String.serializer(), data["description"] ?: EMPTY_JSON_STRING
  )
  set(value) {
    data["description"] = globalJson.encodeToJsonElement(String.serializer(), value)
  }

var StringPropertyBuilder.pattern
  get() = globalJson.decodeFromJsonElement(serializer, data["pattern"] ?: EMPTY_JSON_STRING)
    .toRegex()
  set(value) {
    data["pattern"] = globalJson.encodeToJsonElement(serializer, value.toString())
  }

var <T> PropertyBuilder<T>.default
  get() = globalJson.decodeFromJsonElement(serializer, data["default"] ?: JsonNull)
  set(value) {
    data["default"] = globalJson.encodeToJsonElement(serializer, value)
  }

private val JSON_ZERO = JsonPrimitive(0)

var <T : Number> NumberPropertyBuilder<T>.minimum
  get() = globalJson.decodeFromJsonElement(serializer, data["minimum"] ?: JSON_ZERO)
  set(value) {
    data["minimum"] = globalJson.encodeToJsonElement(serializer, value)
  }

var <T : Number> NumberPropertyBuilder<T>.maximum
  get() = globalJson.decodeFromJsonElement(serializer, data["maximum"] ?: JSON_ZERO)
  set(value) {
    data["maximum"] = globalJson.encodeToJsonElement(serializer, value)
  }

@ExperimentalJsonSchemaDSL
var <T, B : PropertyBuilder<T>> ArrayPropertyBuilder<T, B>.minItems
  get() = globalJson.decodeFromJsonElement(Int.serializer(), data["minItems"] ?: JSON_ZERO)
  set(value) {
    data["minItems"] = globalJson.encodeToJsonElement(Int.serializer(), value)
  }

@ExperimentalJsonSchemaDSL
var <T, B : PropertyBuilder<T>> ArrayPropertyBuilder<T, B>.maxItems
  get() = globalJson.decodeFromJsonElement(Int.serializer(), data["maxItems"] ?: JSON_ZERO)
  set(value) {
    data["maxItems"] = globalJson.encodeToJsonElement(Int.serializer(), value)
  }

@ExperimentalJsonSchemaDSL
inline fun <T, B : PropertyBuilder<T>> ArrayPropertyBuilder<T, B>.items(builder: B.() -> Unit) {
  data["items"] = buildProperty(itemType, builder)
}

@ExperimentalJsonSchemaDSL
fun CommonObjectBuilder<*>.additionalProperties(value: Boolean) {
  data["additionalProperties"] = JsonPrimitive(value)
}

@ExperimentalJsonSchemaDSL
inline fun <T, B : PropertyBuilder<T>> CommonObjectBuilder<*>.additionalProperties(
  type: PropertyType<T, B>,
  builder: B.() -> Unit
) {
  data["additionalProperties"] = buildProperty(type, builder)
}

@ExperimentalJsonSchemaDSL
inline fun <T, B : PropertyBuilder<T>> ObjectMapBuilder<T, B>.additionalProperties(
  builder: B.() -> Unit
) {
  data["additionalProperties"] = buildProperty(valueType, builder)
}

@ExperimentalJsonSchemaDSL
inline fun CommonObjectBuilder<*>.propertyNames(
  builder: StringPropertyBuilder.() -> Unit
) {
  data["propertyNames"] = buildProperty(PropertyType.String, builder)
}
