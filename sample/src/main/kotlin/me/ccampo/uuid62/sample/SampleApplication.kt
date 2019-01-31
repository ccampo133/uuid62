package me.ccampo.uuid62.sample

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer
import me.ccampo.uuid62.core.util.toBase62String
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@SpringBootApplication
class SampleApplication

data class UUID62(val id: UUID)

data class RawUUID(@JsonSerialize(using = UUIDSerializer::class) val id: UUID)

@RestController
class SampleController {


    val uuids = mutableSetOf<UUID62>()

    @GetMapping("/uuids/random")
    fun getRandomUUID62(): UUID62 = UUID62(UUID.randomUUID())

    @GetMapping("/uuids/random/raw")
    fun getRandomRawUUID62(): RawUUID = RawUUID(UUID.randomUUID())

    //TODO: this currently fails when the ID is base62 encoded; need to create a converter -ccampo 2018-05-03
    @PostMapping("/uuids/{id}")
    fun addCustomUUID(@PathVariable id: UUID): ResponseEntity<Unit> {
        uuids.add(UUID62(id))
        val location = URI("/uuids/${id.toBase62String()}")
        return ResponseEntity.created(location).build()
    }

    @GetMapping("/uuids/{id}")
    fun getUUID(@PathVariable id: UUID): UUID62 = uuids.first { it.id == id }

    @GetMapping("/uuids/{id}/raw")
    fun getRawUUID(@PathVariable id: UUID): RawUUID = RawUUID(uuids.first { it.id == id }.id)

    @GetMapping("/uuids")
    fun geUUIDs(): List<UUID62> = uuids.toList()

    @GetMapping("/uuids/raw")
    fun getRawUUIDs(): List<RawUUID> = uuids.map { id -> RawUUID(id.id) }
}

fun main(args: Array<String>) {
    runApplication<SampleApplication>(*args)
}
